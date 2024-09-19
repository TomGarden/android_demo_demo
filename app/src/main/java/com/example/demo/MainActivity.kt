package com.example.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.baidu.carlifevehicle.TomFloatWindow
import com.example.demo.databinding.ActivityBindableMainBinding
import com.orhanobut.logger.Logger

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_bindable_main)


        ActivityBindableMainBinding.inflate(layoutInflater)
        val binding: ActivityBindableMainBinding by lazy {
            DataBindingUtil.setContentView(this, R.layout.activity_bindable_main)
        }
        binding.viewModule =
            ViewModelProvider.NewInstanceFactory().create(TomViewModule::class.java)
        //binding.viewModule = ViewModelProviders.of(this).get(TomViewModule::class.java)
        binding.lifecycleOwner = this

        Logger.e("123")
    }

    override fun onStop() {
        super.onStop()
        TomFloatWindow.get().showFloatWindow()
    }
}