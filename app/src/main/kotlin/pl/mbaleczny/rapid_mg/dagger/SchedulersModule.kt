package pl.mbaleczny.rapid_mg.dagger

import dagger.Module
import dagger.Provides
import pl.mbaleczny.rapid_mg.util.schedulers.BaseSchedulerProvider
import pl.mbaleczny.rapid_mg.util.schedulers.SchedulerProvider
import javax.inject.Singleton

/**
 * Created by mariusz on 08.02.17.
 */
@Module
class SchedulersModule {

    @Provides
    @Singleton
    fun provideSchedulerProvider(): BaseSchedulerProvider = SchedulerProvider()

}