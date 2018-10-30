package com.stylefeng.guns.modular.fans.service.impl;

import com.stylefeng.guns.modular.system.model.Fans;
import com.stylefeng.guns.modular.system.dao.FansMapper;
import com.stylefeng.guns.modular.fans.service.IFansService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 粉丝表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-29
 */
@Service
public class FansServiceImpl extends ServiceImpl<FansMapper, Fans> implements IFansService {

}
