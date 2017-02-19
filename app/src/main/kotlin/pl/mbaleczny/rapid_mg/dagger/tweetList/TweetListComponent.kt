package pl.mbaleczny.rapid_mg.dagger.tweetList

import dagger.Component
import pl.mbaleczny.rapid_mg.dagger.AppComponent
import pl.mbaleczny.rapid_mg.dagger.data.TwitterDataSourceModule
import pl.mbaleczny.rapid_mg.dagger.data.qualifier.TwitterRepoDataSource
import pl.mbaleczny.rapid_mg.dagger.network.NetworkModule
import pl.mbaleczny.rapid_mg.dagger.scope.PerActivity
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.FavoritesPresenterType
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.NewsPresenterType
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.UserTimelinePresenterType
import pl.mbaleczny.rapid_mg.data.TwitterDataSource
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract
import pl.mbaleczny.rapid_mg.tweetList.ui.TweetListFragment

/**
 * @author Mariusz Baleczny
 * @date 04.02.17
 */
@PerActivity
@Component(
        modules = arrayOf(TweetListModule::class,
                NetworkModule::class,
                TwitterDataSourceModule::class),
        dependencies = arrayOf(AppComponent::class))
interface TweetListComponent {

    fun inject(fragment: TweetListFragment)

    @FavoritesPresenterType fun favoritesPresenter(): TweetListContract.Presenter

    @NewsPresenterType fun tweetTimelinePresenter(): TweetListContract.Presenter

    @UserTimelinePresenterType fun userTimelinePresenter(): TweetListContract.Presenter

    @TwitterRepoDataSource fun twitterDataSource(): TwitterDataSource

}