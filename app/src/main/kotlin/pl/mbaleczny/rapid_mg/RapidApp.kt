package pl.mbaleczny.rapid_mg

import android.app.Application
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
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}