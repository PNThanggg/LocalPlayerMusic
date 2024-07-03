package com.module.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    val binding: VB get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflateLayout(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        initListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun initView() {}

    open fun initData() {}

    open fun initListener() {}

    /**override it and inflate your view binding, demo in HomeFragment*/
    abstract fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): VB
}