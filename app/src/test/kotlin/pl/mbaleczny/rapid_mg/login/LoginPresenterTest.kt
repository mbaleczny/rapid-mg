package pl.mbaleczny.rapid_mg.login

import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import org.amshove.kluent.mock
import org.junit.Before
import org.junit.Test

/**
 * Created by mariusz on 07.02.17.
 */
class LoginPresenterTest {

    private lateinit var presenter: LoginPresenter
    private lateinit var view: LoginContract.View

    lateinit var result: Result<TwitterSession>

    @Before
    fun setup() {
        presenter = LoginPresenter()
        view = mock(LoginContract.View::class)
        result = mock(Result::class) as Result<TwitterSession>
        presenter.bindView(view)
    }

    @Test
    @Throws(Exception::class)
    fun onSuccessLogin() {
        presenter.getTwitterCallback().success(result)

        verify(view).onUserLogged(anyOrNull())
        verifyNoMoreInteractions(view)
    }

    @Test
    @Throws(Exception::class)
    fun onLoginFail() {
        presenter.getTwitterCallback().failure(mock(TwitterException::class))

        verify(view).onFailedLogin(anyOrNull())
        verifyNoMoreInteractions(view)
    }

}
