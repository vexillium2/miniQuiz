package com.vexillium.miniQuiz;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import okhttp3.Request;
import okhttp3.RequestBody;

import org.junit.jupiter.api.Test;
import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import okhttp3.Response;

import java.io.IOException;

public class DeepSeekAiTest {

    @Test
    public void test() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"messages\": [\n    {\n      \"content\": \"You are a helpful assistant\",\n      \"role\": \"system\"\n    },\n    {\n      \"content\": \"Hi\",\n      \"role\": \"user\"\n    }\n  ],\n  \"model\": \"deepseek-chat\",\n  \"frequency_penalty\": 0,\n  \"max_tokens\": 2048,\n  \"presence_penalty\": 0,\n  \"response_format\": {\n    \"type\": \"text\"\n  },\n  \"stop\": null,\n  \"stream\": false,\n  \"stream_options\": null,\n  \"temperature\": 1,\n  \"top_p\": 1,\n  \"tools\": null,\n  \"tool_choice\": \"none\",\n  \"logprobs\": false,\n  \"top_logprobs\": null\n}");
        Request request = new Request.Builder()
                .url("https://api.deepseek.com/chat/completions")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer <TOKEN>")
                .build();
        Response response = client.newCall(request).execute();
    }

    @Test
    public void test2() throws IOException {
        HttpResponse<String> response = Unirest.post("https://api.siliconflow.cn/v1/chat/completions")
                .header("Authorization", "Bearer sk-qfhiecaeyutcjxtyjzjxvvgsepcvfytcsyaymbwdselwqvij")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "  \"model\": \"deepseek-ai/DeepSeek-R1-Distill-Qwen-7B\",\n" +
                        "  \"messages\": [\n" +
                        "    {\n" +
                        "      \"role\": \"user\",\n" +
                        "      \"content\": \"输出按照以下json格式，\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"stream\": false,\n" +
                        "  \"max_tokens\": 512,\n" +
                        "  \"stop\": null,\n" +
                        "  \"temperature\": 0.7,\n" +
                        "  \"top_p\": 0.7,\n" +
                        "  \"top_k\": 50,\n" +
                        "  \"frequency_penalty\": 0.5,\n" +
                        "  \"n\": 1,\n" +
                        "  \"response_format\": {\n" +
                        "    \"type\": \"json_object\"\n" +
                        "  }\n" +
                        "}")
                .asString();
        System.out.println(response.getBody());
    }
}
