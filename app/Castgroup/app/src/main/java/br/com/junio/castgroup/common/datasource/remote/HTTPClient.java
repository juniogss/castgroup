package br.com.junio.castgroup.common.datasource.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HTTPClient {

    @NotNull
    public static Retrofit retrofit(){
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addNetworkInterceptor(new StethoInterceptor());

        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(LocalAPI.BASE_URL)
                .client(client.build())
                .build();
    }

}
