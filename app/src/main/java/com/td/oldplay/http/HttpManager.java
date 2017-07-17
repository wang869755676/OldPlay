package com.td.oldplay.http;

import android.content.Context;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.td.oldplay.bean.CommentBean;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.CourseTypeBean;
import com.td.oldplay.bean.HomeCourseInfo;
import com.td.oldplay.bean.TeacherBean;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.api.ApiResponse;
import com.td.oldplay.http.api.ApiService;
import com.td.oldplay.http.api.NetWorkAPI;
import com.td.oldplay.http.exception.ApiException;
import com.td.oldplay.http.subscriber.HttpSubscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class HttpManager {
    public static final String TAG = HttpManager.class.getSimpleName();
    private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit mRetrofit;
    private ApiService mApiService;

    private static Context mContext;
    private volatile static HttpManager instance;

    private HttpManager() {
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("HttpManager", message);
            }
        });
        loggingInterceptor.setLevel(level);
        //拦截请求和响应日志并输出，其实有很多封装好的日志拦截插件，大家也可以根据个人喜好选择。
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(loggingInterceptor);


        OkHttpClient okHttpClient = builder.build();

        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(NetWorkAPI.BASE_URL)
                .client(okHttpClient)
                .build();


        mApiService = mRetrofit.create(ApiService.class);
    }

    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }

    public static void init(Context context) {
        mContext = context;
    }

    private <T> void toSubscribe(Observable<ApiResponse<T>> o, Observer<T> s) {
        o.subscribeOn(Schedulers.io())
                .map(new Function<ApiResponse<T>, T>() {
                    @Override
                    public T apply(@NonNull ApiResponse<T> response) throws Exception {
                        Log.e("===", response.toString() + "  ");
                        int code = Integer.parseInt(response.getErrcode());
                        if (code != MContants.SUCCESS_CODE) {
                            throw new ApiException(code, response.getErrdesc());
                        } else {
                            return response.getData();
                        }
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    public void getTeacherLists(String data, Observer<List<TeacherBean>> observer) {
        toSubscribe(mApiService.getTecherLists(data), observer);
    }

    public void registerUser(HashMap<String, Object> params, Observer<String> observer) {
        toSubscribe(mApiService.registerUser(params), observer);
    }

    public void loginUser(HashMap<String, Object> params, Observer<UserBean> observer) {
        toSubscribe(mApiService.loginUser(params), observer);
    }

    public void getCode(String phone, Observer<String> observer) {
        toSubscribe(mApiService.getCode(phone), observer);
    }

    public void modifyLoginPws(HashMap<String, Object> params, Observer<String> observer) {
        toSubscribe(mApiService.modifyLoginPws(params), observer);
    }

    public void modifyZhifuPws(HashMap<String, Object> params, Observer<String> observer) {
        toSubscribe(mApiService.modifyZhifuPws(params), observer);
    }

    public void modifyUser(HashMap<String, Object> params, MultipartBody.Part file, HttpSubscriber<UserBean> observer) {
        toSubscribe(mApiService.modifyUser(params, file), observer);

    }

    public void forgetPws(HashMap<String, Object> params, Observer<String> observer) {
        toSubscribe(mApiService.forgetPws(params), observer);
    }

    // 课程

    public void getCourseTypes(String parentId, int page, Observer<List<CourseTypeBean>> observer) {
        toSubscribe(mApiService.getCourseTypes(parentId, page), observer);
    }

    public void getCourseRecomments(int page, Observer<List<CourseTypeBean>> observer) {
        toSubscribe(mApiService.getCourseRecomments(page), observer);
    }

    public void getCourseHots(int page, Observer<List<CourseTypeBean>> observer) {
        toSubscribe(mApiService.getCourseHots(page), observer);
    }

    public void getTeachersInCourse(int page, String id, Observer<List<TeacherBean>> observer) {
        toSubscribe(mApiService.getTeachersInCourse(page, id), observer);
    }

    public void getcoursesInTeacher(int page, String id, Observer<List<CourseBean>> observer) {
        toSubscribe(mApiService.getcoursesInTeacher(page, id), observer);
    }

    public void getCommentsInTeacher(int page, String id, Observer<List<CommentBean>> observer) {
        toSubscribe(mApiService.getCommentsInTeacher(page, id), observer);
    }

    public void getHomeCourse( Observer<HomeCourseInfo> observer) {
        toSubscribe(mApiService.getHomeCourse(), observer);
    }
}
