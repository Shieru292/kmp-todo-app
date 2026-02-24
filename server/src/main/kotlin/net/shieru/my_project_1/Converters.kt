package net.shieru.my_project_1

fun ToDoEntity.toDomain() = ToDo(id.value, content)