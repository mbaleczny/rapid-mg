package pl.mbaleczny.rapid_mg.dagger

import dagger.Component
import pl.mbaleczny.rapid_mg.MainActivity
import pl.mbaleczny.rapid_mg.network.TwitterProvider
import pl.mbaleczny.rapid_mg.util.schedulers.BaseSchedulerProvider
import javax.inject.Singleton

/**
 * @author Mariusz Baleczny
 * @date 03.02.17
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, SchedulersModule::class))
interface AppComponent {

    fun schedulerProvider(): BaseSchedulerProvider

    fun inject(mainActivity: MainActivity)

    fun twitterProvider(): TwitterProvider

}