package pl.mbaleczny.rapid_mg.data.source.local

import android.provider.BaseColumns

/**
 * Created by mariusz on 14.02.17.
 */
abstract class TweetsDbContract private constructor() {

    class TweetEntry : BaseColumns {
        companion object {
            val TABLE = "tweet_table"
            val COLUMN_TWEET_ID = "tweet_id"
            val COLUMN_USER_ID = "user_id"
            val COLUMN_CREATED_AT = "created_at"
            val COLUMN_CONTENT = "content"
        }
    }

    abstract class UserEntry : BaseColumns {
        companion object {
            val TABLE = "user_table"
            val COLUMN_ID = "id"
            val COLUMN_PROFILE_IMG = "profile_img"
        }
    }
}