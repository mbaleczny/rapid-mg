package pl.mbaleczny.rapid_mg.dagger.tweetList

import dagger.Module
import dagger.Provides
import pl.mbaleczny.rapid_mg.dagger.scope.PerActivity
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.FavoritesPresenterType
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.NewsPresenterType
import pl.mbaleczny.rapid_mg.data.TwitterDataSource
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract
import pl.mbaleczny.rapid_mg.tweetList.presenter.FavoritesPresenter
import pl.mbaleczny.rapid_mg.tweetList.presenter.TweetTimelinePresenter

/**
 * Created by mariusz on 03.02.17.
 */
@Module
class NewsModule {

    @Provides
    @PerActivity
    @NewsPresenterType
    fun provideNewsPresenter(twitterDataSource: TwitterDataSource): TweetListContract.Presenter {
        return TweetTimelinePresenter(twitterDataSource)
    }

    @Provides
    @PerActivity
    @FavoritesPresenterType
    fun provideFavoritesPresenter(twitterDataSource: TwitterDataSource): TweetListContract.Presenter {
        return FavoritesPresenter(twitterDataSource)
    }

}
