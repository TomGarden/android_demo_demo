package io.github.tomgarden.wallet_connect_demo.utils

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.platform.AndroidUriHandler
import androidx.compose.ui.platform.UriHandler
import com.orhanobut.logger.Logger
import com.walletconnect.android.Core
import com.walletconnect.android.CoreClient
import com.walletconnect.android.relay.ConnectionType
import com.walletconnect.sign.client.Sign
import com.walletconnect.sign.client.SignClient
import io.github.tomgarden.wallet_connect_demo.BuildConfig
import io.github.tomgarden.wallet_connect_demo.utils.Chains.Companion.toMap
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableSource
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.functions.Function
import java.lang.Exception
import java.util.concurrent.TimeUnit

object WalletConnectSdkV2 {

    val dappDelegate = object : SignClient.DappDelegate {
        override fun onSessionApproved(approvedSession: Sign.Model.ApprovedSession) {
            // Triggered when Dapp receives the session approval from wallet
            Logger.d(gson.toJson(approvedSession))
            SignClient.getListOfActiveSessions().also {
                Logger.i("② SignClient.getListOfActiveSessions : ${gson.toJson(it)}")
            }
        }

        override fun onSessionRejected(rejectedSession: Sign.Model.RejectedSession) {
            // Triggered when Dapp receives the session rejection from wallet
            Logger.d(gson.toJson(rejectedSession))
        }

        override fun onSessionUpdate(updatedSession: Sign.Model.UpdatedSession) {
            // Triggered when Dapp receives the session update from wallet
            Logger.d(gson.toJson(updatedSession))
        }

        override fun onSessionExtend(session: Sign.Model.Session) {
            // Triggered when Dapp receives the session extend from wallet
            Logger.d(gson.toJson(session))
        }

        override fun onSessionEvent(sessionEvent: Sign.Model.SessionEvent) {
            // Triggered when the peer emits events that match the list of events agreed upon session settlement
            Logger.d(gson.toJson(sessionEvent))
        }

        override fun onSessionDelete(deletedSession: Sign.Model.DeletedSession) {
            // Triggered when Dapp receives the session delete from wallet
            Logger.d(gson.toJson(deletedSession))
        }

        override fun onSessionRequestResponse(response: Sign.Model.SessionRequestResponse) {
            // Triggered when Dapp receives the session request response from wallet
            Logger.d(gson.toJson(response))
        }

        override fun onConnectionStateChange(state: Sign.Model.ConnectionState) {
            //Triggered whenever the connection state is changed
            Logger.d(gson.toJson(state))
        }

        override fun onError(error: Sign.Model.Error) {
            // Triggered whenever there is an issue inside the SDK" +
            Logger.d(error.throwable.stackTraceToString())
        }
    }

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
            onSuccess = {
                Logger.i("singClient 初始化成功")
                SignClient.setDappDelegate(dappDelegate)
                Logger.i("singClient 设置委托成功")
            },
            onError = { error ->
                // Error will be thrown if there's an issue during initialization
                Logger.e("singClient 初始化失败 , 需要自己管理者合格异常信息 : " + error.throwable.stackTraceToString())
            })

    }

    fun getConnectParams(): Sign.Params.Connect {

        val expiry =
            (System.currentTimeMillis() / 1000) + TimeUnit.SECONDS.convert(3, TimeUnit.MINUTES)

        val pairing = CoreClient.Pairing.create { error ->
            Logger.e(error.throwable.stackTraceToString())
        }?.let {
            Core.Model.Pairing(
                topic = it.topic,
                expiry = expiry,
                peerAppMetaData = it.peerAppMetaData,
                relayProtocol = it.relayProtocol,
                relayData = it.relayData,
                uri = it.uri,
                isActive = it.isActive,
                registeredMethods = it.registeredMethods,
            )
        }?:let {
            throw RuntimeException("创建 pair 失败 , 详情查看日志")
        }




        val properties: Map<String, String> = mapOf(
            Pair("caip154-mandatory", "true"),
            Pair("sessionExpiry", "$expiry"),
            Pair("expiry", "$expiry"),
        )



        return Sign.Params.Connect(
//            namespaces = Chains.getDefNamespace(),
            namespaces = Chains.ETHEREUM_MAIN.toMap(),
            optionalNamespaces = Chains.POLYGON_MUMBAI.toMap(),
            properties = properties,
            pairing = pairing,
        )
    }

    fun signConnect(context: Context, callBack: Function2<Sign.Params.Connect, UriHandler, Unit>) {
        val conntect = getConnectParams()
        val uriHandler = AndroidUriHandler(context)


        Logger.i("conntect : " + gson.toJson(conntect))

        fun disconnectAndReconnect() {
            val disposable = Completable.create { emitter ->
                try {
                    SignClient.getListOfActiveSessions().forEach { session ->
                        val disconnect = Sign.Params.Disconnect(session.topic)
                        SignClient.disconnect(
                            disconnect,
                            onSuccess = { Logger.i("断连 : ${gson.toJson(session)}") },
                            onError = { emitter.onError(it.throwable) })
                    }
                } catch (exception: Exception) {
                    emitter.onError(exception)
                } finally {
                    emitter.onComplete()
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Logger.i("线程检查")
                    SignClient.connect(
                        connect = conntect,
                        onSuccess = {
                            Logger.i("SignClient.connect 成功 : " + gson.toJson(SignClient.getListOfActiveSessions()))
                            callBack.invoke(conntect, uriHandler)
                        },
                        onError = { Logger.i("SignClient.connect 失败 " + it.throwable.stackTraceToString()) }
                    )
                }, {
                    Logger.e(it.stackTraceToString())
                })
        }

        fun getActiveSessionOrConnect(){
            val disposable = Observable.create { emitter ->
                try {
                    val activeSessions = SignClient.getListOfActiveSessions()
                    Logger.i("SignClient.getListOfActiveSessions() : ${gson.toJson(activeSessions)}")
                    emitter.onNext(activeSessions)
                } catch (exception: Exception) {
                    emitter.onError(exception)
                } finally {
                    emitter.onComplete()
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({sessionList->

                    if(!sessionList.isNullOrEmpty()) {
                        Toast.makeText(context, "存在 活着的 session", Toast.LENGTH_SHORT).show()
                        return@subscribe
                    }

                    SignClient.connect(
                        connect = conntect,
                        onSuccess = {
                            Logger.i("SignClient.connect 成功 : " + gson.toJson(SignClient.getListOfActiveSessions()))
                            callBack.invoke(conntect, uriHandler)
                        },
                        onError = { Logger.i("SignClient.connect 失败 " + it.throwable.stackTraceToString()) }
                    )
                }, {
                    Logger.e(it.stackTraceToString())
                })
        }

        disconnectAndReconnect()


    }

    fun gotoWallet(context: Context, uriHandler: UriHandler, connect: Sign.Params.Connect): Dialog {
//        SignClient.request()

        return AlertDialog.Builder(context)
            .setPositiveButton(Wallet.MetaMask.walletName) { dialog: DialogInterface, which: Int ->
                uriHandler.goToNativeWallet(
                    connect.pairing.uri,
                    Wallet.MetaMask.nativeLink,
                    Wallet.MetaMask.universalLink,
                    Wallet.MetaMask.playStoreLink,
                )
            }
            .setNeutralButton(Wallet.OkxWallet.walletName) { dialog: DialogInterface, which: Int ->
                uriHandler.goToNativeWallet(
                    connect.pairing.uri,
                    Wallet.OkxWallet.nativeLink,
                    Wallet.OkxWallet.universalLink,
                    Wallet.OkxWallet.playStoreLink,
                )
            }
            .setNegativeButton("cancel") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
            .create()
    }

}