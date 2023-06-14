package io.github.tomgarden.HandlerTest

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import io.github.tomgarden.HandlerTest.thread.ThreadOne
import io.github.tomgarden.HandlerTest.thread.ThreadTwo
import io.github.tomgarden.HandlerTest.ui.theme.DemoTheme


class MainActivity : ComponentActivity() {

    val threadOne  = ThreadOne()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Logger.addLogAdapter(AndroidLogAdapter())
        setContent{

            DemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ColumnViews()
                }
            }

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}

/*#region Composable*/

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = Modifier
            .background(Color.Gray)
            .then(modifier),
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun MessageCard(text: String) = Text(
    text = "$text \n这里我们测试了 : 使用 Jetpack 进行 组合 和 重组 ",
    modifier = Modifier
        .background(Color.Yellow),
        //.padding(10.dp)
        //.paddingFrom(LastBaseline, 10.dp, 10.dp)
        //.paddingFrom(HorizontalAlignmentLine())
    style = MaterialTheme.typography.bodySmall
)

@ExperimentalMaterial3Api
@Composable
fun ColumnViews() = Column {

    val mContext = LocalContext.current
    var rememberInt by remember { mutableStateOf(0) }

    Greeting("Android")
    MessageCard("你好啊 Jetpack Compose")

    Surface(shape = MaterialTheme.shapes.large, shadowElevation = 2.dp, tonalElevation = 1.dp) {
        Row(modifier = Modifier.padding(8.dp)) {/*横向列表*/
            Button(onClick = {
                Toast.makeText(mContext, "toast 文本", Toast.LENGTH_SHORT).show()
            }) {
                Text(text = "点击我试试看")
            }

            Button(onClick = { rememberInt++ }) {
                Text(text = "click $rememberInt")
            }
        }
    }

    Box {/* 堆叠内容 */
        Text(text = "Text 123")
        Text(text = "____ ___")
    }

    /************/

    HandlerTestGroup()
}

@ExperimentalMaterial3Api
@Composable
fun HandlerTestGroup() = Column() {

    val mContext = LocalContext.current

    Button(onClick = { /*TODO*/ }) {
        Text("ThreadOne , Handler 持有者")
    }

    var editStr by remember { mutableStateOf("") }

    TextField(
        value = editStr,
        onValueChange = { editStr = it },
        placeholder = { Text("请填写要发送的消息文本") })

    Button(onClick = {
        ThreadTwo.sendMsg(editStr)
    }) {
        Text("ThreadTwo , 通过 Handler 向 ThreadOne 发送消息")
    }

    Button(onClick = {
        mContext.startActivity(Intent(mContext,LifecycleActivity::class.java))
    }) {
        Text("start new Activity")
    }
}

/*#endregion Composable*/

/*#region Preview*/
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DemoTheme {
        Greeting("Android")
    }
}

@Preview
@Composable
fun PreviewMessageCard() = MessageCard(
    text = "你好啊 Jetpack Compose 123 " )

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true, showBackground = true,
    device = "id:Nexus 4",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewColumnViews() = ColumnViews()

/*#endregion Preview*/

/*#region test code */

fun testFunc(): Unit {
    var mIntent:Intent = Intent()
    mIntent.component
    mIntent.action
    mIntent.categories

    mIntent.data
    mIntent.extras
    mIntent.flags
    mIntent.type
}

/*#endregion test code */

