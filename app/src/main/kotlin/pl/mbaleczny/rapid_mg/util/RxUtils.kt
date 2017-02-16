package pl.mbaleczny.rapid_mg.util

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import pl.mbaleczny.rapid_mg.RapidApp
import pl.mbaleczny.rapid_mg.util.schedulers.BaseSchedulerProvider
import java.io.IOException

/**
 * Created by mariusz on 16.02.17.
 */
class RxUtils {

    companion object {
        fun <T> applySchedulers(schedulerProvider: BaseSchedulerProvider?)
                : ObservableTransformer<T, T> =
                ObservableTransformer {
                    it.subscribeOn(schedulerProvider?.io())
                            .observeOn(schedulerProvider?.ui())
                }

        fun <T> connectivityCheck(): ObservableTransformer<T, T> =
                ObservableTransformer {
                    if (RapidApp.isNetworkAvailable()) {
                        it
                    } else {
                        Observable.error(IOException("No Internet connection"))
                    }
                }
    }

}