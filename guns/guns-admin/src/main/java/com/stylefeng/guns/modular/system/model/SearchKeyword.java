package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 关键词管理表,前台获取按照 order by orders desc,hit_num desc 排序
 * </p>
 *
 * @author 李俊
 * @since 2018-10-30
 */
@TableName("sys_search_keyword")
@Getter
@Setter
@ToString
public class SearchKeyword extends Model<SearchKeyword> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 关键词内容
     */
    private String content;
    /**
     * 命中次数
     */
    @TableField("hit_num")
    private Integer hitNum;
    /**
     * 排序值
     */
    private String orders;
    private Timestamp submit_time;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
