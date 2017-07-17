package com.td.oldplay.http.api;


import com.td.oldplay.bean.CommentBean;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.CourseTypeBean;
import com.td.oldplay.bean.HomeCourseInfo;
import com.td.oldplay.bean.TeacherBean;
import com.td.oldplay.bean.TestBean;
import com.td.oldplay.bean.UserBean;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {

    @FormUrlEncoded
    @POST("query?key=7c2d1da3b8634a2b9fe8848c3a9edcba")
    Observable<ApiResponse<TestBean>> getDatas(@Field("pno") int pno, @Field("ps") int ps, @Field("dtype") String dtype);

    @GET(NetWorkAPI.GET_MORE_THEACHER_LIST+"/{data}")
    Observable<ApiResponse<List<TeacherBean>>> getTecherLists(@Path("data") String data);

    @FormUrlEncoded
    @POST(NetWorkAPI.REGISTER_API)
    Observable<ApiResponse<String>> registerUser(@FieldMap() HashMap<String,Object>maps);

    @FormUrlEncoded
    @POST(NetWorkAPI.LOGIN_API)
    Observable<ApiResponse<UserBean>> loginUser(@FieldMap() HashMap<String,Object>maps);

    @FormUrlEncoded
    @POST(NetWorkAPI.GETCODE_API)
    Observable<ApiResponse<String>> getCode(@Field("phone") String phone);

    @FormUrlEncoded
    @POST(NetWorkAPI.UPDATEPWS_API)
    Observable<ApiResponse<String>> modifyLoginPws(@FieldMap() HashMap<String,Object>maps);

    @FormUrlEncoded
    @POST(NetWorkAPI.UPDATEZPWS_API)
    Observable<ApiResponse<String>> modifyZhifuPws(@FieldMap() HashMap<String,Object>maps);

    @Multipart
    @FormUrlEncoded
    @POST(NetWorkAPI.UPDATEUSE_API)
    Observable<ApiResponse<UserBean>> modifyUser(@FieldMap() HashMap<String,Object>maps,@Part MultipartBody.Part picFile);

    @FormUrlEncoded
    @POST(NetWorkAPI.FORGETPWS_API)
    Observable<ApiResponse<String>> forgetPws(@FieldMap() HashMap<String,Object>maps);

    @FormUrlEncoded
    @POST(NetWorkAPI.COURSE_TYPE_API)
    Observable<ApiResponse<List<CourseTypeBean>>> getCourseTypes(@Field("parentId") String parentId,@Field("page") int  page);

    @FormUrlEncoded
    @POST(NetWorkAPI.COURSE_RECOMMENT_API)
    Observable<ApiResponse<List<CourseTypeBean>>> getCourseRecomments(@Field("page") int  page);

    @FormUrlEncoded
    @POST(NetWorkAPI.COURSE_HOT_API)
    Observable<ApiResponse<List<CourseTypeBean>>> getCourseHots(@Field("page") int  page);

    @FormUrlEncoded
    @POST(NetWorkAPI.COURSE_TEACHER_API)
    Observable<ApiResponse<List<TeacherBean>>> getTeachersInCourse(@Field("page") int  page,@Field("coursesTypeId") String coursesTypeId);

    @FormUrlEncoded
    @POST(NetWorkAPI.TEACHER_COURSE_API)
    Observable<ApiResponse<List<CourseBean>>> getcoursesInTeacher(@Field("page") int  page, @Field("userId") String userId);

    @FormUrlEncoded
    @POST(NetWorkAPI.COURSE_COMMENT_API)
    Observable<ApiResponse<List<CommentBean>>> getCommentsInTeacher(@Field("page") int  page, @Field("teacherId") String userId);


    @FormUrlEncoded
    @POST(NetWorkAPI.SEARCH_C_T_API)
    Observable<ApiResponse<String>> searchCommentsTeachers(@Field("teacherId") String userId);


    @POST(NetWorkAPI.HOME_COURS_API)
    Observable<ApiResponse<HomeCourseInfo>> getHomeCourse();
}
