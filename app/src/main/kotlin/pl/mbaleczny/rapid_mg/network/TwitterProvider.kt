package pl.mbaleczny.rapid_mg.network

import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterSession
import javax.net.ssl.SSLSocketFactory

/**
 * @author Mariusz Baleczny
 * *
 * @date 11.03.17
 */

interface TwitterProvider {

    fun apiClient(): TwitterApiClient

    fun addApiClient(session: TwitterSession, apiClient: TwitterApiClient)

    fun sessionManager(): SessionManager<TwitterSession>

    fun session(): TwitterSession?

    fun logout()

    fun authConfig(): TwitterAuthConfig?

    fun sslSocketFactory(): SSLSocketFactory?

}
