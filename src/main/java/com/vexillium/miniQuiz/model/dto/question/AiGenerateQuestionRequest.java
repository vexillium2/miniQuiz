package com.vexillium.miniQuiz.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * Ai 生成问题请求
 *
 * @author <a href="https://github.com/vexillium2">vexillium</a>
 * @from <a href="https://vexillium.icu"></a>
 */
@Data
public class AiGenerateQuestionRequest implements Serializable {

    /**
     * 应用 ID
     */
    private Long appId;

    /**
     * 题目描述
     */
    private String description;

    /**
     * 题目数量
     */
    private int questionNumber;

    /**
     * 选项数量
     */
    private int optionNumber;

    private static final long serialVersionUID = 1L;
}
