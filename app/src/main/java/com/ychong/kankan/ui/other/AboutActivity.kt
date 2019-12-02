package com.ychong.kankan.ui.other

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.ychong.kankan.R
import com.ychong.baselib.base.BaseActivity
import com.ychong.baselib.utils.BaseUtils

/**
 * 关于
 */
class AboutActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_about
    }

    private var backIv: ImageView? = null
    private var titleTv: TextView? = null
    private var versionNumberTv: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        initView()
        initData()
        initListener()
    }

    private fun initListener() {
        backIv!!.setOnClickListener { onBackPressed() }

    }

    @SuppressLint("SetTextI18n")
    private fun initData() {
        titleTv!!.text = getString(R.string.about_kankan)
        versionNumberTv!!.text = "V"+ BaseUtils.getAppVersionName(this)
    }

    private fun initView() {
        backIv = findViewById(R.id.left_iv) as ImageView?
        titleTv = findViewById(R.id.title_tv) as TextView?
        versionNumberTv = findViewById(R.id.version_number_tv) as TextView?
    }

    private fun initLayout() {
        //setContentView(R.layout.activity_about)
    }
}
