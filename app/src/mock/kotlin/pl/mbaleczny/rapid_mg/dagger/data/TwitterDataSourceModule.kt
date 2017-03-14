package pl.mbaleczny.rapid_mg.dagger.data

import dagger.Module
import dagger.Provides
import pl.mbaleczny.rapid_mg.dagger.data.qualifier.TwitterRepoDataSource
import pl.mbaleczny.rapid_mg.dagger.scope.PerActivity
import pl.mbaleczny.rapid_mg.data.MockTwitterRepo
import pl.mbaleczny.rapid_mg.data.TwitterDataSource

/**
 * @author Mariusz Baleczny
 * @date 09.02.17
 */
@Module
class TwitterDataSourceModule {

    @Provides
    @PerActivity
    @TwitterRepoDataSource
    fun provideTwitterRepo(): TwitterDataSource = MockTwitterRepo()

}