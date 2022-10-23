package work.moonzs.base.handler;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.log.dialect.console.ConsoleLogFactory;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.exception.ServiceException;
import work.moonzs.base.exception.ValidateException;
import work.moonzs.domain.ResponseResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Controller接口参数校验失败处理
 *
 * @author Moondust月尘
 */
@RestControllerAdvice
public class RestControllerExceptionHandler {
    static {
        // TODO 应该符合springboot控制台输出的日志
        LogFactory.setCurrentLogFactory(new ConsoleLogFactory());
    }

    private static final Log LOG = LogFactory.get();

    /**
     * 如果有参数校验失败，会将错误信息封装成对象组装在BindingResult里
     * 全局异常处理，捕捉抛出的MethodArgumentNotValidException异常，并进行相应的处理。
     * 这个是用@Valid等注解才触发的异常处理
     *
     * @param e 异常
     * @return {@link ResponseResult}<{@link ?}>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<String> list = new LinkedList<>();
        result.getFieldErrors().forEach(error -> {
            String field = error.getField();
            Object value = error.getRejectedValue();
            String msg = error.getDefaultMessage();
            LOG.error("错误字段 -> {} 错误值 -> {} 原因 -> {}", field, value, msg);
            list.add(msg);
        });
        return ResponseResult.fail(45000, list.toString());
    }

    /**
     * 如果是自定义校验注解，用这个异常处理
     *
     * @param e 异常
     * @return {@link ResponseResult}<{@link ?}>
     */
    @ExceptionHandler(ValidateException.class)
    public ResponseResult handleValidate(ValidateException e) {
        LOG.error("失败原因: {}", e.getMessage());
        return ResponseResult.ofException(e);
    }

    /**
     * 处理"Required request body is missing
     *
     * @return {@link ResponseResult}<{@link ?}>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseResult handleHttpMessageNotReadable() {
        return ResponseResult.fail(AppHttpCodeEnum.REQUIRED_REQUEST_BODY);
    }

    /**
     * 处理自定义服务异常
     *
     * @param e e
     * @return {@link ResponseResult}<{@link ?}>
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseResult handleService(ServiceException e) {
        LOG.error("失败原因: {}", e.getMessage());
        return ResponseResult.ofException(e);
    }

    /**
     * 处理JWT过期异常
     *
     * @param e e
     * @return {@link ResponseResult}
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseResult handleExpiredJwt(ExpiredJwtException e) {
        return ResponseResult.fail(AppHttpCodeEnum.TOKEN_ABNORMAL);
    }
}
