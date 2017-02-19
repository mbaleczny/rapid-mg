package pl.mbaleczny.rapid_mg

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twitter.sdk.android.core.TwitterCore
import pl.mbaleczny.rapid_mg.login.LoginActivity
import pl.mbaleczny.rapid_mg.tweetList.ui.TweetListActivity

/**
 * Launcher Activity.
 *
 * @author Mariusz Baleczny
 * @date 11.02.17
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
        startActivity(getRedirectIntent())
    }

    /**
     * Checks whether activeSession exists or not and
     * returns suitable Intent to invoke.
     */
    private fun getRedirectIntent(): Intent? {
        return when (TwitterCore.getInstance()?.sessionManager?.activeSession) {
            null -> Intent(this, LoginActivity::class.java)
            else -> Intent(this, TweetListActivity::class.java)
        }
    }

}