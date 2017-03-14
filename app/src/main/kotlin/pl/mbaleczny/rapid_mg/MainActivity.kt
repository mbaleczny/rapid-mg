package pl.mbaleczny.rapid_mg

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pl.mbaleczny.rapid_mg.login.LoginActivity
import pl.mbaleczny.rapid_mg.network.TwitterProvider
import pl.mbaleczny.rapid_mg.tweetList.ui.TweetListActivity
import javax.inject.Inject

/**
 * Launcher Activity.
 *
 * @author Mariusz Baleczny
 * @date 11.02.17
 */
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var twitterProvider: TwitterProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RapidApp.appComponent.inject(this)
        finish()
        startActivity(getRedirectIntent())
    }

    /**
     * Checks whether activeSession exists or not and
     * returns suitable Intent to invoke.
     */
    private fun getRedirectIntent(): Intent? {
        return when (twitterProvider.session()) {
            null -> Intent(this, LoginActivity::class.java)
            else -> Intent(this, TweetListActivity::class.java)
        }
    }

}