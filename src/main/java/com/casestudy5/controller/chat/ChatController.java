package com.casestudy5.controller.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Nhận tin nhắn từ người dùng và gửi lại cho seller
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public String sendMessage(String message) {
        return message;
    }

    // Tạo một endpoint để nhận tin nhắn từ client và phát lại đến seller hoặc người dùng
    public void sendPrivateMessage(String user, String message) {
        messagingTemplate.convertAndSendToUser(user, "/queue/messages", message);
    }
}