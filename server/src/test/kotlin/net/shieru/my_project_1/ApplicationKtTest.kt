package net.shieru.my_project_1

import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class ApplicationKtTest {

    @Test
    fun testPost() = testApplication {
        application {
            TODO("Add the Ktor module for the test")
        }
        client.post("/").apply {
            TODO("Please write your test here")
        }
    }
}