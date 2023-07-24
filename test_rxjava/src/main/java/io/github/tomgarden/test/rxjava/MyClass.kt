package io.github.tomgarden.test.rxjava

import java.util.Calendar

class MyClass {

}

fun main(args: Array<String>) {
//    var stringBuilder = StringBuilder("0")
//    System.out.println("aaa" + stringBuilder)
//    stringBuilder.append("1").also {
//        System.out.println("bbb" + stringBuilder)
//        it.append("2")
//        System.out.println("ccc" + stringBuilder)
//    }
//    System.out.println("ddd" + stringBuilder)
    //                            0, 1, 2, 3, 4, 5, 6, 7

//    ThreadTest().testFunctionFieldThread()
//    return
//
//    KotlinTest().testGlobalScope()
//
//    return
//
//    val unCustomClassical = SquareStyle.CLASSICAL
//    val customClassical = SquareStyle.CLASSICAL.also { it.isCustom = true }
//    System.out.println(unCustomClassical == customClassical)

//    return
//
//    KotlinTest().textUntil()
//    return
//
//    val clazz = "String"::class.java
//    return
//
//    RxJavaTest().textFlatMap2()
//    return
//
//
//    val list = mutableListOf<Any>(1, 2, 3, 4, 5, 6, 7)
//    list.subList(1 + 1, 1 + 3).clear()
//    System.out.println("ddd")


//    KotlinTest().test()
    KotlinObj(1)
}

//gsonTestFunc
fun main2(args: Array<String>) {

    //val string = GsonTestData().toJsonString()
//    val string = listOf(123L,2345L,456457L).toJsonString()
//    println(string)
//    val obj = string.toObjFromJsonString<List<Long>>()
//    println(obj)
    print(Calendar.getInstance())
}

enum class SquareStyle(val value: Int, var isCustom: Boolean) {
    CLASSICAL(1, false),       /*古典*/
    CUSTOM(2, false),          /*自定义 , 实际上还没有用到*/
    TOM_FIRST(3, false),       /*历史 又名 HISTORY*/
    SIMPLE_CONTRAST(4, false), /*简约*/
    PICTURE(5, false),         /*贴图*/
    ;
}