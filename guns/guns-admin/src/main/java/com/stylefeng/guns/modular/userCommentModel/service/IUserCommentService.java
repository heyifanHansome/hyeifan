package com.stylefeng.guns.modular.userCommentModel.service;

import com.stylefeng.guns.modular.system.model.UserComment;
import com.baomidou.mybatisplus.service.IService;
import com.stylefeng.guns.modular.system.vo.commentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户评论表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-12
 */
public interface IUserCommentService extends IService<UserComment> {

    List<commentVo> selectCommentByUserId(@Param("userId")Integer userId, @Param("columnId")Integer columnId);
}
