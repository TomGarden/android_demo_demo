package io.github.tomgarden.HandlerTest

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import com.orhanobut.logger.Logger

class ActivityTwo: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.i("onCreate")
        super.onCreate(savedInstanceState)

        setContent {
            Column() {
                Button(onClick = { startActivity(Intent(this@ActivityTwo, ActivityTwo::class.java)) }) {
                    Text(text = "启动自己(②/ActivityTwo)")
                }

                Button(onClick = { startActivity(Intent(this@ActivityTwo, ActivityOne::class.java)) }) {
                    Text(text = "启动①(ActivityOne)")
                }

            }
        }

//        val intent:Intent
//        val sc: ServiceConnection = object :ServiceConnection{
//            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//                service.
//            }
//
//            override fun onServiceDisconnected(name: ComponentName?) {
//                TODO("Not yet implemented")
//            }
//
//        }
//        bindService(intent,sc,1)
//        val service:Service;
//        service.stopSelf()
//        startService()
//        bindService()
    }

    override fun onStart() {
        super.onStart()
        Logger.i("onStart")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Logger.i("onRestoreInstanceState")
    }

    override fun onResume() {
        super.onResume()
        Logger.i("onResume")
    }

    override fun onPause() {
        super.onPause()
        Logger.i("onPause")
    }

    override fun onStop() {
        super.onStop()
        Logger.i("onStop")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Logger.i("onSaveInstanceState")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.i("onDestroy")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Logger.i("onNewIntent(Intent)")
    }



}