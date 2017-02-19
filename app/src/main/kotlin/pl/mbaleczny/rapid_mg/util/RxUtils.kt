package pl.mbaleczny.rapid_mg.util

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import pl.mbaleczny.rapid_mg.R
import pl.mbaleczny.rapid_mg.RapidApp
import pl.mbaleczny.rapid_mg.util.schedulers.BaseSchedulerProvider
import java.io.IOException

/**
 * @author Mariusz Baleczny
 * @date 16.02.17
 */

/**
 * Sets schedulers for default action (which is network request)
 */
fun <T> applySchedulers(schedulerProvider: BaseSchedulerProvider?)
        : ObservableTransformer<T, T> =
        ObservableTransformer {
            it.subscribeOn(schedulerProvider?.io())
                    .observeOn(schedulerProvider?.ui())
        }

/**
 *  Handles action on network connection availability.
 */
fun <T> connectivityCheck(): ObservableTransformer<T, T> =
        ObservableTransformer {
            if (RapidApp.isNetworkAvailable())
                it
            else
                Observable.error(IOException(RapidApp.instance.
                        getString(R.string.no_internet_connection_message)))
        }