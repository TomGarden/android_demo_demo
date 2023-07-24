package io.github.tomgarden.kotlincoroutine

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class InstanceModule @Inject constructor() {


    @Inject
    lateinit var app: Application

    @Inject
//    @ApplicationContext
    lateinit var appContext: Context

    private val httpLogInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }
    private val okHttpClient: OkHttpClient by lazy { okHttpClient() }
    private val retrofit: Retrofit by lazy { retrofit() }

    @Provides
    fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLogInterceptor)
        .build()

    private fun retrofit(): Retrofit {
//         Log.e("TAG", appContext.resources.getString(R.string.app_name))
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .baseUrl("http://192.168.31.67:8080")
            .build()
    }

    @Provides
    fun apiClient() = retrofit.create(ApiClient::class.java)
}