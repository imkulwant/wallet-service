package com.kulsin.wallet.errorhandling;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class WalletExceptionHandler extends ExceptionHandlerExceptionResolver {

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {
        int status = response.getStatus();
        String message = "";

        if(exception instanceof MethodArgumentNotValidException) {
            message = Objects.requireNonNull(((MethodArgumentNotValidException) exception).getFieldError()).getDefaultMessage();
        } else {
            message = exception.getMessage();
        }

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("errorStatus", status);
        modelMap.addAttribute("errorMessage", message);
        return new ModelAndView("error", modelMap);
    }

}
