package ying.backend_features.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import ying.backend_features.utils.CommonJSONUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ying on 2017-04-16.
 */
@ControllerAdvice
@ResponseBody
public class ExceptionsHandler {
    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        e.printStackTrace();
        return CommonJSONUtils.getErrorJson("0", "Internal exception. " + getMessage(e));
    }

    private String getMessage(Exception e) {
        if (e == null)
            return "";

        if (e.getMessage() == null)
            return "";

        return e.getMessage();
    }
}
