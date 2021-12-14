package com.ouvrirdeveloper.progressui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


open class BaseActivity() : AppCompatActivity() {


    private val progressLayout: View by lazy {
        var pLayout = findViewById<View>(R.id.cl_progress_loading)
        val parent: ViewGroup = (findViewById(android.R.id.content) as ViewGroup)
        val viewGroup = parent.getChildAt(0) as ViewGroup
        pLayout = LayoutInflater.from(this).inflate(R.layout.progress_layout, null, false)
        pLayout.elevation = 5f
        if (viewGroup is ConstraintLayout) {
            val set1 = ConstraintSet()
            pLayout.setLayoutParams(LinearLayoutCompat.LayoutParams(0, 0))
            viewGroup.addView(pLayout)
            set1.connect(
                pLayout.getId(),
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
            )
            set1.connect(
                pLayout.getId(),
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT,
                0
            )
            set1.connect(
                pLayout.getId(),
                ConstraintSet.RIGHT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT,
                0
            )
            set1.connect(
                pLayout.getId(),
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
            )
            set1.applyTo(viewGroup)
        }
        pLayout
    }

    fun showProgress(
        backgroundColorRes: Int? = null,
        message: String = R.string.loading_please_wait.asString(this),
        showRetry: Boolean = false,
        lottieFile: Int = -1,
        reTry: (() -> Unit)? = null
    ) = progressLayout.apply {
        if (backgroundColorRes != null) {
            this.findViewById<View>(R.id.viewBg)?.apply {
                this.setBackgroundColor(backgroundColorRes)
                this.setAlpha(1f)
            }
        } else {
            this.findViewById<View>(R.id.viewBg)?.apply {
                this.background = null
                this.setAlpha(1f)
            }
        }
        val regularProgressBar = this.findViewById<ProgressBar>(R.id.progress_bar_loading)

        this.findViewById<LottieAnimationView>(R.id.lottie_loading)?.apply {
            if (showRetry) {
                regularProgressBar?.apply {
                    gone()
                }
                setAnimation(if (lottieFile == -1) R.raw.no_internet else lottieFile)
                playAnimation()
                makeVisible()
            } else if (lottieFile == -1) {
                gone()
                regularProgressBar?.apply {
                    makeVisible()
                }
            } else {
                regularProgressBar?.apply {
                    gone()
                }
                setAnimation(if (lottieFile == -1) R.raw.loading else lottieFile)
                playAnimation()
                makeVisible()
            }

        }
        this.findViewById<TextView>(R.id.text_view_loading)?.apply {
            text = message
        }
        this.findViewById<TextView>(R.id.tv_retry)?.apply {
            if (showRetry) {
                text = R.string.retry.asString(this@BaseActivity)
                setOnClickListener {
                    reTry?.invoke()
                }
                makeVisible()
                requestLayout()
            } else {
                makeInvisible()
            }
        }
        makeVisible()
    }

    fun hideProgress() = CoroutineScope(Dispatchers.Main).launch {
        delay(500)
        lifecycleScope.launchWhenStarted {
            // progressLayout.slideAnimation(SlideDirection.DOWN, SlideType.HIDE, 700)
            progressLayout.makeInvisible()
        }
    }

    fun showRetry(
        message: String,
        reTry: (() -> Unit)? = null,
        backgroundColorRes: Int? = null,
        lottieFile: Int = -1
    ) {
        showProgress(
            message = message,
            reTry = reTry,
            showRetry = true,
            backgroundColorRes = backgroundColorRes,
            lottieFile = lottieFile
        )
    }


}

fun View.gone() {
    visibility = View.GONE
}

fun Int.asString(context: Context) = context.getString(this)

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}
