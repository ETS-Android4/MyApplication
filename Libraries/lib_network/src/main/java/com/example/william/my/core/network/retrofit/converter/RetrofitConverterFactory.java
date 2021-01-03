package com.example.william.my.core.network.retrofit.converter;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 重写父类中responseBodyConverter，该方法用来转换服务器返回数据
 * 重写父类中requestBodyConverter，该方法用来转换发送给服务器的数据
 */
public class RetrofitConverterFactory extends Converter.Factory {

    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static RetrofitConverterFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    //@SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static RetrofitConverterFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new RetrofitConverterFactory(gson);
    }

    private final Gson gson;

    private RetrofitConverterFactory(Gson gson) {
        this.gson = gson;
    }

    /**
     * 需要重写父类中responseBodyConverter，该方法用来转换服务器返回数据
     */
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NonNull Type type, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new RetrofitResponseBodyConverter<>(gson, adapter);
    }

    /**
     * 需要重写父类中requestBodyConverter，该方法用来转换发送给服务器的数据
     */
    @Override
    public Converter<?, RequestBody> requestBodyConverter(@NonNull Type type, @NonNull Annotation[] parameterAnnotations, @NonNull Annotation[] methodAnnotations, @NonNull Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new RetrofitRequestBodyConverter<>(gson, adapter);
    }
}
