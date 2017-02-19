package pl.mbaleczny.rapid_mg.user

import com.twitter.sdk.android.core.models.User
import pl.mbaleczny.rapid_mg.util.base.BasePresenter
import pl.mbaleczny.rapid_mg.util.base.BaseView

/**
 * Contract for User details Presenter and View.
 *
 * @author Mariusz Baleczny
 * @date 11.02.17
 */
interface UserContract {

    interface View : BaseView {
        fun setUser(user: User?)
        fun showError(it: Throwable?)
    }

    interface Presenter : BasePresenter<View> {
        fun setUserId(userId: Long)
    }

}