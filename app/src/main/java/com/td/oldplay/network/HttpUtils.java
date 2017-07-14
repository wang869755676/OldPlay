package com.td.oldplay.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.td.oldplay.utils.SharePreferenceUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * play的网络数据源，通过Retrofit请求得到
 * Created by snowbean on 16-6-6.
 */
public class HttpUtils {

    private static final String BASE_URL = "http://172.16.0.203:8080";
    private Retrofit mRetrofit;
    private HttpService mPlayService;
    private ErrorListener mErrorListener;


    //TODO 待完善，还有一些配置未加入，现在只加入请求头
    public HttpUtils(final SharePreferenceUtil userDataUtils) {
        final Gson gson = new GsonBuilder()
              /*  .registerTypeAdapter(EmojiType.class, new EmojiSerializer())
                .registerTypeAdapter(LevelType.class, new LevelSerializer())
                .registerTypeAdapter(GenderType.class, new GenderSerializer())
                .registerTypeAdapter(NotificationType.class, new NotificationSerializer())
                .registerTypeAdapter(NewsType.class,new NewsSerializer())*/
                .create();

        final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final Interceptor mTokenInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request originRequest = chain.request();
                Request authorisedRequest = originRequest.newBuilder()
                        .build();
                Response response = chain.proceed(authorisedRequest);
                if (response.code() == 403 || response.code() == 500) {
                    if (mErrorListener != null) {
                        mErrorListener.onRemoteErrorHappened(response.code());
                    }
                }
                return response;

            }

        };
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(mTokenInterceptor)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mPlayService = mRetrofit.create(HttpService.class);
    }

    public void setErrorListener(ErrorListener errorListener) {
        mErrorListener = errorListener;
    }


}
