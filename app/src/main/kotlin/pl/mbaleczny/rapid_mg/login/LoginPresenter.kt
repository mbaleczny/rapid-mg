package pl.mbaleczny.rapid_mg.login

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession

/**
 * Created by mariusz on 03.02.17.
 */
class LoginPresenter : LoginContract.Presenter {

    var loginView: LoginContract.View? = null

    override fun getTwitterCallback(): Callback<TwitterSession> {
        return object : Callback<TwitterSession>() {

            override fun success(result: Result<TwitterSession>?) {
                val session: TwitterSession? = result?.data
                loginView?.onUserLogged(session?.userName)
            }

            override fun failure(exception: TwitterException?) {
                loginView?.onFailedLogin(exception)
            }
        }
    }

    override fun bindView(view: LoginContract.View) {
        loginView = view
    }

    override fun unBind() {
        loginView = null
    }
}