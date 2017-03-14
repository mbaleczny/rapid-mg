package pl.mbaleczny.rapid_mg.dagger.network

import dagger.Module
import dagger.Provides
import pl.mbaleczny.rapid_mg.dagger.scope.PerActivity
import pl.mbaleczny.rapid_mg.network.CustomTwitterApiClient
import pl.mbaleczny.rapid_mg.network.RxTwitterService
import pl.mbaleczny.rapid_mg.network.TwitterProvider

/**
 * @author Mariusz Baleczny
 * @date 04.02.17
 */
@Module
class NetworkModule {

    @Provides
    @PerActivity
    fun twitterApiClient(twitterProvider: TwitterProvider): CustomTwitterApiClient
            = CustomTwitterApiClient(twitterProvider)

    @Provides
    @PerActivity
    fun rxTwitterService(twitterApiClient: CustomTwitterApiClient): RxTwitterService
            = twitterApiClient.getCustomService(RxTwitterService::class.java) as RxTwitterService

}