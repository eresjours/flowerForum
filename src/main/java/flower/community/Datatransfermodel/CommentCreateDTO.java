package flower.community.Datatransfermodel;

import lombok.Data;

/**
 * @author WsW
 * @version 1.0
 */

@Data
public class CommentCreateDTO {

    private Long parentId;
    private String content;
    private Integer type;
}
