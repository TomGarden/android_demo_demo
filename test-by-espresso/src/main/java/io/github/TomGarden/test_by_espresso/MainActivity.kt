package io.github.TomGarden.test_by_espresso

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    //private val vBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        R.layout.activity_main_2
        //setContentView(R.layout.tom_yeo.activity_main)
        //setContentView(vBinding.root)

        //vBinding.btnImbtn.setOnClickListener { Log.e(TAG, "按钮点击") }
    }

    fun tomTest(){
        //mInst
    }
}