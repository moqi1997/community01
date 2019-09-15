package com.moqi.community01.dto;

import lombok.Data;

@Data
public class CommentCreateDTO {
    private Integer parenId;
    private String content;
    private Integer type;
}
