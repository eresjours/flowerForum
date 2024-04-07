package flower.community.Datatransfermodel;

import flower.community.model.User;
import lombok.Data;

/**
 * @author WsW
 * @version 1.0
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private User user;
}
