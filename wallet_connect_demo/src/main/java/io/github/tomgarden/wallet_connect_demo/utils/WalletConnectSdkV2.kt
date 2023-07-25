package io.github.tomgarden.wallet_connect_demo.utils

import android.app.Application
import com.orhanobut.logger.Logger
import com.walletconnect.android.Core
import com.walletconnect.android.CoreClient
import com.walletconnect.android.relay.ConnectionType
import com.walletconnect.sign.client.Sign
import com.walletconnect.sign.client.SignClient
import io.github.tomgarden.wallet_connect_demo.BuildConfig

object WalletConnectSdkV2 {

    fun init(application: Application) {
        val WALLET_CONNECT_PROD_RELAY_URL = "relay.walletconnect.com"

        val serverUri =
            "wss://$WALLET_CONNECT_PROD_RELAY_URL?projectId=${BuildConfig.WALLET_CONNECT_PROJECT_ID}"
        val appMetaData = Core.Model.AppMetaData(
            name = "Kotlin Dapp",
            description = "Kotlin Dapp Implementation",
            url = "kotlin.dapp.walletconnect.com",
            icons = listOf("https://gblobscdn.gitbook.com/spaces%2F-LJJeCjcLrr53DcT1Ml7%2Favatar.png?alt=media"),
            redirect = "kotlin-dapp-wc://request"
        )

        CoreClient.initialize(
            relayServerUrl = serverUri,
            connectionType = ConnectionType.AUTOMATIC,
            application = application,
            metaData = appMetaData
        ) {
            Logger.e("CoreClient 初始化失败 , 需要自己管理者合格异常信息 : " + it.throwable.stackTraceToString())
        }


        SignClient.initialize(
            init = Sign.Params.Init(core = CoreClient),
            onSuccess = { Logger.i("singClient 初始化成功") },
            onError = { error ->
                // Error will be thrown if there's an issue during initialization
                Logger.e("singClient 初始化失败 , 需要自己管理者合格异常信息 : " + error.throwable.stackTraceToString())
            })

    }


}