package pl.mbaleczny.rapid_mg.tweetList.presenter

import io.reactivex.disposables.Disposable
import pl.mbaleczny.rapid_mg.data.TwitterDataSource

/**
 * Created by mariusz on 09.02.17.
 */
class FavoritesPresenter(twitterDataSource: TwitterDataSource)
    : BaseTweetListPresenter(twitterDataSource) {

    override fun getTweets(userId: Long?): Disposable =
            applyObserver(twitterDataSource.favorites(userId, TWEETS_COUNT))

}