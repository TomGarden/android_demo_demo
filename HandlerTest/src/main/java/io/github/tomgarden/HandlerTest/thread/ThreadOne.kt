package io.github.tomgarden.HandlerTest.thread

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.orhanobut.logger.Logger

class ThreadOne: Thread() {

    companion object {
        val instance: ThreadOne by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ThreadOne().apply { start() } }
    }

    private lateinit var handler :Handler

    override fun run() {
        Looper.prepare()
        super.run()

        handler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)

                val msgStr =  "msg.what : ${msg.what} \n" +
                         "msg.what : ${msg.arg1} \n" +
                         "msg.what : ${msg.arg2} \n" +
                         "msg.what : ${msg.obj as String}"
                Logger.d(msgStr)
            }

        }

        Looper.loop()
    }

    fun quite() = Looper.myLooper()?.quitSafely()

    fun getHandler(): Handler? = if (this::handler.isInitialized) handler else null

}

class ThreadTwo(val str:String):Thread(){

    companion object {
        fun sendMsg(str: String) = ThreadTwo(str).start()
    }

    override fun run() {
        super.run()
        ThreadOne.instance.getHandler()?.let { handler->
            Logger.i(str)
            val msg:Message = handler.obtainMessage().apply { obj = str }
            //msg.sendingUid
            handler.sendMessage(msg)
        }
    }
}