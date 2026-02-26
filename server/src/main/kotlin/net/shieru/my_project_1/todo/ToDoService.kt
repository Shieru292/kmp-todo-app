package net.shieru.my_project_1.todo

import arrow.core.Either
import arrow.core.raise.either
import net.shieru.my_project_1.IdAlreadyExistsError
import net.shieru.my_project_1.ToDo
import net.shieru.my_project_1.ToDoEntity
import net.shieru.my_project_1.ToDos
import net.shieru.my_project_1.ValidationError
import net.shieru.my_project_1.toDomain
import net.shieru.my_project_1.validateToDo
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException

class ToDoService {
    fun getAllToDos(): List<ToDo> = transaction {
        ToDoEntity.all().map { it.toDomain() }
    }

    fun getToDoById(id: Int): ToDo? = transaction {
        ToDoEntity.find { ToDos.id eq id }.firstOrNull()?.toDomain()
    }

    fun createToDo(todo: ToDo): Either<ValidationError, ToDo> = either {
        validateToDo(todo).bind()

        val domainTodo = Either.catch {
            transaction {
                val entity = ToDoEntity.new { content = todo.content }
                entity.toDomain()
            }
        }.mapLeft { e ->
            if (e is ExposedSQLException && e.sqlState == "23505") IdAlreadyExistsError else throw e
        }.bind()
        domainTodo
    }
}