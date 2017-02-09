package pl.mbaleczny.rapid_mg.data

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import io.reactivex.Observable

/**
 * Created by mariusz on 09.02.17.
 */
interface TwitterDataSource {

    fun getUserTimeline(userId: Long?, count: Int?): Observable<List<Tweet>>

    fun favorites(userId: Long?, count: Int?): Observable<List<Tweet>>

    fun favorite(id: Long?): Observable<Tweet>

    fun unFavorite(id: Long?): Observable<Tweet>

    fun getUser(userId: Long?, includeEntities: Boolean): Observable<User>

}