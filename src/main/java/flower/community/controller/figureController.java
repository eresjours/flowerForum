package flower.community.controller;

import cmcc.iot.onenet.javasdk.api.cmds.SendCmdsApi;
import cmcc.iot.onenet.javasdk.api.datastreams.FindDatastreamListApi;
import cmcc.iot.onenet.javasdk.response.BasicResponse;
import cmcc.iot.onenet.javasdk.response.cmds.NewCmdsResponse;
import cmcc.iot.onenet.javasdk.response.datastreams.DatastreamsResponse;
import flower.community.Datatransfermodel.ResultDTO;
import flower.community.exception.CustomizeErrorCode;
import flower.community.model.User;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class figureController {

    /**
     * 获取当前登录用户的name
     *
     * @param request HTTP 请求对象
     * @return 包含用户基本信息的响应结果,如果未登录则返回错误码
     */
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


    /**
     * 通过 JavaSDK 从云平台获取多个数据流的数据
     *
     * @return 包含多个数据流数据的响应结果
     */
    @ResponseBody //将返回的数据自动反序列化为json形式返回前端
    @GetMapping("/figure/getInfo")
    public Object testFindDatastreamsListApi(){
        // 通过JavaSDK从云平台获取数据
        String datastreamids = "light,humidity,temperature";
        String devid = "1215022480";
        String key = "x5rm91T8S5p0zNLsr3AXtWopi=A=";
        /**
         * 查询多个数据流
         * @param datastreamids:数据流名称 ,String
         * @param devid:设备ID,String
         * @param key:masterkey 或者 设备apikey
         */
        FindDatastreamListApi api = new FindDatastreamListApi(datastreamids, devid, key);
        BasicResponse<List<DatastreamsResponse>> response = api.executeApi();
        System.out.println("errno:"+response.errno+" error:"+response.error);
//        System.out.println(response.getJson());

        return response.getJson();
    }

    /**
     * 通过 JavaSDK 向云平台发送命令,实现浇水功能
     *
     * @return 包含命令响应的结果
     * @throws IOException 如果发生 IO 异常
     */
    @ResponseBody //将返回的数据自动反序列化为json形式返回前端
    @GetMapping("/figure/watering")
    public Object testSendStrCmdsApi() throws IOException {
        String devId = "1215022480";
        String key = "x5rm91T8S5p0zNLsr3AXtWopi=A=";
        String text = "watering";
        /**
         * 发送命令
         * @param devId：接收该数据的设备ID（必选），String
         * @param qos:是否需要响应，默认为0,Integer
         * 0：不需要响应，即最多发送一次，不关心设备是否响应；
         * 1：需要响应，如果设备收到命令后没有响应，则会在下一次设备登陆时若命令在有效期内(有效期定义参见timeout参数）则会继续发送。
         * 对响应时间无限制，多次响应以最后一次为准。
         * 本参数仅当type=0时有效；
         * @param timeOut:命令有效时间，默认0,Integer
         * 0：在线命令，若设备在线,下发给设备，若设备离线，直接丢弃；
         *  >0： 离线命令，若设备在线，下发给设备，若设备离线，在当前时间加timeout时间内为有效期，有效期内，若设备上线，则下发给设备。单位：秒，有效围：0~2678400。
         *  本参数仅当type=0时有效；
         * @param type://默认0。0：发送CMD_REQ包，1：发送PUSH_DATA包
         * @param contents:用户自定义数据：json、string、二进制数据（小于64K）
         * @param key:masterkey或者设备apikey
         */
        SendCmdsApi api = new SendCmdsApi(devId, null, null, null, text, key);
        BasicResponse<NewCmdsResponse> response = api.executeApi();
        System.out.println(response.getJson());
        return response.getJson();
    }

}
