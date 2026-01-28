package com.yuwan.picture.mapper;

import com.yuwan.picture.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 30391
 * @description 针对表【user(用户)】的数据库操作Mapper
 * @createDate 2026-01-28 16:20:01
 * @Entity com.yuwan.picture.model.entity.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




