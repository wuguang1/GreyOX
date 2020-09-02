package com.deepblue.greyox.frg;

import android.os.Bundle;

import com.deepblue.greyox.R;

import com.deepblue.greyox.frg.BaseFrg


class HomeFragment : BaseFrg() {

    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_home)
    }

    override fun initView() {
    }

    override fun loaddata() {
//      sendwebSocket(,context = context)
    }

    override fun onSuccess(data: String?, method: String) {
    }

}