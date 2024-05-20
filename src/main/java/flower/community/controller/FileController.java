package flower.community.controller;

import flower.community.Datatransfermodel.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class FileController {

    /*
    处理图片上传
    方法一：对象方式存储
    方法一 较好，但需要服务器先不实现了

    方法二：转换为64编码，存入数据库
    */

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload() {

        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/administrator.png");
        return fileDTO;
    }
}
