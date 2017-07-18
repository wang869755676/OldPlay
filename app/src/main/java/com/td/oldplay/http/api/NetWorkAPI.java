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


    public final static String UPDATEPWS_API = "lnrzb/user/revisePassword";

    public final static String FORGETPWS_API = "lnrzb/user/forgetPassword";

    public final static String UPDATEUSE_API = "lnrzb/user/updateUser";


    public final static String COURSE_TYPE_API = "lnrzb/courses/findCoursesType";

    public final static String COURSE_RECOMMENT_API = "lnrzb/courses/findCommendCourses";

    public final static String COURSE_HOT_API = "lnrzb/courses/findHotCourses";
    /**
     *  课程下的老师
     */
    public final static String COURSE_TEACHER_API = "lnrzb/courses/findteacherByCourseId";

    /**
     *  老师所对应的课程
     */
    public final static String TEACHER_COURSE_API = "lnrzb/courses/findCoursesByUserId";

    public final static String COURSE_COMMENT_API = "lnrzb/courses/findTeacherComment";

    public final static String SEARCH_C_T_API = "lnrzb/courses/findTeacherOrCourses";

    public final static String HOME_COURS_API = "lnrzb/courses/toCourseHomePage";

    public final static String TEACHER_DETAIL_API = "lnrzb/courses/findDescribes";

    public final static String SHOP_TEACHER_API = "lnrzb/courses/findGoodsByUserId";
    // 商品相关

    public final static String SHOP_RECOMMENT_API = " lnrzb/goods/findCommendGoods";


    public final static String SHOP_COMMENT_API = "lnrzb/goods/findGoodsAssess";

    // 产片分类
    public final static String FIND_SHOPBYID_API = "lnrzb/goods/findGoodsByType";

    /**
     *  商品详情
     */
    public final static String SHOP_DETAIL_API = "lnrzb/goods/findGoodsById";




    // 地址管理=====================================================

    public final static String GET_ADDRESS_API = "lnrzb/address/findAllAddress";
    public final static String UPDATE_ADDRESS_API = "lnrzb/address/updateAddress";
    public final static String DELETE_ADDRESS_API = "lnrzb/address/delAddress";
    public final static String ADD_ADDRESS_API = "lnrzb/address/saveAddress";
    public final static String SET_ADDRESS_API = "lnrzb/address/setDefault";


}
