package com.stylefeng.guns.api.user.classroom;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.stylefeng.guns.core.util.ResultMsg;
import com.stylefeng.guns.modular.classroom.service.IClassroomService;
import com.stylefeng.guns.modular.system.model.Classroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Created by Heyifan Cotter on 2018/10/13.
 */
@RequestMapping("classroomApi")
@RestController
public class classroomApiInteface {
    @Autowired
    private IClassroomService classroomService;

    /**
     * 根据传入的栏目id获得当前的所有数据
     * @param value 前台传入的id
     * @return  根据当前的id 获得当前数据
     */
    @RequestMapping("getAllClassRoom/{value}")
    ResultMsg getAllClassRoom(@PathVariable("value") Integer value) {
        try {
            PageHelper pageHelper = new PageHelper();
            pageHelper.startPage(1,2);

            EntityWrapper<Classroom> entityWrapper = new EntityWrapper<>();
            entityWrapper.where("column_id", value);
            List<Classroom> classrooms = classroomService.selectList(entityWrapper);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.success("获取失败!","code : 500",HttpStatus.BAD_REQUEST);
        }
        return ResultMsg.success("获取成功!","code : 200", HttpStatus.OK);
    }


}
