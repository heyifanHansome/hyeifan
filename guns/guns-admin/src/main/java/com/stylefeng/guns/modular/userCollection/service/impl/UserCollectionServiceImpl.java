package com.stylefeng.guns.modular.userCollection.service.impl;

import com.stylefeng.guns.modular.system.model.UserCollection;
import com.stylefeng.guns.modular.system.dao.UserCollectionMapper;
import com.stylefeng.guns.modular.userCollection.service.IUserCollectionService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户收藏管理表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@Service
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection> implements IUserCollectionService {

}
