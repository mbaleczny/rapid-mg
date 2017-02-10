package pl.mbaleczny.rapid_mg.tweetList.presenter

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import pl.mbaleczny.rapid_mg.data.TwitterDataSource
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract

/**
 * Created by mariusz on 09.02.17.
 */
abstract class BaseTweetListPresenter(val twitterDataSource: TwitterDataSource) : TweetListContract.Presenter {

    protected var view: TweetListContract.View? = null
    protected val disposables = CompositeDisposable()

    abstract fun getTweets(userId: Long?): Disposable

    override fun bindView(view: TweetListContract.View) {
        this.view = view
    }

    override fun unBind() {
        view = null
        unsubscribe()
    }

    override fun unsubscribe() = disposables.clear()

    override fun subscribe(userId: Long?) {
        disposables.add(getTweets(userId))
    }

    override fun onLike(tweet: Tweet) {
    }

    override fun onUnlike(tweet: Tweet) {
    }

    override fun showUser(user: User) {
    }

}