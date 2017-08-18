package com.td.oldplay.http.api;

/**
 * Created by my on 2017/7/7.
 */

public class NetWorkAPI {

    /**
     * 请求的基本路径
     */
    public static final String BASE_URL = "http://172.16.0.205:8080/";
  // public static final String BASE_URL = "http://222.187.223.4:8081/";

    /**
     * 老师数据
     */
    public final static String GET_MORE_THEACHER_LIST = "app/t_students/listTeacherForMore/";


    public final static String REGISTER_API = "lnrzb/user/userRegister";

    public final static String BOUND_USER_API = "lnrzb/user/bindPhone";

    public final static String IS_BOUND_API = "lnrzb/user/checkBindPhone";

    public final static String LOGIN_API = "lnrzb/user/userLogin";

    public final static String GETCODE_API = "lnrzb/user/sendvcode";


    public final static String UPDATEPWS_API = "lnrzb/user/revisePassword";

    public final static String FORGETPWS_API = "lnrzb/user/forgetPassword";

    public final static String LOGINOUT_API = "lnrzb/user/exituser";

    public final static String UPDATEUSE_API = "lnrzb/user/updateUser";


    public final static String COURSE_TYPE_API = "lnrzb/courses/findCoursesType";

    public final static String COURSE_RECOMMENT_API = "lnrzb/courses/findCommendCourses";

    public final static String COURSE_HOT_API = "lnrzb/courses/findHotCourses";
    /**
     * 课程下的老师
     */
    public final static String COURSE_TEACHER_API = "lnrzb/courses/findteacherByCourseId";

    /**
     * 老师所对应的课程
     */
    public final static String TEACHER_COURSE_API = "lnrzb/courses/findCoursesByUserId";

    public final static String COURSE_COMMENT_API = "lnrzb/courses/findTeacherComment";

    public final static String SEARCH_C_T_API = "lnrzb/courses/findTeacherOrCourses";

    public final static String HOME_COURS_API = "lnrzb/courses/toCourseHomePage";

    public final static String TEACHER_DETAIL_API = "lnrzb/courses/findDescribes";

    public final static String SHOP_TEACHER_API = "lnrzb/courses/findGoodsByUserId";

    public final static String CONCERN_TEACHER_API = "lnrzb/courses/followTeacher";

    public final static String IS_TEACHER_API = "lnrzb/courses/isFollow";

    public final static String OPEN_COURSE_API = "lnrzb/courses/applyCourses";

    public final static String COMMENT_TEACHER_API = "lnrzb/courses/sendTeacherComment";
    // 商品相关

    public final static String SHOP_RECOMMENT_API = " lnrzb/goods/findCommendGoods";
    public final static String SHOP_DISCOUNT_API = " lnrzb/goods/findPreferentialGoods";

    public final static String SHOP_COMMENT_API = "lnrzb/goods/findGoodsAssess";

    // 产片分类
    public final static String FIND_SHOPBYID_API = "lnrzb/goods/findGoodsByType";

    /**
     * 商品详情
     */
    public final static String SHOP_DETAIL_API = "lnrzb/goods/findGoodsById";

    public final static String GET_COMMENTS_API = "lnrzb/goods/findGoodsAssess";
    public final static String CREATE_ORDER_API = "lnrzb/order/createOrderByGoods";
    public final static String ADD_CAR_API = "lnrzb/cart/saveCart";

    public final static String GET_CARS_API = "lnrzb/cart/findCartByUserId";

    public final static String DELET_CAR_API = "lnrzb/cart/deleteCart";

    public final static String UPDATE_CARs_API = "lnrzb/cart/updateCart";

    public final static String HOME_SHOP_API = " lnrzb/goods/toGoodsHomePage";

    public final static String CREATE_ORDER_CAR_API = "lnrzb/order/createOrder";


    public final static String APPLY_SCORE_API = "lnrzb/order/employScore";

    public final static String SEACHER_SHOP_API = "lnrzb/goods/findGoodsByName";

    public final static String GET_SHOPTYPES_API = "lnrzb/goods/findAllgoodsType";

    // 地址管理=====================================================

    public final static String GET_ADDRESS_API = "lnrzb/address/findAllAddress";
    public final static String UPDATE_ADDRESS_API = "lnrzb/address/updateAddress";
    public final static String DELETE_ADDRESS_API = "lnrzb/address/delAddress";
    public final static String ADD_ADDRESS_API = "lnrzb/address/saveAddress";
    public final static String SET_ADDRESS_API = "lnrzb/address/setDefault";

    // 我的相关==================
    public final static String GET_MYCONCERNS_API = "lnrzb/userfocus/getUserFocus";
    public final static String GET_MYCOURSES_API = "lnrzb/userCourses/getUserCourses";
    public final static String GET_MYORDERS_API = "lnrzb/mine/mineOrder";

    public final static String COMMENT_SHOP_API = "lnrzb/mine/sendGoodsComment";

    public final static String GET_MYWALLET_API = "lnrzb/mine/myPurse";

    public final static String ORDER_DETAIL_API = "lnrzb/mine/mineOrderDetail";

    public final static String CONFIRM_ORDER_API = "lnrzb/mine/takeOveGoods";
    public final static String GET_FORUM_API = "lnrzb/mine/findMineTopic";

    public final static String FEED_BACK_API = "lnrzb/mine/feedbackPropose";

    public final static String MY_WALLET_DETAIL_API = "lnrzb/mine/findRechargeSetById";

    // 论坛相关===========================
    public final static String GET_HOMR_FORUMS_API = "lnrzb/forum/findAllBoard";


    public final static String GET_FORUMS_BY_TYPE_API = "lnrzb/forum/findTopicByBoardId";

    public final static String FORUMS_DETAIL_API = "lnrzb/forum/findTopicDetail";

    public final static String FORUMS_LIKE_API = "lnrzb/forum/addLikeTopic";

    public final static String GET_FORUM_COMMENT_API = "lnrzb/forum/findTopicComment";

    public final static String SEND_COMMENT_API = "lnrzb/forum/sendComment";

    public final static String POST_FORUMS_CONTENT_API = "lnrzb/forum/sendTopic";
    public final static String POST_FORUMS_PIC_API = "lnrzb/forum/uploadImages";
    public final static String POST_FORUMS_VIDEO_API = "lnrzb/forum/uploadVideos";
    public final static String POST_FORUMS_VOICE_API = "lnrzb/forum/uploadSpeechs";




    public final static String GET_PAY_SCORETYPE = "lnrzb/pay/api/findScoreOffsetRule";

    public final static String SET_JOIN_MONEY = "lnrzb/pay/api/setConnectPrice";

    public final static String GET_JOIN_MONEY = "lnrzb/pay/api/findConnectPrice";

    public final static String GET_PAY_ACCOUNT = "lnrzb/pay/api/toSurplusPay";

    public final static String GET_LOGISTICS_AIP= "lnrzb/kuaidi/api/expressDelivery";


    public final static String CREAT_LIV_ROOM_AIP= "lnrzb/live/anchorEstablishRoom";

    public final static String QUIT_LIV_ROOM_AIP= "lnrzb/live/anchorQuit";

    public final static String GET_SEE_LIVES_AIP= "lnrzb/live/findAudience";

    public final static String QUIT_ROOM_AIP= "lnrzb/live/audienceQuit";


    public final static String PAY_WECHAT_AIP= "lnrzb/pay/api/weixinPay";

    public final static String PAY_APLIAY_AIP= "lnrzb/pay/api/zhifubaoPay";

    public final static String PAY_ACOUNT_AIP= "lnrzb/pay/api/surplusPay";
}
