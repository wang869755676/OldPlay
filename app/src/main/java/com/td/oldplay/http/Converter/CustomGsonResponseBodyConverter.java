package com.td.oldplay.http.Converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.td.oldplay.http.api.ApiResponse;
import com.td.oldplay.http.exception.ApiException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;

final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        ApiResponse httpStatus = gson.fromJson(response, ApiResponse.class);
        if (httpStatus.getData() == null) {  // 处理返回的数据为你ull
            value.close();
            throw new ApiException(Integer.parseInt(httpStatus.getErrcode()), httpStatus.getErrdesc());
        }
        if (httpStatus.getData() instanceof String && !httpStatus.getErrcode().equals("0")) {
            value.close();
            throw new ApiException(Integer.parseInt(httpStatus.getErrcode()), httpStatus.getErrdesc());
        }
        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);

        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
