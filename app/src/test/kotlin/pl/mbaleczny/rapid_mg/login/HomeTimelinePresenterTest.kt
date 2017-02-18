package pl.mbaleczny.rapid_mg.login

import com.nhaarman.mockito_kotlin.*
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract
import pl.mbaleczny.rapid_mg.tweetList.presenter.HomeTimelinePresenter
import java.util.*

/**
 * Created by mariusz on 08.02.17.
 */
class HomeTimelinePresenterTest
    : BaseTweetListPresenterTest<TweetListContract.View, HomeTimelinePresenter>() {

    private val tweets: MutableList<Tweet> = ArrayList()
    private val tweet: Tweet = mock()

    @Before
    @Throws(Exception::class)
    fun setup() {
        super.setUp()
        presenter = HomeTimelinePresenter(repo)
        view = mock()
        presenter.bindView(view)
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

    @Test
    fun getFavoritesOnline_isCorrect() {
        presenter._userId = 1

        val tweets: List<Tweet> = arrayListOf(mock(), mock())
        whenever(remoteDataSource.favorites(eq(1), eq(10), anyOrNull(), anyOrNull()))
                .doReturn(Observable.just(tweets))
    }
}


