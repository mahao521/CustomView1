package com.mahao.customview.okhttp

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.EMPTY_RESPONSE
import okhttp3.internal.closeQuietly
import okhttp3.internal.connection.Exchange
import okhttp3.internal.connection.RouteException
import okhttp3.internal.http.HttpMethod
import okhttp3.internal.http.RealInterceptorChain
import okhttp3.internal.http.RetryAndFollowUpInterceptor
import okhttp3.internal.http.StatusLine
import okhttp3.internal.http2.ConnectionShutdownException
import okhttp3.internal.withSuppressed
import okio.buffer
import java.io.IOException
import java.net.HttpURLConnection
import java.net.ProtocolException
import java.net.Proxy

class OkHttpTest {

}