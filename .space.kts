/**
* JetBrains Space Automation
* This Kotlin-script file lets you automate build activities
* For more info, refer to https://www.jetbrains.com/help/space/automation.html
*/

@file:DependsOn("com.squareup.okhttp:okhttp:2.7.4")

import com.squareup.okhttp.*

job("Get example.com") {
    container("openjdk:11") {
        kotlinScript {
            val client = OkHttpClient()
            val request = Request.Builder().url("http://example.com").build()
            val response = client.newCall(request).execute()
            println(response)
        }
    }
}
