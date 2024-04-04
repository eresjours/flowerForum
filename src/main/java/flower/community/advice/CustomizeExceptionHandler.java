package flower.community.advice;

import flower.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WsW
 * @version 1.0
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e,
                        Model model) {

        if (e instanceof CustomizeException) {
            // 抛过来的异常处理
            model.addAttribute("message", e.getMessage());
        } else {
            // 默认的异常处理
            model.addAttribute("message", "服务太热了！稍后再试");
        }

        return new ModelAndView("/error");
    }
}
