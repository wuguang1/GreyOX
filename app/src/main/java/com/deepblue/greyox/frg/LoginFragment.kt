package com.deepblue.greyox.frg

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.deepblue.greyox.Const
import com.deepblue.greyox.R
import com.deepblue.greyox.act.IndexActSpecial
import com.deepblue.greyox.ada.BaseAdapter
import com.deepblue.greyox.ada.LoginUsersAdapter
import com.deepblue.library.planbmsg.JsonUtils
import com.deepblue.library.planbmsg.bean.UserInfo
import com.deepblue.library.planbmsg.msg2000.GetAllUsersReq
import com.deepblue.library.planbmsg.msg2000.GetAllUsersRes
import com.deepblue.library.planbmsg.msg2000.LoginReq
import com.deepblue.library.planbmsg.msg2000.LoginRes
import com.mdx.framework.Frame
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_login.*


class LoginFragment : BaseFrg() {
    var passwords: ArrayList<Int> = ArrayList()
    var usersDataList = ArrayList<UserInfo>()
    private lateinit var mAdapter: LoginUsersAdapter

    override fun create(var1: Bundle?) {
        setContentView(R.layout.frg_login)
        Frame.HANDLES.closeWidthOut("LoginFragment")
    }

    override fun initView() {
        rl_login_etuser.setOnClickListener(this)
        view_pwd.setOnClickListener(this)
        tv_login0.setOnClickListener(this)
        tv_login1.setOnClickListener(this)
        tv_login2.setOnClickListener(this)
        tv_login3.setOnClickListener(this)
        tv_login4.setOnClickListener(this)
        tv_login5.setOnClickListener(this)
        tv_login6.setOnClickListener(this)
        tv_login7.setOnClickListener(this)
        tv_login8.setOnClickListener(this)
        tv_login9.setOnClickListener(this)
        tv_login_cancel.setOnClickListener(this)
        tv_login_delete.setOnClickListener(this)
        btn_login.setOnClickListener(this)

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycleview_users.layoutManager = layoutManager
        initAdapter(context!!)
    }

