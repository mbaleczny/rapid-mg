package pl.mbaleczny.rapid_mg.login

import com.nhaarman.mockito_kotlin.*
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import pl.mbaleczny.rapid_mg.data.TwitterDataSource
import pl.mbaleczny.rapid_mg.data.TwitterRepo
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract
import pl.mbaleczny.rapid_mg.tweetList.presenter.HomeTimelinePresenter
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
    private val scheduler: TestScheduler = TestScheduler()

    @Before
    @Throws(Exception::class)
    fun setup() {
        presenter = HomeTimelinePresenter(TwitterRepo(remoteDataSource, localDataSource, scheduler))
        presenter.bindView(view)

        whenever(remoteDataSource.getHomeTimeline(eq(1), any(), anyOrNull(), anyOrNull()))
                .thenReturn(Observable.just(tweets))
        whenever(remoteDataSource.getHomeTimeline(eq(0), any(), anyOrNull(), anyOrNull()))
                .doReturn(Observable.error(TwitterException("error")))
    }

    @Test
    fun getTweets_isCorrect() {
        presenter._userId = 1
        presenter.firstId = 0

        presenter.loadNewerTweets()
        scheduler.triggerActions()

        verify(view).setTweets(eq(tweets))
    }

    @Test
    fun getTweets_failed() {
        presenter._userId = 0

        presenter.loadNewerTweets()
        scheduler.triggerActions()

        verify(view).showError(Mockito.any(TwitterException::class.java))
    }

}


