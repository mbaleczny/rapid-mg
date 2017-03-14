package pl.mbaleczny.rapid_mg.dagger

import com.twitter.sdk.android.core.TwitterCore
import dagger.Module
import dagger.Provides
import pl.mbaleczny.rapid_mg.RapidApp
import pl.mbaleczny.rapid_mg.dagger.network.FakeTwitterCore
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
    fun provideTwitterCore(): TwitterCore {
        return FakeTwitterCore.getInstance()
    }

}