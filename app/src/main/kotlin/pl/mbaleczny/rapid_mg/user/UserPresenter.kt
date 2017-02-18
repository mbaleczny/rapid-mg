package pl.mbaleczny.rapid_mg.user

import io.reactivex.disposables.CompositeDisposable
import pl.mbaleczny.rapid_mg.data.TwitterDataSource

/**
 * Created by mariusz on 11.02.17.
 */
class UserPresenter(val dataSource: TwitterDataSource) : UserContract.Presenter {

    private var view: UserContract.View? = null
    private var userId: Long? = null
    private val disposables = CompositeDisposable()

    override fun subscribe() {
        disposables.add(dataSource.getUser(userId, false)
                .subscribe({ view?.setUser(it) }, { view?.showError(it) }))
    }

    override fun setUserId(userId: Long) {
        this.userId = userId
    }

    override fun bindView(view: UserContract.View): Unit {
        this.view = view
    }

    override fun unBind() {
        view = null
    }
}