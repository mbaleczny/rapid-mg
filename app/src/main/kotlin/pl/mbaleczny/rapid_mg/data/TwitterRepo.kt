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

    override fun getUserTimeline(userId: Long?, count: Int?): Observable<List<Tweet>> {
        return rxTwitterService.userTimeline(userId, count)
                .subscribeOn(io)
                .observeOn(ui)
    }

    override fun favorites(userId: Long?, count: Int?): Observable<List<Tweet>> {
        return rxTwitterService.favorites(userId, count)
                .subscribeOn(io)
                .observeOn(ui)
    }

    override fun favorite(id: Long?): Observable<Tweet> {
        return rxTwitterService.favorite(id)
                .subscribeOn(io)
                .observeOn(ui)
    }

    override fun unFavorite(id: Long?): Observable<Tweet> {
        return rxTwitterService.unFavorite(id)
                .subscribeOn(io)
                .observeOn(ui)
    }

    override fun getUser(userId: Long?, includeEntities: Boolean): Observable<User> {
        return rxTwitterService.user(userId, includeEntities)
                .subscribeOn(io)
                .observeOn(ui)
    }

}

