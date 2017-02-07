package pl.mbaleczny.rapid_mg.dagger.login

import dagger.Component
import pl.mbaleczny.rapid_mg.dagger.scope.PerActivity
import pl.mbaleczny.rapid_mg.login.LoginFragment

/**
 * Created by mariusz on 04.02.17.
 */
@PerActivity
@Component(modules = arrayOf(LoginModule::class))
interface LoginComponent {

    fun inject(fragment: LoginFragment)

}