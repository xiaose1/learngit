package cn.lin037.monitor.utils;

import cn.lin037.monitor.domain.vo.ResultVO;
import cn.lin037.monitor.enums.CodeEnum;
import cn.lin037.monitor.exception.CommonException;

public class ResultUtil {

    public static ResultVO success(Object data){
        return new ResultVO(CodeEnum.SUCCESS_COMMON.getCode(), CodeEnum.SUCCESS_COMMON.getMessage(), data);
    }

    public static ResultVO success(String description){
        return new ResultVO(CodeEnum.SUCCESS_COMMON.getCode(), CodeEnum.SUCCESS_COMMON.getMessage(), description, null);
    }

    public static ResultVO success(){
        return new ResultVO(CodeEnum.SUCCESS_NOT_CONTENT.getCode(), CodeEnum.SUCCESS_NOT_CONTENT.getMessage(), null);
    }

    public static ResultVO error(CodeEnum codeEnum){
        return new ResultVO(codeEnum.getCode(), codeEnum.getMessage(), null);
    }

    public static ResultVO error(CodeEnum codeEnum, String description){
        return new ResultVO(codeEnum.getCode(), codeEnum.getMessage(), description, null);
    }

    public static ResultVO error(CommonException commonException){
        return new ResultVO(commonException.getCode(), commonException.getMessage(), commonException.getDescription(), null);
    }
}
