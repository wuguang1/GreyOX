package com.deepblue.greyox.websocket

import android.content.Context
import android.util.Log
import com.deepblue.greyox.view.LoadingDialog
import com.deepblue.library.planbmsg.JsonUtils
import com.mdx.framework.Frame
import okhttp3.*
import java.util.concurrent.TimeUnit

class WebSocketClient3(url: String) {
    private var mWebSocket: WebSocket? = null
    private var client: OkHttpClient? = null
    var loadDialog: LoadingDialog? = null

    @JvmField
    var host: String = ""

    var connectStatus = -1

    init {
        this.host = url
    }

    private fun connect(url: String) {
        try {
            if (connectStatus != CONNECT_CONNECTING) {
                connectStatus = CONNECT_CONNECTING
                host = url
                val request = Request.Builder().url(url).build()
                client = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()
                val webSocketListener = object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        super.onOpen(webSocket, response)
                        connectStatus = CONNECT_SUCCESS
                        Frame.HANDLES.sentAll(10001, CONNECT_SUCCESS)
                    }

                    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                        super.onFailure(webSocket, t, response)
                        t.printStackTrace()
                        Log.e("web", "onFailure")
//                        dismissLoadDialog(-1)
                        if (connectStatus != CONNECT_ERROR) {
                            connectStatus = CONNECT_ERROR
                            Frame.HANDLES.sentAll(10001, CONNECT_ERROR)
                        }
                        mWebSocket?.close(1000, "null")
                        mWebSocket = null
                    }


                    override fun onMessage(webSocket: WebSocket, text: String) {
                        Log.e("web", "back" + text)
                        if (text.isNotEmpty()) {
                            try {
                                val res = JsonUtils.fromJson(text, com.deepblue.library.planbmsg.Response::class.java)
                                res?.let {
                                    Frame.HANDLES.sentAll(it.type, text)
                                    dismissLoadDialog(res.type)
                                }
//                                socketMessageCallback?.onMessage(text)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                        super.onClosed(webSocket, code, reason)
                        Log.e("web", "onClosed")
                        if (connectStatus != CONNECT_CLOSE) {
                            connectStatus = CONNECT_CLOSE
                            Frame.HANDLES.sentAll(10001, CONNECT_CLOSE)
                        }
                        mWebSocket?.close(1000, "null")
                        mWebSocket = null
                    }

                    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                        super.onClosing(webSocket, code, reason)
                        if (connectStatus != CONNECT_CLOSE) {
                            connectStatus = CONNECT_CLOSE
                            Frame.HANDLES.sentAll(10001, CONNECT_CLOSE)
                        }
                    }
                }
                client?.dispatcher?.cancelAll()
                mWebSocket = client?.newWebSocket(request, webSocketListener)
            }
        } catch (e: Exception) {
            connectStatus = -1
            e.printStackTrace()
        }
    }

    fun sendMessage(request: com.deepblue.library.planbmsg.Request, context: Context? = null, isShowLoading: Boolean = false, isCanceledOnTouchOutside: Boolean = false) {
        if (isShowLoading) {
            dismissLoadDialog(-1)
        }
        context?.let {
            loadDialog = LoadingDialog(it)
            if (isShowLoading && !loadDialog!!.isShowing) {
                loadDialog?.setCanceledOnTouchOutside(isCanceledOnTouchOutside)
                loadDialog?.currentType = request.type
                loadDialog?.show()
            }
        }
        if (mWebSocket != null && connectStatus == CONNECT_SUCCESS) {
            Log.e("web", "send$request")
            mWebSocket?.send(request.toString())
        } else {
            if (connectStatus != CONNECT_CONNECTING) {
                connect(host)
            }
        }
    }

    fun dismissLoadDialog(res_type: Int) {
        var aa = loadDialog?.currentType
        loadDialog?.let {
            if (it.isShowing && (res_type.toString().contains(it.currentType.toString()) || res_type == -1)) {
                it.dismissDiaolog()
            }
        }
    }

    fun isConnected(): Boolean {
        return connectStatus == CONNECT_SUCCESS
    }


    fun destroy() {
        mWebSocket?.close(1000, "null")
        mWebSocket = null
        webSocketClient?.connectStatus = -1
        webSocketClient = null
        client = null
        host = ""
    }

    companion object {

        private var webSocketClient: WebSocketClient3? = null
        const val CONNECT_CONNECTING = 0//正在连接
        const val CONNECT_SUCCESS = 1//连接成功
        const val CONNECT_ERROR = 2//连接失败
        const val CONNECT_CLOSE = 3//连接断开

        @Synchronized
        @JvmStatic
        fun getInstance(url: String): WebSocketClient3 {
            if (webSocketClient == null) {
                webSocketClient = WebSocketClient3(url)
            }
            return webSocketClient!!
        }

    }
}