    private fun initAdapter(contexts: Context) {
        mAdapter = LoginUsersAdapter(contexts, usersDataList, R.layout.adapter_user)
        recycleview_users.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
        mAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                activity?.runOnUiThread {
                    tv_login_user.text = usersDataList[position].name
//                    img_down.setImageResource(R.drawable.down)
                    recycleview_users.visibility = View.GONE
                    ll_pw.visibility = View.VISIBLE
                    btn_login.visibility = View.GONE
                    ll_keyboard.visibility = View.VISIBLE
                    if (passwords.size > 0) {
                        passwords.clear()
                        uiChange()
                    }
                }
            }
        })
    }

    override fun loaddata() {
        sendwebSocket(GetAllUsersReq(), context)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.rl_login_etuser -> {
                if (usersDataList.isNotEmpty() && usersDataList.size > 0) {
                    if (recycleview_users.visibility == View.VISIBLE) {
                        recycleview_users.visibility = View.GONE
                        ll_pw.visibility = View.VISIBLE
//                        img_down.setImageResource(R.drawable.down)
                        if (tv_login_user.text == getText(R.string.login_tips) || (passwords != null && passwords.size == 6)) {
                            btn_login.visibility = View.VISIBLE
                            ll_keyboard.visibility = View.GONE
                        } else {
                            btn_login.visibility = View.GONE
                            ll_keyboard.visibility = View.VISIBLE
                            uiChange()
                        }
                    } else {
//                        img_down.setImageResource(R.drawable.up)
                        recycleview_users.visibility = View.VISIBLE
                        ll_pw.visibility = View.GONE
                        btn_login.visibility = View.GONE
                        ll_keyboard.visibility = View.GONE
                    }
                } else {
                    Helper.toast("当前无用户")
                }
            }
            R.id.view_pwd -> {
                if (tv_login_user.text == getText(R.string.login_tips)) {
                    Helper.toast(getText(R.string.login_tips).toString())
                } else {
                    if (passwords.size == 6) {
                        btn_pwsix.isFocusable = true
                        btn_pwsix.isFocusableInTouchMode = true
                        btn_pwsix.requestFocus()
                        btn_pwsix.setSelection(1)
                    }
                    btn_login.visibility = View.GONE
                    ll_keyboard.visibility = View.VISIBLE
                }
            }
            R.id.tv_login0 -> {
                if (passwords.size < 6) {
                    passwords.add(0)
                    uiChange()
                }
            }
            R.id.tv_login1 -> {
                if (passwords.size < 6) {
                    passwords.add(1)
                    uiChange()
                }
            }
            R.id.tv_login2 -> {
                if (passwords.size < 6) {
                    passwords.add(2)
                    uiChange()
                }
            }
            R.id.tv_login3 -> {
                if (passwords.size < 6) {
                    passwords.add(3)
                    uiChange()
                }
            }
            R.id.tv_login4 -> {
                if (passwords.size < 6) {
                    passwords.add(4)
                    uiChange()
                }
            }
            R.id.tv_login5 -> {
                if (passwords.size < 6) {
                    passwords.add(5)
                    uiChange()
                }
            }
            R.id.tv_login6 -> {
                if (passwords.size < 6) {
                    passwords.add(6)
                    uiChange()
                }
            }
            R.id.tv_login7 -> {
                if (passwords.size < 6) {
                    passwords.add(7)
                    uiChange()
                }
            }
            R.id.tv_login8 -> {
                if (passwords.size < 6) {
                    passwords.add(8)
                    uiChange()
                }
            }
            R.id.tv_login9 -> {
                if (passwords.size < 6) {
                    passwords.add(9)
                    uiChange()
                }
            }
            R.id.tv_login_cancel -> {
                if (passwords.size > 0) {
                    passwords.clear()
                    uiChange()
                }
            }
            R.id.tv_login_delete -> {
                if (passwords.size > 0) {
                    passwords.removeAt(passwords.size - 1)
                    uiChange()
                }
            }
            R.id.btn_login -> {
                val name = tv_login_user.text.toString()
                if (passwords.size > 5 && tv_login_user.text.toString().isNotEmpty() && (getText(R.string.login_tips).toString() != name)) {
                    val passwd = StringBuffer()
                    for (j in passwords.indices) {
                        passwd.append(passwords[j])
                    }
                    sendwebSocket(LoginReq().robot2(tv_login_user.text.toString(), passwd.toString()), context, true)
                } else {
                    Helper.toast("请填写正确信息")
                }
            }
        }
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        super.disposeMsg(type, obj)
        when (type) {
            12003 -> {
                val loginRes = JsonUtils.fromJson(obj.toString(), LoginRes::class.java)
                if (loginRes?.error_code == 0 || loginRes?.error_code == -3) {
                    Const.user = loginRes.getJson()
                    Helper.startActivity(context, HomeFragment::class.java, IndexActSpecial::class.java)
                    finish()
                } else if (loginRes?.error_code == -1) {
                    Helper.toast("用户密码错误")
                }
            }
            12007 -> {
                val allUsersRes = JsonUtils.fromJson(obj.toString(), GetAllUsersRes::class.java)
                val alluser = allUsersRes!!.getJson()!!.users
                usersDataList.clear()
                usersDataList.addAll(alluser)
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun uiChange() {
        btn_pwone.setText("")
        btn_pwtwo.setText("")
        btn_pwthree.setText("")
        btn_pwfour.setText("")
        btn_pwfive.setText("")
        btn_pwsix.setText("")

        if (passwords.size == 0) {
            btn_pwone.isFocusable = true
            btn_pwone.isFocusableInTouchMode = true
            btn_pwone.requestFocus()
        }
        if (passwords.size > 0) {
            btn_pwone.setText(passwords.get(0).toString() + "")
            btn_pwtwo.isFocusable = true
            btn_pwtwo.isFocusableInTouchMode = true
            btn_pwtwo.requestFocus()
        }
        if (passwords.size > 1) {
            btn_pwtwo.setText(passwords.get(1).toString() + "")
            btn_pwthree.isFocusable = true
            btn_pwthree.isFocusableInTouchMode = true
            btn_pwthree.requestFocus()
        }
        if (passwords.size > 2) {
            btn_pwthree.setText(passwords.get(2).toString() + "")
            btn_pwfour.isFocusable = true
            btn_pwfour.isFocusableInTouchMode = true
            btn_pwfour.requestFocus()
        }
        if (passwords.size > 3) {
            btn_pwfour.setText(passwords.get(3).toString() + "")
            btn_pwfive.isFocusable = true
            btn_pwfive.isFocusableInTouchMode = true
            btn_pwfive.requestFocus()
        }
        if (passwords.size > 4) {
            btn_pwfive.setText(passwords.get(4).toString() + "")
            btn_pwsix.isFocusable = true
            btn_pwsix.isFocusableInTouchMode = true
            btn_pwsix.requestFocus()
        }
        if (passwords.size > 5) {
            btn_pwsix.setText(passwords.get(5).toString() + "")
            tv_yhdl.isFocusable = true
            tv_yhdl.isFocusableInTouchMode = true
            tv_yhdl.requestFocus()
            btn_login.visibility = View.VISIBLE
            ll_keyboard.visibility = View.GONE
        }

    }

    override fun setActionBar(actionBar: LinearLayout?) {
    }
}