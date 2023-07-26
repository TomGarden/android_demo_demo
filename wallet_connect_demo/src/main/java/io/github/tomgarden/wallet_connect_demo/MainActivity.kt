package io.github.tomgarden.wallet_connect_demo

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.tooling.preview.Preview
import com.walletconnect.sign.client.Sign
import io.github.tomgarden.wallet_connect_demo.ui.theme.DemoTheme
import io.github.tomgarden.wallet_connect_demo.utils.WalletConnectSdkV2
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context: Context =  LocalContext.current
    val activity: ComponentActivity = context as ComponentActivity

    Button(onClick = {
        WalletConnectSdkV2.signConnect(context,object :Function2<Sign.Params.Connect, UriHandler, Unit>{
            override fun invoke(connect: Sign.Params.Connect, uriHandler: UriHandler) {
                activity.runOnUiThread {
                    WalletConnectSdkV2.gotoWallet(context, uriHandler,connect).show()
                }
            }
        })
    }) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DemoTheme {
        Greeting("Android")
    }
}