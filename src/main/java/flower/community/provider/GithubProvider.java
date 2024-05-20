package flower.community.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import flower.community.Datatransfermodel.AccessTokenDTO;
import flower.community.Datatransfermodel.GithubUser;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;
import okhttp3.RequestBody;

import java.io.IOException;

/**
 * @author WsW
 * @version 1.0
 */
@Component //将当前的类初始化到spring的上下文中，方便对象的调用，不需要实例化
public class GithubProvider {

    /**
     * 通过向 GitHub 的 OAuth 认证服务发送请求获取访问令牌(Access Token)
     *
     * @param accessTokenDTO 包含了获取访问令牌所需的参数,如客户端ID、客户端密钥等
     * @return 获取到的访问令牌字符串,如果获取失败则返回 null
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {

        // 1.指定请求体的媒体类型为 JSON 格式。
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        // 2.创建一个 OkHttpClient 对象用于发送 HTTP 请求
        OkHttpClient client = new OkHttpClient();

        // 3.将传入的 AccessTokenDTO 对象序列化为 JSON 格式,作为请求体
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        // 4.构建一个 POST 请求对象,目标 URL 为 GitHub 的 OAuth 认证服务地址
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        // 5.使用 OkHttpClient 发送请求并获取响应
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            // 6.从响应体中通过字符串分割操作获取实际的访问令牌值
            // 通过split从URL中获得token
            // 默认响应格式 access_token=gho_16C7e42F292c6912E7710c838347Ae178B4a&scope=repo%2Cgist&token_type=bearer
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 7.如果获取成功,返回访问令牌字符串;如果发生异常,则返回 null
        return null;
    }

    /**
     * 使用访问令牌从 GitHub API 获取当前授权用户的信息
     *
     * @param accessToken 已获取的有效 GitHub 访问令牌
     * @return 返回封装了用户信息的 GithubUser 对象,如果获取失败则返回 null
     */
    public GithubUser getUser(String accessToken) {

        // 1.创建一个 OkHttpClient 对象用于发送 HTTP 请求
        OkHttpClient client = new OkHttpClient();
        // 2.构建一个 GET 请求对象,目标 URL 为 GitHub API 获取用户信息的端点
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                // 3.在请求头中添加 Authorization 字段,使用 token 作为前缀携带获取的访问令牌进行认证
                .header("Authorization","token "+accessToken)
                .build();

        try {
            // 4.使用 OkHttpClient 发送请求并获取响应
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            // 5.将响应体JSON数据反序列化为 GithubUser 对象
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {

        }
        // 6.如果获取成功,返回 GithubUser 对象;如果发生 IO 异常,则返回 null
        return null;
    }
}
