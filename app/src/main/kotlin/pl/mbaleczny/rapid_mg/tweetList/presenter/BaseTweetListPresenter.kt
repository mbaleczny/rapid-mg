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
 * Base class for presenters which handles [view]
 * actions and passes to them data from [dataSource].
 *
 * @param [dataSource] source of tweets
 *
 * @author Mariusz Baleczny
 * @date 09.02.17
 */
abstract class BaseTweetListPresenter(val dataSource: TwitterDataSource)
    : TweetListContract.Presenter {

    companion object {
        val TWEETS_COUNT = 20
    }

    var _userId: Long? = null
    var firstId: Long? = null
    var lastId: Long? = null

    protected var view: TweetListContract.View? = null
    protected val tweets: MutableList<Tweet> = arrayListOf()
    protected val disposables = CompositeDisposable()

    private val tweetDateTimeComparator: Comparator<Tweet> =
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

    private fun sendLike(tweet: Tweet): Disposable =
            dataSource.favorite(_userId, tweet)
                    .subscribe({}, { view?.showError(it) })

    private fun removeLike(tweet: Tweet): Disposable =
            dataSource.unFavorite(_userId, tweet)
                    .subscribe({ }, { view?.showError(it) })

    /**
     * Concatenate Observable of tweets stored in list with Observable argument.
     * Empty lists are filtered out and items are distinct by its id to got rid of
     * doubles. Then it's sorted by date and time. The final Observable result is
     * subscribed with specified actions onNext and onError.
     */
    protected fun applyObserver(observable: Observable<List<Tweet>>): Disposable =
            Observable.concat<List<Tweet>>(Observable.just(tweets), observable)
                    .filter { !it.isEmpty() }
                    .flatMapIterable { it -> it }
                    .distinct { it.id }
                    .toSortedList(tweetDateTimeComparator)
                    .subscribe({ onNext(it) }, { onError(it) })

    /**
     * Id of first and last element are stored for next call, ie. refreshing
     * by swipe refresh sinceId will be passed to network request as reference
     * for next possible Tweets. Same way scrolling down the list maxId will
     * define reference to older Tweets.
     */
    private fun onNext(data: List<Tweet>) {
        if (data.isNotEmpty()) {
            firstId = data.first().id
            lastId = data.last().id
        } else {
            firstId = null
            lastId = null
        }
        view?.setTweets(data)
        view?.hideProgress()
    }

    private fun onError(t: Throwable) {
        view?.showError(t)
        view?.hideProgress()
    }
}