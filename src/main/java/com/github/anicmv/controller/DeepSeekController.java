package com.github.anicmv.controller;

import io.github.pigmesh.ai.deepseek.config.DeepSeekProperties;
import io.github.pigmesh.ai.deepseek.core.DeepSeekClient;
import io.github.pigmesh.ai.deepseek.core.chat.ChatCompletionRequest;
import io.github.pigmesh.ai.deepseek.core.chat.ChatCompletionResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author anicmv
 * @date 2025/4/6 13:49
 * @description TODO
 */
@RestController
public class DeepSeekController {
    @Autowired
    private DeepSeekClient deepSeekClient;
    @Autowired
    private DeepSeekProperties deepSeekProperties;


    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatCompletionResponse> chat(HttpServletResponse response, String prompt) {
        response.setContentType("text/event-stream;charset=UTF-8");
        return deepSeekClient.chatFluxCompletion(prompt);
    }

    @GetMapping(value = "/sync/chat")
    public ChatCompletionResponse syncChat(HttpServletResponse response, String prompt) {
        response.setContentType("text/event-stream;charset=UTF-8");
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                // 根据渠道模型名称动态修改这个参数
                .model(deepSeekProperties.getModel())
                .addUserMessage(prompt).build();

        return deepSeekClient.chatCompletion(request).execute();
    }

}
