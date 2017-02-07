package pl.mbaleczny.rapid_mg.dagger.login

import dagger.Module
import dagger.Provides
import pl.mbaleczny.rapid_mg.dagger.scope.PerActivity
import pl.mbaleczny.rapid_mg.login.LoginContract
import pl.mbaleczny.rapid_mg.login.LoginPresenter

/**
 * Created by mariusz on 04.02.17.
 */
@Module
class LoginModule {

    @Provides
    @PerActivity
    fun provideLoginPresenter(): LoginContract.Presenter {
        return LoginPresenter()
    }
}