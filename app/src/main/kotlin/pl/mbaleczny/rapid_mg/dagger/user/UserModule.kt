package pl.mbaleczny.rapid_mg.dagger.user

import dagger.Module
import dagger.Provides
import pl.mbaleczny.rapid_mg.dagger.scope.UserScope
import pl.mbaleczny.rapid_mg.data.TwitterDataSource
import pl.mbaleczny.rapid_mg.user.UserContract
import pl.mbaleczny.rapid_mg.user.UserPresenter

/**
 * Created by mariusz on 11.02.17.
 */
@Module
class UserModule {

    @Provides
    @UserScope
    fun provideUserPresenter(twitterDataSource: TwitterDataSource): UserContract.Presenter {
        return UserPresenter(twitterDataSource)
    }

}