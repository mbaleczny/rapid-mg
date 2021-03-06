package pl.mbaleczny.rapid_mg.tweetList

import com.twitter.sdk.android.core.models.Tweet
import pl.mbaleczny.rapid_mg.tweetList.adapter.TweetsRecyclerAdapter
import pl.mbaleczny.rapid_mg.util.base.BasePresenter
import pl.mbaleczny.rapid_mg.util.base.BaseView

/**
 * Contract for TweetList Presenter and View.
 *
 * @author Mariusz Baleczny
 * @date 03.02.17
 */
interface TweetListContract {

    interface View : BaseView {
        fun setTweets(data: List<Tweet>?)
        fun showError(e: Throwable?)
        fun hideProgress()
        fun showMessage(s: String)
        fun openUserActivity(id: Long)
    }

    interface Presenter : BasePresenter<View>, TweetsRecyclerAdapter.onTweetAction {
        fun loadNewerTweets()
        fun loadOlderTweets()
        fun loadFreshList()
        fun setUserId(userId: Long?)
    }

}