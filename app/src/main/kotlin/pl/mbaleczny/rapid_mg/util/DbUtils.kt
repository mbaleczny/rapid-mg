package pl.mbaleczny.rapid_mg.util

import android.database.Cursor
import com.google.gson.Gson
import com.twitter.sdk.android.core.models.Tweet
import pl.mbaleczny.rapid_mg.data.source.local.TweetsDbContract

/**
 * Created by mariusz on 15.02.17.
 */

fun Cursor.toTweet(gson: Gson): Tweet {
    val tweetJson: String? = getString(getColumnIndex(TweetsDbContract.TweetEntry.COLUMN_CONTENT))
    return gson.fromJson(tweetJson, Tweet::class.java)
}