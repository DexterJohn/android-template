@file:Suppress("MemberVisibilityCanBePrivate")

package org.jdc.template.ui.navigation

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.NavDestinationBuilder
import androidx.navigation.NavGraphBuilder
import androidx.navigation.fragment.fragment

abstract class NavFragmentRoute {
    /**
     * Route definition
     */
    abstract fun defineRoute(): String

    /**
     * Define arguments, routes, deeplinks, etc for the default fragment
     */
    abstract fun <D: NavDestination> NavDestinationBuilder<D>.setupNav()

    /**
     * Used when creating navigation graph
     */
    inline fun <reified T: Fragment> addNavigationRoute(navGraphBuilder: NavGraphBuilder, context: Context) {
        navGraphBuilder.apply {
            // default fragment
            fragment<T>(defineRoute()) {
                label = getLabel(context)
                setupNav()
            }
        }
    }

    /**
     * Define label for route
     */
    open fun getLabel(context: Context): String? = null
}

/**
 * Simple Route that requires no arguments
 */
abstract class SimpleNavFragmentRoute(val route: String, val labelId: Int? = null) : NavFragmentRoute() {
    override fun defineRoute(): String = route

    override fun <D : NavDestination> NavDestinationBuilder<D>.setupNav() {
    }

    override fun getLabel(context: Context): String? {
        return labelId?.let { context.getString(it) }
    }
}