package io.github.tomgarden.kotlincoroutine

import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiClient {
    @GET("/testApi")
    suspend fun  reposForUser(): JsonObject

    @GET("/testApi")
    fun  reposForUserObservable(): Observable<JsonObject>

    @GET("/testApi")
    suspend fun testApi2(): String
}