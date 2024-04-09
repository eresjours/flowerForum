package flower.community.enums;

/**
 * @author WsW
 * @version 1.0
 */
public enum NotificationStatusEnum {

    UNREAD(0),
    READ(1);

    private int status;

    NotificationStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
