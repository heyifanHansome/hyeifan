package com.stylefeng.guns.modular.userResume.service.impl;

import com.stylefeng.guns.modular.system.model.UserResume;
import com.stylefeng.guns.modular.system.dao.UserResumeMapper;
import com.stylefeng.guns.modular.userResume.service.IUserResumeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户简历管理表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@Service
public class UserResumeServiceImpl extends ServiceImpl<UserResumeMapper, UserResume> implements IUserResumeService {

    @Override
    public List<Map<String, Object>> list(String condition) {
        return this.baseMapper.list(condition);
    }
}
