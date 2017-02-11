package pl.mbaleczny.rapid_mg.tweetList.presenter

import com.twitter.sdk.android.core.models.Tweet
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import pl.mbaleczny.rapid_mg.data.TwitterDataSource
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract

/**
 * Created by mariusz on 04.02.17.
 */

class TweetTimelinePresenter(twitterDataSource: TwitterDataSource)
    : BaseTweetListPresenter(twitterDataSource), TweetListContract.Presenter {

    override fun subscribe(userId: Long?) {
        disposables.add(getTweets(userId))
    }

    override fun getTweets(userId: Long?): Disposable =
            applyObserver(twitterDataSource.getUserTimeline(userId, TWEETS_COUNT))

    override fun onLike(tweet: Tweet) {
        disposables.add(sendLike(tweet))
    }

    override fun onUnlike(tweet: Tweet) {
        disposables.add(removeLike(tweet))
    }

    override fun bindView(view: TweetListContract.View) {
        this.view = view
    }

    override fun unBind() {
        view = null
        unsubscribe()
    }

    private fun sendLike(tweet: Tweet): Disposable {
        return Observable.fromCallable { twitterDataSource.favorite(tweet.id) }
                .subscribe({ }, { view?.showError(it) })
    }

    private fun removeLike(tweet: Tweet): Disposable {
        return Observable.fromCallable { twitterDataSource.unFavorite(tweet.id) }
                .subscribe({ }, { view?.showError(it) })
    }
}