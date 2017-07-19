package com.td.oldplay.http;

import android.content.Context;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.td.oldplay.bean.AddressBean;
import com.td.oldplay.bean.CommentBean;
import com.td.oldplay.bean.CourseBean;
import com.td.oldplay.bean.CourseTypeBean;
import com.td.oldplay.bean.HomeCourseInfo;
import com.td.oldplay.bean.OrderBean;
import com.td.oldplay.bean.ShopBean;
import com.td.oldplay.bean.ShopDetail;
import com.td.oldplay.bean.TeacherBean;
import com.td.oldplay.bean.TeacherDetail;
import com.td.oldplay.bean.UserBean;
import com.td.oldplay.contants.MContants;
import com.td.oldplay.http.Converter.CustomGsonConverterFactory;
import com.td.oldplay.http.api.ApiResponse;
import com.td.oldplay.http.api.ApiService;
import com.td.oldplay.http.api.NetWorkAPI;
import com.td.oldplay.http.exception.ApiException;
import com.td.oldplay.http.subscriber.HttpSubscriber;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

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
                .addConverterFactory(CustomGsonConverterFactory.create())
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
                           /* if(response.getData()==null){
                                return  new T();
                            }*/
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



    public void modifyUser(HashMap<String, RequestBody> params, HttpSubscriber<UserBean> observer) {
        toSubscribe(mApiService.modifyUser(params), observer);

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

    public void getHomeCourse(Observer<HomeCourseInfo> observer) {
        toSubscribe(mApiService.getHomeCourse(), observer);
    }


    public void searchCommentsTeachers(String id, Observer<List<CourseTypeBean>> observer) {
        toSubscribe(mApiService.searchCommentsTeachers(id), observer);
    }

    public void getShopInTeacher(String id,int page, Observer<List<ShopBean>> observer) {
        toSubscribe(mApiService.getShopInTeacher(id,page), observer);
    }
    public void getCourseDetail(String id, Observer<TeacherDetail> observer) {
        toSubscribe(mApiService.getCourseDetail(id), observer);
    }


    //========================
    public void getShopRecomments(int page, Observer<List<ShopBean>> observer) {
        toSubscribe(mApiService.getShopRecomments(page), observer);
    }

    public void getShopRecomments(int page, String id, Observer<List<CommentBean>> observer) {
        toSubscribe(mApiService.getCommentsInShop(page, id), observer);
    }

    public void getShopByType(int page, int type, Observer<List<ShopBean>> observer) {
        toSubscribe(mApiService.getShopByType(page, type), observer);
    }

    public void getShopDetail(String shopId, Observer<ShopDetail> observer) {
        toSubscribe(mApiService.getShopDetail(shopId), observer);
    }

    public void getShopComments(String shopId,int page, Observer<List<CommentBean>> observer) {
        toSubscribe(mApiService.getShopComments(shopId,page), observer);
    }
    public void createOrder(HashMap<String,Object> params, Observer<OrderBean> observer) {
        toSubscribe(mApiService.createOrder(params), observer);
    }

    public void addCar(HashMap<String,Object> params, Observer<String> observer) {
        toSubscribe(mApiService.addCar(params), observer);
    }

    public void getCars(String userId,int page, Observer<String> observer) {
        toSubscribe(mApiService.getCars(userId,page), observer);
    }

    public void deleteCars(String carId, Observer<String> observer) {
        toSubscribe(mApiService.deleteCars(carId), observer);
    }






    //=============================

    public void getAddresse(int page, String id, Observer<List<AddressBean>> observer) {
        toSubscribe(mApiService.getAddress(page,id), observer);
    }

    public void updateAddress(HashMap<String,Object> params, Observer<String> observer) {
        toSubscribe(mApiService.updateAddress(params), observer);
    }

    public void deleteAddress(String id, Observer<String> observer) {
        toSubscribe(mApiService.deleteAddress(id), observer);
    }
    public void setDefaultAddress(String id,String userId ,Observer<String> observer) {
        toSubscribe(mApiService.setDefaultAddress(id,userId), observer);
    }

    public void addAddress(HashMap<String,Object> params, Observer<String> observer) {
        toSubscribe(mApiService.addAddress(params), observer);
    }
}
