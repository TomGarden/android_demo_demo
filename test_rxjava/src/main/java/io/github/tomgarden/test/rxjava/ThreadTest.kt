package io.github.tomgarden.test.rxjava

/**
 * describe :
 *
 * author : tom
 *
 * time : 2021-12-28
 */
class ThreadTest {

    public fun testFunctionFieldBody() {
        var flag = 1
        System.out.println("${Thread.currentThread().name} 定义一个变量 : $flag")
        flag += 1
        System.out.println("${Thread.currentThread().name}     变量自增 : $flag")
        Thread.sleep(1L * 60 * 1000)
    }

    public fun testFunctionFieldThread(){
        val threadA  = Thread({testFunctionFieldBody()},"threadA")
        val threadB  = Thread({testFunctionFieldBody()},"threadB")
        threadA.start()
        Thread.sleep(2000)
        threadB.start()
    }

}