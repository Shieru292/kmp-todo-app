package net.shieru.my_project_1

const val MAX_NAME_LENGTH = 50

sealed interface ValidationResult
object Valid : ValidationResult
sealed interface ValidationError : ValidationResult
object EmptyContentError : ValidationError
object NegativeIdError : ValidationError
data class NameTooLongError(val maxLength: Int, val actualLength: Int) : ValidationError

fun validateToDo(todo: ToDo): ValidationResult {
    if (todo.content.isEmpty()) return EmptyContentError
    if (todo.id < 0) return NegativeIdError
    if (todo.content.length > MAX_NAME_LENGTH) return NameTooLongError(MAX_NAME_LENGTH, todo.content.length)

    return Valid
}
