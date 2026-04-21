package org.example.pfebackend.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
    private Integer discussionId;
    private String content;
    private String who;
}
