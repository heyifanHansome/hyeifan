package com.stylefeng.guns.modular.system.service.impl;

import com.stylefeng.guns.modular.system.model.UserApi;
import com.stylefeng.guns.modular.system.dao.UserApiMapper;
import com.stylefeng.guns.modular.system.service.IUserApiService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 前台用户表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-10
 */
@Service
public class UserApiServiceImpl extends ServiceImpl<UserApiMapper, UserApi> implements IUserApiService {

}
