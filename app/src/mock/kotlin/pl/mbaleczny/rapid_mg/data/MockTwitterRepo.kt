package pl.mbaleczny.rapid_mg.data

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.TweetBuilder
import com.twitter.sdk.android.core.models.User
import com.twitter.sdk.android.core.models.UserBuilder
import io.reactivex.Observable
import org.joda.time.DateTime

/**
 * @author Mariusz Baleczny
 * *
 * @date 02.03.17
 */

class MockTwitterRepo : TwitterDataSource {

    private val userTimeline: MutableList<Tweet> = mutableListOf()
    private val homeTimeline: MutableList<Tweet> = mutableListOf()
    private val favorites: MutableList<Tweet> = mutableListOf()
    private val users: MutableList<User> = mutableListOf()

    private val kermit: User
            = UserBuilder().setId(0)
            .setScreenName("kermitthefrog")
            .setName("Just Kermit")
            .build()

    private val tweet1: Tweet
            = TweetBuilder()
            .setId(0)
            .setFavorited(false)
            .setFavoriteCount(0)
            .setText("hey all!")
            .setCreatedAt(DateTime.now().toString())
            .setUser(kermit)
            .build()

    private val tweet2: Tweet
            = TweetBuilder()
            .setId(1)
            .setText("I'm just mocking with ya")
            .setCreatedAt(DateTime.now().toString())
            .setFavorited(false)
            .setFavoriteCount(0)
            .setUser(kermit)
            .build()

    private val piggy: User = UserBuilder()
            .setId(1)
            .setScreenName("piggy")
            .setName("Piggy Piggy").build()

    private val tweet3: Tweet
            = TweetBuilder()
            .setFavorited(true)
            .setFavoriteCount(1)
            .setText("Just hanging out with Kermit yall")
            .setId(2)
            .setUser(piggy)
            .setCreatedAt(DateTime.now().toString())
            .build()

    private val tweets: List<Tweet> = mutableListOf(tweet1, tweet2)

    private val favored: MutableList<Tweet> = mutableListOf()

    init {
        homeTimeline.addAll(tweets)

        favorites.add(tweet3)

        users.add(kermit)
        users.add(piggy)
    }

    override fun getUserTimeline(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>>
            = convert(userTimeline, userId, count, sinceId, maxId)

    override fun getHomeTimeline(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>>
            = convert(homeTimeline, userId, count, sinceId, maxId)

    override fun favorites(userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>>{
        val obs = Observable.fromArray(favorites)
                .flatMapIterable { it -> it }
                .filter { if (sinceId != null) it.id > sinceId else true }
                .filter { if (maxId != null) it.id < maxId else true }
        return if (count != null)
            obs.takeLast(count).toList().toObservable()
        else
            obs.toList().toObservable()
    }

    override fun favorite(userId: Long?, tweet: Tweet?)
            : Observable<Tweet> {
        return Observable.fromCallable { favorites.contains(tweet) }
                .switchMap { it ->
                    if (!it) {
                        val tw = changeFavCount(tweet!!, 1)
                        favorites.add(tw)
                        Observable.just(tw)
                    } else {
                        Observable.empty()
                    }
                }
    }

    private fun changeFavCount(tw: Tweet, value: Int): Tweet
            = TweetBuilder()
            .copy(tw)
            .setFavoriteCount(tw.favoriteCount + value)
            .build()

    override fun unFavorite(userId: Long?, tweet: Tweet?)
            : Observable<Tweet> {
        return Observable.empty()
    }

    override fun getUser(userId: Long?, includeEntities: Boolean)
            : Observable<User>
            = Observable.fromArray(users)
            .flatMapIterable { it -> it }
            .filter { it.id == userId }
            .firstOrError().toObservable()

    private fun convert(list: List<Tweet>, userId: Long?, count: Int?, sinceId: Long?, maxId: Long?)
            : Observable<List<Tweet>> {
        val obs = Observable.fromArray(list)
                .flatMapIterable { it -> it }
                .filter { it.user.id == userId }
                .filter { if (sinceId != null) it.id > sinceId else true }
                .filter { if (maxId != null) it.id < maxId else true }
        return if (count != null)
            obs.takeLast(count).toList().toObservable()
        else
            obs.toList().toObservable()
    }
}
