@file:OptIn(ExperimentalJsExport::class)

package net.shieru.my_project_1

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@Serializable
@JsExport
data class ToDoCreationError(val message: String)