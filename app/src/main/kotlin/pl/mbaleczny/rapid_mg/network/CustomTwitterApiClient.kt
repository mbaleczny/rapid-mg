package pl.mbaleczny.rapid_mg.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.internal.TwitterApi
import com.twitter.sdk.android.core.internal.network.OkHttpClientHelper.getOkHttpClientBuilder
import com.twitter.sdk.android.core.models.BindingValues
import com.twitter.sdk.android.core.models.BindingValuesAdapter
import com.twitter.sdk.android.core.models.SafeListAdapter
import com.twitter.sdk.android.core.models.SafeMapAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ConcurrentHashMap

/**
 * TwitterApiClient with added [HttpLoggingInterceptor][okhttp3.logging.HttpLoggingInterceptor]
 * and [RxJava2CallAdapterFactory][com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory].
 *
 * @author Mariusz Baleczny
 * @date 08.02.17
 */
class CustomTwitterApiClient(session: TwitterSession) : TwitterApiClient(session) {

    internal val services: ConcurrentHashMap<Class<*>, Any>
    internal val retrofit: Retrofit

    init {
        this.services = ConcurrentHashMap()
        val client = buildHttpClient(session)
        this.retrofit = buildRetrofit(client, TwitterApi())
        TwitterCore.getInstance().addApiClient(session, this)
    }

    private fun buildHttpClient(session: TwitterSession): OkHttpClient {
        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        return getOkHttpClientBuilder(session,
                TwitterCore.getInstance().authConfig,
                TwitterCore.getInstance().sslSocketFactory)
                .addInterceptor(loggingInterceptor)
                .build()
    }

    private fun buildRetrofit(client: OkHttpClient, twitterApi: TwitterApi): Retrofit {
        return Retrofit.Builder()
                .client(client)
                .baseUrl(twitterApi.baseHostUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .build()
    }

    private fun buildGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapterFactory(SafeListAdapter())
                .registerTypeAdapterFactory(SafeMapAdapter())
                .registerTypeAdapter(BindingValues::class.java, BindingValuesAdapter())
                .create()
    }

    fun <T> getCustomService(cls: Class<T>): T {
        if (!services.contains(cls)) {
            (services).putIfAbsent(cls, retrofit.create(cls))
        }
        return services[cls] as T
    }

}