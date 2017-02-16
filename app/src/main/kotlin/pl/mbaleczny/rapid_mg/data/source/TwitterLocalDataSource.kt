package pl.mbaleczny.rapid_mg.data.source

import android.content.Context
import com.google.gson.Gson
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import io.reactivex.Observable
import pl.mbaleczny.rapid_mg.data.TwitterDataSource

/**
 * Created by mariusz on 14.02.17.
 */
class TwitterLocalDataSource : TwitterDataSource {

    companion object {
        private var instance: TwitterLocalDataSource? = null
        fun getInstance(context: Context, gson: Gson)
                : TwitterLocalDataSource {

            if (instance == null) {
                instance = TwitterLocalDataSource(context, gson)
            }
            return instance!!
        }
    }

    private var tweetsDbHelper: TweetsDbHelper

    private constructor(context: Context, gson: Gson) {
        tweetsDbHelper = TweetsDbHelper(context, gson)
    }


    override fun getUserTimeline(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>> = unsupportedOperationError()


    override fun getHomeTimeline(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>> = unsupportedOperationError()


    override fun favorites(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>>
            = Observable.fromCallable { tweetsDbHelper.getFavorites(userId, count, sinceId, maxId) }


    override fun favorite(userId: Long?, tweet: Tweet?)
            : Observable<Tweet>
            = Observable.fromCallable { tweetsDbHelper.saveFavorite(userId, tweet) }


    override fun unFavorite(userId: Long?, tweet: Tweet?)
            : Observable<Tweet>
            = Observable.fromCallable { tweetsDbHelper.removeFavorite(userId, tweet) }


    override fun getUser(userId: Long?, includeEntities: Boolean)
            : Observable<User>
            = unsupportedOperationError()


    private fun <T> unsupportedOperationError(): Observable<T>
            = Observable.error(Exception("Unsupported operation"))

}