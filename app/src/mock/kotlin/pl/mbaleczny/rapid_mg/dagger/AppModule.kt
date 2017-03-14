package pl.mbaleczny.rapid_mg.dagger

import dagger.Module
import dagger.Provides
import pl.mbaleczny.rapid_mg.RapidApp
import pl.mbaleczny.rapid_mg.network.MockTwitterProvider
import pl.mbaleczny.rapid_mg.network.TwitterProvider
import javax.inject.Singleton

/**
 * @author Mariusz Baleczny
 * @date 03.02.17
 */
@Module
class AppModule(private var application: RapidApp) {

    @Provides
    @Singleton
    fun provideRapidApp(): RapidApp = application

    @Provides
    @Singleton
    fun provideTwitterProvider(): TwitterProvider {
        return MockTwitterProvider()
    }

}