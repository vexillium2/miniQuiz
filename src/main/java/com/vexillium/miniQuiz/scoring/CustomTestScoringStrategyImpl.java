package com.vexillium.miniQuiz.scoring;

import com.vexillium.miniQuiz.model.entity.App;
import com.vexillium.miniQuiz.model.entity.UserAnswer;
import com.vexillium.miniQuiz.service.QuestionService;

import javax.annotation.Resource;
import java.util.List;

public class CustomTestScoringStrategyImpl implements ScoringStrategy {

    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringStrategy scoringStrategy;

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {


        return null;
    }
}
