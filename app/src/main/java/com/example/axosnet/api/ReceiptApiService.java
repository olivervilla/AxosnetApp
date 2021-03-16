package com.example.axosnet.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ReceiptApiService {
    @GET("getall")
    Call<String> getAllReceipts();

    @POST("insert")
    Call<ResponseBody> insertReceipt(
            @Query("provider") String provider,
            @Query("amount") Double amount,
            @Query("comment") String comment,
            @Query("emission_date") String date,
            @Query("currency_code") String code
    );

    @POST("delete")
    Call<ResponseBody> deleteReceipt(
            @Query("id") int id
    );

    @POST("update")
    Call<ResponseBody> updateReceipt(
            @Query("id") int id,
            @Query("provider") String provider,
            @Query("amount") Double amount,
            @Query("comment") String comment,
            @Query("emission_date") String date,
            @Query("currency_code") String code
    );
}
