package pl.mbaleczny.rapid_mg.dagger

import dagger.Component
import pl.mbaleczny.rapid_mg.news.NewsActivity
import javax.inject.Singleton

/**
 * Created by mariusz on 03.02.17.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun inject(newsActivity: NewsActivity)

}