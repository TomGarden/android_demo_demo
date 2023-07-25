package io.github.tomgarden.wallet_connect_demo.utils

enum class Wallet(
    val id: String,
    val walletName: String,
    val imageUrl: String,
    val nativeLink: String?,
    val universalLink: String?,
    val playStoreLink: String?,
) {

    MetaMask(
        id = "c57ca95b47569778a828d19178114f4db188b89b763c899ba0be274e97267d96",
        walletName = "MetaMask",
        imageUrl = "https://registry.walletconnect.com//w3m/v1/getWalletImage/5195e9db-94d8-4579-6f11-ef553be95100?projectId=9ea7b387b173e08bb915d838f489fef9",
        nativeLink = "metamask://",
        universalLink = "https://metamask.app.link",
        playStoreLink = "https://play.google.com/store/apps/details?id=io.metamask",
    ),


    OkxWallet(
        id = "971e689d0a5be527bac79629b4ee9b925e82208e5168b733496a09c0faed0709",
        walletName = "OKX Wallet",
        imageUrl = "https://registry.walletconnect.com//w3m/v1/getWalletImage/45f2f08e-fc0c-4d62-3e63-404e72170500?projectId=9ea7b387b173e08bb915d838f489fef9",
        nativeLink = "okex://main",
        universalLink = "https://www.okx.com/download",
        playStoreLink = "https://play.google.com/store/apps/details?id=com.okinc.okex.gp",
    ),



}
