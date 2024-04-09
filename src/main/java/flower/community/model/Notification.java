package flower.community.model;

import lombok.Data;

/**
 * @author WsW
 * @version 1.0
 */
@Data
public class Notification {

//    ID            BIGINT auto_increment
//    primary key,
//    NOTIFIER      BIGINT        not null,
//    RECEIVER      BIGINT        not null,
//    OUTERID       BIGINT        not null,
//    TYPE          INT           not null,
//    GMT_CREATE    BIGINT        not null,
//    STATUS        INT default 0 not null,
//    NOTIFIER_NAME VARCHAR(100),
//    OUTER_TITLE   VARCHAR(256)

    private Long id;
    private Long notifier;      // 通知人
    private Long receiver;      // 回复人
    private Long outerid;
    private Integer type;       // 通知类型
    private Long gmtCreate;     // 创建时间
    private Integer status;
    private String notifierName;
    private String outerTitle;

}
