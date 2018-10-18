package com.stylefeng.guns.modular.userCommentModel.service.impl;

import com.stylefeng.guns.modular.system.model.UserComment;
import com.stylefeng.guns.modular.system.dao.UserCommentMapper;
import com.stylefeng.guns.modular.system.vo.commentVo;
import com.stylefeng.guns.modular.userCommentModel.service.IUserCommentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * <p>
 * 用户评论表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-12
 */
@Service
public class UserCommentServiceImpl extends ServiceImpl<UserCommentMapper, UserComment> implements IUserCommentService {

   public  List<commentVo> selectCommentByUserId(Integer columnId,Integer userId){
        return  this.baseMapper.selectCommentByUserId(columnId,userId);
    }
}
