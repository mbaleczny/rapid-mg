package pl.mbaleczny.rapid_mg.user

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import com.twitter.sdk.android.core.models.User
import kotlinx.android.synthetic.main.activity_user.*
import pl.mbaleczny.rapid_mg.R
import pl.mbaleczny.rapid_mg.dagger.user.DaggerUserComponent
import pl.mbaleczny.rapid_mg.dagger.user.UserModule
import pl.mbaleczny.rapid_mg.tweetList.adapter.TweetListPagerAdapter
import pl.mbaleczny.rapid_mg.tweetList.ui.TweetListActivity
import pl.mbaleczny.rapid_mg.tweetList.ui.TweetListFragment
import pl.mbaleczny.rapid_mg.util.USER_ID_ARG
import javax.inject.Inject

/**
 * @author Mariusz Baleczny
 * @date 11.02.17
 */
class UserActivity : AppCompatActivity(), UserContract.View {

    @Inject
    lateinit var presenter: UserContract.Presenter

    private var pagerAdapter: TweetListPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        if (!intent.hasExtra(USER_ID_ARG)) {
            Toast.makeText(this, R.string.internal_error_message, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        injectDependencies()
        presenter.bindView(this)

        val userId = intent.extras.getLong(USER_ID_ARG)

        presenter.setUserId(userId)
        initToolbar()
        initTabLayout()
        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar)
        addTweetListTabs(userId)
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unBind()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setUser(user: User?) {
        val userPanelViewHolder = UserPanelViewHolder(this)
        userPanelViewHolder.bind(user)
    }

    override fun showError(it: Throwable?) {
        Toast.makeText(this, it?.message, Toast.LENGTH_SHORT).show()
    }

    private fun addScreen(fragment: TweetListFragment) {
        val pos = pagerAdapter?.count ?: 0
        pagerAdapter?.addFragment(pos, fragment)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        setupActionBar()
        collapsing_toolbar?.setExpandedTitleColor(
                ContextCompat.getColor(this, android.R.color.transparent))
        collapsing_toolbar.isTitleEnabled = false
    }

    private fun setupActionBar() {
        val ab: ActionBar? = supportActionBar
        ab?.setHomeButtonEnabled(true)
        ab?.setDisplayHomeAsUpEnabled(true)
        ab?.setDisplayShowHomeEnabled(true)
    }

    private fun initTabLayout() {
        pagerAdapter = TweetListPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter
        tabLayout?.setupWithViewPager(viewPager)
    }

    private fun injectDependencies() {
        DaggerUserComponent.builder()
                .tweetListComponent(TweetListActivity.tweetListComponent)
                .userModule(UserModule())
                .build().inject(this)
    }

    private fun addTweetListTabs(userId: Long) {
        val tweetListFragment = TweetListFragment.newInstance(userId,
                TweetListActivity.tweetListComponent?.userTimelinePresenter()!!)
        val favoritesFragment = TweetListFragment.newInstance(userId,
                TweetListActivity.tweetListComponent?.favoritesPresenter()!!)

        addScreen(tweetListFragment)
        addScreen(favoritesFragment)

        tabLayout?.getTabAt(0)?.setIcon(R.drawable.ic_home)
        tabLayout?.getTabAt(1)?.setIcon(R.drawable.ic_favorite)
    }
}
