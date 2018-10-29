package com.stylefeng.guns.modular.follow.service.impl;

import com.stylefeng.guns.modular.system.model.Follow;
import com.stylefeng.guns.modular.system.dao.FollowMapper;
import com.stylefeng.guns.modular.follow.service.IFollowService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 关注关系表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-29
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

}
