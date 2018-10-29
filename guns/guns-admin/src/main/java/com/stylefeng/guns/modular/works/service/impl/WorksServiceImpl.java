package com.stylefeng.guns.modular.works.service.impl;

import com.stylefeng.guns.modular.system.model.Works;
import com.stylefeng.guns.modular.system.dao.WorksMapper;
import com.stylefeng.guns.modular.works.service.IWorksService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 作品管理表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@Service
public class WorksServiceImpl extends ServiceImpl<WorksMapper, Works> implements IWorksService {

    @Override
    public List<Map<String, Object>> list(String condition) {
        return this.baseMapper.list(condition);
    }

    @Override
    public List<Works> findByCommdCretaeTime(Date createTime) {
        return this.baseMapper.findByCommdCretaeTime(createTime);
    }
}
