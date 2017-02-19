package pl.mbaleczny.rapid_mg.tweetList.presenter

import io.reactivex.disposables.Disposable
import pl.mbaleczny.rapid_mg.data.TwitterDataSource
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract

/**
 * Presenter for TweetList of logged user timeline
 *
 * @param [twitterDataSource] source of tweets
 *
 * @author Mariusz Baleczny
 * @date 04.02.17
 */

class HomeTimelinePresenter(twitterDataSource: TwitterDataSource)
    : BaseTweetListPresenter(twitterDataSource), TweetListContract.Presenter {

    override fun loadNewerTweets() {
        disposables.add(getTweets(_userId, firstId, null))
    }

    override fun loadOlderTweets() {
        disposables.add(getTweets(_userId, null, lastId))
    }

    override fun getTweets(_userId: Long?, sinceId: Long?, maxId: Long?): Disposable =
            applyObserver(dataSource.getHomeTimeline(_userId, TWEETS_COUNT, sinceId, maxId))

    override fun bindView(view: TweetListContract.View) {
        this.view = view
    }

    override fun unBind() {
        view = null
        unsubscribe()
    }
}