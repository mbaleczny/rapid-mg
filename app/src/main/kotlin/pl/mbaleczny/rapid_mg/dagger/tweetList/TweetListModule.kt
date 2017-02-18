package pl.mbaleczny.rapid_mg.dagger.tweetList

import com.twitter.sdk.android.core.TwitterCore
import dagger.Module
import dagger.Provides
import pl.mbaleczny.rapid_mg.dagger.scope.PerActivity
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.FavoritesPresenterType
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.NewsPresenterType
import pl.mbaleczny.rapid_mg.dagger.tweetList.qualifier.TwitterRepoDataSource
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
    fun provideNewsPresenter(@TwitterRepoDataSource twitterDataSource: TwitterDataSource)
            : TweetListContract.Presenter
            = HomeTimelinePresenter(twitterDataSource)

    @Provides
    @FavoritesPresenterType
    fun provideFavoritesPresenter(@TwitterRepoDataSource twitterDataSource: TwitterDataSource)
            : TweetListContract.Presenter =
            FavoritesPresenter(
                    twitterDataSource,
                    TwitterCore.getInstance().sessionManager.activeSession.userId)

    @Provides
    @UserTimelinePresenterType
    fun provideUserTimelinePresenter(@TwitterRepoDataSource twitterDataSource: TwitterDataSource)
            : TweetListContract.Presenter
            = UserTimelinePresenter(twitterDataSource)

}
