package cn.lin037.monitor.config;

import cn.lin037.monitor.interceptor.AuthorityInterceptor;
import cn.lin037.monitor.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebBaseConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final AuthorityInterceptor authorityInterceptor;

    @Autowired
    public WebBaseConfig(LoginInterceptor loginInterceptor, AuthorityInterceptor authorityInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.authorityInterceptor = authorityInterceptor;
    }

    /**
     * 设置跨域访问
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有请求路径跨域访问
                .allowedOriginPatterns("*") // 允许哪些域名进行跨域访问
                .allowedMethods("*") // 允许的请求方法类型
                .allowCredentials(true) // 是否携带Cookie，默认false
                .maxAge(3600) // 预检请求的缓存时间（单位：秒）
                .allowedHeaders("*"); // 允许的请求头类型
    }

    /**
     * 配置自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/images/**")
                .excludePathPatterns("/users/login", "/users/register");
        registry.addInterceptor(authorityInterceptor)
                .addPathPatterns("/users/**")
                .addPathPatterns("/plants/**")
                .excludePathPatterns("/users/getAuthority")
                .excludePathPatterns("/plants/search/*")
                .excludePathPatterns("/plants/count")
                .excludePathPatterns("/plants/get/*")
                .excludePathPatterns("/users/login", "/users/logout", "/users/selfMsg", "/users/updateSelf", "/users/register");
    }
}
