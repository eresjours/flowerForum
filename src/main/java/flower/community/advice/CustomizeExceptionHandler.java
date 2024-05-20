package flower.community.advice;

import com.alibaba.fastjson.JSON;
import flower.community.Datatransfermodel.ResultDTO;
import flower.community.exception.CustomizeErrorCode;
import flower.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author WsW
 * @version 1.0
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

    /*
        跳转页面和返回json不能同时存在，采用手写方式实现
     */
    /**
     * 统一处理应用程序中抛出的异常
     *
     * @param e                 抛出的异常对象
     * @param model             用于在页面上显示错误信息
     * @param request           HTTP 请求对象
     * @param response          HTTP 响应对象
     * @return 如果是 JSON 请求,则返回 null;否则返回一个 ModelAndView 对象,用于渲染错误页面
     */
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e,
                  Model model,
                  HttpServletRequest request,
                  HttpServletResponse response) { //HttpServletRequest表示请求过来的信息，HttpServletResponse表示所有响应的信息

        // 根据请求中的 Content-Type 判断类型
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {

            ResultDTO resultDTO;
            if (e instanceof CustomizeException) {
                // 抛过来的自定义异常处理
                // 返回json
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            } else {
                // 默认的异常处理
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }

            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO)); // 将resultDTO转换为json格式
                writer.close(); // 关闭流
            } catch (IOException ioe) {
            }
            return null;

        } else {

            // 错误页面跳转
            if (e instanceof CustomizeException) {
                // 抛过来的异常处理
                model.addAttribute("message", e.getMessage());
            } else {
                // 默认的异常处理
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }

            return new ModelAndView("/error");
        }
    }
}
