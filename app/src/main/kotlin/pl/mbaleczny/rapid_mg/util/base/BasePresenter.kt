package pl.mbaleczny.rapid_mg.util.base

/**
 * Common interface for all Presenters.
 *
 * @author Mariusz Baleczny
 * @date 03.02.17
 */
interface BasePresenter<in T : BaseView> {

    fun bindView(view: T)

    fun unBind()

    fun subscribe() {}

    fun unsubscribe() {}

}