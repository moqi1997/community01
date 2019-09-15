package com.moqi.community01.dto;

import com.moqi.community01.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Integer id;
    private Integer parentId;
    private Integer type;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Integer commentarot;
    private Integer commentCount;
    private User user;
}
