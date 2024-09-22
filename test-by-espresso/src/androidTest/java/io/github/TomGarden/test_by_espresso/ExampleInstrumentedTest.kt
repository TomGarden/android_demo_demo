package io.github.TomGarden.test_by_espresso

import android.app.Instrumentation
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private val TAG = "ExampleInstrumentedTest"

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        // 手动启动 MainActivity
        scenario = ActivityScenario.launch(MainActivity::class.java)

    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("io.github.TomGarden.test_by_espresso", appContext.packageName)
    }

    @Test
    fun btnClick(){

        Log.e(TAG, scenario.state.name)


        Espresso.onView(ViewMatchers.withId(R.id.btn_imbtn))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.tv_imtv))
            //.perform(ViewActions.replaceText("点击动作已经触发了"))
            .perform(ViewActions.replaceText("点击动作已经触发了"))

        // 保证 Activity 处于 resume 状态直到测试结束
        //scenario.moveToState(Lifecycle.State.RESUMED)
        // 等待2秒，确保动作完成
        //Thread.sleep(5000)
    }


    @After
    fun shutdown() {
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    fun tomTest(){
        val mInstrumentation = Instrumentation()
    }
}