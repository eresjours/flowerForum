package flower.community.interceptor;

import flower.community.mapper.UserMapper;
import flower.community.model.User;
import flower.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author WsW
 * @version 1.0
 */

@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    /**
     * 拦截器的预处理方法,在请求处理之前执行
     *
     * @param request  HTTP请求对象
     * @param response HTTP响应对象
     * @param handler  处理器对象
     * @return 是否继续执行请求处理
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
            在用户访问网站首页时检查是否存在有效的身份认证信息（通过Cookie中的token）
            如果存在则将用户信息存储到会话中，以便后续使用。
         */
        Cookie[] cookies = request.getCookies();
        //当清除cookie以后，没有任何cookie时会产生空指针异常
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    // 如果user对象不为空说明登录成功
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                        Long unreadCount = notificationService.unreadCount(user.getId());
                        request.getSession().setAttribute("unreadCount", unreadCount);
                    }
                    break;
                }
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /*
        postHandle 会在 请求处理完成后 执行,即在控制器方法执行完成后执行。
        该方法可以用于进行一些资源清理、统计或日志记录等操作。
        它只是简单地调用了父类的 postHandle 方法,没有执行其他逻辑。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /*
        afterCompletion 会在整个请求处理完成后执行,即在视图渲染完成后执行。
        该方法可以用于进行一些资源清理、统计或日志记录等操作。
        它只是简单地调用了父类的 afterCompletion 方法,没有执行其他逻辑。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
