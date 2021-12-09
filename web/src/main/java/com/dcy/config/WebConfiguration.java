package com.dcy.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.URLUtil;
import com.dcy.system.model.Resource;
import com.dcy.system.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2019/11/8 8:47
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private ResourceService resourcesService;

    @Value("${ignored}")
    private List<String> ignored;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // token拦截器 获取用户信息
        registry.addInterceptor(new SaRouteInterceptor((request, response, handler) -> {
            String url = URLUtil.getPath(request.getRequestPath());
            // 拦截所有URL请求
            SaRouter.match("/**", StpUtil::checkLogin);
            // 获取所有的路由表
            List<Resource> resourcesList = resourcesService.getRouterList();
            for (Resource resource : resourcesList) {
                // 如果URL匹配成功就不循环了，直接退出循环
                // 如果匹配不成功就匹配 /** URL路径了
                if (antPathMatcher.match(resource.getResPath(), url) && request.getMethod().equalsIgnoreCase(resource.getHttpMethod())) {
                    SaRouter.match(resource.getResPath(), () -> StpUtil.checkPermission(resource.getPermission()));
                    break;
                }
            }
        })).addPathPatterns("/**").excludePathPatterns(ignored);
    }
}
