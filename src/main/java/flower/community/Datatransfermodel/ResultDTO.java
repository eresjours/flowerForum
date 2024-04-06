package flower.community.Datatransfermodel;

import flower.community.exception.CustomizeErrorCode;
import flower.community.exception.CustomizeException;
import lombok.Data;

/**
 * @author WsW
 * @version 1.0
 */
@Data
public class ResultDTO {

    private Integer code;       // 状态码
    private String message;     // 提示信息

    public static ResultDTO errorOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(), e.getMessage());
    }

    public static ResultDTO okOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

}
