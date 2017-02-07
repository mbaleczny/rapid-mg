package pl.mbaleczny.rapid_mg.login

import android.app.Activity
import android.util.Log
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import pl.mbaleczny.rapid_mg.R

/**
 * Created by mariusz on 03.02.17.
 */
class LoginPresenter : LoginContract.Presenter {

    private var loginView: LoginContract.View? = null

    override fun onSuccessLogin(result: Result<TwitterSession>?) {
        val session: TwitterSession? = result?.data
        Log.d(javaClass.simpleName, "Logged user = " + session?.userName)

        val msg: String = String.format("Logged in successfully. \nWelcome %s!", session?.userName)
        loginView?.showToast(msg)
        loginView?.setResultAndFinish(Activity.RESULT_OK)
    }

    override fun onFailedLogin(exception: TwitterException?) {
        Log.d(javaClass.simpleName, exception?.message)
        loginView?.showToast(R.string.fail_to_login_message)
    }

    override fun bindView(view: LoginContract.View) {
        loginView = view
    }

    override fun unBind() {
        loginView = null
    }
}