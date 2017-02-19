package pl.mbaleczny.rapid_mg.login

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import pl.mbaleczny.rapid_mg.R
import pl.mbaleczny.rapid_mg.dagger.login.DaggerLoginComponent
import pl.mbaleczny.rapid_mg.dagger.login.LoginComponent
import pl.mbaleczny.rapid_mg.dagger.login.LoginModule
import pl.mbaleczny.rapid_mg.util.showFragment

/**
 * @author Mariusz Baleczny
 * @date 04.02.17
 */
class LoginActivity : AppCompatActivity() {

    companion object {
        lateinit var loginComponent: LoginComponent
    }

    init {
        loginComponent = DaggerLoginComponent.builder()
                .loginModule(LoginModule())
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (savedInstanceState == null) {
            val loginFragment = LoginFragment.newInstance()

            loginComponent.inject(loginFragment)

            showFragment(loginFragment, false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val loginFragment: Fragment? = supportFragmentManager
                .findFragmentById(R.id.activity_container)
        loginFragment?.onActivityResult(requestCode, resultCode, data)
    }
}