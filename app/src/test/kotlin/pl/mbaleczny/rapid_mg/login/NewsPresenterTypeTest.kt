package pl.mbaleczny.rapid_mg.login

import com.nhaarman.mockito_kotlin.*
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import pl.mbaleczny.rapid_mg.data.TwitterRepo
import pl.mbaleczny.rapid_mg.network.RxTwitterService
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract
import pl.mbaleczny.rapid_mg.tweetList.presenter.TweetTimelinePresenter

/**
 * Created by mariusz on 08.02.17.
 */
class NewsPresenterTypeTest {

    private lateinit var presenter: TweetTimelinePresenter

    private var twitterService: RxTwitterService = mock()
    private var view: TweetListContract.View = mock()
    private val tweets: List<Tweet> = arrayListOf(mock(), mock())
    private val scheduler: TestScheduler = TestScheduler()

    @Before
    @Throws(Exception::class)
    fun setup() {
        presenter = TweetTimelinePresenter(TwitterRepo(twitterService, scheduler))
        presenter.bindView(view)

        whenever(twitterService.userTimeline(eq(1), any()))
                .thenReturn(Observable.just(tweets))
        whenever(twitterService.userTimeline(eq(0), any()))
                .doReturn(Observable.error(TwitterException("error")))
    }

    @Test
    fun getTweets_isCorrect() {
        presenter.subscribe(1)
        scheduler.triggerActions()

        verify(view).setTweets(any())
    }

    @Test
    fun getTweets_failed() {
        presenter.subscribe(0)
        scheduler.triggerActions()

        verify(view).showError(Mockito.any(TwitterException::class.java))
    }

}


