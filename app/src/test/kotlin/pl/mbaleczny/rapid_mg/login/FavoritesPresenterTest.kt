package pl.mbaleczny.rapid_mg.login

import com.nhaarman.mockito_kotlin.*
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract
import pl.mbaleczny.rapid_mg.tweetList.presenter.FavoritesPresenter
import java.io.IOException
import java.util.*

/**
 * Created by mariusz on 08.02.17.
 */
class FavoritesPresenterTest
    : BaseTweetListPresenterTest<TweetListContract.View, FavoritesPresenter>() {

    private val tweets: MutableList<Tweet> = ArrayList()
    private val tweet: Tweet = mock()
    private var loggedUserId: Long = 1

    @Before
    fun setup() {
        super.setUp()
        presenter = FavoritesPresenter(repo, loggedUserId)
        view = mock()
        presenter.bindView(view)
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
    fun favoriteOffline_failed() {
        presenter._userId = 0

        mockNetworkAvailabilityCheck(false)
        whenever(remoteDataSource.favorite(eq(0), eq(tweet)))
                .thenReturn(Observable.just(tweet))
        whenever(localDataSource.favorite(eq(0), eq(tweet)))
                .thenReturn(Observable.just(tweet))

        presenter.onLike(tweet)
        scheduler.triggerActions()

        verify(view).showError(Mockito.any(IOException::class.java))
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

        verify(view).setTweets(any())
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
    fun unFavoriteOffline_failed() {
        presenter._userId = 0

        mockNetworkAvailabilityCheck(false)
        whenever(remoteDataSource.unFavorite(eq(0), eq(tweet)))
                .thenReturn(Observable.just(tweet))
        whenever(localDataSource.unFavorite(eq(0), eq(tweet)))
                .thenReturn(Observable.just(tweet))

        presenter.onUnlike(tweet)
        scheduler.triggerActions()

        verify(view).showError(Mockito.any(IOException::class.java))
    }


    @Test
    fun getFavoritesOnline_isCorrect() {
        presenter._userId = 1

        tweets.add(tweet)
        whenever(remoteDataSource.favorites(eq(1), any(), anyOrNull(), anyOrNull()))
                .thenReturn(Observable.just(tweets))

        whenever(localDataSource.favorite(eq(1), eq(tweet)))
                .thenReturn(Observable.just(tweet))

        presenter.loadFreshList()
        scheduler.triggerActions()

        verify(view).setTweets(eq(tweets))
    }

    @Test
    fun getFavoritesOnline_failed() {
        presenter._userId = 1

        tweets.add(tweet)
        whenever(remoteDataSource.favorites(any(), any(), anyOrNull(), anyOrNull()))
                .thenReturn(Observable.error(TwitterException("error")))

        presenter.loadFreshList()
        scheduler.triggerActions()

        verify(view).showError(Mockito.any(TwitterException::class.java))
    }

    @Test
    fun getFavoritesOffline_isCorrect() {
        presenter._userId = 1

        mockNetworkAvailabilityCheck(false)
        tweets.add(tweet)
        whenever(localDataSource.favorites(eq(1), any(), anyOrNull(), anyOrNull()))
                .thenReturn(Observable.just(tweets))

        presenter.loadFreshList()
        scheduler.triggerActions()

        verify(view).setTweets(eq(tweets))
    }

}


