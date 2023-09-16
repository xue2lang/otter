package com.alibaba.otter.client.handler;


import com.alibaba.otter.client.common.exception.ServiceException;
import com.alibaba.otter.client.model.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * MVC异常拦截器
 *
 * @author rewerma 2019-07-13 下午05:12:16
 * @version 1.0.0
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    /**
     * 通用异常处理
     *
     * @param e 异常
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = Exception.class)
    public BaseModel commonExceptionHandle(Exception e) {
        if (e instanceof ServiceException) {
            logger.error(e.getMessage());
        } else {
            logger.error(e.getMessage(), e);
        }
        BaseModel res = new BaseModel();
        res.setCode(50000);
        res.setMessage(e.getMessage());
        return res;
    }
}
