@file:OptIn(ExperimentalJsExport::class, DelicateCoroutinesApi::class, ExperimentalJsCollectionsApi::class)
package net.shieru.my_project_1

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.Promise
import kotlin.js.collections.JsReadonlyArray


@JsExport
fun fetchAllToDo(): Promise<JsReadonlyArray<ToDo>> = GlobalScope.promise {
    fetchAllToDoSuspend().asJsReadonlyArrayView()
}

@JsExport
fun fetchToDo(todoId: Int): Promise<ToDo?> = GlobalScope.promise {
    fetchToDoSuspend(todoId)
}

@JsExport
fun addToDo(todo: ToDo): Promise<ToDo> = GlobalScope.promise {
    addToDoSuspend(todo)
}

@JsExport
data class JsValidationResult(val isValid: Boolean, val errorMessage: String? = null)

@JsExport
fun validateToDoJs(todo: ToDo): JsValidationResult {
    return validateToDo(todo).fold(
        ifLeft = {
            when(it) {
                EmptyContentError -> JsValidationResult(false, "内容を入力してください。")
                IdAlreadyExistsError -> JsValidationResult(false, "既に存在するIDです。")
                is NameTooLongError -> JsValidationResult(false, "内容は${it.maxLength}文字以内で入力してください。")
                NegativeIdError -> JsValidationResult(false, "IDは正の数値で入力してください。")
            }
        },
        ifRight = {
            JsValidationResult(true)
        }
    )
}
