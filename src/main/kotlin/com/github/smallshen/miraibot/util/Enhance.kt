package com.github.smallshen.miraibot.util

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.*
import java.io.InputStream

suspend fun HttpClient.getImg(url: String): InputStream {
    val response = get<HttpResponse>(url)
    return response.content.toInputStream()
}
