//
//  FrgErrorList
//
//  Created by 86139 on 2020-09-07 09:55:37
//  Copyright (c) 86139 All rights reserved.


/**
   
*/

package com.deepblue.greyox.frg;
import android.os.Bundle;

import com.deepblue.greyox.R;

import android.widget.TextView;
import android.widget.ListView;



class FrgErrorList : BaseFrg() {

  override fun create(savedInstanceState: Bundle?) {
           setContentView(R.layout.frg_error_list)
  }

  override fun initView() {
  }

  override fun loaddata() {
  }

  override fun onSuccess(data: String?, method: String) {
  }
 
}