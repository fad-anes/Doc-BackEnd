package org.example.pfebackend.Service;

import org.example.pfebackend.Entity.Discussion;
import org.example.pfebackend.Entity.Message;
import org.example.pfebackend.Repository.DiscussionRepo;
import org.example.pfebackend.Repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private DiscussionRepo discussionRepo;

    public Message sendMessage(Integer discussionId, String content, String who) {

        Optional<Discussion> discussionOpt = discussionRepo.findById(discussionId);

        if (!discussionOpt.isPresent()) {
            return null;
        }

        Message message = new Message();
        message.setContent(content);
        message.setWho(who); // "DOCTOR" ou "PATIENT"
        message.setDiscussion(discussionOpt.get());
        message.setCreatedAt(LocalDateTime.now());

        return messageRepo.save(message);
    }

}
