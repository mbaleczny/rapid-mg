package pl.mbaleczny.rapid_mg.network

import com.twitter.sdk.android.core.*

import pl.mbaleczny.rapid_mg.network.TwitterProvider
import javax.net.ssl.SSLSocketFactory

/**
 * @author Mariusz Baleczny
 * *
 * @date 14.03.17
 */

class ProdTwitterProvider : TwitterProvider {

    override fun authConfig(): TwitterAuthConfig? {
        return TwitterCore.getInstance().authConfig
    }

    override fun sslSocketFactory(): SSLSocketFactory? {
        return TwitterCore.getInstance().sslSocketFactory
    }

    override fun addApiClient(session: TwitterSession, apiClient: TwitterApiClient) {
        TwitterCore.getInstance().addApiClient(session, apiClient)
    }

    override fun apiClient(): TwitterApiClient {
        return TwitterCore.getInstance().apiClient
    }

    override fun sessionManager(): SessionManager<TwitterSession> {
        return TwitterCore.getInstance().sessionManager
    }

    override fun session(): TwitterSession? {
        return TwitterCore.getInstance().sessionManager.activeSession
    }

    override fun logout() {
        TwitterCore.getInstance().logOut()
    }
}
