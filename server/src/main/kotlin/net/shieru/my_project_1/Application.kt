package net.shieru.my_project_1

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import net.shieru.my_project_1.todo.ToDoService
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

@OptIn(ExperimentalKtorApi::class)
fun Application.module() {
    Database.connect("jdbc:sqlite:data.db", driver = "org.sqlite.JDBC")
    transaction {
        SchemaUtils.create(ToDos)
    }
    install(ContentNegotiation) {
        json()
    }
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }
    routing {
        val toDoService = ToDoService()
        configureToDoRoutes(toDoService)
    }
}