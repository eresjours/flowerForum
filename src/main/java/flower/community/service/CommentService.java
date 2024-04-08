package flower.community.service;

import flower.community.Datatransfermodel.CommentDTO;
import flower.community.Datatransfermodel.QuestionDTO;
import flower.community.enums.CommentTypeEnum;
import flower.community.exception.CustomizeErrorCode;
import flower.community.exception.CustomizeException;
import flower.community.mapper.CommentMapper;
import flower.community.mapper.QuestionMapper;
import flower.community.mapper.UserMapper;
import flower.community.model.Comment;
import flower.community.model.Question;
import flower.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author WsW
 * @version 1.0
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    /*添加评论*/
    @Transactional  //添加事务功能，当对数据库的操作有一条错误，全部进行回滚
    public void insert(Comment comment) {

        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUNT);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        // 根据评论的类型判断是问题的评论还是回复的评论
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            // 回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            // 将回复评论的评论数增加
            commentMapper.updateCommentCount(dbComment.getId());

        } else {
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUNT);
            }

            // 将问题的评论数增加
            questionMapper.updateCommentCount(question.getId());
        }

        commentMapper.insert(comment);
    }

    /*根据Id获取问题的评论*/
    public List<CommentDTO> listByQuestionId(Long id) {

        List<Comment> comments = questionMapper.selectByQuestionId(id);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }

        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {  // 遍历comments获取所有评论人的id
            User user = userMapper.findByID(comment.getCommentator());
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(user);          //questionDTO就相当于一个组合对象 question+user
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }

    // 根据commentId获取二级评论，可以和上面的方法进行重构//todo
    public List<CommentDTO> listByCommentId(Long id) {
        List<Comment> comments = questionMapper.selectByCommentId(id);

        if (comments.size() == 0) {
            return new ArrayList<>();
        }

        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {  // 遍历comments获取所有评论人的id
            User user = userMapper.findByID(comment.getCommentator());
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(user);          //questionDTO就相当于一个组合对象 question+user
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }
}
