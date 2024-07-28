package com.example.moviescreen.utilitiesclasses.baseclass

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * [BaseActivity] that adds helper methods for data binding.
 */

abstract class DataBindingActivity<TBinding : ViewDataBinding> : BaseActivity() {


    /**
     * The Layout Resource ID for the activity. This is inflated automatically.
     */
    abstract val layoutRes: Int
        @LayoutRes get

    /**
     * The inflated [ViewDataBinding]
     */
    protected lateinit var binding: TBinding

    /**
     * Creates the [ViewDataBinding] for this view.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.lifecycleOwner = this
        onBindView(binding)
        binding.executePendingBindings()
    }

    /**
     * Called during onCreate, immediately after the view has been inflated.
     * Override this to bind values to the view.
     * [ViewDataBinding.executePendingBindings] will be executed after this method.
     */
    protected open fun onBindView(binding: TBinding) {}


}

