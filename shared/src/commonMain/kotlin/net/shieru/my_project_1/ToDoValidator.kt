package net.shieru.my_project_1

import arrow.core.Either
import arrow.core.left
import arrow.core.right

const val MAX_NAME_LENGTH = 50

sealed interface ValidationResult
object Valid : ValidationResult
sealed interface ValidationError : ValidationResult
object EmptyContentError : ValidationError
object NegativeIdError : ValidationError
object IdAlreadyExistsError : ValidationError
data class NameTooLongError(val maxLength: Int, val actualLength: Int) : ValidationError

fun validateToDo(todo: ToDo): Either<ValidationError, ToDo> {
    if (todo.content.isEmpty()) return EmptyContentError.left()
    if (todo.id < 0) return NegativeIdError.left()
    if (todo.content.length > MAX_NAME_LENGTH) return NameTooLongError(MAX_NAME_LENGTH, todo.content.length).left()

    return todo.right()
}
