package pl.mbaleczny.rapid_mg.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import pl.mbaleczny.rapid_mg.R

/**
 * Created by mariusz on 04.02.17.
 */

val USER_ID_ARG = "user_id"

fun AppCompatActivity.showFragment(fragment: Fragment, addToBackStack: Boolean) {
    val existingFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.activity_container)

    if (existingFragment == null || existingFragment.javaClass.toString() != fragment.javaClass.toString()) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                .replace(R.id.activity_container, fragment)

        if (addToBackStack) transaction.addToBackStack(null)

        transaction.commit()
    }
}
