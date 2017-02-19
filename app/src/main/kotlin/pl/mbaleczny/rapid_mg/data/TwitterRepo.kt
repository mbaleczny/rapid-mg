package pl.mbaleczny.rapid_mg.data

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import io.reactivex.Observable
import pl.mbaleczny.rapid_mg.RapidApp
import pl.mbaleczny.rapid_mg.util.applySchedulers
import pl.mbaleczny.rapid_mg.util.connectivityCheck
import pl.mbaleczny.rapid_mg.util.schedulers.BaseSchedulerProvider

/**
 * Manages data sources, ie. remote and local and applies
 * RxJava schedulers.
 *
 * @author Mariusz Baleczny
 * @date 09.02.17
 */
class TwitterRepo(
        remoteDataSource: TwitterDataSource,
        localDataSource: TwitterDataSource,
        schedulerProvider: BaseSchedulerProvider) : TwitterDataSource {

    private var remoteSource: TwitterDataSource = remoteDataSource
    private var localSource: TwitterDataSource? = localDataSource
    private var schedulerProvider: BaseSchedulerProvider? = schedulerProvider

    override fun getUserTimeline(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>>
            = (remoteSource.getUserTimeline(userId, count, sinceId, maxId)
            .compose(applySchedulers<List<Tweet>>(schedulerProvider))
            .compose(connectivityCheck<List<Tweet>>()))


    override fun getHomeTimeline(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>>
            = remoteSource.getHomeTimeline(userId, count, sinceId, maxId)
            .compose(applySchedulers<List<Tweet>>(schedulerProvider))
            .compose(connectivityCheck<List<Tweet>>())

    /**
     * Network availability check as last resort for Retrofit2
     * error on offline request.
     */
    override fun favorites(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>> =
            if (RapidApp.isNetworkAvailable())
                getAndSaveFavTweets(userId, count, sinceId, maxId)
            else
                getLocalFavorites(userId, count, sinceId, maxId)


    override fun favorite(userId: Long?, tweet: Tweet?): Observable<Tweet>
            = remoteSource.favorite(userId, tweet)
            .switchMap { localSource?.favorite(userId, it) }
            .compose(applySchedulers<Tweet>(schedulerProvider))
            .compose(connectivityCheck<Tweet>())


    override fun unFavorite(userId: Long?, tweet: Tweet?): Observable<Tweet>
            = remoteSource.unFavorite(userId, tweet)
            .switchMap { localSource?.unFavorite(userId, tweet) }
            .compose(applySchedulers<Tweet>(schedulerProvider))
            .compose(connectivityCheck<Tweet>())


    override fun getUser(userId: Long?, includeEntities: Boolean): Observable<User>
            = remoteSource.getUser(userId, includeEntities)
            .compose(applySchedulers<User>(schedulerProvider))
            .compose(connectivityCheck<User>())


    private fun getLocalFavorites(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>> =
            localSource?.favorites(userId, count, sinceId, maxId)!!
                    .compose(applySchedulers<List<Tweet>>(schedulerProvider))


    private fun getAndSaveFavTweets(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>>
            = remoteSource.favorites(userId, count, sinceId, maxId)
            .switchMap { Observable.fromIterable(it) }
            .switchMap { localSource?.favorite(userId, it) }
            .toList()
            .toObservable()
            .compose(applySchedulers<List<Tweet>>(schedulerProvider))

}
