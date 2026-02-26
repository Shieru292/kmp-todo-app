package net.shieru.my_project_1

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.getOrFail
import net.shieru.my_project_1.todo.ToDoService

fun Routing.configureToDoRoutes(toDoService: ToDoService) {
    get("/") {
        val allToDos = toDoService.getAllToDos()
        call.respond(allToDos)
    }
    get("/hello") {
        call.respond("Hello, Ktor!")
    }
    get("/{todoId}") {
        val todoId = call.parameters.getOrFail("todoId").toInt()
        val todo = toDoService.getToDoById(todoId) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(todo)
    }
    post("/") {
        val todo = call.receive<ToDo>()

        toDoService.createToDo(todo).fold(
            ifLeft = { validationError ->
                when (validationError) {
                    EmptyContentError -> call.respond(HttpStatusCode.BadRequest, "Content cannot be empty.")
                    IdAlreadyExistsError -> call.respond(HttpStatusCode.Conflict, "ID already exists.")
                    is NameTooLongError -> call.respond(HttpStatusCode.BadRequest, "Name is too long.")
                    NegativeIdError -> call.respond(HttpStatusCode.BadRequest, "ID must be positive.")
                }
            },
            ifRight = { validated ->
                call.respond(HttpStatusCode.Created, validated)
            }
        )
    }
}
