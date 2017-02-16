package pl.mbaleczny.rapid_mg.data.source.remote

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import io.reactivex.Observable
import pl.mbaleczny.rapid_mg.data.TwitterDataSource
import pl.mbaleczny.rapid_mg.network.RxTwitterService

/**
 * Created by mariusz on 16.02.17.
 */
class TwitterRemoteDataSource(val rxTwitterService: RxTwitterService) : TwitterDataSource {

    override fun getUserTimeline(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>>
            = rxTwitterService.userTimeline(userId, count, sinceId, maxId)


    override fun getHomeTimeline(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>>
            = rxTwitterService.homeTimeline(userId, count, sinceId, maxId)


    override fun favorites(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>>
            = rxTwitterService.favorites(userId, count, sinceId, maxId)


    override fun favorite(userId: Long?, tweet: Tweet?)
            : Observable<Tweet>
            = rxTwitterService.favorite(tweet?.id)


    override fun unFavorite(userId: Long?, tweet: Tweet?)
            : Observable<Tweet>
            = rxTwitterService.unFavorite(tweet?.id)


    override fun getUser(userId: Long?, includeEntities: Boolean)
            : Observable<User>
            = rxTwitterService.user(userId)

}