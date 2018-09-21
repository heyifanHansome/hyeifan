package com.stylefeng.guns.modular.cloumnType.service.impl;

import com.stylefeng.guns.modular.system.model.ColumnType;
import com.stylefeng.guns.modular.system.dao.ColumnTypeMapper;
import com.stylefeng.guns.modular.cloumnType.service.IColumnTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 栏目分类表column_type（如：作品、资讯（匠厨专访、活动报道、热门话题）、活动（交流会、比赛、拜师会）、课堂、招聘这些分类） 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-21
 */
@Service
public class ColumnTypeServiceImpl extends ServiceImpl<ColumnTypeMapper, ColumnType> implements IColumnTypeService {

}
