package cn.lin037.monitor.handler;

import cn.lin037.monitor.domain.vo.ResultVO;
import cn.lin037.monitor.enums.CodeEnum;
import cn.lin037.monitor.exception.CommonException;
import cn.lin037.monitor.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ResultVO commonExceptionHandler(CommonException commonException) {
        log.info("CommonException " + commonException.getMessage() + "---" + commonException.getDescription());
        return ResultUtil.error(commonException);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResultVO runtimeExceptionHandler(RuntimeException runtimeException){
        log.error("RuntimeException " + runtimeException);
        return ResultUtil.error(CodeEnum.ERROR_UNKNOWN);
    }

    /**
     * 405异常捕获
     * @param httpRequestMethodNotSupportedException 异常类
     * @return 错误json信息
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultVO httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException){
        log.info("HttpRequestMethodNotSupportedException" + httpRequestMethodNotSupportedException);
        return ResultUtil.error(CodeEnum.ERROR_UNSUPPORTED_METHOD);
    }

    /**
     * 415异常捕获
     * @param httpMediaTypeNotSupportedException 异常类
     * @return 错误json信息
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResultVO httpMediaTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException){
        log.info("HttpMediaTypeNotSupportedException" + httpMediaTypeNotSupportedException);
        return ResultUtil.error(CodeEnum.ERROR_UNSUPPORTED_MEDIA_TYPE);
    }
}
