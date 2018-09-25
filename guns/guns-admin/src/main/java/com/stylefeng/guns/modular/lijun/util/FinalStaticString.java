package com.stylefeng.guns.modular.lijun.util;

public class FinalStaticString {
	/**
	 * 后台没有权限拦截跳转页面
	 */
	public final static String MANAGE_NO_POTENCE_URI="/manage/main.htm";
	/**
	 * 后台未登录拦截跳转页面
	 */
	public final static String MANAGE_LOGIN_URI="/manage/tologin.htm";
	/**
	 * 前台未登录拦截跳转页面
	 */
	public final static String PAGE_LOGIN_URI="/page/tologin.htm";
	

	/**
	 * session中admin角色的key
	 */
	public final static String MANAGE_ADMIN="admin";
	/**
	 * session中admin管理权限的key
	 */
	public final static String ADMIN_GROUP_MENU_POTENCE="admin_potence";
	/**
	 * session中后台登录页面验证码的key
	 */
	public final static String MANAGE_CODE="manage_code";
	/**
	 * session中前台登录页面验证码的key
	 */
	public final static String PAGE_CODE="manage_code";
	/**
	 * session中前台用户的key
	 */
	public final static String PAGE_USER="page_user";


	public static final String FILEPATHIMG = "/lijun/zhyk/uploadFiles/uploadImgs/";		//图片上传路径
//	public static final String FILEPATHFILE = "/lijun/zhyk/uploadFiles/file/";			//文件上传路径

//	public static final String FILEPATHIMG = "D:/zhyk/uploadFiles/uploadImgs/";		//图片上传路径
	public static final String FILEPATHFILE = "D:/zhyk/uploadFiles/file/";			//文件上传路径
}
