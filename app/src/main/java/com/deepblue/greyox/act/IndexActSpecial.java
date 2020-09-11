package com.deepblue.greyox.act;// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.deepblue.greyox.R;
import com.mdx.framework.Frame;
import com.mdx.framework.activity.BaseActivity;
import com.mdx.framework.utility.Helper;

public class IndexActSpecial extends BaseActivity {
    public IndexActSpecial() {
    }

    private long exitTime = 0L;

    public void create(Bundle savedInstanceState) {
        this.setContentView(R.layout.default_act_title_special);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && event.getAction() == 0) {
            if (System.currentTimeMillis() - this.exitTime > 2000L) {
                Helper.toast("再按一次退出程序!");
                this.exitTime = System.currentTimeMillis();
            } else {
                Frame.finish();
                System.exit(0);
            }

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
