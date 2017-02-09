package pl.mbaleczny.rapid_mg.login

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import pl.mbaleczny.rapid_mg.util.BasePresenter
import pl.mbaleczny.rapid_mg.util.BaseView

/**
 * Created by mariusz on 03.02.17.
 */
interface LoginContract {

    interface View : BaseView {
        fun onUserLogged(userName: String?)
        fun onFailedLogin(exception: TwitterException?)
    }

    interface Presenter : BasePresenter<View> {
        fun getTwitterCallback(): Callback<TwitterSession>
    }

}