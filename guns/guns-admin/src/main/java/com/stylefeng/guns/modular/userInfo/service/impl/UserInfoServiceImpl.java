package com.stylefeng.guns.modular.userInfo.service.impl;

import com.stylefeng.guns.modular.system.model.UserInfo;
import com.stylefeng.guns.modular.system.dao.UserInfoMapper;
import com.stylefeng.guns.modular.userInfo.service.IUserInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户详情表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    @Override
    public List<Map<String, Object>> list(String condition) {
        return this.baseMapper.list(condition);
    }
}
