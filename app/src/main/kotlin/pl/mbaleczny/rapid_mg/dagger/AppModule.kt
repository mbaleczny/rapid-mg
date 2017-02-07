package pl.mbaleczny.rapid_mg.dagger

import dagger.Module
import dagger.Provides
import pl.mbaleczny.rapid_mg.RapidApp
import javax.inject.Singleton

/**
 * Created by mariusz on 03.02.17.
 */
@Module
class AppModule(private var application: RapidApp) {

    @Provides
    @Singleton
    fun provideRapidApp(): RapidApp = application

}