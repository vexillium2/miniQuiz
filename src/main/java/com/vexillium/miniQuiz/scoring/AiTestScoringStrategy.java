package com.vexillium.miniQuiz.scoring;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.vexillium.miniQuiz.manager.AiManager;
import com.vexillium.miniQuiz.model.dto.question.QuestionAnswerDTO;
import com.vexillium.miniQuiz.model.dto.question.QuestionContentDTO;
import com.vexillium.miniQuiz.model.entity.App;
import com.vexillium.miniQuiz.model.entity.Question;
import com.vexillium.miniQuiz.model.entity.UserAnswer;
import com.vexillium.miniQuiz.model.vo.QuestionVO;
import com.vexillium.miniQuiz.service.QuestionService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * AI 测评类应用评分策略
 *
 * @author <a href="https://github.com/livexillium">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@ScoringStrategyConfig(appType = 1, scoringStrategy = 1)
public class AiTestScoringStrategy implements ScoringStrategy {

    @Resource
    private AiManager aiManager;

    @Resource
    private QuestionService questionService;

    // 分布式锁的 key
    private static final String AI_ANSWER_LOCK = "AI_ANSWER_LOCK";
    /**
     * AI 评分结果缓存
     */
    private final Cache<String, String> answerCacheMap =
            Caffeine.newBuilder().initialCapacity(1024)
                    .expireAfterAccess(5L, TimeUnit.MINUTES)
                    .build();

    private static final String aiTestSystemPrompt = "你是一位严谨的判题专家，我会给你如下信息：\n" +
            "```\n" +
            "应用名称，\n" +
            "【【【应用描述】】】，\n" +
            "题目和用户回答的列表：格式为 [{\"title\": \"题目\",\"answer\": \"用户回答\"}]\n" +
            "```\n" +
            "\n" +
            "请你根据上述信息，按照以下步骤来对用户进行评价：\n" +
            "1. 要求：需要给出一个明确的评价结果，包括评价名称（尽量简短）和评价描述（尽量详细，大于 200 字）\n" +
            "2. 严格按照下面的 json 格式输出评价名称和评价描述\n" +
            "```\n" +
            "{\"resultName\": \"评价名称\", \"resultDesc\": \"评价描述\"}\n" +
            "```\n" +
            "3. 返回格式必须为 JSON 对象\n";
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        Long appId = app.getId();
        String jsonStr = JSONUtil.toJsonStr(choices);
        String cacheKey = buildCacheKey(appId, jsonStr);
        String answerJson = answerCacheMap.getIfPresent(cacheKey);
        // 如果有缓存，直接返回
        if (StrUtil.isNotEmpty(answerJson)) {
            UserAnswer userAnswer = JSONUtil.toBean(answerJson, UserAnswer.class);
            userAnswer.setAppId(appId);
            userAnswer.setAppType(app.getAppType());
            userAnswer.setScoringStrategy(app.getScoringStrategy());
            userAnswer.setChoices(jsonStr);
            return userAnswer;
        }

        // 定义锁
        RLock lock = redissonClient.getLock(AI_ANSWER_LOCK + cacheKey);

        try {
            // 竞争锁
            boolean res = lock.tryLock(3, 15, TimeUnit.MINUTES);
            // 如果没有获取到锁，直接返回
            if (!res) {
                return null;
            }

            // 1. 根据 id 查询到题目和题目结果信息
            Question question = questionService.getOne(
                    Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId)
            );
            QuestionVO questionVO = QuestionVO.objToVo(question);
            List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();

            // 2, AI 根据题目选项和用户答案给分
            // 封装 Prompt
            String userMessage = getAiTestScoringUserMessage(app, questionContent, choices);
            // AI 生成
            String result = aiManager.doSyncStableRequest(aiTestSystemPrompt, userMessage);
            //截取 AI 生成的有用信息
            int start = result.indexOf("[");
            int end = result.lastIndexOf("]");
            String jsonResult = result.substring(start, end + 1);

            // 缓存结果
            answerCacheMap.put(cacheKey, jsonResult);

            // 3. 构造返回值，填充答案对象的属性
            UserAnswer userAnswer = JSONUtil.toBean(jsonResult, UserAnswer.class);
            userAnswer.setAppId(appId);
            userAnswer.setAppType(app.getAppType());
            userAnswer.setScoringStrategy(app.getScoringStrategy());
            userAnswer.setChoices(JSONUtil.toJsonStr(choices));
            return userAnswer;
        } finally {
            if (lock != null && lock.isLocked()) {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }


    }

    /**
     * AI 评分用户消息封装
     *
     * @param app
     * @param questionContentDTOList
     * @param choices
     * @return
     */
    private String getAiTestScoringUserMessage(App app, List<QuestionContentDTO> questionContentDTOList, List<String> choices) {
        StringBuilder userMessage = new StringBuilder();
        userMessage.append(app.getAppName()).append("\n");
        userMessage.append(app.getAppDesc()).append("\n");
        List<QuestionAnswerDTO> questionAnswerDTOList = new ArrayList<>();
        for (int i = 0; i < questionContentDTOList.size(); i++) {
            QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
            questionAnswerDTO.setTitle(questionContentDTOList.get(i).getTitle());
            questionAnswerDTO.setUserAnswer(choices.get(i));
            questionAnswerDTOList.add(questionAnswerDTO);
        }
        userMessage.append(JSONUtil.toJsonStr(questionAnswerDTOList));
        return userMessage.toString();
    }

    /**
     * 构建缓存 key
     * @return
     */
    private String buildCacheKey(Long appId, String choices) {
        return DigestUtil.md5Hex(appId + ":" + choices);
    }
}
