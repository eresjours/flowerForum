package flower.community.service;

import flower.community.Datatransfermodel.CommentDTO;
import flower.community.enums.CommentTypeEnum;
import flower.community.enums.NotificationTypeEnum;
import flower.community.enums.NotificationStatusEnum;
import flower.community.exception.CustomizeErrorCode;
import flower.community.exception.CustomizeException;
import flower.community.mapper.CommentMapper;
import flower.community.mapper.NotificationMapper;
import flower.community.mapper.QuestionMapper;
import flower.community.mapper.UserMapper;
import flower.community.model.Comment;
import flower.community.model.Notification;
import flower.community.model.Question;
import flower.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    
    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * 保存新的评论
     *
     * @param comment      要保存的评论对象
     * @param commentator  发表评论的用户对象
     */
    @Transactional  //添加事务功能，当对数据库的操作有一条错误，全部进行回滚
    public void insert(Comment comment, User commentator) {

        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUNT);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        // 根据评论的类型判断是问题的评论还是回复的评论
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            // 回复评论
            // 根据二级评论获得一级评论的ID，判断是否存在
            Comment dbComment = commentMapper.selectByCommentID(comment.getParentId());
            System.out.println("comment.getParentId"+comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            // 将回复评论的评论数增加
            commentMapper.updateCommentCount(dbComment.getId());

            // 找到回复的评论属于哪个问题，获取问题名字
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUNT);
            }

            //  创建通知
            createNotify(comment, dbComment, commentator.getName(), question.getTitle());

        } else {
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUNT);
            }

            // 将问题的评论数增加
            questionMapper.updateCommentCount(question.getId());

            createNotify(comment, question, commentator.getName(), question.getTitle());
        }

        commentMapper.insert(comment);
    }

    /**
     * 为新创建的评论生成通知
     *
     * @param comment      新创建的评论对象
     * @param question     被评论的问题对象
     * @param notifierName 发起评论的用户名
     * @param outerTitle   被评论的问题标题
     */
    private void createNotify(Comment comment, Question question, String notifierName, String outerTitle) {

        // 自己回复自己不需要通知
        if (comment.getCommentator() == question.getCreator()) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(NotificationTypeEnum.REPLAY_QUESTION.getType());
        notification.setOuterid(comment.getParentId());     // 回复的问题的ID
        notification.setReceiver(question.getCreator());   // 回复的问题的作者
        notification.setNotifier(comment.getCommentator());     // 发起通知的通知者的ID
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());  //设置未读
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insertNotified(notification);
    }

    /**
     * 为新创建的评论回复生成通知
     *
     * @param comment       新创建的评论回复对象
     * @param dbComment     被回复的评论对象
     * @param notifierName  发起评论回复的用户名
     * @param outerTitle    被回复的评论所在问题的标题
     */
    private void createNotify(Comment comment, Comment dbComment, String notifierName, String outerTitle) {

        // 自己回复自己，不需要通知
        if (comment.getCommentator() == dbComment.getCommentator()) {
            return;
        }
        /* 新插入的comment 对 dbComment 进行回复 */
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(NotificationTypeEnum.REPLAY_COMMENT.getType());
        notification.setOuterid(comment.getParentId());     // 回复的评论的ID
        notification.setReceiver(dbComment.getCommentator());   // 回复的评论的作者
        notification.setNotifier(comment.getCommentator());     // 发起通知的通知者的ID
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());  //设置未读
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insertNotified(notification);
    }

    /**
     * 根据问题 ID 获取该问题的所有评论
     *
     * @param id 问题 ID
     * @return 包含评论信息和评论人信息的 CommentDTO 列表
     */
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

    /**
     * 根据评论 ID 获取该评论下的所有二级评论
     * 可以和上面的方法进行重构 todo
     *
     * @param id 评论 ID
     * @return 包含二级评论信息和评论人信息的 CommentDTO 列表
     */
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

    public Long getQuestionIdByParentId(Long outerid) {
        return commentMapper.getQuestionIdByParentId(outerid);
    }
}
