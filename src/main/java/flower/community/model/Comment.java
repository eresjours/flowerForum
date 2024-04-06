package flower.community.model;

import lombok.Data;

/**
 * @author WsW
 * @version 1.0
 */
@Data
public class Comment {

    private Long id;            // 评论id
    private Long parentId;      // 父类id
    private Integer type;       // 父类类型
    private Long commentator;   // 评论人id
    private Long gmtCreate;     // 创建时间
    private Long gmtModified;   // 更新时间
    private Long likeCount;     // 点赞数
    private String content;     // 评论内容
}
