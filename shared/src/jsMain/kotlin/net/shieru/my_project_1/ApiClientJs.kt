@file:OptIn(ExperimentalJsExport::class, DelicateCoroutinesApi::class, ExperimentalJsCollectionsApi::class)
package net.shieru.my_project_1

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.Promise
import kotlin.js.collections.JsReadonlyArray

@JsExport
fun fetchHello(): Promise<String> = GlobalScope.promise {
    fetchHelloSuspend()
}

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
    return when (val validationResult = validateToDo(todo)) {
        is EmptyContentError -> JsValidationResult(false, "内容を入力してください")
        is NegativeIdError -> JsValidationResult(false, "IDは0以上の整数でなければなりません")
        is NameTooLongError -> JsValidationResult(false, "名前が長すぎます。${validationResult.maxLength}文字以下にしてください。")
        is Valid -> JsValidationResult(true)
    }
}
