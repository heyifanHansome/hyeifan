package com.stylefeng.guns.api.testLambda;


/**
 * Created by Heyifan Cotter on 2018/10/19.
 */
public class Java8Tester {

    public static void main(String[] args) {
        Java8Tester tester = new Java8Tester();
        // 类型声明
        heyfianTool heyfianTool = (Integer a, Integer b) -> a - b;
        heyfianTool delete = (Integer c, Integer d) -> c + d;
        System.out.println(tester.addHeyifan(10, 5, heyfianTool));
        System.out.println(tester.addHeyifan(10, 5, delete));
    }

    interface heyfianTool {
        Integer addHeyifan(Integer a, Integer b);
    }

    private Integer addHeyifan(Integer a, Integer b, heyfianTool heyfianTool) {
        return heyfianTool.addHeyifan(a, b);
    }
}
