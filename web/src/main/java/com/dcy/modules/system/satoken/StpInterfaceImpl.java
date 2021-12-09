package com.dcy.modules.system.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.dcy.modules.system.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author：dcy
 * @Description: 自定义权限验证接口扩展
 * @Date: 2021/4/7 8:15
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private AuthService authService;

    /**
     * 返回账号拥有的权限码
     *
     * @param loginId
     * @param loginKey
     * @return
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginKey) {
        return authService.getResourcesCodeByUserId((String) loginId);
    }

    /**
     * 返回账号拥有的角色标识
     *
     * @param loginId
     * @param loginKey
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginKey) {
        return authService.getAuthRoleKeyByUserId((String) loginId);
    }


}
