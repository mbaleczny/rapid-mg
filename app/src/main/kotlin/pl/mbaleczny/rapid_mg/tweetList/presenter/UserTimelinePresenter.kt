package pl.mbaleczny.rapid_mg.tweetList.presenter

import io.reactivex.disposables.Disposable
import pl.mbaleczny.rapid_mg.data.TwitterDataSource

/**
 * Created by mariusz on 09.02.17.
 */
class UserTimelinePresenter(twitterDataSource: TwitterDataSource)
    : BaseTweetListPresenter(twitterDataSource) {

    override fun loadNewerTweets() {
        disposables.add(getTweets(_userId, firstId, null))
    }

    override fun loadOlderTweets() {
        disposables.add(getTweets(_userId, null, lastId))
    }

    override fun getTweets(_userId: Long?, sinceId: Long?, maxId: Long?): Disposable =
            applyObserver(dataSource.getUserTimeline(_userId, TWEETS_COUNT, sinceId, maxId))

}