package flower.community.exception;

/**
 * @author WsW
 * @version 1.0
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUNT(2001, "你找的问题不在了，换一个试试？"),
    TARGET_PARAM_NOT_FOUNT(2002, "未选中任何问题或评论进行回复"),
    NO_LOGIN(2003, "未登录，请登陆后重试"),
    SYS_ERROR(2004, "服务太热了！稍后再试!"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "该评论已不存在");

    private Integer code;
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
