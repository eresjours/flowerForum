package flower.community.Datatransfermodel;

import flower.community.model.User;
import lombok.Data;

/**
 * @author WsW
 * @version 1.0
 */
@Data
public class NotificationDTO {

    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private String outerTitle;
    private Long outerid;
    private String typeName;
    private Integer type;
}
