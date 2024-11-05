package cn.lin037.monitor.interceptor;

import cn.lin037.monitor.domain.vo.UsersVO;
import cn.lin037.monitor.enums.CodeEnum;
import cn.lin037.monitor.exception.CommonException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;

import static cn.lin037.monitor.utils.FinalValueUtil.USER_LOGIN_STATUS;

/**
 * Login拦截器
 */
@Component
@ControllerAdvice
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("登录拦截器拦截成功！" + "当前访问页面：" + request.getRequestURI());

        // 如果是预检请求
        if ("OPTIONS".equals(request.getMethod())) return true;

        //获取Session中的登录信息
        UsersVO usersVO = (UsersVO) request.getSession().getAttribute(USER_LOGIN_STATUS);

        //如果未登录
        if (null == usersVO) throw new CommonException(CodeEnum.ERROR_NOT_LOGIN);

        //如果已登录，则通过
        return true;
    }
}
