package com.dcy.modules.system.dtomapper;

import com.dcy.modules.system.dto.input.*;
import com.dcy.modules.system.dto.output.LoginOutputDTO;
import com.dcy.modules.system.dto.output.UserInfoListExcelOutputDTO;
import com.dcy.modules.system.dto.output.UserInfoListOutputDTO;
import com.dcy.system.model.UserInfo;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/10/23 9:28
 */
@Mapper(componentModel = "spring")
public interface MUserInfoMapper {

    /**
     * 登录转换
     *
     * @param loginInputDTO
     * @return
     */
//    @Mappings({
//            @Mapping(source = "username",target = "username"),  //相同可以不写
//            @Mapping(source = "a字段",target = "b字段")  //不同的时候
//    })
//    UserInfo loginInputDTOToUserInfo(LoginInputDTO loginInputDTO);

    UserInfo userInfoCreateInputDTOToUserInfo(UserInfoCreateInputDTO userInfoCreateInputDTO);

    UserInfo userInfoUpdateInputDTOToUserInfo(UserInfoUpdateInputDTO userInfoUpdateInputDTO);

    UserInfo userInfoUpdateInfoInputDTOToUserInfo(UserInfoUpdateInfoInputDTO userInfoUpdateInfoInputDTO);

    UserInfo userInfoResetPassInputDTOToUserInfo(UserInfoResetPassInputDTO userInfoResetPassInputDTO);


    UserInfo userInfoSearchInputDTOToUserInfo(UserInfoSearchInputDTO userInfoSearchInputDTO);

    UserInfoListOutputDTO userInfoToUserInfoListOutputDTO(UserInfo userInfo);

    List<UserInfoListOutputDTO> userInfosToUserInfoListOutputDTOs(List<UserInfo> userInfos);


    LoginOutputDTO userInfoToLoginOutputDTO(UserInfo userInfo);

    UserInfoListExcelOutputDTO toExcel(UserInfo userInfo);

    List<UserInfoListExcelOutputDTO> toExcel(List<UserInfo> userInfo);
}
