package flower.community.Datatransfermodel;

import lombok.Data;

/**
 * @author WsW
 * @version 1.0
 */
@Data
public class AccessTokenDTO {

    private String client_id;       //从 GitHub 收到的 OAuth 应用的客户端 ID
    private String client_secret;   //从 GitHub 收到的 OAuth 应用的客户端密码
    private String code;            //作为对步骤 1(请求用户的 GitHub 身份) 的响应而收到的代码
    private String redirect_uri;    //应用程序中授权后发送用户的 URL
    private String state;           //一个不可猜测的随机字符串。它用于防止跨站点请求伪造攻击
}
