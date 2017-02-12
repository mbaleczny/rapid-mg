package pl.mbaleczny.rapid_mg.tweetList.presenter

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.joda.time.DateTimeComparator
import pl.mbaleczny.rapid_mg.data.TwitterDataSource
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract
import pl.mbaleczny.rapid_mg.util.parseDateTime

/**
 * Created by mariusz on 09.02.17.
 */
abstract class BaseTweetListPresenter(val twitterDataSource: TwitterDataSource) : TweetListContract.Presenter {

    companion object {
        val TWEETS_COUNT = 20
    }

    protected var view: TweetListContract.View? = null
    protected val disposables = CompositeDisposable()

    protected val tweets: MutableList<Tweet> = arrayListOf()

    var userId: Long? = null
    var firstId: Long? = null
    var lastId: Long? = null

    abstract fun getTweets(userId: Long?, sinceId: Long?, maxId: Long?): Disposable

    override fun bindView(view: TweetListContract.View) {
        this.view = view
    }

    override fun unBind() {
        view = null
        unsubscribe()
    }

    override fun unsubscribe() {
        disposables.clear()
        view?.hideProgress()
    }

    override fun onLike(tweet: Tweet) {
    }

    override fun onUnlike(tweet: Tweet) {
    }

    override fun showUser(user: User) {
        view?.openUserActivity(user.id)
    }

    protected fun applyObserver(observable: Observable<List<Tweet>>): Disposable =
            observable
                    .map { it ->
                        tweets.addAll(it)
                        tweets
                    }.flatMapIterable { it -> it }
                    .toSortedList { o1, o2 ->
                        val dt1 = parseDateTime(o1?.createdAt)
                        val dt2 = parseDateTime(o2?.createdAt)
                        dt1?.isBefore(dt2)
                        -DateTimeComparator.getInstance().compare(dt1, dt2)
                    }.subscribe({ t -> view?.setTweets(t) },
                    { t -> view?.showError(t) })
}