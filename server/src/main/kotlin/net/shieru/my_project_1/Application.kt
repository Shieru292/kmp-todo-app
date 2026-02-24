package net.shieru.my_project_1

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.utils.io.*
import org.jetbrains.exposed.v1.core.eq
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
        get("/") {
            val allToDos = transaction {
                ToDoEntity.all().map { it.toDomain() }
            }
            call.respond(allToDos)
        }

        get("/hello") {
            call.respond("Hello, Ktor!")
        }

        get("/{todoId}") {
            val todoId = call.parameters.getOrFail("todoId").toInt()
            val todo = transaction {
                ToDoEntity.find { ToDos.id eq todoId }.firstOrNull()?.toDomain()
            }
            if (todo == null) {
                return@get call.respond(HttpStatusCode.NotFound)
            }
            call.respond(todo)
        }

        post("/") {
            val todo = call.receive<ToDo>()
            when (val validationResult = validateToDo(todo)) {
                is EmptyContentError -> return@post call.respond(HttpStatusCode.BadRequest, "Content cannot be empty")
                is NegativeIdError -> return@post call.respond(HttpStatusCode.BadRequest, "ID cannot be negative")
                is NameTooLongError -> {
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        "Content length cannot exceed ${validationResult.maxLength}, but was ${validationResult.actualLength}"
                    )
                }

                is Valid -> {}
            }
            val savedTodo = transaction {
                val entity = ToDoEntity.new {
                    content = todo.content
                }

                entity.toDomain()
            }

            call.respond(HttpStatusCode.Created, savedTodo)

        }
    }
}