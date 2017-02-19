package pl.mbaleczny.rapid_mg.login

import com.nhaarman.mockito_kotlin.*
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.User
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import pl.mbaleczny.rapid_mg.user.UserContract
import pl.mbaleczny.rapid_mg.user.UserPresenter

/**
 * @author Mariusz Baleczny
 * @date 18.02.17
 */
class UserPresenterTest : BaseTweetListPresenterTest<UserContract.View, UserPresenter>() {

    @Before
    override fun setUp() {
        super.setUp()
        presenter = UserPresenter(repo)
        view = mock()
        presenter.bindView(view)
    }

    @Test
    fun getUser_isCorrect() {
        presenter.setUserId(1)

        val user: User = mock()

        whenever(remoteDataSource.getUser(eq(1), any()))
                .thenReturn(Observable.just(user))

        presenter.subscribe()
        scheduler.triggerActions()

        verify(view).setUser(eq(user))
    }

    @Test
    fun getUser_failed() {
        presenter.setUserId(1)

        whenever(remoteDataSource.getUser(eq(1), any()))
                .doReturn(Observable.error(TwitterException("error message")))

        presenter.subscribe()
        scheduler.triggerActions()

        verify(view).showError(Mockito.any(TwitterException::class.java))
    }
}