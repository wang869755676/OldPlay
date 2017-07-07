package com.td.oldplay.http.api;


import com.td.oldplay.bean.TeacherBean;
import com.td.oldplay.bean.TestBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {

    @FormUrlEncoded
    @POST("query?key=7c2d1da3b8634a2b9fe8848c3a9edcba")
    Observable<ApiResponse<TestBean>> getDatas(@Field("pno") int pno, @Field("ps") int ps, @Field("dtype") String dtype);

    @GET(NetWorkAPI.GET_MORE_THEACHER_LIST+"/{data}")
    Observable<ApiResponse<List<TeacherBean>>> getTecherLists(@Path("data") String data);


}
