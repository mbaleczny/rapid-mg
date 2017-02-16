package pl.mbaleczny.rapid_mg.tweetList.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.twitter.sdk.android.core.TwitterCore
import kotlinx.android.synthetic.main.activity_tweet_list.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import pl.mbaleczny.rapid_mg.R
import pl.mbaleczny.rapid_mg.RapidApp
import pl.mbaleczny.rapid_mg.dagger.network.NetworkModule
import pl.mbaleczny.rapid_mg.dagger.tweetList.DaggerTweetListComponent
import pl.mbaleczny.rapid_mg.dagger.tweetList.TweetListComponent
import pl.mbaleczny.rapid_mg.dagger.tweetList.TweetListModule
import pl.mbaleczny.rapid_mg.login.LoginActivity
import pl.mbaleczny.rapid_mg.tweetList.adapter.TweetListPagerAdapter


class TweetListActivity : AppCompatActivity() {

    companion object {
        var tweetListComponent: TweetListComponent? = null
    }

    private var pagerAdapter: TweetListPagerAdapter? = null
    private var pressAgainToExit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_list)
        initToolbar()
        initTabLayout()

        goToUserMainScreen()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        setupActionBar()
        setupMenu(menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.clearOnPageChangeListeners()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount != 0 || pressAgainToExit) {
            super.onBackPressed()
            return
        }
        pressAgainToExit = true
        toast(R.string.press_again_to_leave)
        Handler().postDelayed({ pressAgainToExit = false }, 2000)
    }

    private fun setupActionBar() {
        val ab: ActionBar? = supportActionBar
        ab?.setHomeButtonEnabled(false)
        ab?.setDisplayHomeAsUpEnabled(false)
        ab?.setDisplayShowHomeEnabled(false)
    }

    private fun setupMenu(menu: Menu?) {
        menuInflater.inflate(R.menu.tweet_list_activity_menu, menu)
        val logoutItem: MenuItem? = menu?.findItem(R.id.action_logout)
        logoutItem?.setOnMenuItemClickListener { item ->
            alert(R.string.logout_alert_message) {
                positiveButton(R.string.yes) { logout() }
                negativeButton(R.string.no) { }
            }.show()
            true
        }
    }

    private fun logout() {
        TwitterCore.getInstance().logOut()
        val i = Intent(this, LoginActivity::class.java)
        Intent.makeRestartActivityTask(i.component)
        finish()
        startActivity(i)
    }

    private fun initTabLayout() {
        val viewPager = findViewById(R.id.viewPager) as ViewPager
        pagerAdapter = TweetListPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(pageChangeListener)
    }

    private fun initNewsComponent() {
        if (tweetListComponent == null)
            tweetListComponent = DaggerTweetListComponent.builder()
                    .appComponent(RapidApp.appComponent)
                    .tweetListModule(TweetListModule())
                    .networkModule(NetworkModule())
                    .build()
    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    private fun goToUserMainScreen() {
        initNewsComponent()

        val userId = TwitterCore.getInstance().sessionManager.activeSession.userId

        val tweetListFragment = TweetListFragment.newInstance(userId,
                tweetListComponent?.tweetTimelinePresenter()!!)
        val favoritesFragment = TweetListFragment.newInstance(userId,
                tweetListComponent?.favoritesPresenter()!!)

        addScreen(tweetListFragment)
        addScreen(favoritesFragment)

        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_home)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_favorite)
    }

    private fun addScreen(fragment: TweetListFragment) {
        val pos = pagerAdapter?.count ?: 0
        pagerAdapter?.addFragment(pos, fragment)
    }

    private val pageChangeListener: ViewPager.OnPageChangeListener =
            object : ViewPager.OnPageChangeListener {
                var oldPosition = 0
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    pagerAdapter?.fragments?.get(position)?.onResume()
                    pagerAdapter?.fragments?.get(oldPosition)?.onPause()

                    oldPosition = position
                }
            }
}
