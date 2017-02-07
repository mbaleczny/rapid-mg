package pl.mbaleczny.rapid_mg.login

import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import pl.mbaleczny.rapid_mg.BasePresenter
import pl.mbaleczny.rapid_mg.BaseView

/**
 * Created by mariusz on 03.02.17.
 */
interface LoginContract {

    interface View : BaseView {
        fun setResultAndFinish(resultCode: Int)
        fun showToast(msg: String)
        fun showToast(msgRes: Int)
    }

    interface Presenter : BasePresenter<View> {
        fun onSuccessLogin(result: Result<TwitterSession>?)
        fun onFailedLogin(exception: TwitterException?)
    }

}