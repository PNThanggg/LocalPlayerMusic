package com.module.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.module.core.extension.addBit
import com.module.core.extension.getContrastColor
import com.module.core.extension.removeBit
import com.module.core.utils.CoreConstants.DARK_GREY
import com.module.core.utils.isRPlus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    // region variable
    companion object {
        private const val TIME_DELAY_CLICK = 200L
    }

    lateinit var binding: VB
    private var isAvailableClick = true
    // end region

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeCreate()

        super.onCreate(savedInstanceState)

        binding = inflateViewBinding(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView(savedInstanceState)
        initData()
        initListener()
    }

    open fun initView(savedInstanceState: Bundle?) {}

    open fun initData() {}

    open fun initListener() {}

    open fun beforeCreate() {}

    /**override it and inflate your view binding, demo in MainActivity*/
    abstract fun inflateViewBinding(inflater: LayoutInflater): VB

    private fun delayClick() {
        lifecycleScope.launch(Dispatchers.IO) {
            isAvailableClick = false
            delay(TIME_DELAY_CLICK)
            isAvailableClick = true
        }
    }

    fun View.clickSafety(action: () -> Unit) {
        this.setOnClickListener {
            if (isAvailableClick) {
                action()
                delayClick()
            }
        }
    }
}