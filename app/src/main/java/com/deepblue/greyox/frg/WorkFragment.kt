package com.deepblue.greyox.frg

import android.os.Bundle
import android.view.View
import com.deepblue.greyox.R
import kotlinx.android.synthetic.main.frg_work.*

class WorkFragment : BaseFrg() {
    override fun create(var1: Bundle?) {
        setContentView(R.layout.frg_work)
    }

    override fun initView() {
        btn_work_chance.setOnClickListener(this)
        btn_work_stop_continu.setOnClickListener(this)
    }

    override fun loaddata() {
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
        }
    }

}