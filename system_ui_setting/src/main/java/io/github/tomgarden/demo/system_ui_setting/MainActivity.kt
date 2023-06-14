package io.github.tomgarden.demo.system_ui_setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

    }

    private fun initView() {
        findViewById<Button>(R.id.btn_resume).setOnClickListener{
            window?.decorView?.apply {
                systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
                //WindowInsetsController.
            }
        }
        //btn_dark_status_bar_navigation_bar
    }
}