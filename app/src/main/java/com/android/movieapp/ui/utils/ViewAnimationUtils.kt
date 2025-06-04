package com.android.movieapp.ui.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

object ViewAnimationUtils {

    private const val DEFAULT_FADE_DURATION = 300L
    private const val QUICK_FADE_DURATION = 200L

    private fun animateAlpha(
        view: View,
        targetAlpha: Float,
        duration: Long,
        onStart: (() -> Unit)? = null,
        onEnd: (() -> Unit)? = null
    ) {
        onStart?.invoke()
        view.animate()
            .alpha(targetAlpha)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction { onEnd?.invoke() }
            .start()
    }

    private fun fadeIn(
        view: View,
        duration: Long = DEFAULT_FADE_DURATION,
        onAnimationEnd: (() -> Unit)? = null
    ) {
        animateAlpha(
            view = view,
            targetAlpha = 1f,
            duration = duration,
            onStart = {
                view.visibility = View.VISIBLE
                view.alpha = 0f
            },
            onEnd = onAnimationEnd
        )
    }

    private fun fadeOut(
        view: View,
        duration: Long = QUICK_FADE_DURATION,
        hideAfterAnimation: Boolean = true,
        onAnimationEnd: (() -> Unit)? = null
    ) {
        animateAlpha(
            view = view,
            targetAlpha = 0f,
            duration = duration,
            onEnd = {
                if (hideAfterAnimation) {
                    view.visibility = View.GONE
                }
                onAnimationEnd?.invoke()
            }
        )
    }

    fun showWithFadeIn(view: View, duration: Long = DEFAULT_FADE_DURATION) {
        fadeIn(view, duration)
    }

    fun hideWithFadeOut(view: View, duration: Long = QUICK_FADE_DURATION) {
        fadeOut(view, duration, hideAfterAnimation = true)
    }

    fun hideInstantly(view: View) {
        view.apply {
            visibility = View.GONE
            alpha = 0f
        }
    }

    fun animateEntrance(
        view: View,
        position: Int,
        baseDelay: Long = 50L,
        duration: Long = 300L
    ) {
        val delay = position * baseDelay

        view.apply {
            alpha = 0f
            translationX = 100f
        }

        val fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        val slideIn = ObjectAnimator.ofFloat(view, "translationX", 100f, 0f)

        AnimatorSet().apply {
            playTogether(fadeIn, slideIn)
            this.duration = duration
            startDelay = delay
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    fun animateClick(view: View, duration: Long = 150L) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f, 1f)

        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            this.duration = duration
            start()
        }
    }
}
