package flower.community.Datatransfermodel;

import flower.community.exception.CustomizeErrorCode;
import flower.community.exception.CustomizeException;
import lombok.Data;

import java.util.List;

/**
 * @author WsW
 * @version 1.0
 */
@Data
public class ResultDTO<T> {

    private Integer code;       // 状态码
    private String message;     // 提示信息
    private T data;             // 因为返回的对象不可能都一样，所以使用泛型(代表一切)

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

    public static <T> ResultDTO okOf(T t) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }

}
