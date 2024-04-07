package flower.community.controller;

import flower.community.Datatransfermodel.ResultDTO;
import flower.community.exception.CustomizeErrorCode;
import flower.community.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class figureController {

    @ResponseBody //将返回的数据自动反序列化为json形式返回前端
    @GetMapping("/figure")
    public Object figure(HttpServletRequest request){
        // 通过request获取session，进而获取其中的user
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        // 将用户对象转换为 JSON 格式并返回
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", user.getName());

        return userData;
    }
}
