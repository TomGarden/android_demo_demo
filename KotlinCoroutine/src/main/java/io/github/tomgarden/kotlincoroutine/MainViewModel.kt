package io.github.tomgarden.kotlincoroutine

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.internal.functions.Functions
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
class MainViewModel @Inject constructor(){

    @Inject
    lateinit var apiClient: ApiClient

    @Inject
    lateinit var application: Application

    @Inject
    @ApplicationContext
    lateinit var appContext: Context


//    @Inject
//    lateinit var

    fun startNetOpt() = GlobalScope.launch {

        apiClient.reposForUserObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Functions.emptyConsumer(), { Log.e("MainViewModel",it.toString()) }, Functions.EMPTY_ACTION)
    }

    fun testDialog(){
//        Toast.makeText(application,application.getText(R.string.app_name),Toast.LENGTH_SHORT).show()
         OtherObject(appContext).checkInitResult()
//         OtherObject().checkInitResult()
    }


}