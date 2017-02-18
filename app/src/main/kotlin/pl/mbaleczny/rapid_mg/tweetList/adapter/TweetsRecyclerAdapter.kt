package pl.mbaleczny.rapid_mg.tweetList.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import com.twitter.sdk.android.tweetui.ToggleImageButton
import pl.mbaleczny.rapid_mg.R
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract
import pl.mbaleczny.rapid_mg.util.transformTweetDateTime


/**
 * Created by mariusz on 05.02.17.
 */
class TweetsRecyclerAdapter(val context: Context) : RecyclerView.Adapter<TweetsRecyclerAdapter.ViewHolder>() {

    constructor(context: Context, listener: TweetListContract.Presenter) : this(context) {
        this.listener = listener
    }

    val tweets = mutableListOf<Tweet>()
    private val inflater = LayoutInflater.from(context)

    var listener: TweetListContract.Presenter? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.tweet_view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(tweets[position])
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    fun setTweets(tweets: Iterable<Tweet>) {
        this.tweets.clear()
        this.tweets.addAll(tweets)
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean {
        return tweets.isEmpty()
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private var avatar: ImageView = itemView?.findViewById(R.id.tweet_vh_avatar) as ImageView
        private var fullName: TextView = itemView?.findViewById(R.id.tweet_vh_full_name) as TextView
        private var screen_name: TextView = itemView?.findViewById(R.id.tweet_vh_screen_name) as TextView
        private var timestamp: TextView = itemView?.findViewById(R.id.tweet_vh_timestamp) as TextView
        private var tweetText: TextView = itemView?.findViewById(R.id.tweet_vh_text) as TextView
        private var heartButton: ToggleImageButton =
                itemView?.findViewById(R.id.tw__tweet_like_button) as ToggleImageButton

        @SuppressLint("SetTextI18n", "PrivateResource")
        fun bind(tweet: Tweet) {

            Picasso.with(context)
                    .load(tweet.user.profileImageUrl)
                    .noPlaceholder()
                    .error(R.drawable.tw__ic_tweet_photo_error_light)
                    .into(avatar)

            avatar.setOnClickListener { listener?.showUser(tweet.user) }

            fullName.text = tweet.user.name
            tweetText.text = tweet.text
            screen_name.text = String.format(
                    context.getString(R.string.screen_name_template),
                    tweet.user.screenName)

            timestamp.text = "• " + transformTweetDateTime(tweet.createdAt)

            heartButton.isToggledOn = tweet.favorited
            heartButton.setOnClickListener { v ->
                if (heartButton.isToggledOn)
                    listener?.onLike(tweet)
                else
                    listener?.onUnlike(tweet)
            }
        }
    }

    interface onTweetAction {

        fun onLike(tweet: Tweet)

        fun onUnlike(tweet: Tweet)

        fun showUser(user: User)

    }
}