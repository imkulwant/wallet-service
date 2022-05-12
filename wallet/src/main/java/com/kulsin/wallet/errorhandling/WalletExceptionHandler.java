package com.kulsin.wallet.errorhandling;

import com.kulsin.accounting.account.AccountServiceException;
import com.kulsin.accounting.transaction.TransactionServiceException;
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
        String message = "";

        if(exception instanceof MethodArgumentNotValidException) {
            message = Objects.requireNonNull(((MethodArgumentNotValidException) exception).getFieldError()).getDefaultMessage();
            return errorModelAndView(400, message);

        } else if ( exception instanceof WalletException){
            message = exception.getMessage();
            return errorModelAndView(400, message);

        }  else if ( exception instanceof AccountServiceException || exception instanceof TransactionServiceException){
            message = exception.getMessage();
            return errorModelAndView(500, message);

        } else {
            return errorModelAndView(500, message);
        }

    }

    private ModelAndView errorModelAndView(int status, String message) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("errorStatus", status);
        modelMap.addAttribute("errorMessage", message);
        return new ModelAndView("error", modelMap);
    }

}
