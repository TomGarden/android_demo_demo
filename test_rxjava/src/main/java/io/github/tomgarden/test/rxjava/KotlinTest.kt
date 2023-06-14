package io.github.tomgarden.test.rxjava

import kotlinx.coroutines.*


/**
 * describe :
 *
 * author : tom
 *
 * time : 2021-09-11
 */
class KotlinTest {

    fun testGlobalScope() {
        GlobalScope.launch {

            System.out.println("11111111" + Thread.currentThread().name)

            withContext(Dispatchers.IO) {
                Thread.sleep(1000)
                System.out.println(Dispatchers.IO.toString() + "\t" + Thread.currentThread().name)
                Thread.sleep(1000)
            }

            System.out.println("222222222" + Thread.currentThread().name)

            withContext(Dispatchers.Unconfined) {
                System.out.println(Dispatchers.Main.toString() + "\t" + Thread.currentThread().name)
            }

            System.out.println("33333333" + Thread.currentThread().name)

        }
        runBlocking {     // 但是这个表达式阻塞了主线程
            delay(5000L)  // ……我们延迟 2 秒来保证 JVM 的存活
        }
    }

    fun textUntil() {
        for (index: Int in 0 until 5) {
            System.out.println(index)
        }
    }

    fun textUntil2() {
        for (index: Int in 0 .. 5) {
            System.out.println(index)
        }
    }
}