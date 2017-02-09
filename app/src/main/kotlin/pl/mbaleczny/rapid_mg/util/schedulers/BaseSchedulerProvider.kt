package pl.mbaleczny.rapid_mg.util.schedulers

import io.reactivex.Scheduler


/**
 * Created by mariusz on 08.02.17.
 */
interface BaseSchedulerProvider {

    fun io(): Scheduler

    fun ui(): Scheduler

    fun computation(): Scheduler

}