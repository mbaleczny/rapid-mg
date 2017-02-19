package pl.mbaleczny.rapid_mg.dagger.network

import com.twitter.sdk.android.core.TwitterCore
import dagger.Module
import dagger.Provides
import pl.mbaleczny.rapid_mg.dagger.scope.PerActivity
import pl.mbaleczny.rapid_mg.network.CustomTwitterApiClient
import pl.mbaleczny.rapid_mg.network.RxTwitterService

/**
 * @author Mariusz Baleczny
 * @date 04.02.17
 */
@Module
class NetworkModule {

    @Provides
    @PerActivity
    fun twitterApiClient(twitterCore: TwitterCore): CustomTwitterApiClient
            = CustomTwitterApiClient(twitterCore)

    @Provides
    @PerActivity
    fun rxTwitterService(twitterApiClient: CustomTwitterApiClient): RxTwitterService
            = twitterApiClient.getCustomService(RxTwitterService::class.java)

}