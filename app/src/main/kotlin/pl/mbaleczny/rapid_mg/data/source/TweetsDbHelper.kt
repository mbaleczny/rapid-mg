package pl.mbaleczny.rapid_mg.data.source

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import com.google.gson.Gson
import com.twitter.sdk.android.core.models.Tweet
import pl.mbaleczny.rapid_mg.util.TweetUtils
import pl.mbaleczny.rapid_mg.util.toTweet

/**
 * Created by mariusz on 14.02.17.
 */
class TweetsDbHelper(context: Context, val gson: Gson)
    : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        val DB_NAME = "Tweets.db"
        val DB_VERSION = 1

        val TEXT_TYPE = " TEXT"
        val INTEGER_TYPE = " INTEGER"
        val BLOB_TYPE = " BLOB"
        val COMMA_SEP = ","

        val DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS "

        val PRAGMA_FOREIGN_KEYS_ON = "PRAGMA foreign_keys = ON"

        val SQL_CREATE_USERS = "CREATE TABLE " + TweetsDbContract.UserEntry.TABLE + " (" +
                BaseColumns._ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                TweetsDbContract.UserEntry.COLUMN_ID + INTEGER_TYPE +
                " UNIQUE COLLATE NOCASE NOT NULL" + COMMA_SEP +
                TweetsDbContract.UserEntry.COLUMN_PROFILE_IMG + BLOB_TYPE + " NOT NULL )"

        val SQL_CREATE_TWEETS = "CREATE TABLE " + TweetsDbContract.TweetEntry.TABLE + " (" +
                BaseColumns._ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                TweetsDbContract.TweetEntry.COLUMN_TWEET_ID + INTEGER_TYPE + " NOT NULL" +
                COMMA_SEP +
                TweetsDbContract.TweetEntry.COLUMN_USER_ID + INTEGER_TYPE + COMMA_SEP +
                TweetsDbContract.TweetEntry.COLUMN_CONTENT + TEXT_TYPE + ")"

        val SQL_DELETE_TWEETS = DROP_TABLE_IF_EXISTS + TweetsDbContract.TweetEntry.TABLE

        val SQL_DELETE_USERS = DROP_TABLE_IF_EXISTS + TweetsDbContract.UserEntry.TABLE
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(PRAGMA_FOREIGN_KEYS_ON)
        db?.execSQL(SQL_CREATE_USERS)
        db?.execSQL(SQL_CREATE_TWEETS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_TWEETS)
        db?.execSQL(SQL_DELETE_USERS)
        onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            db?.setForeignKeyConstraintsEnabled(true)
    }

    fun getFavorites(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : List<Tweet> {

        var selection: String = TweetsDbContract.TweetEntry.COLUMN_USER_ID + "=?"
        val selectionArgs: Array<String> = arrayOf(userId.toString())

        if (sinceId != null && maxId != null) {
            selection += " AND " + TweetsDbContract.TweetEntry.COLUMN_TWEET_ID + " between ? AND ?"
            selectionArgs.plus(maxId.toString())
        } else if (sinceId != null && maxId == null) {
            selection += " AND " + TweetsDbContract.TweetEntry.COLUMN_TWEET_ID + ">?"
            selectionArgs.plus(sinceId.toString())
        } else if (sinceId == null && maxId != null) {
            selection += " AND " + TweetsDbContract.TweetEntry.COLUMN_TWEET_ID + "<?"
            selectionArgs.plus(maxId.toString())
        }

        val db = readableDatabase
        val c: Cursor? = db.query(TweetsDbContract.TweetEntry.TABLE, null,
                selection, selectionArgs, null, null, null, count?.toString())

        val tweets: MutableList<Tweet> = mutableListOf()

        if (c != null && c.moveToFirst()) {
            while (c.moveToNext()) {
                tweets.add(c.toTweet(gson))
            }
        }

        c?.close()
        db.close()

        return tweets
    }

    fun saveFavorite(userId: Long?, tweet: Tweet?): Tweet {
        val db = readableDatabase

        tweet?.favorited

        val values: ContentValues = ContentValues()
        values.put(TweetsDbContract.TweetEntry.COLUMN_TWEET_ID, tweet?.id.toString())
        values.put(TweetsDbContract.TweetEntry.COLUMN_USER_ID, userId)
        values.put(TweetsDbContract.TweetEntry.COLUMN_CONTENT, TweetUtils.tweetToJson(tweet!!))

        db.insert(TweetsDbContract.TweetEntry.TABLE, null, values)
        db.close()
        return tweet
    }

    fun removeFavorite(userId: Long?, tweet: Tweet?): Tweet {
        val db = readableDatabase

        val selection = TweetsDbContract.TweetEntry.COLUMN_TWEET_ID + "=? AND " +
                TweetsDbContract.TweetEntry.COLUMN_USER_ID + "=?"
        val selectionArgs: Array<String> = arrayOf(tweet?.id.toString(), userId.toString())

        db.delete(TweetsDbContract.TweetEntry.TABLE, selection, selectionArgs)
        db.close()

        return tweet!!
    }

}
