package pl.mbaleczny.rapid_mg.dagger.tweetList

import dagger.Component
import pl.mbaleczny.rapid_mg.dagger.AppComponent
import pl.mbaleczny.rapid_mg.dagger.data.TwitterDataSourceModule
import pl.mbaleczny.rapid_mg.dagger.network.NetworkModule
import pl.mbaleczny.rapid_mg.dagger.scope.PerActivity
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.FavoritesPresenterType
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.NewsPresenterType
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract
import pl.mbaleczny.rapid_mg.tweetList.ui.TweetListFragment

/**
 * Created by mariusz on 04.02.17.
 */
@PerActivity
@Component(modules = arrayOf(NewsModule::class, NetworkModule::class, TwitterDataSourceModule::class),
        dependencies = arrayOf(AppComponent::class))
interface NewsComponent {

    fun inject(fragment: TweetListFragment)

    @FavoritesPresenterType
    fun favoritesPresenter(): TweetListContract.Presenter

    @NewsPresenterType
    fun newsPresenter(): TweetListContract.Presenter

}