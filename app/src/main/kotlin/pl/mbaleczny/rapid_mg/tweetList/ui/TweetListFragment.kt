package pl.mbaleczny.rapid_mg.tweetList.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.twitter.sdk.android.core.models.Tweet
import pl.mbaleczny.rapid_mg.R
import pl.mbaleczny.rapid_mg.tweetList.TweetListContract
import pl.mbaleczny.rapid_mg.tweetList.adapter.TweetsRecyclerAdapter
import pl.mbaleczny.rapid_mg.user.UserActivity
import pl.mbaleczny.rapid_mg.util.USER_ID_ARG


/**
 * Created by mariusz on 04.02.17.
 */
class TweetListFragment : Fragment(), TweetListContract.View {

    companion object {
        fun newInstance(userId: Long, presenter: TweetListContract.Presenter): TweetListFragment {
            val args: Bundle = Bundle()
            args.putLong(USER_ID_ARG, userId)
            val f = TweetListFragment()
            f.presenter = presenter
            f.arguments = args
            return f
        }
    }

    lateinit var presenter: TweetListContract.Presenter

    private var tweetList: RecyclerView? = null
    private var emptyLabel: TextView? = null
    private var swipeRefresh: SwipeRefreshLayout? = null

    private var adapter: TweetsRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.setUserId(arguments?.getLong(USER_ID_ARG))
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater?.inflate(R.layout.fragment_tweet_list, container, false)
        tweetList = v?.findViewById(R.id.fragment_tweet_list_recycler_view) as RecyclerView
        emptyLabel = v?.findViewById(R.id.empty_list_label) as TextView
        adapter = TweetsRecyclerAdapter(context, presenter)

        initTweetList()

        swipeRefresh = v?.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        swipeRefresh?.setOnRefreshListener { presenter.loadFreshList() }

        presenter.bindView(this)
        presenter.loadFreshList()

        return v
    }

    override fun onResume() {
        super.onResume()
        presenter.bindView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unBind()
        hideProgress()
    }

    override fun setTweets(data: List<Tweet>?) {
        adapter?.setTweets(data!!)
    }

    override fun showError(e: Throwable?) {
        Log.e(javaClass.simpleName, e?.message ?: "Error occurred")
        showMessage(e?.message ?: "Error occurred")
    }

    override fun hideProgress() {
        swipeRefresh?.isRefreshing = false
        emptyLabel?.visibility = if (adapter?.isEmpty() as Boolean) View.VISIBLE else View.GONE
    }

    override fun showMessage(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    override fun openUserActivity(id: Long) {
        val i = Intent(activity, UserActivity::class.java)
        i.putExtra(USER_ID_ARG, id)
        activity.startActivity(i)
    }

    private fun initTweetList() {
        val layoutManager: LinearLayoutManager = LinearLayoutManager(context)
        tweetList?.layoutManager = layoutManager
        tweetList?.adapter = adapter

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tweetList?.setOnScrollChangeListener(object
                : RecyclerView.OnScrollListener(), View.OnScrollChangeListener {
                override fun onScrollChange(v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int,
                                            oldScrollY: Int) {
                    onScrollChange(layoutManager)
                }
            })
        } else {
            tweetList?.setOnScrollListener(object :
                    RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    onScrollChange(layoutManager)
                }
            })
        }
    }

    private fun onScrollChange(layoutManager: LinearLayoutManager) {
        val firstVisibleItemPos = layoutManager.findFirstCompletelyVisibleItemPosition()
        val lastVisibleItemPos = layoutManager.findLastCompletelyVisibleItemPosition()

        if (firstVisibleItemPos != 0 &&
                lastVisibleItemPos == adapter?.itemCount?.minus(1)) {
            presenter.loadOlderTweets()
        }
    }
}
