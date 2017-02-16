package pl.mbaleczny.rapid_mg.data

import android.support.annotation.VisibleForTesting
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import io.reactivex.Observable
import io.reactivex.Scheduler
import pl.mbaleczny.rapid_mg.RapidApp
import pl.mbaleczny.rapid_mg.util.RxUtils.Companion.applySchedulers
import pl.mbaleczny.rapid_mg.util.RxUtils.Companion.connectivityCheck
import pl.mbaleczny.rapid_mg.util.schedulers.BaseSchedulerProvider

/**
 * Created by mariusz on 09.02.17.
 */
class TwitterRepo : TwitterDataSource {

    private var remoteSource: TwitterDataSource
    private var localSource: TwitterDataSource?
    private var schedulerProvider: BaseSchedulerProvider? = null

    private var io: Scheduler
    private var ui: Scheduler
    private var computation: Scheduler

    constructor(remoteDataSource: TwitterDataSource,
                localDataSource: TwitterDataSource,
                schedulerProvider: BaseSchedulerProvider) {
        this.remoteSource = remoteDataSource
        this.localSource = localDataSource
        this.schedulerProvider = schedulerProvider

        io = schedulerProvider.io()
        ui = schedulerProvider.ui()
        computation = schedulerProvider.computation()
    }

    @VisibleForTesting
    constructor(remoteDataSource: TwitterDataSource,
                localDataSource: TwitterDataSource,
                scheduler: Scheduler) {
        this.remoteSource = remoteDataSource
        this.localSource = localDataSource
        this.schedulerProvider = null
        io = scheduler
        ui = scheduler
        computation = scheduler
    }


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
            .switchMap { localSource?.unFavorite(userId, it) }
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
