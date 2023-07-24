package io.github.tomgarden.kotlincoroutine

import android.app.Application
import android.content.Context
import android.util.Log
import dagger.Component
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Singleton



//@Module
//@EntryPoint
//@InstallIn(SingletonComponent::class)
//class OtherObject  {
class OtherObject @Inject constructor(@ApplicationContext val appContext: Context) {


//    @Inject
//    lateinit var app: Application

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface OtherObjectEntryPoint {
        fun okHttpClient(): OkHttpClient
    }
    var okHttpClient: OkHttpClient
        @Inject set

    init {
        okHttpClient = EntryPoints.get(appContext,OtherObjectEntryPoint::class.java).okHttpClient()
//        Log.e("TOM_GARDEN",appContext.resources.getString(R.string.app_name))
//        Log.e("TOM_GARDEN",app.resources.getString(R.string.app_name))
    }



//    @Inject lateinit var okHttpClient: OkHttpClient
//    @Inject lateinit var instanceModule: InstanceModule

//    @Inject constructor()

//    init {
//        DaggerAppComponent.create().inject(this)
//    }

    fun checkInitResult() {
        if(this::okHttpClient!=null) {
            println("初始化成功")
        }else{
            println("失败失败")
        }
    }


}