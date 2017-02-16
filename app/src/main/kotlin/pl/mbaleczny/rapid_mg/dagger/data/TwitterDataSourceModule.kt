package pl.mbaleczny.rapid_mg.dagger.data

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import pl.mbaleczny.rapid_mg.RapidApp
import pl.mbaleczny.rapid_mg.dagger.scope.PerActivity
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.LocalTwitterDataSource
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.RemoteTwitterDataSource
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.TwitterRepoDataSource
import pl.mbaleczny.rapid_mg.data.TwitterDataSource
import pl.mbaleczny.rapid_mg.data.TwitterRepo
import pl.mbaleczny.rapid_mg.data.source.local.TwitterLocalDataSource
import pl.mbaleczny.rapid_mg.data.source.remote.TwitterRemoteDataSource
import pl.mbaleczny.rapid_mg.network.RxTwitterService
import pl.mbaleczny.rapid_mg.util.schedulers.BaseSchedulerProvider

/**
 * Created by mariusz on 09.02.17.
 */
@Module
class TwitterDataSourceModule {

    @Provides
    @PerActivity
    @LocalTwitterDataSource
    fun provideLocalDataSource(): TwitterDataSource
            = TwitterLocalDataSource.getInstance(RapidApp.instance, Gson())

    @Provides
    @PerActivity
    @RemoteTwitterDataSource
    fun provideRemoteDataSource(rxTwitterService: RxTwitterService): TwitterDataSource
            = TwitterRemoteDataSource(rxTwitterService)

    @Provides
    @PerActivity
    @TwitterRepoDataSource
    fun provideTwitterRepo(@RemoteTwitterDataSource remoteDataSource: TwitterDataSource,
                           @LocalTwitterDataSource localDataSource: TwitterDataSource,
                           schedulerProvider: BaseSchedulerProvider): TwitterDataSource
            = TwitterRepo(remoteDataSource, localDataSource, schedulerProvider)

}