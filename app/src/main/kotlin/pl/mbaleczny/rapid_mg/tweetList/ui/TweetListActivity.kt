package pl.mbaleczny.rapid_mg.tweetList.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.twitter.sdk.android.core.TwitterCore
import kotlinx.android.synthetic.main.activity_tweet_list.*
import pl.mbaleczny.rapid_mg.R
import pl.mbaleczny.rapid_mg.RapidApp
import pl.mbaleczny.rapid_mg.dagger.network.NetworkModule
import pl.mbaleczny.rapid_mg.dagger.tweetList.DaggerNewsComponent
import pl.mbaleczny.rapid_mg.dagger.tweetList.NewsComponent
import pl.mbaleczny.rapid_mg.dagger.tweetList.NewsModule
import pl.mbaleczny.rapid_mg.login.LoginActivity
import pl.mbaleczny.rapid_mg.tweetList.adapter.TweetListPagerAdapter


class TweetListActivity : AppCompatActivity() {

    companion object {
        var newsComponent: NewsComponent? = null
    }

    private var pagerAdapter: TweetListPagerAdapter? = null

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
            TwitterCore.getInstance().logOut()
            val i = Intent(this, LoginActivity::class.java)
            Intent.makeRestartActivityTask(i.component)
            finish()
            startActivity(i)
            true
        }
    }

    private fun initTabLayout() {
        val viewPager = findViewById(R.id.viewPager) as ViewPager
        pagerAdapter = TweetListPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun initNewsComponent() {
        if (newsComponent == null)
            newsComponent = DaggerNewsComponent.builder()
                    .appComponent(RapidApp.appComponent)
                    .newsModule(NewsModule())
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

        val tweetListFragment = TweetListFragment.newInstance(userId, newsComponent?.newsPresenter()!!)
        val favoritesFragment = TweetListFragment.newInstance(userId, newsComponent?.favoritesPresenter()!!)

        addScreen(tweetListFragment)
        addScreen(favoritesFragment)

        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_home)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_favorite)
    }

    private fun addScreen(fragment: TweetListFragment) {
        val pos = pagerAdapter?.count ?: 0
        pagerAdapter?.addFragment(pos, fragment)
    }
}
