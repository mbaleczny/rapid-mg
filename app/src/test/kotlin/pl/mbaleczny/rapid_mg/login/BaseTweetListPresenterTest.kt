package pl.mbaleczny.rapid_mg.login

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.mockito.Mockito
import pl.mbaleczny.rapid_mg.RapidApp
import pl.mbaleczny.rapid_mg.data.TwitterDataSource
import pl.mbaleczny.rapid_mg.data.TwitterRepo
import pl.mbaleczny.rapid_mg.util.base.BasePresenter
import pl.mbaleczny.rapid_mg.util.base.BaseView
import pl.mbaleczny.rapid_mg.util.schedulers.BaseSchedulerProvider

/**
 * @author Mariusz Baleczny
 * @date 18.02.17
 */
abstract class BaseTweetListPresenterTest<V : BaseView, T : BasePresenter<V>> {

    protected lateinit var presenter: T
    protected lateinit var view: V
    protected lateinit var repo: TwitterDataSource

    protected var remoteDataSource: TwitterDataSource = mock()
    protected var localDataSource: TwitterDataSource = mock()
    protected val schedulerProvider: BaseSchedulerProvider = mock()

    protected val scheduler: TestScheduler = TestScheduler()

    @Before
    open fun setUp() {
        repo = TwitterRepo(remoteDataSource, localDataSource, schedulerProvider)

        mockSchedulers()
        mockNetworkAvailabilityCheck(true)
    }

    protected fun mockSchedulers() {
        whenever(schedulerProvider.io()).thenReturn(scheduler)
        whenever(schedulerProvider.ui()).thenReturn(scheduler)
        whenever(schedulerProvider.computation()).thenReturn(scheduler)
    }

    protected fun mockNetworkAvailabilityCheck(available: Boolean) {
        val rapidApp = Mockito.mock(RapidApp::class.java)
        RapidApp.setTestInstance(rapidApp)

        val cm: ConnectivityManager = mock()
        val info: NetworkInfo = mock()
        whenever(info.isConnectedOrConnecting).thenReturn(available)
        whenever(cm.activeNetworkInfo).thenReturn(info)
        whenever(rapidApp.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(cm)
    }
}