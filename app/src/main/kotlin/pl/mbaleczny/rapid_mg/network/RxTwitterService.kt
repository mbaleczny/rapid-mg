package pl.mbaleczny.rapid_mg.network

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by mariusz on 08.02.17.
 */
interface RxTwitterService {

    @GET("/1.1/statuses/user_timeline.json")
    fun userTimeline(@Query("user_id") userId: Long?,
                     @Query("count") count: Int?,
                     @Query("since_id") sinceId: Long?,
                     @Query("max_id") maxId: Long?): Observable<List<Tweet>>

    @GET("/1.1/statuses/home_timeline.json")
    fun homeTimeline(@Query("user_id") userId: Long?,
                     @Query("count") count: Int?,
                     @Query("since_id") sinceId: Long?,
                     @Query("max_id") maxId: Long?): Observable<List<Tweet>>

    @GET("/1.1/favorites/list.json")
    fun favorites(@Query("user_id") userId: Long?,
                  @Query("count") count: Int?,
                  @Query("since_id") sinceId: Long?,
                  @Query("max_id") maxId: Long?): Observable<List<Tweet>>

    @GET("/1.1/users/show.json")
    fun user(@Query("user_id") userId: Long?,
             @Query("include_entities") includeEntities: Boolean = true): Observable<User>

    @FormUrlEncoded
    @POST("/1.1/favorites/destroy.json")
    fun unFavorite(@Field("id") id: Long?,
                   @Field("include_entities") includeEntities: Boolean = false): Observable<Tweet>

    @FormUrlEncoded
    @POST("/1.1/favorites/create.json")
    fun favorite(@Field("id") id: Long?,
                 @Field("include_entities") includeEntities: Boolean = false): Observable<Tweet>

}