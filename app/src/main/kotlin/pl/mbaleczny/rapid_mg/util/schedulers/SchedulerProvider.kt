package pl.mbaleczny.rapid_mg.util.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Default implementation of [BaseSchedulerProvider]
 * for Android.
 *
 * @author Mariusz Baleczny
 * @date 08.02.17
 */
class SchedulerProvider : BaseSchedulerProvider {

    override fun io(): Scheduler = Schedulers.io()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

    override fun computation(): Scheduler = Schedulers.computation()

}