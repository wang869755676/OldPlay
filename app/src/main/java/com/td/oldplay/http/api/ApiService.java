package com.td.oldplay.http.api;


import com.td.oldplay.bean.AddressBean;
import com.td.oldplay.bean.CommentBean;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.CourseTypeBean;
import com.td.oldplay.bean.CreateOrder;
import com.td.oldplay.bean.HomeCourseInfo;
import com.td.oldplay.bean.OrderBean;
import com.td.oldplay.bean.SearchCourse;
import com.td.oldplay.bean.ShopBean;
import com.td.oldplay.bean.ShopDetail;
import com.td.oldplay.bean.TeacherBean;
import com.td.oldplay.bean.TeacherDetail;
import com.td.oldplay.bean.TestBean;
import com.td.oldplay.bean.UserBean;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
    Observable<ApiResponse<List<CourseBean>>> getcoursesInTeacher(@Field("page") int page, @Field("userId") String userId);

    @FormUrlEncoded
    @POST(NetWorkAPI.COURSE_COMMENT_API)
    Observable<ApiResponse<List<CommentBean>>> getCommentsInTeacher(@Field("page") int page, @Field("teacherId") String userId);


    @FormUrlEncoded
    @POST(NetWorkAPI.SEARCH_C_T_API)
    Observable<ApiResponse<List<CourseTypeBean>>> searchCommentsTeachers(@Field("teacherId") String userId);


    @POST(NetWorkAPI.HOME_COURS_API)
    Observable<ApiResponse<HomeCourseInfo>> getHomeCourse();


    @FormUrlEncoded
    @POST(NetWorkAPI.TEACHER_DETAIL_API)
    Observable<ApiResponse<TeacherDetail>> getCourseDetail(@Field("coursesId") String coursesId);
    @FormUrlEncoded
    @POST(NetWorkAPI.SHOP_TEACHER_API)
    Observable<ApiResponse<List<ShopBean>>> getShopInTeacher(@Field("userId") String userId,@Field("page") int page);

    // =================商品============
    @FormUrlEncoded
    @POST(NetWorkAPI.FIND_SHOPBYID_API)
    Observable<ApiResponse<List<ShopBean>>> getShopByType(@Field("page")int page, @Field("goodTypeId")int type);

    /*   @FormUrlEncoded
       @POST(NetWorkAPI.SHOP_COMMENT_API)
       Observable<ApiResponse<List<ShopBean>>> getShopHots(int page);*/
    @FormUrlEncoded
    @POST(NetWorkAPI.SHOP_RECOMMENT_API)
    Observable<ApiResponse<List<ShopBean>>> getShopRecomments(@Field("page")int page);

    @FormUrlEncoded
    @POST(NetWorkAPI.SHOP_COMMENT_API)
    Observable<ApiResponse<List<CommentBean>>> getCommentsInShop(@Field("page") int page, @Field("goodsId") String id);


    @FormUrlEncoded
    @POST(NetWorkAPI.SHOP_DETAIL_API)
    Observable<ApiResponse<ShopDetail>> getShopDetail(@Field("goodsId")String shopId) ;

    @FormUrlEncoded
    @POST(NetWorkAPI.GET_COMMENTS_API)
    Observable<ApiResponse<List<CommentBean>>> getShopComments(@Field("goodsId")String shopId,@Field("page")int page) ;



    @FormUrlEncoded
    @POST(NetWorkAPI.CREATE_ORDER_API)
    Observable<ApiResponse<OrderBean>> createOrder(@FieldMap() HashMap<String, Object> maps) ;

    @FormUrlEncoded
    @POST(NetWorkAPI.ADD_CAR_API)
    Observable<ApiResponse<String>> addCar(@FieldMap() HashMap<String, Object> maps) ;

    @FormUrlEncoded
    @POST(NetWorkAPI.GET_CARS_API)
    Observable<ApiResponse<String>> getCars(@Field("userId")String userId,@Field("page")int page) ;

    @FormUrlEncoded
    @POST(NetWorkAPI.DELET_CAR_API)
    Observable<ApiResponse<String>> deleteCars(@Field("cartId")String cartId) ;



    @FormUrlEncoded
    @POST(NetWorkAPI.GET_ADDRESS_API)
    Observable<ApiResponse<List<AddressBean>>> getAddress(@Field("page") int page, @Field("userId") String id);

    @FormUrlEncoded
    @POST(NetWorkAPI.UPDATE_ADDRESS_API)
    Observable<ApiResponse<String>> updateAddress(@FieldMap() HashMap<String,Object> params);

    @FormUrlEncoded
    @POST(NetWorkAPI.DELETE_ADDRESS_API)
    Observable<ApiResponse<String >> deleteAddress(@Field("addressId") String id);

    @FormUrlEncoded
    @POST(NetWorkAPI.SET_ADDRESS_API)
    Observable<ApiResponse<String>> setDefaultAddress(@Field("addressId") String id, @Field("userId") String userId);

    @FormUrlEncoded
    @POST(NetWorkAPI.ADD_ADDRESS_API)
    Observable<ApiResponse<String>> addAddress(@FieldMap() HashMap<String,Object> params);


}
