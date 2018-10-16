package com.stylefeng.guns.modular.systemTarget.service.impl;

import com.stylefeng.guns.core.node.ZTreeNode;
import com.stylefeng.guns.modular.system.model.Target;
import com.stylefeng.guns.modular.system.dao.TargetMapper;
import com.stylefeng.guns.modular.systemTarget.service.ITargetService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 会员等级评价指标体系 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-16
 */
@Service
public class TargetServiceImpl extends ServiceImpl<TargetMapper, Target> implements ITargetService {

    @Override
    public List<ZTreeNode> targetTreeList() {
        return this.baseMapper.targetTreeList();
    }
}
