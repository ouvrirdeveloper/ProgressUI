package com.ouvrirdeveloper.basearc.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ouvrirdeveloper.progressui.BaseActivity

abstract class BaseActivityWithBinding<T : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    showHomeAsUp: Boolean = true
) : BaseActivity() {

    private var _binding: T? = null

    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBinding()
    }

    private fun setBinding() {
        _binding = DataBindingUtil.setContentView<T>(this, layoutRes)
    }
}