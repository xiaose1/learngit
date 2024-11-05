package cn.lin037.monitor.enums;

import lombok.Getter;

@Getter
public enum CodeEnum {

    SUCCESS_COMMON(200, "请求处理成功"),
    SUCCESS_NOT_CONTENT(204, "请求处理成功，且无内容返回"),
    ERROR_PARAM(40000, "请求失败，请求参数错误"),
    ERROR_NULL(40001, "请求失败，请求参数为空"),
    ERROR_NOT_LOGIN(40100, "未登录，无法访问"),
    ERROR_UNAUTHORIZED(40101, "用户无权限访问"),
    ERROR_UNSUPPORTED_METHOD(40500, "不支持该请求方法"),
    ERROR_NO_FOUND(40400, "未找到目标请求资源"),
    ERROR_UNSUPPORTED_MEDIA_TYPE(41500, "不支持该媒体类型"),
    ERROR_UNKNOWN(50000, "服务器未知错误，请联系管理员"),
    ;
    public final Integer code;

    public final String message;

    CodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
