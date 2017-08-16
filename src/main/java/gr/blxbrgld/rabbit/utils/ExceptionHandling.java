package gr.blxbrgld.rabbit.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Generic Exception Handler
 * @author blxbrgld
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception) {
        log.error("Exception", exception);
        return Constants.ERROR_PAGE;
    }
}
