package com.stylefeng.guns.modular.banner.service.impl;

import com.stylefeng.guns.modular.system.model.Banner;
import com.stylefeng.guns.modular.system.dao.BannerMapper;
import com.stylefeng.guns.modular.banner.service.IBannerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 广告(后期跳转h5页面时,还要记录这个接口,或者这个广告被访问了多少次) 服务实现类
 * </p>
 *
 * @author 李俊
 * @since 2018-10-22
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {

}
