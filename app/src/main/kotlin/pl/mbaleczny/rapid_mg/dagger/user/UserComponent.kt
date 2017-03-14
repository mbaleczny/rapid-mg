package pl.mbaleczny.rapid_mg.dagger.user

import dagger.Component
import pl.mbaleczny.rapid_mg.dagger.scope.UserScope
import pl.mbaleczny.rapid_mg.dagger.tweetList.TweetListComponent
import pl.mbaleczny.rapid_mg.network.TwitterProvider
import pl.mbaleczny.rapid_mg.user.UserActivity

/**
 * @author Mariusz Baleczny
 * @date 11.02.17
 */
@UserScope
@Component(
        dependencies = arrayOf(TweetListComponent::class),
        modules = arrayOf(UserModule::class))
interface UserComponent {

    fun inject(activity: UserActivity)

    fun twitterProvider(): TwitterProvider

}