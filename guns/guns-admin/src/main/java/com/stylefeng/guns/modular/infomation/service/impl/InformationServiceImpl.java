package com.stylefeng.guns.modular.infomation.service.impl;

import com.stylefeng.guns.modular.system.model.Information;
import com.stylefeng.guns.modular.system.dao.InformationMapper;
import com.stylefeng.guns.modular.infomation.service.IInformationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
资讯管理表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-21
 */
@Service
public class InformationServiceImpl extends ServiceImpl<InformationMapper, Information> implements IInformationService {

    @Override
    public List<Map<String, Object>> list(String condition) {
        return this.baseMapper.list(condition);
    }
}
