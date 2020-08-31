//
//  FrgHome
//
//  Created by 86139 on 2020-08-26 14:50:18
//  Copyright (c) 86139 All rights reserved.


/**

*/

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
  }

  override fun onSuccess(data: String?, method: String) {
  }

}