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
import java.util.*

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

    var _userId: Long? = null
    var firstId: Long? = null
    var lastId: Long? = null

    private val tweetDateComparator: Comparator<Tweet> =
            Comparator { o1, o2 ->
                val dt1 = parseDateTime(o1?.createdAt)
                val dt2 = parseDateTime(o2?.createdAt)
                dt1?.isBefore(dt2)
                -DateTimeComparator.getInstance().compare(dt1, dt2)
            }

    abstract fun getTweets(_userId: Long?, sinceId: Long?, maxId: Long?): Disposable

    override fun bindView(view: TweetListContract.View) {
        this.view = view
    }

    override fun unBind() {
        view = null
        unsubscribe()
    }

    override fun setUserId(userId: Long?) {
        _userId = userId
    }

    override fun unsubscribe() {
        disposables.clear()
        view?.hideProgress()
    }

    override fun onLike(tweet: Tweet) {
        disposables.add(sendLike(tweet))
    }

    override fun onUnlike(tweet: Tweet) {
        disposables.add(removeLike(tweet))
    }

    override fun showUser(user: User) {
        view?.openUserActivity(user.id)
    }

    override fun loadFreshList() {
        tweets.clear()
        firstId = null
        lastId = null
        loadNewerTweets()
    }

    private fun sendLike(tweet: Tweet): Disposable {
        return twitterDataSource.favorite(tweet.id)
                .subscribe({}, { view?.showError(it) })
    }

    private fun removeLike(tweet: Tweet): Disposable {
        return twitterDataSource.unFavorite(tweet.id)
                .subscribe({ }, { view?.showError(it) })
    }

    protected fun applyObserver(observable: Observable<List<Tweet>>): Disposable =
            observable
                    .map { it ->
                        tweets.addAll(it)
                        tweets
                    }
                    .flatMapIterable { it -> it }
                    .distinct { it.id }
                    .toSortedList(tweetDateComparator)
                    .subscribe({ onNext(it) }, { onError(it) })

    private fun onNext(data: List<Tweet>) {
        if (data.isNotEmpty()) {
            firstId = data.first().id
            lastId = data.last().id
            view?.setTweets(data)
        }
        view?.hideProgress()
    }

    private fun onError(t: Throwable) {
        view?.showError(t)
        view?.hideProgress()
    }
}