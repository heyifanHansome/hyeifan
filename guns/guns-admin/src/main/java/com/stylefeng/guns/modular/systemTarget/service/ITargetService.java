package com.stylefeng.guns.modular.systemTarget.service;

import com.stylefeng.guns.core.node.ZTreeNode;
import com.stylefeng.guns.modular.system.model.Target;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 会员等级评价指标体系 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-16
 */
public interface ITargetService extends IService<Target> {
    /**
     * 获取列表树
     *
     * @return
     * @date 2017年2月19日 下午1:33:51
     */
    List<ZTreeNode> targetTreeList();
}
