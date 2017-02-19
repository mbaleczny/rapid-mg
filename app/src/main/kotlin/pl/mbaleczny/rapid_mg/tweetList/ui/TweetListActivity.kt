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
import pl.mbaleczny.rapid_mg.dagger.tweetList.DaggerTweetListComponent
import pl.mbaleczny.rapid_mg.dagger.tweetList.TweetListComponent
import pl.mbaleczny.rapid_mg.login.LoginActivity
import pl.mbaleczny.rapid_mg.tweetList.adapter.TweetListPagerAdapter
import javax.inject.Inject

/**
 * @author Mariusz Baleczny
 * @date 10.02.17
 */
class TweetListActivity : AppCompatActivity() {

    companion object {
        private const val ON_BACK_PRESS_TIMEOUT: Long = 2000
        var tweetListComponent: TweetListComponent? = null
    }

    @Inject
    lateinit var twitterCore: TwitterCore

    private var pagerAdapter: TweetListPagerAdapter? = null
    private var pressAgainToExit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_list)
        initTweetListComponent()
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

    /**
     * Handles action on back press action with custom behaviour.
     * For the first time displays a Toast with message
     * "Press back again to leaven" and holds that state
     * for time defined in [ON_BACK_PRESS_TIMEOUT]. During that
     * time another [onBackPressed] invocation will cause leaving the app.
     * After that period of time, [onBackPressed] action is reset.
     */
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount != 0 || pressAgainToExit) {
            super.onBackPressed()
            return
        }
        pressAgainToExit = true
        toast(R.string.press_again_to_leave)
        Handler().postDelayed({ pressAgainToExit = false }, ON_BACK_PRESS_TIMEOUT)
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
        twitterCore.logOut()
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

    private fun initTweetListComponent() {
        if (tweetListComponent == null)
            tweetListComponent = DaggerTweetListComponent.builder()
                    .appComponent(RapidApp.appComponent)
                    .build()
        tweetListComponent?.inject(this)
    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    private fun goToUserMainScreen() {
        val userId = twitterCore.sessionManager.activeSession.userId

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

    /**
     * Fragment inside ViewPager have different lifecycle. Methods
     * like [onResume], [onPause] are not invoked, so [pageChangeListener]
     * solves this by calling those methods when detected page change.
     */
    private val pageChangeListener: ViewPager.OnPageChangeListener =
            object : ViewPager.OnPageChangeListener {
                var oldPosition = 0
                override fun onPageScrollStateChanged(state: Int) = Unit

                override fun onPageScrolled(position: Int,
                                            positionOffset: Float,
                                            positionOffsetPixels: Int) = Unit

                override fun onPageSelected(position: Int) {
                    pagerAdapter?.fragments?.get(position)?.onResume()
                    pagerAdapter?.fragments?.get(oldPosition)?.onPause()

                    oldPosition = position
                }
            }
}
