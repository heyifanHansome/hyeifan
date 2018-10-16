package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.core.node.ZTreeNode;
import com.stylefeng.guns.modular.system.model.Target;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 会员等级评价指标体系 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-16
 */
public interface TargetMapper extends BaseMapper<Target> {
    /**
     * 获取菜单列表树
     *
     * @return
     * @date 2017年2月19日 下午1:33:51
     */
    List<ZTreeNode> targetTreeList();
}
