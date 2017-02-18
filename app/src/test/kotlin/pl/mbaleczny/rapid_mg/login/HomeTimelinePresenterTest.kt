package pl.mbaleczny.rapid_mg.login

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.nhaarman.mockito_kotlin.*
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import pl.mbaleczny.rapid_mg.RapidApp
import pl.mbaleczny.rapid_mg.data.TwitterDataSource
import pl.mbaleczny.rapid_mg.data.TwitterRepo
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract
import pl.mbaleczny.rapid_mg.tweetList.presenter.HomeTimelinePresenter
import pl.mbaleczny.rapid_mg.util.schedulers.BaseSchedulerProvider
import java.util.*

/**
 * Created by mariusz on 08.02.17.
 */
class HomeTimelinePresenterTest {

    private lateinit var presenter: HomeTimelinePresenter

    private var remoteDataSource: TwitterDataSource = mock()
    private var localDataSource: TwitterDataSource = mock()
    private var view: TweetListContract.View = mock()
    private val tweets: MutableList<Tweet> = ArrayList()
    private val tweet: Tweet = mock()
    private val schedulerProvider: BaseSchedulerProvider = mock()
    private val scheduler: TestScheduler = TestScheduler()

    @Before
    @Throws(Exception::class)
    fun setup() {
        presenter = HomeTimelinePresenter(
                TwitterRepo(
                        remoteDataSource,
                        localDataSource,
                        schedulerProvider))
        presenter.bindView(view)

        mockSchedulers()
        mockNetworkAvailabilityCheck()
    }

    private fun mockSchedulers() {
        whenever(schedulerProvider.io()).thenReturn(scheduler)
        whenever(schedulerProvider.ui()).thenReturn(scheduler)
        whenever(schedulerProvider.computation()).thenReturn(scheduler)
    }

    private fun mockNetworkAvailabilityCheck() {
        val rapidApp = Mockito.mock(RapidApp::class.java)
        RapidApp.setTestInstance(rapidApp)

        val cm: ConnectivityManager = mock()
        val info: NetworkInfo = mock()
        Mockito.`when`(info.isConnectedOrConnecting).thenReturn(true)
        Mockito.`when`(cm.activeNetworkInfo).thenReturn(info)
        Mockito.`when`(rapidApp.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(cm)
    }

    @Test
    fun getTweets_isCorrect() {
        presenter._userId = 1
        presenter.firstId = 0

        whenever(remoteDataSource.getHomeTimeline(eq(1), any(), anyOrNull(), anyOrNull()))
                .thenReturn(Observable.just(tweets))

        presenter.loadNewerTweets()
        scheduler.triggerActions()

        verify(view).setTweets(eq(tweets))
    }

    @Test
    fun getTweets_failed() {
        presenter._userId = 0

        whenever(remoteDataSource.getHomeTimeline(eq(0), any(), anyOrNull(), anyOrNull()))
                .doReturn(Observable.error(TwitterException("error")))

        presenter.loadNewerTweets()
        scheduler.triggerActions()

        verify(view).showError(Mockito.any(TwitterException::class.java))
    }

    @Test
    fun favorite_isCorrect() {
        presenter._userId = 1

        whenever(remoteDataSource.favorite(eq(1), eq(tweet)))
                .doReturn(Observable.just(tweet))
        whenever(localDataSource.favorite(eq(1), eq(tweet)))
                .doReturn(Observable.just(tweet))

        presenter.onLike(tweet)
        scheduler.triggerActions()

        verifyZeroInteractions(view)
    }

    @Test
    fun favorite_failed() {
        presenter._userId = 0

        whenever(remoteDataSource.favorite(eq(0), eq(tweet)))
                .doReturn(Observable.error(TwitterException("error")))
        whenever(localDataSource.favorite(eq(0), eq(tweet)))
                .doReturn(Observable.error(TwitterException("error")))

        presenter.onLike(tweet)
        scheduler.triggerActions()

        verify(view).showError(Mockito.any(TwitterException::class.java))
    }

    @Test
    fun unfavorite_isCorrect() {
        presenter._userId = 1

        whenever(remoteDataSource.unFavorite(eq(1), eq(tweet)))
                .doReturn(Observable.just(tweet))
        whenever(localDataSource.unFavorite(eq(1), eq(tweet)))
                .doReturn(Observable.just(tweet))

        presenter.onUnlike(tweet)
        scheduler.triggerActions()

        verifyZeroInteractions(view)
    }

    @Test
    fun unfavorite_failed() {
        presenter._userId = 0

        whenever(remoteDataSource.unFavorite(eq(0), eq(tweet)))
                .doReturn(Observable.error(TwitterException("error")))
        whenever(localDataSource.unFavorite(eq(0), eq(tweet)))
                .doReturn(Observable.error(TwitterException("error")))

        presenter.onUnlike(tweet)
        scheduler.triggerActions()

        verify(view).showError(Mockito.any(TwitterException::class.java))
    }

}


