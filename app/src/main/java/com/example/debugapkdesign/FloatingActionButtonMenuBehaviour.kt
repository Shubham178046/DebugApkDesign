package com.example.debugapkdesign

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import com.github.clans.fab.FloatingActionMenu
import com.google.android.material.snackbar.Snackbar.SnackbarLayout


class FloatingActionMenuBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<FrameLayout>() {
    private var mTranslationY = 0f

   override fun layoutDependsOn(
       parent: CoordinatorLayout,
       child: FrameLayout,
       dependency: View
   ): Boolean {
        return dependency is SnackbarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: FrameLayout,
        dependency: View
    ): Boolean {
        if (child is FloatingActionMenu && dependency is SnackbarLayout) {
            updateTranslation(parent, child, dependency)
        }
        return false
    }

    private fun updateTranslation(parent: CoordinatorLayout, child: View, dependency: View) {
        val translationY = getTranslationY(parent, child)
        if (translationY != mTranslationY) {
            ViewCompat.animate(child)
                .cancel()
            if (Math.abs(translationY - mTranslationY) == dependency.getHeight() as Float) {
                ViewCompat.animate(child)
                    .translationY(translationY)
                    .setListener(null as ViewPropertyAnimatorListener?)
            } else {
                ViewCompat.setTranslationY(child, translationY)
            }
            mTranslationY = translationY
        }
    }

    private fun getTranslationY(parent: CoordinatorLayout, child: View): Float {
        var minOffset = 0.0f
        val dependencies: List<*> = parent.getDependencies(child)
        var i = 0
        val z = dependencies.size
        while (i < z) {
            val view: View = dependencies[i] as View
            if (view is SnackbarLayout && parent.doViewsOverlap(child, view)) {
                minOffset = Math.min(
                    minOffset,
                    ViewCompat.getTranslationY(view) - view.getHeight() as Float
                )
            }
            ++i
        }
        return minOffset
    }

    /**
     * onStartNestedScroll and onNestedScroll will hide/show the FabMenu when a scroll is detected.
     */
    override open  fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FrameLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int
    ): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(
                    coordinatorLayout!!, child, directTargetChild, target,
                    nestedScrollAxes
                )
    }

     override fun onNestedScroll(
         coordinatorLayout: CoordinatorLayout,
         child: FrameLayout,
         target: View,
         dxConsumed: Int,
         dyConsumed: Int,
         dxUnconsumed: Int,
         dyUnconsumed: Int
     ) {
        super.onNestedScroll(
            coordinatorLayout!!, child, target, dxConsumed, dyConsumed, dxUnconsumed,
            dyUnconsumed
        )
        val fabMenu = child as FloatingActionMenu
        if (dyConsumed > 0 && !fabMenu.isMenuButtonHidden) {
            fabMenu.hideMenuButton(true)
        } else if (dyConsumed < 0 && fabMenu.isMenuButtonHidden) {
            fabMenu.showMenuButton(true)
        }
    }
}