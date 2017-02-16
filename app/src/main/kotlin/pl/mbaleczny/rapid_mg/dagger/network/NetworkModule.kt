package pl.mbaleczny.rapid_mg.dagger.network

import com.twitter.sdk.android.Twitter
import dagger.Module
import dagger.Provides
import pl.mbaleczny.rapid_mg.dagger.scope.PerActivity
import pl.mbaleczny.rapid_mg.network.CustomTwitterApiClient
import pl.mbaleczny.rapid_mg.network.RxTwitterService

/**
 * Created by mariusz on 04.02.17.
 */
@Module
class NetworkModule {

    @Provides
    @PerActivity
    fun twitterApiClient(): CustomTwitterApiClient
            = CustomTwitterApiClient(Twitter.getSessionManager().activeSession)

    @Provides
    @PerActivity
    fun rxTwitterService(twitterApiClient: CustomTwitterApiClient): RxTwitterService
            = twitterApiClient.getCustomService(RxTwitterService::class.java)

}