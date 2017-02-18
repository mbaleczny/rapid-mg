package pl.mbaleczny.rapid_mg.user

import android.view.View
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.core.models.User
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.user_panel.*
import pl.mbaleczny.rapid_mg.R
import pl.mbaleczny.rapid_mg.RapidApp

/**
 * Created by mariusz on 11.02.17.
 */
class UserPanelViewHolder(val activity: UserActivity) {

    private var title: String? = null
    private var subtitle: String? = null

    fun bind(user: User?) {
        setUserAttributes(user)
        loadImages(user)
        title = user?.name
        subtitle = String.format(RapidApp.instance.getString(R.string.tweets_count_template),
                user?.statusesCount)
        setToolbarTitleAndSubtitle()
    }

    private fun setUserAttributes(user: User?) {
        if (user?.description.isNullOrEmpty()) {
            activity.user_panel_summary.visibility = View.GONE
        } else {
            activity.user_panel_summary.text = user?.description
        }
        activity.user_panel_screen_name.text = String.format(
                RapidApp.instance.getString(R.string.screen_name_template),
                user?.screenName)
        activity.user_panel_full_name.text = user?.name
        activity.user_panel_followers_counter.text = user?.followersCount.toString()
        activity.user_panel_following_counter.text = user?.friendsCount.toString()
    }

    private fun loadImages(user: User?) {
        Picasso.with(activity)
                .load(user?.profileImageUrl)
                .error(R.drawable.tw__ic_tweet_photo_error_light)
                .into(activity.user_panel_avatar)

        Picasso.with(activity)
                .load(user?.profileBannerUrl)
                .error(R.drawable.tw__ic_tweet_photo_error_light)
                .into(activity.user_panel_banner)
    }

    private fun setToolbarTitleAndSubtitle() {
        activity.app_bar_layout.addOnOffsetChangedListener { appBarLayout, offset ->
            val toolbarCollapsed = Math.abs(offset) >= appBarLayout.totalScrollRange
            activity.toolbar?.title = if (toolbarCollapsed) title else ""
            activity.toolbar?.subtitle = if (toolbarCollapsed) subtitle else ""
        }
    }
}