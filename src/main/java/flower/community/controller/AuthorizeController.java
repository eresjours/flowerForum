package flower.community.controller;

import flower.community.Datatransfermodel.AccessTokenDTO;
import flower.community.Datatransfermodel.GithubUser;
import flower.community.model.User;
import flower.community.provider.GithubProvider;
import flower.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author WsW
 * @version 1.0
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    /*
        value 注解回去配置文件中寻找对应 key 的 value 去赋值
     */
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.url}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callBack(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);

        /*
            当使用github登录成功以后
            会获取用户信息，生成一个token
            将token放入user对象，存入数据库
            并且将token放入cookie
         */
        // 如果用户不为空，说明登陆成功
        if (githubUser != null && githubUser.getId() != null) {
            User user = new User();
            String token = UUID.randomUUID().toString();
//            String token = String.valueOf(githubUser.getId());
            user.setToken(token);
            user.setName(githubUser.getName());
            /*
                如果通过数据库能查到accountId等于当前登录的accountId的时候
                就把数据库的token进行更新
                如果没有就进行插入操作
             */
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.createOrUpdate(user);
//            userMapper.insert(user);
            response.addCookie(new Cookie("token", token));
            return "redirect:/"; //重定向到 index 页面
        } else {
            // 登录失败，重新登录
            return "redirect:/";
        }
    }


    // 退出登录
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        // 移除session中的user
        request.getSession().removeAttribute("user");
        // 移除cookie
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
