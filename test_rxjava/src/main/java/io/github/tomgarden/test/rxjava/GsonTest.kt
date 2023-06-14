package io.github.tomgarden.test.rxjava

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * describe :
 *
 * author : tom
 *
 * time : 2021-07-16
 */
class GsonTest {


}

class GsonTestData {
    val string = "我是字符串"
    val int = 111111
    val list = listOf<String>("asdf", "898", "Any()")
}


fun Any.toJsonString(): String = Gson().toJson(this)

inline fun <reified T> String.toObjFromJsonString(): T {
    val listType = object : TypeToken<T>() {}.type
    return Gson().fromJson<T>(this, listType)
}