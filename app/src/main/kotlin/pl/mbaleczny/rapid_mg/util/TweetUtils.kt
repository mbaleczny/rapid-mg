package pl.mbaleczny.rapid_mg.util

import com.google.gson.JsonObject
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User

/**
 * Converts [Tweet] to/from Json String.
 * [Tweet] class contains attributes @SerializedName
 * but in this application many of them are redundant.
 *
 * @author Mariusz Baleczny
 * @date 15.02.17
 */
class TweetUtils {
    companion object {

        fun tweetToJson(t: Tweet): String {
            val tweet = JsonObject()

            tweet.addProperty("id", t.id)
            tweet.addProperty("text", t.text)
            tweet.addProperty("created_at", t.createdAt)
            tweet.addProperty("favorite_count", t.favoriteCount)
            tweet.addProperty("favorited", t.favorited)

            tweet.add("user", userToJson(t.user))

            return tweet.toString()
        }

        fun userToJson(u: User): JsonObject {
            val user = JsonObject()

            user.addProperty("id", u.id)
            user.addProperty("name", u.name)
            user.addProperty("screen_name", u.screenName)
            user.addProperty("profile_image_url", u.profileImageUrl)

            return user
        }
    }
}