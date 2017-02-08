package pl.mbaleczny.rapid_mg.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import pl.mbaleczny.rapid_mg.R
import javax.inject.Inject

/**
 * Created by mariusz on 03.02.17.
 */
class LoginFragment : Fragment(), LoginContract.View {

    companion object {
        fun newInstance(): LoginFragment = LoginFragment()
    }

    @Inject
    lateinit var presenter: LoginContract.Presenter

    private lateinit var loginButton: TwitterLoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.bindView(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View? = inflater?.inflate(R.layout.fragment_login, container, false)
        loginButton = v?.findViewById(R.id.fragment_login_twitter_login_button) as TwitterLoginButton
        loginButton.callback = presenter.getTwitterCallback()
        return v
    }

    override fun onUserLogged(userName: String?) {
        val msg: String = String.format("Logged in successfully. \nWelcome %s!", userName)
        showToast(msg)
        setResultAndFinish(Activity.RESULT_OK)
    }

    override fun onFailedLogin(exception: TwitterException?) {
        Log.e(javaClass.simpleName, exception?.message as String)
        if (exception?.message != null) showToast(R.string.fail_to_login_message)
    }

    fun showToast(msg: String) {
        Toast.makeText(activity.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun showToast(msgRes: Int) {
        Toast.makeText(activity.applicationContext, msgRes, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginButton.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unBind()
    }

    fun setResultAndFinish(resultCode: Int) {
        activity.setResult(resultCode)
        activity.finish()
    }

}