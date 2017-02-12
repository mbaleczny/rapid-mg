package pl.mbaleczny.rapid_mg.dagger.tweetList

import dagger.Module
import dagger.Provides
import pl.mbaleczny.rapid_mg.dagger.scope.PerActivity
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.FavoritesPresenterType
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.NewsPresenterType
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.UserTimelinePresenterType
import pl.mbaleczny.rapid_mg.data.TwitterDataSource
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract
import pl.mbaleczny.rapid_mg.tweetList.presenter.FavoritesPresenter
import pl.mbaleczny.rapid_mg.tweetList.presenter.HomeTimelinePresenter
import pl.mbaleczny.rapid_mg.tweetList.presenter.UserTimelinePresenter

/**
 * Created by mariusz on 03.02.17.
 */
@Module
class TweetListModule {

    @Provides
    @PerActivity
    @NewsPresenterType
    fun provideNewsPresenter(twitterDataSource: TwitterDataSource): TweetListContract.Presenter {
        return HomeTimelinePresenter(twitterDataSource)
    }

    @Provides
    @PerActivity
    @FavoritesPresenterType
    fun provideFavoritesPresenter(twitterDataSource: TwitterDataSource): TweetListContract.Presenter {
        return FavoritesPresenter(twitterDataSource)
    }

    @Provides
    @PerActivity
    @UserTimelinePresenterType
    fun provideUserTimelinePresenter(twitterDataSource: TwitterDataSource): TweetListContract.Presenter {
        return UserTimelinePresenter(twitterDataSource)
    }

}
