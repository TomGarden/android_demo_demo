package io.github.tomgarden.HandlerTest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.orhanobut.logger.Logger

class ActivityOne: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.i("onCreate")
        super.onCreate(savedInstanceState)
        val btn = Button(this)
        btn.text = "点击我启动新的 Activity"
        var intent = Intent(this, ActivityTwo::class.java)
        intent.flags
        btn.setOnClickListener { startActivity(Intent(this, ActivityTwo::class.java)) }
        setContentView(btn)
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
}