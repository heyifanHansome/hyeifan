package com.stylefeng.guns.modular.city.service.impl;

import com.stylefeng.guns.modular.system.model.City;
import com.stylefeng.guns.modular.system.dao.CityMapper;
import com.stylefeng.guns.modular.city.service.ICityService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 城市管理表city 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements ICityService {

    @Override
    public List<Map<String, Object>> list(String condition) {
        return this.baseMapper.list(condition);
    }
}
