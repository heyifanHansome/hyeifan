package com.stylefeng.guns.modular.club.service.impl;

import com.stylefeng.guns.modular.system.model.Club;
import com.stylefeng.guns.modular.system.dao.ClubMapper;
import com.stylefeng.guns.modular.club.service.IClubService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 星厨俱乐部 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@Service
public class ClubServiceImpl extends ServiceImpl<ClubMapper, Club> implements IClubService {

    @Override
    public List<Map<String, Object>> list(String condition) {
        return this.baseMapper.list(condition);
    }
}
