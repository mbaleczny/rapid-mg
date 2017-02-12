package pl.mbaleczny.rapid_mg.dagger.user

import dagger.Component
import pl.mbaleczny.rapid_mg.dagger.scope.UserScope
import pl.mbaleczny.rapid_mg.dagger.tweetList.TweetListComponent
import pl.mbaleczny.rapid_mg.user.UserActivity

/**
 * Created by mariusz on 11.02.17.
 */
@UserScope
@Component(
        dependencies = arrayOf(TweetListComponent::class),
        modules = arrayOf(UserModule::class))
interface UserComponent {

    fun inject(activity: UserActivity)

}