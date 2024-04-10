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

    /* todo
    待完成
    处理图片上传 */

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload() {

        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/administrator.png");
        return fileDTO;
    }
}
