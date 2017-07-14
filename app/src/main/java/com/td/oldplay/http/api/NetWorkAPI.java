package com.td.oldplay.http.api;

/**
 * Created by my on 2017/7/7.
 */

public class NetWorkAPI {

    /**
     *  请求的基本路径
     */
    public static final String BASE_URL ="http://172.16.0.203:8080/" ;

    /**
     *  老师数据
     */
    public final static String GET_MORE_THEACHER_LIST = "app/t_students/listTeacherForMore/";


    public final static String REGISTER_API = "lnrzb/user/userRegister";

    public final static String LOGIN_API = "lnrzb/user/userLogin";

    public final static String GETCODE_API = "lnrzb/user/sendvcode";
}
