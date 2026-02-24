package net.shieru.my_project_1

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

const val URL_BASE = "http://localhost:$SERVER_PORT"

internal val httpClient = HttpClient() {
    install(ContentNegotiation) {
        json()
    }
}

internal suspend fun fetchHelloSuspend(): String {
    val response = httpClient.get("$URL_BASE/hello")
    return response.bodyAsText()
}

internal suspend fun fetchAllToDoSuspend(): List<ToDo> {
    val response = httpClient.get("$URL_BASE/")
    return response.body()
}

internal suspend fun fetchToDoSuspend(todoId: Int): ToDo? {
    val response = httpClient.get("$URL_BASE/$todoId")
    if (response.status != HttpStatusCode.OK) {
        return null
    }
    return response.body()
}

internal suspend fun addToDoSuspend(todo: ToDo): ToDo {
    val response = httpClient.post("$URL_BASE/") {
        contentType(ContentType.Application.Json)
        setBody(todo)
    }
    return response.body()
}