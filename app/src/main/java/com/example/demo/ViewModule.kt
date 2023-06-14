package com.example.demo

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class TomViewModule(
    private val _text: MutableLiveData<String> = MutableLiveData<String>("32"),
    val text: LiveData<String> = _text
) : ViewModel() {


//    val textClickListener = View.OnClickListener {
//        text = text.toIntOrNull()?.let { (it + 1).toString() } ?: let { "1" }
//    }

    fun textClickListener(view: View) {

        _text.value =
            _text.value?.let { string ->
                string.toIntOrNull()?.let { ((it + 1).toString()) }
            } ?: let {
                "1"
            }

        Log.e("TAG", text.value.toString())
        Log.d("TAG", _text.value.toString())
    }
}