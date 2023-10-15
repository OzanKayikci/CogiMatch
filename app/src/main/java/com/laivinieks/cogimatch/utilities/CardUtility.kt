package com.laivinieks.cogimatch.utilities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.PathInterpolator
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.laivinieks.cogimatch.R
import com.laivinieks.cogimatch.data.entity.Card
import java.util.concurrent.TimeUnit

object CardUtility {

    fun flipAnimation(
        cardView: CardView,
        imageView: ImageView,
        cardImage: Drawable?,
        cardBack: Drawable,
        isClose: Boolean
    ) {
        val firstHalfRotate = ObjectAnimator.ofFloat(cardView, "ScaleX", 1f, 1.15f, 0f)
        val secondHalfRotate = ObjectAnimator.ofFloat(cardView, "ScaleX", 0f, 1.15f, 1f)
        val scaleY = ObjectAnimator.ofFloat(cardView, "scaleY", 1f, 1.15f, 1f)

        firstHalfRotate.interpolator = DecelerateInterpolator()
        secondHalfRotate.interpolator = AccelerateInterpolator()



        firstHalfRotate.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                //change Image
                if (isClose
                ) {
                    imageView.setImageDrawable(cardImage)

                } else {
                    imageView.setImageDrawable(cardBack)
                }
                secondHalfRotate.start()

            }

        })
        firstHalfRotate.start()
        scaleY.start()
        scaleY.duration = 300
        firstHalfRotate.duration = 150
        secondHalfRotate.duration = 150

    }


    fun compareCards(openedCard: Card, selectedCard: Card, callback: (Boolean) -> Unit) {

        if (openedCard.image == selectedCard.image) {
            callback(true)

        } else {
            callback(false)
        }
    }


    fun explodeAnimation(cardView: CardView) {
        val scaleX = ObjectAnimator.ofFloat(cardView, "scaleX", 1f, 1.15f, 0f)
        val scaleY = ObjectAnimator.ofFloat(cardView, "scaleY", 1f, 1.15f, 0f)
        val alpha = ObjectAnimator.ofFloat(cardView, "alpha", 0.3f, 1f, 0f)

        val animatorSet = AnimatorSet()
        animatorSet.play(scaleX).with(scaleY).with(alpha)
        animatorSet.duration = Constants.ANIMATION_DURATION

        animatorSet.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                cardView.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })

        animatorSet.start()
    }


    fun sizeConvertorFromDimension(context: Context, multiplayer: Float): Float {

        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        Log.d("Height", screenHeight.toString())
        return if (screenHeight <= 2500) {
            multiplayer / 1.125f
        } else {
            multiplayer
        }
    }


    fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {

        var milliseconds: Long = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        val timeString =
            "${if (minutes < 10) "0" else ""}$minutes:" +
                    "${if (seconds < 10) "0" else ""}$seconds"
        if (!includeMillis) {
            return timeString
        }

        milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliseconds /= 10
        val timeStringWithMillis =
            timeString + ":${if (milliseconds < 10) "0" else ""}$milliseconds "
        return timeStringWithMillis
    }

    fun scoreCalculator(turns: Int, matches: Int, stage: Int): Int {
        return (1000 * ((matches * matches).toFloat() * stage / turns)).toInt()
    }

}