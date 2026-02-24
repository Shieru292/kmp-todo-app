package net.shieru.my_project_1

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object ToDos : IntIdTable("todos") {
    val content = varchar("content", 50)
}

class ToDoEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ToDoEntity>(ToDos)

    var content by ToDos.content
}