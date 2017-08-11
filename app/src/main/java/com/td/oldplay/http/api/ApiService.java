package com.td.oldplay.http.api;


import com.td.oldplay.bean.AddressBean;
import com.td.oldplay.bean.CommentBean;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.CourseTypeBean;
import com.td.oldplay.bean.CreateOrder;
import com.td.oldplay.bean.ForumBean;
import com.td.oldplay.bean.ForumDetial;
import com.td.oldplay.bean.ForumType;
import com.td.oldplay.bean.HomeCourseInfo;
import com.td.oldplay.bean.HomeShopInfo;
import com.td.oldplay.bean.LogisticsBean;
import com.td.oldplay.bean.OrderBean;
import com.td.oldplay.bean.OrderDetail;
import com.td.oldplay.bean.PayAccountBefore;
import com.td.oldplay.bean.RechargeInfo;
import com.td.oldplay.bean.ScoreOffset;
import com.td.oldplay.bean.SearchCourse;
import com.td.oldplay.bean.ShopBean;
import com.td.oldplay.bean.ShopCarBean;
import com.td.oldplay.bean.ShopDetail;
import com.td.oldplay.bean.ShopType;
import com.td.oldplay.bean.TeacherBean;
import com.td.oldplay.bean.TeacherDetail;
import com.td.oldplay.bean.TestBean;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.bean.WalletBean;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface ApiService {

    @FormUrlEncoded
    @POST("query?key=7c2d1da3b8634a2b9fe8848c3a9edcba")
    Observable<ApiResponse<TestBean>> getDatas(@Field("pno") int pno, @Field("ps") int ps, @Field("dtype") String dtype);

    @GET(NetWorkAPI.GET_MORE_THEACHER_LIST + "/{data}")
    Observable<ApiResponse<List<TeacherBean>>> getTecherLists(@Path("data") String data);

    @FormUrlEncoded
    @POST(NetWorkAPI.REGISTER_API)
    Observable<ApiResponse<String>> registerUser(@FieldMap() HashMap<String, Object> maps);

    @FormUrlEncoded
    @POST(NetWorkAPI.LOGIN_API)
    Observable<ApiResponse<UserBean>> loginUser(@FieldMap() HashMap<String, Object> maps);

    @FormUrlEncoded
    @POST(NetWorkAPI.GETCODE_API)
    Observable<ApiResponse<String>> getCode(@Field("phone") String phone);

    @FormUrlEncoded
    @POST(NetWorkAPI.UPDATEPWS_API)
    Observable<ApiResponse<String>> modifyLoginPws(@FieldMap() HashMap<String, Object> maps);


    @Multipart
    @POST(NetWorkAPI.UPDATEUSE_API)
    Observable<ApiResponse<UserBean>> modifyUser(@PartMap() HashMap<String, RequestBody
            > maps);

    @FormUrlEncoded
    @POST(NetWorkAPI.FORGETPWS_API)
    Observable<ApiResponse<String>> forgetPws(@FieldMap() HashMap<String, Object> maps);

    @POST(NetWorkAPI.LOGINOUT_API)
    Observable<ApiResponse<String>> loginOut();

    @FormUrlEncoded
    @POST(NetWorkAPI.COURSE_TYPE_API)
    Observable<ApiResponse<List<CourseTypeBean>>> getCourseTypes(@Field("parentId") String parentId, @Field("page") int page);

    @FormUrlEncoded
    @POST(NetWorkAPI.COURSE_RECOMMENT_API)
    Observable<ApiResponse<List<CourseTypeBean>>> getCourseRecomments(@Field("page") int page);

    @FormUrlEncoded
    @POST(NetWorkAPI.COURSE_HOT_API)
    Observable<ApiResponse<List<CourseTypeBean>>> getCourseHots(@Field("page") int page);

    @FormUrlEncoded
    @POST(NetWorkAPI.COURSE_TEACHER_API)
    Observable<ApiResponse<List<TeacherBean>>> getTeachersInCourse(@Field("page") int page, @Field("coursesTypeId") String coursesTypeId);

    @FormUrlEncoded
    @POST(NetWorkAPI.TEACHER_COURSE_API)
    Observable<ApiResponse<List<CourseBean>>> getcoursesInTeacher(@Field("page") int page, @Field("userId") String userId, @Field("teacherId") String teacherId, @Field("coursesTypeId") String coursesTypeId);

    @FormUrlEncoded
    @POST(NetWorkAPI.COURSE_COMMENT_API)
    Observable<ApiResponse<List<CommentBean>>> getCommentsInTeacher(@Field("page") int page, @Field("teacherId") String userId);

    @FormUrlEncoded
    @POST(NetWorkAPI.COURSE_COMMENT_API)
    Observable<ApiResponse<List<CommentBean>>> getCommentsInTeacher(@Field("teacherId") String userId);


    @FormUrlEncoded
    @POST(NetWorkAPI.SEARCH_C_T_API)
    Observable<ApiResponse<List<CourseTypeBean>>> searchCommentsTeachers(@Field("name") String userId, @Field("type") int type);


    @POST(NetWorkAPI.HOME_COURS_API)
    Observable<ApiResponse<HomeCourseInfo>> getHomeCourse();


    @FormUrlEncoded
    @POST(NetWorkAPI.TEACHER_DETAIL_API)
    Observable<ApiResponse<TeacherDetail>> getCourseDetail(@Field("teacherId") String teacherId,@Field("coursesTypeId") String coursesTypeId);

    @FormUrlEncoded
    @POST(NetWorkAPI.SHOP_TEACHER_API)
    Observable<ApiResponse<List<ShopBean>>> getShopInTeacher(@Field("userId") String userId, @Field("page") int page, @Field("type") int type, @Field("sort") int sort);

    @FormUrlEncoded
    @POST(NetWorkAPI.CONCERN_TEACHER_API)
    Observable<ApiResponse<String>> concernTeacher(@Field("userId") String userId, @Field("teacherId") String teacherId);

    @FormUrlEncoded
    @POST(NetWorkAPI.COMMENT_TEACHER_API)
    Observable<ApiResponse<String>> commentTeacher(@Field("userId") String userId, @Field("teacherId") String teacherId, @Field("content") String content);

    @FormUrlEncoded
    @POST(NetWorkAPI.IS_TEACHER_API)
    Observable<ApiResponse<Integer>> isConcernTeacher(@Field("userId") String userId, @Field("teacherId") String teacherId);

    @FormUrlEncoded
    @POST(NetWorkAPI.OPEN_COURSE_API)
    Observable<ApiResponse<String>> openCourse(@Field("userId") String userId, @Field("coursesId") String coursesId);


    // =================商品============
    @FormUrlEncoded
    @POST(NetWorkAPI.FIND_SHOPBYID_API)
    Observable<ApiResponse<List<ShopBean>>> getShopByType(@Field("page") int page, @Field("goodsTypeId") int type, @Field("type") int Sortype, @Field("sort") int sort);

    /*   @FormUrlEncoded
       @POST(NetWorkAPI.SHOP_COMMENT_API)
       Observable<ApiResponse<List<ShopBean>>> getShopHots(int page);*/
    @FormUrlEncoded
    @POST(NetWorkAPI.SHOP_RECOMMENT_API)
    Observable<ApiResponse<List<ShopBean>>> getShopRecomments(@Field("page") int page, @Field("type") int type, @Field("sort") int sort);

    @FormUrlEncoded
    @POST(NetWorkAPI.SHOP_DISCOUNT_API)
    Observable<ApiResponse<List<ShopBean>>> getShopDiscounts(@Field("page") int page, @Field("type") int type, @Field("sort") int sort);


    @FormUrlEncoded
    @POST(NetWorkAPI.SHOP_DETAIL_API)
    Observable<ApiResponse<ShopDetail>> getShopDetail(@Field("goodsId") String shopId, @Field("userId") String userId);

    @FormUrlEncoded
    @POST(NetWorkAPI.GET_COMMENTS_API)
    Observable<ApiResponse<List<CommentBean>>> getShopComments(@Field("goodsId") String shopId, @Field("page") int page);

    @POST(NetWorkAPI.HOME_SHOP_API)
    Observable<ApiResponse<HomeShopInfo>> getHomeShop();

    @FormUrlEncoded
    @POST(NetWorkAPI.CREATE_ORDER_CAR_API)
    Observable<ApiResponse<List<OrderBean>>> createOrderCars(@Field("cartIds") List<String> carIds, @Field("userId") String userId);

    @FormUrlEncoded
    @POST(NetWorkAPI.APPLY_SCORE_API)
    Observable<ApiResponse<Float>> applyScore(@Field("userId") String userId, @Field("orderIds") List<String> orderIds);


    @FormUrlEncoded
    @POST(NetWorkAPI.CREATE_ORDER_API)
    Observable<ApiResponse<OrderBean>> createOrder(@FieldMap() HashMap<String, Object> maps);

    @FormUrlEncoded
    @POST(NetWorkAPI.ADD_CAR_API)
    Observable<ApiResponse<String>> addCar(@FieldMap() HashMap<String, Object> maps);

    @FormUrlEncoded
    @POST(NetWorkAPI.GET_CARS_API)
    Observable<ApiResponse<List<ShopCarBean>>> getCars(@Field("userId") String userId, @Field("page") int page);

    @FormUrlEncoded
    @POST(NetWorkAPI.DELET_CAR_API)
    Observable<ApiResponse<String>> deleteCars(@Field("cartId") String cartId);

    @FormUrlEncoded
    @POST(NetWorkAPI.UPDATE_CARs_API)
    Observable<ApiResponse<String>> updateCars(@Field("cartList") String datas);

    @FormUrlEncoded
    @POST(NetWorkAPI.GET_ADDRESS_API)
    Observable<ApiResponse<List<AddressBean>>> getAddress(@Field("page") int page, @Field("userId") String id);

    @FormUrlEncoded
    @POST(NetWorkAPI.UPDATE_ADDRESS_API)
    Observable<ApiResponse<String>> updateAddress(@FieldMap() HashMap<String, Object> params);

    @FormUrlEncoded
    @POST(NetWorkAPI.DELETE_ADDRESS_API)
    Observable<ApiResponse<String>> deleteAddress(@Field("addressId") String id);

    @FormUrlEncoded
    @POST(NetWorkAPI.SET_ADDRESS_API)
    Observable<ApiResponse<String>> setDefaultAddress(@Field("addressId") String id, @Field("userId") String userId);

    @FormUrlEncoded
    @POST(NetWorkAPI.ADD_ADDRESS_API)
    Observable<ApiResponse<String>> addAddress(@FieldMap() HashMap<String, Object> params);

    @FormUrlEncoded
    @POST(NetWorkAPI.GET_MYCOURSES_API)
    Observable<ApiResponse<List<CourseTypeBean>>> getMyCourses(@Field("userId") String id);

    @FormUrlEncoded
    @POST(NetWorkAPI.GET_MYCONCERNS_API)
    Observable<ApiResponse<List<TeacherBean>>> getMyConCerns(@Field("userId") String id);

    @FormUrlEncoded
    @POST(NetWorkAPI.GET_MYORDERS_API)
    Observable<ApiResponse<List<OrderBean>>> getMyOrders(@Field("userId") String id, @Field("page") int page);

    @FormUrlEncoded
    @POST(NetWorkAPI.SEACHER_SHOP_API)
    Observable<ApiResponse<List<ShopBean>>> searchShop(@FieldMap() HashMap<String, Object> params);


    @FormUrlEncoded
    @POST(NetWorkAPI.GET_SHOPTYPES_API)
    Observable<ApiResponse<List<ShopType>>> getShopTypesById(@Field("parentType") int parentType);
    //======================================luntan

    @POST(NetWorkAPI.GET_HOMR_FORUMS_API)
    Observable<ApiResponse<List<ForumType>>> getHomeForums();

    @FormUrlEncoded
    @POST(NetWorkAPI.GET_FORUMS_BY_TYPE_API)
    Observable<ApiResponse<List<ForumBean>>> getForumsNyId(@Field("boardId") String boardId, @Field("page") int page);


    @FormUrlEncoded
    @POST(NetWorkAPI.FORUMS_DETAIL_API)
    Observable<ApiResponse<ForumDetial>> getForumDetials(@Field("topicId") String topicId, @Field("userId") String userId);

    @FormUrlEncoded
    @POST(NetWorkAPI.FORUMS_LIKE_API)
    Observable<ApiResponse<String>> forumLikeAction(@Field("topicId") String topicId, @Field("userId") String userId, @Field("flag") boolean action);

    @FormUrlEncoded
    @POST(NetWorkAPI.GET_FORUM_COMMENT_API)
    Observable<ApiResponse<List<CommentBean>>> getForumComment(@Field("topicId") String id, @Field("page") int page);

    @FormUrlEncoded
    @POST(NetWorkAPI.SEND_COMMENT_API)
    Observable<ApiResponse<String>> sendComment(@FieldMap() HashMap<String, Object> params);

    @FormUrlEncoded
    @POST(NetWorkAPI.POST_FORUMS_CONTENT_API)
    Observable<ApiResponse<String>> postForumContent(@FieldMap() HashMap<String, Object> params);


    @Multipart
    @POST(NetWorkAPI.POST_FORUMS_PIC_API)
    Observable<ApiResponse<String>> postForumPic(@PartMap() HashMap<String, RequestBody> maps);


    @Multipart
    @POST(NetWorkAPI.POST_FORUMS_VIDEO_API)
    Observable<ApiResponse<String>> postForumVideo(@PartMap() HashMap<String, RequestBody> maps);


    @Multipart
    @POST(NetWorkAPI.POST_FORUMS_VOICE_API)
    Observable<ApiResponse<String>> postForumVoicec(@PartMap() HashMap<String, RequestBody> maps);


    //====================

    @FormUrlEncoded
    @POST(NetWorkAPI.GET_MYWALLET_API)
    Observable<ApiResponse<WalletBean>> getMyWallets(@FieldMap() HashMap<String, Object> maps);

    @FormUrlEncoded
    @POST(NetWorkAPI.MY_WALLET_DETAIL_API)
    Observable<ApiResponse<RechargeInfo>> walletDetail(@Field("id") String id);

    @FormUrlEncoded
    @POST(NetWorkAPI.GET_FORUM_API)
    Observable<ApiResponse<List<ForumBean>>> getMyForums(@Field("userId") String userId, @Field("page") int page);

    @FormUrlEncoded
    @POST(NetWorkAPI.FEED_BACK_API)
    Observable<ApiResponse<String>> feedBack(@Field("userId") String userId, @Field("content") String content);

    @FormUrlEncoded
    @POST(NetWorkAPI.COMMENT_SHOP_API)
    Observable<ApiResponse<String>> commentShop(@Field("userId") String userId, @Field("goodsId") List<String> goodsId, @Field("content") List<String> content);

    @FormUrlEncoded
    @POST(NetWorkAPI.ORDER_DETAIL_API)
    Observable<ApiResponse<OrderDetail>> orderDetails(@Field("orderId") String orderId);


    @FormUrlEncoded
    @POST(NetWorkAPI.CONFIRM_ORDER_API)
    Observable<ApiResponse<String>> confirmOrder(@Field("orderId") String orderId);


    @POST(NetWorkAPI.GET_PAY_SCORETYPE)
    Observable<ApiResponse<ScoreOffset>> getPayScoreRule();

    @FormUrlEncoded
    @POST(NetWorkAPI.SET_JOIN_MONEY)
    Observable<ApiResponse<String>> setJoinMoney(@Field("userId") String userId, @Field("price") String price);

    @FormUrlEncoded
    @POST(NetWorkAPI.GET_JOIN_MONEY)
    Observable<ApiResponse<Float>> getJoinMoney(@Field("userId") String userId);

    @FormUrlEncoded
    @POST(NetWorkAPI.GET_PAY_ACCOUNT)
    Observable<ApiResponse<PayAccountBefore>> getPayAccount(@Field("userId") String userId,@Field("totalPrice") String totalPrice,@Field("type") int type);

    @FormUrlEncoded
    @POST(NetWorkAPI.GET_LOGISTICS_AIP)
    Observable<ApiResponse<LogisticsBean>> getLogistics(@Field("logistic") String logistic, @Field("logisticnum") String logisticnum);


}
