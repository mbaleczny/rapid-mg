package pl.mbaleczny.rapid_mg

import android.app.Application
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import io.fabric.sdk.android.Fabric
import pl.mbaleczny.rapid_mg.dagger.AppComponent
import pl.mbaleczny.rapid_mg.dagger.AppModule
import pl.mbaleczny.rapid_mg.dagger.DaggerAppComponent

/**
 * Created by mariusz on 03.02.17.
 */
class RapidApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initFabric()
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    private fun initFabric() {
        val authConfig = TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET)
        Fabric.with(this, Twitter(authConfig))
    }
}