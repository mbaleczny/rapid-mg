package pl.mbaleczny.rapid_mg.data

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import io.reactivex.Observable

/**
 * Contract for data source implementations.
 *
 * @author Mariusz Baleczny
 * @date 09.02.17
 */
interface TwitterDataSource {

    fun getUserTimeline(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>>

    fun getHomeTimeline(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>>

    fun favorites(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?): Observable<List<Tweet>>

    fun favorite(userId: Long?, tweet: Tweet?): Observable<Tweet>

    fun unFavorite(userId: Long?, tweet: Tweet?): Observable<Tweet>

    fun getUser(userId: Long?, includeEntities: Boolean): Observable<User>

}