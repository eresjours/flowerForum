package flower.community.Datatransfermodel;

import flower.community.model.User;
import lombok.Data;

/**
 * @author WsW
 * @version 1.0
 */
@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
