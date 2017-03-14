package pl.mbaleczny.rapid_mg.network

import com.twitter.sdk.android.core.*
import javax.net.ssl.SSLSocketFactory

/**
 * @author Mariusz Baleczny
 * @date 20.02.17
 */
class MockTwitterProvider : TwitterProvider {

    override fun addApiClient(session: TwitterSession, apiClient: TwitterApiClient) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun authConfig(): TwitterAuthConfig? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sslSocketFactory(): SSLSocketFactory? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun apiClient(): TwitterApiClient {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sessionManager(): SessionManager<TwitterSession> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun session(): TwitterSession {
        return TwitterSession(TwitterAuthToken("token", "secret"), 0, "test_user")
    }
}