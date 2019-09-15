package com.moqi.community01.service;

import com.moqi.community01.dto.CommentDTO;
import com.moqi.community01.enums.CommentTypeEnum;
import com.moqi.community01.exception.CustomizeErrorCode;
import com.moqi.community01.exception.CustomizeException;
import com.moqi.community01.mapper.CommentMapper;
import com.moqi.community01.mapper.QuestionExtMapper;
import com.moqi.community01.mapper.QuestionMapper;
import com.moqi.community01.mapper.UserMapper;
import com.moqi.community01.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0){
                throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);

            }
            commentMapper.insert(comment);
             } else {
            //回复问题
        Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }

    public List<CommentDTO> listByTargetId(Integer id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        //去重评论用户
        Set<Integer> comentators = comments.stream().map(comment -> comment.getCommentarot()).collect(Collectors.toSet());
        List<Integer> userIds = new ArrayList<>();
        userIds.addAll(comentators);
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        /*------------------------------------------*/

        // 获取评论人并转换为 Map
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        // 转换 comment 为 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentarot()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
