package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.UserComment;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.modular.system.vo.commentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户评论表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-12
 */
public interface UserCommentMapper extends BaseMapper<UserComment> {


    List<commentVo> selectCommentByUserId(@Param("userId") Integer userId,@Param("columnId") Integer columnId);
}
