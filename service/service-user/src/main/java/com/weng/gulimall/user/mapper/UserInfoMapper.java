package com.weng.gulimall.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weng.gulimall.model.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lingzi
* @description 针对表【user_info(用户表)】的数据库操作Mapper
* @createDate 2022-09-12 00:15:13
* @Entity com.weng.gulimall.user.domain.UserInfo
*/
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}




