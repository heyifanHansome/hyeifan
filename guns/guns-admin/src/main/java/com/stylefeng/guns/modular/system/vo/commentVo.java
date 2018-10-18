package com.stylefeng.guns.modular.system.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Heyifan Cotter on 2018/10/18.
 * 评论实体类封装
 */

@Getter
@Setter
@ToString
public class commentVo{
    private String username ;
    private String avatar;
    private String beforetime;
    private String content;
    private String like;
}
