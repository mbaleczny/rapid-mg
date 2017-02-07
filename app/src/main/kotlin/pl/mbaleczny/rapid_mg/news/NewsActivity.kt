package pl.mbaleczny.rapid_mg.news

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import io.fabric.sdk.android.Fabric
import pl.mbaleczny.rapid_mg.BuildConfig.TWITTER_KEY
import pl.mbaleczny.rapid_mg.BuildConfig.TWITTER_SECRET
import pl.mbaleczny.rapid_mg.R
import pl.mbaleczny.rapid_mg.login.LoginActivity


class NewsActivity : AppCompatActivity() {

    companion object {
        val LOGIN_REQUEST: Int = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        initFabric()

        val session = Twitter.getSessionManager().activeSession
        if (session != null) {
            goToUserNewsScreen()
        } else {
            goToLoginScreen()
        }
    }

    private fun initFabric() {
        val authConfig = TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET)
        Fabric.with(this, Twitter(authConfig))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(javaClass.simpleName,
                String.format("requestCode = %d, resultCode = %d, data = %s",
                        requestCode,
                        resultCode,
                        data?.extras.toString()))

        when (requestCode) {
            LOGIN_REQUEST -> goToUserNewsScreen()

            else -> Toast.makeText(this, R.string.unsupported_request_code_message, Toast.LENGTH_LONG).show()
        }
    }

    fun goToLoginScreen() {
        val i: Intent = Intent(this, LoginActivity::class.java)

        startActivityForResult(i, LOGIN_REQUEST)
    }

    private fun goToUserNewsScreen() {
    }
}
