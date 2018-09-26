package com.stylefeng.guns.modular.userWork.service.impl;

import com.stylefeng.guns.modular.system.model.UserWork;
import com.stylefeng.guns.modular.system.dao.UserWorkMapper;
import com.stylefeng.guns.modular.userWork.service.IUserWorkService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 作品关联表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@Service
public class UserWorkServiceImpl extends ServiceImpl<UserWorkMapper, UserWork> implements IUserWorkService {

    @Override
    public List<Map<String, Object>> list(String condition) {
        return this.baseMapper.list(condition);
    }
}
