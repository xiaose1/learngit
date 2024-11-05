package cn.lin037.monitor.exception;

import cn.lin037.monitor.enums.CodeEnum;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException{

    private final Integer code;
    private final String description;

    public CommonException(Integer code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public CommonException(CodeEnum codeEnum, String description) {
        super(codeEnum.getMessage());
        this.code = codeEnum.getCode();
        this.description = description;
    }

    public CommonException(CodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.code = codeEnum.getCode();
        this.description = null;
    }

}
