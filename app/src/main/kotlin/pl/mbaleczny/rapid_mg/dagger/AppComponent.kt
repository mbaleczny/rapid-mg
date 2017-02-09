package pl.mbaleczny.rapid_mg.dagger

import dagger.Component
import pl.mbaleczny.rapid_mg.tweetList.ui.TweetListActivity
import pl.mbaleczny.rapid_mg.util.schedulers.BaseSchedulerProvider
import javax.inject.Singleton

/**
 * Created by mariusz on 03.02.17.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, SchedulersModule::class))
interface AppComponent {

    fun inject(newsActivity: TweetListActivity)

    fun schedulerProvider(): BaseSchedulerProvider

}