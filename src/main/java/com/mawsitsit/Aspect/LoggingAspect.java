package com.mawsitsit.Aspect;

import com.mawsitsit.Controller.RESTController;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoggingAspect {

  private Logger logger = LoggerFactory.getLogger(RESTController.class);

@AfterReturning("execution(* com.mawsitsit.Controller.RESTController..*(..)) && args(.., request)")
  public void loggingAdvice(HttpServletRequest request) {
    logger.info(request.getServerName() + " HTTP-REQUEST " + request.getRequestURI());
  }

@AfterReturning("execution(@org.springframework.web.bind.annotation.ExceptionHandler * *(..)) && args(.., request)")
  public void loggingExceptionAdvice(HttpServletRequest request) {
    logger.warn(request.getServerName() + " HTTP-ERROR " + request.getRequestURI());
  }

}
