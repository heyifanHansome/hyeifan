package com.stylefeng.guns.modular.resumeLive.service.impl;

import com.stylefeng.guns.modular.system.model.ResumeLive;
import com.stylefeng.guns.modular.system.dao.ResumeLiveMapper;
import com.stylefeng.guns.modular.resumeLive.service.IResumeLiveService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户经历管理表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@Service
public class ResumeLiveServiceImpl extends ServiceImpl<ResumeLiveMapper, ResumeLive> implements IResumeLiveService {


    @Override
    public List<Map<String, Object>> list(String condition) {
        return this.baseMapper.list(condition);
    }
}
