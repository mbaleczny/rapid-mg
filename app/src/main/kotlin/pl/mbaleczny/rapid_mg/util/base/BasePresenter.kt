package pl.mbaleczny.rapid_mg.util.base

/**
 * Created by mariusz on 03.02.17.
 */
interface BasePresenter<in T : BaseView> {

    fun bindView(view: T)

    fun unBind()

    fun subscribe() {}

    fun unsubscribe() {}

}