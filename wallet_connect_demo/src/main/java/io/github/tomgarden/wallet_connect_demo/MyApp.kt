package io.github.tomgarden.wallet_connect_demo

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import io.github.tomgarden.wallet_connect_demo.utils.WalletConnectSdkV2


class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(AndroidLogAdapter())

        WalletConnectSdkV2.init(this)
    }
}