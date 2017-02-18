package pl.mbaleczny.rapid_mg.tweetList.presenter

import com.twitter.sdk.android.core.models.Tweet
import io.reactivex.disposables.Disposable
import pl.mbaleczny.rapid_mg.data.TwitterDataSource

/**
 * Created by mariusz on 09.02.17.
 */
class FavoritesPresenter(twitterDataSource: TwitterDataSource, val loggedUserId: Long?)
    : BaseTweetListPresenter(twitterDataSource) {

    override fun loadNewerTweets() {
        disposables.add(getTweets(_userId, firstId, null))
    }

    override fun loadOlderTweets() {
        disposables.add(getTweets(_userId, null, lastId))
    }

    override fun onUnlike(tweet: Tweet) {
        if (_userId == loggedUserId)
            disposables.add(removeLike(tweet))
        else
            super.onUnlike(tweet)
    }

    private fun removeLike(tweet: Tweet): Disposable =
            dataSource.unFavorite(_userId, tweet)
                    .subscribe({
                        tweets.remove(tweet)
                        view?.setTweets(tweets)
                    }, { view?.showError(it) })

    override fun getTweets(_userId: Long?, sinceId: Long?, maxId: Long?): Disposable {
        return applyObserver(dataSource.favorites(_userId, TWEETS_COUNT, sinceId, maxId))
    }

}