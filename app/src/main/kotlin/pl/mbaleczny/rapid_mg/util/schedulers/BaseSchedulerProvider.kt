package pl.mbaleczny.rapid_mg.util.schedulers

import io.reactivex.Scheduler


/**
 * Common interface of class which provides
 * Reactive Extensions' [Scheduler].
 *
 * @author Mariusz Baleczny
 * @date 08.02.17
 */
interface BaseSchedulerProvider {

    fun io(): Scheduler

    fun ui(): Scheduler

    fun computation(): Scheduler

}