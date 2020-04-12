package ru.gonchar17narod.transvoicestudio.view.custom_views

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.webkit.WebView

class LollipopFixedWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : WebView(context.createConfigurationContext(Configuration()), attrs, defStyleAttr, defStyleRes)