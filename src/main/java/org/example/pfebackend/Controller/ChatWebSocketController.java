package org.example.pfebackend.Controller;

import org.example.pfebackend.Dto.MessageDto;
import org.example.pfebackend.Entity.Message;
import org.example.pfebackend.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(MessageDto dto) {

        Message saved = messageService.sendMessage(
                dto.getDiscussionId(),
                dto.getContent(),
                dto.getWho()
        );

        if (saved == null) return;

        messagingTemplate.convertAndSend(
                "/topic/discussion/" + dto.getDiscussionId(),
                saved
        );
    }
}
