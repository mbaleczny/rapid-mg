package pl.mbaleczny.rapid_mg.data

import android.support.annotation.VisibleForTesting
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User
import io.reactivex.Observable
import io.reactivex.Scheduler
import pl.mbaleczny.rapid_mg.network.RxTwitterService
import pl.mbaleczny.rapid_mg.util.schedulers.BaseSchedulerProvider

/**
 * Created by mariusz on 09.02.17.
 */
class TwitterRepo : TwitterDataSource {

    private var rxTwitterService: RxTwitterService
    private var schedulerProvider: BaseSchedulerProvider? = null

    private var io: Scheduler
    private var ui: Scheduler
    private var computation: Scheduler

    constructor(rxTwitterService: RxTwitterService,
                schedulerProvider: BaseSchedulerProvider) {
        this.rxTwitterService = rxTwitterService
        this.schedulerProvider = schedulerProvider

        io = schedulerProvider.io()
        ui = schedulerProvider.ui()
        computation = schedulerProvider.computation()
    }

    @VisibleForTesting
    constructor(rxTwitterService: RxTwitterService, scheduler: Scheduler) {
        this.rxTwitterService = rxTwitterService
        this.schedulerProvider = null
        io = scheduler
        ui = scheduler
        computation = scheduler
    }

    override fun getUserTimeline(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?): Observable<List<Tweet>>
            = compose(rxTwitterService.userTimeline(userId, count, sinceId, maxId))

    override fun getHomeTimeline(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?): Observable<List<Tweet>>
            = compose(rxTwitterService.homeTimeline(userId, count, sinceId, maxId))

    override fun favorites(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?): Observable<List<Tweet>>
            = compose(rxTwitterService.favorites(userId, count, sinceId, maxId))

    override fun favorite(id: Long?): Observable<Tweet>
            = compose(rxTwitterService.favorite(id))

    override fun unFavorite(id: Long?): Observable<Tweet>
            = compose(rxTwitterService.unFavorite(id))

    override fun getUser(userId: Long?, includeEntities: Boolean): Observable<User>
            = compose(rxTwitterService.user(userId, includeEntities))

    private fun <T> compose(obs: Observable<T>): Observable<T> {
        return obs.subscribeOn(io).observeOn(ui)
    }

}

