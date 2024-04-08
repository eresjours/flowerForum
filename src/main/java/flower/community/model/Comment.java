package flower.community.model;

import lombok.Data;

/**
 * @author WsW
 * @version 1.0
 */
@Data
public class Comment {

    private Long id;            // 评论id
    private Long parentId;      // 父类id 当type=1时为questionId；type=2时为上级评论的id
    private Integer type;       // 类型 1为问题的评论 2为评论的评论
    private Long commentator;   // 评论人id
    private Long gmtCreate;     // 创建时间
    private Long gmtModified;   // 更新时间
    private Long likeCount;     // 点赞数
    private String content;     // 评论内容
    private Integer commentCount;   //评论数
}
