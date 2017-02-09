package pl.mbaleczny.rapid_mg.dagger.data

import dagger.Module
import dagger.Provides
import pl.mbaleczny.rapid_mg.dagger.scope.PerActivity
import pl.mbaleczny.rapid_mg.data.TwitterDataSource
import pl.mbaleczny.rapid_mg.data.TwitterRepo
import pl.mbaleczny.rapid_mg.network.RxTwitterService
import pl.mbaleczny.rapid_mg.util.schedulers.BaseSchedulerProvider

/**
 * Created by mariusz on 09.02.17.
 */
@Module
class TwitterDataSourceModule {

    @Provides
    @PerActivity
    fun provideTwitterDataSource(rxTwitterService: RxTwitterService,
                                 schedulerProvider: BaseSchedulerProvider): TwitterDataSource {
        return TwitterRepo(rxTwitterService, schedulerProvider)
    }

}