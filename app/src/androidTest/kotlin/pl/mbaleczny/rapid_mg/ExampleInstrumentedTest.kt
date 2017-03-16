package pl.mbaleczny.rapid_mg

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.mbaleczny.rapid_mg.tweetList.ui.TweetListActivity

/**
 * Instrumentation test, which will execute on an Android device.

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Rule
    @JvmField val rule: ActivityTestRule<TweetListActivity>
            = ActivityTestRule(TweetListActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        val flavor: String = if (BuildConfig.FLAVOR === "prod") "" else "." + BuildConfig.FLAVOR
        assertEquals("pl.mbaleczny.rapid_mg" + flavor, appContext.packageName)
    }
}
