package pl.mbaleczny.rapid_mg.tweetList.presenter

import io.reactivex.disposables.Disposable
import pl.mbaleczny.rapid_mg.data.TwitterDataSource
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract

/**
 * Created by mariusz on 04.02.17.
 */

class HomeTimelinePresenter(twitterDataSource: TwitterDataSource)
    : BaseTweetListPresenter(twitterDataSource), TweetListContract.Presenter {

    override fun loadNewerTweets() {
        disposables.add(getTweets(userId, firstId, null))
    }

    override fun loadOlderTweets() {
        disposables.add(getTweets(userId, null, lastId))
    }

    override fun getTweets(userId: Long?, sinceId: Long?, maxId: Long?): Disposable =
            applyObserver(twitterDataSource.getHomeTimeline(userId, TWEETS_COUNT, sinceId, maxId))

    override fun bindView(view: TweetListContract.View) {
        this.view = view
    }

    override fun unBind() {
        view = null
        unsubscribe()
    }
}