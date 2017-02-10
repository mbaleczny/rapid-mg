package pl.mbaleczny.rapid_mg.tweetList.ui

import android.os.Bundle
import android.support.v4.app.Fragment
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


/**
 * Created by mariusz on 04.02.17.
 */
class TweetListFragment : Fragment(), TweetListContract.View {

    companion object {
        private val USER_ID = "user_id"

        fun newInstance(userId: Long, presenter: TweetListContract.Presenter): TweetListFragment {
            val args: Bundle = Bundle()
            args.putLong(USER_ID, userId)
            val f = TweetListFragment()
            f.presenter = presenter
            f.arguments = args
            return f
        }
    }

    lateinit var presenter: TweetListContract.Presenter

    private var userId: Long? = null

    private var tweetList: RecyclerView? = null
    private var emptyLabel: TextView? = null

    private var adapter: TweetsRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getLong(USER_ID)
        presenter.bindView(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater?.inflate(R.layout.fragment_tweet_list, container, false)
        tweetList = v?.findViewById(R.id.fragment_tweet_list_recycler_view) as RecyclerView
        emptyLabel = v?.findViewById(R.id.empty_list_label) as TextView
        adapter = TweetsRecyclerAdapter(context, presenter)

        tweetList?.layoutManager = LinearLayoutManager(context)
        tweetList?.adapter = adapter
        return v
    }

    override fun setTweets(data: List<Tweet>?) {
        adapter?.setTweets(data!!)
        emptyLabel?.visibility = if (data?.isEmpty() as Boolean) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe(userId)
    }

    override fun showError(e: Throwable?) {
        Log.e(javaClass.simpleName, e?.message)
        Toast.makeText(context, e?.message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unBind()
    }

    override fun hideProgress() {
    }

    override fun showMessage(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}