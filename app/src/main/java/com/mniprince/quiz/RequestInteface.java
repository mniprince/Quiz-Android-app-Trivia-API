package com.mniprince.quiz;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


interface RequestInteface {
    @GET("api/questions?")
    Call<List<QuizModel>> getJson(@Query("categories") String key,
                                  @Query("limit") String key1,
                                  @Query("difficulty") String key2);
}
