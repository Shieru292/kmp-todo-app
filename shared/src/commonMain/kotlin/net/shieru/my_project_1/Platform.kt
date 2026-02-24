package net.shieru.my_project_1

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform