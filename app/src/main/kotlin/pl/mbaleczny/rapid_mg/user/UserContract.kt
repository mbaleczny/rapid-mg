package pl.mbaleczny.rapid_mg.user

import com.twitter.sdk.android.core.models.User
import pl.mbaleczny.rapid_mg.util.BasePresenter
import pl.mbaleczny.rapid_mg.util.BaseView

/**
 * Created by mariusz on 11.02.17.
 */
interface UserContract {

    interface View : BaseView {
        fun setUser(user: User?)
        fun showError(it: Throwable?)
    }

    interface Presenter : BasePresenter<UserContract.View> {
        fun setUserId(userId: Long)
    }

}