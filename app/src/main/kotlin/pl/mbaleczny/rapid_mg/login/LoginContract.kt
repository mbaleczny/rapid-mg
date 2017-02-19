package pl.mbaleczny.rapid_mg.login

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import pl.mbaleczny.rapid_mg.util.base.BasePresenter
import pl.mbaleczny.rapid_mg.util.base.BaseView

/**
 * @author Mariusz Baleczny
 * @date 03.02.17
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