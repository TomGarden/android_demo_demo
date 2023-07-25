package io.github.tomgarden.wallet_connect_demo.utils

import androidx.compose.ui.platform.UriHandler
import com.orhanobut.logger.Logger
import timber.log.Timber
import java.net.URLEncoder

private const val WC_URI_QUERY = "wc?uri="

/**
 * Go to native wallet
 *
 * @param uri
 * @param nativeLink        就是 Application Scheme : metamask://
 * @param universalLink     官方下载链接             : https://metamask.app.link
 * @param playStoreLink     GooglePlay 下载链接     : https://play.google.com/store/apps/details?id=io.metamask
 */
fun UriHandler.goToNativeWallet(
    uri: String,
    nativeLink: String?,
    universalLink: String?,
    playStoreLink: String?
) {

    Logger.i(
        "uri : $uri\n" +
                "nativeLink : $nativeLink\n" +
                "universalLink : $universalLink\n" +
                "playStoreLink : $playStoreLink\n"
    )

    try {
        when {
            !nativeLink.isNullOrBlank() -> openUri(formatNativeDeeplink(nativeLink, uri))
            !universalLink.isNullOrBlank() -> goToUniversalLink(uri, universalLink, playStoreLink)
            !playStoreLink.isNullOrBlank() -> goToPlayStore(playStoreLink)
            else -> Timber.e("Invalid wallet links")
        }
    } catch (e: Exception) {
        Timber.e(e)
        goToUniversalLink(uri, universalLink, playStoreLink)
    }
}

fun UriHandler.goToUniversalLink(uri: String, universalLink: String?, playStoreLink: String?) {
    try {
        when {
            !universalLink.isNullOrBlank() -> openUri(formatUniversalLink(universalLink, uri))
            !playStoreLink.isNullOrBlank() -> goToPlayStore(playStoreLink)
            else -> Timber.e("Invalid wallet links")
        }
    } catch (e: Exception) {
        Timber.e(e)
    }
}

private fun UriHandler.goToPlayStore(playStoreLink: String) {
    try {
        openUri(playStoreLink)
    } catch (e: Exception) {
        Timber.e(e)
    }
}

private fun formatNativeDeeplink(nativeLink: String, uri: String): String =
    nativeLink + WC_URI_QUERY + uri.encodeUri()

private fun formatUniversalLink(universalLink: String, uri: String): String {
    val plainAppUrl = if (universalLink.endsWith('/')) {
        universalLink.dropLast(1)
    } else {
        universalLink
    }
    return plainAppUrl + "/" + WC_URI_QUERY + uri.encodeUri()
}

private fun String.encodeUri() = URLEncoder.encode(this, "UTF-8")

fun UriHandler.openPlayStore(playStoreLink: String?) {
    playStoreLink?.let { openUri(it) } ?: Timber.e("Invalid play store link")
}
