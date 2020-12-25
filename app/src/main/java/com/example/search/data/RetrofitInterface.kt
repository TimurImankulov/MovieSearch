package com.example.search.data

import com.example.search.data.model.SearchModel
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {

    @GET(".")
    fun searchFilm(@Query("apikey") api: String,
                   @Query("s") query: String): Single<SearchModel>

    @GET(".")
    fun searchFilmTwo(@Query("apikey") api: String,
                   @Query("s") query: String): Observable<SearchModel>
}