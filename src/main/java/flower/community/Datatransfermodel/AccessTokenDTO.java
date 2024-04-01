package flower.community.Datatransfermodel;

/**
 * @author WsW
 * @version 1.0
 */
public class AccessTokenDTO {

    private String client_id;       //从 GitHub 收到的 OAuth 应用的客户端 ID
    private String client_secret;   //从 GitHub 收到的 OAuth 应用的客户端密码
    private String code;            //作为对步骤 1(请求用户的 GitHub 身份) 的响应而收到的代码
    private String redirect_uri;    //应用程序中授权后发送用户的 URL
    private String state;           //一个不可猜测的随机字符串。它用于防止跨站点请求伪造攻击

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
