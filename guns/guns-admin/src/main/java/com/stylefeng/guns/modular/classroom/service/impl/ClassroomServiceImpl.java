package com.stylefeng.guns.modular.classroom.service.impl;

import com.stylefeng.guns.modular.system.model.Classroom;
import com.stylefeng.guns.modular.system.dao.ClassroomMapper;
import com.stylefeng.guns.modular.classroom.service.IClassroomService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 星厨课堂 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-09-20
 */
@Service
public class ClassroomServiceImpl extends ServiceImpl<ClassroomMapper, Classroom> implements IClassroomService {

    @Override
    public List<Map<String, Object>> list(String condition) {
        return this.baseMapper.list(condition);
    }
}
