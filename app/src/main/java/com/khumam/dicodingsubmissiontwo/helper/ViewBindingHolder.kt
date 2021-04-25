package com.khumam.dicodingsubmissiontwo

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding

interface ViewBindingHolder<T : ViewBinding> {

    val binding: T?

    fun initBinding(binding: T, fragment: Fragment, onBound: (T.() -> Unit)?): View

    fun requireBinding(block: (T.() -> Unit)? = null): T
}

class ViewBindingHolderImpl<T : ViewBinding> : ViewBindingHolder<T>, LifecycleObserver {

    override var binding: T? = null
    var lifecycle: Lifecycle? = null

    private lateinit var fragmentName: String

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyView() {
        lifecycle?.removeObserver(this)
        lifecycle = null
        binding = null
    }

    override fun requireBinding(block: (T.() -> Unit)?) =
        binding?.apply { block?.invoke(this) } ?: throw IllegalStateException("Gagal menghubungkan dengan fragment : $fragmentName")

    override fun initBinding(binding: T, fragment: Fragment, onBound: (T.() -> Unit)?): View {
        this.binding = binding
        lifecycle = fragment.viewLifecycleOwner.lifecycle
        lifecycle?.addObserver(this)
        fragmentName = fragment::class.simpleName ?: "N/A"
        onBound?.invoke(binding)
        return binding.root
    }
}