package pl.mbaleczny.rapid_mg

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
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
        lateinit var instance: RapidApp
        lateinit var appComponent: AppComponent

        fun isNetworkAvailable(): Boolean {
            val cm: ConnectivityManager? = instance.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager

            val info = cm?.activeNetworkInfo
            return info != null && info.isConnectedOrConnecting
        }
    }

    override fun onCreate() {
        super.onCreate()
        initFabric()
        instance = this
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    private fun initFabric() {
        val authConfig = TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET)
        Fabric.with(this, Twitter(authConfig))
    }
}