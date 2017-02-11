package pl.mbaleczny.rapid_mg.tweetList

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import pl.mbaleczny.rapid_mg.tweetList.adapter.TweetsRecyclerAdapter
import pl.mbaleczny.rapid_mg.util.BasePresenter
import pl.mbaleczny.rapid_mg.util.BaseView

/**
 * Created by mariusz on 03.02.17.
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
        fun subscribe(userId: Long?)

        override fun onLike(tweet: Tweet)

        override fun onUnlike(tweet: Tweet)

        override fun showUser(user: User)
    }

}