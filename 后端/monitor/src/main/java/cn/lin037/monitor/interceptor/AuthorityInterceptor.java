package cn.lin037.monitor.interceptor;

import cn.lin037.monitor.domain.vo.UsersVO;
import cn.lin037.monitor.enums.CodeEnum;
import cn.lin037.monitor.exception.CommonException;
import cn.lin037.monitor.utils.FinalValueUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 权限校验拦截器
 */
@Component
public class AuthorityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("权限拦截器拦截成功！\n" + "当前访问页面：" + request.getRequestURI());

        // 如果是预检请求
        if ("OPTIONS".equals(request.getMethod())) return true;

        UsersVO usersVO = (UsersVO)request.getSession().getAttribute(FinalValueUtil.USER_LOGIN_STATUS);

        Integer userRole = usersVO.getUserRole();
        if (userRole != 1) throw new CommonException(CodeEnum.ERROR_UNAUTHORIZED);

        return true;
    }
}
