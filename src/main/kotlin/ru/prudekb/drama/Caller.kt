package ru.prudekb.drama

import java.io.FileReader

// "https://oauth.vk.com/authorize?client_id=[ИД_ПРИЛОЖЕНИЯ]&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=groups&response_type=token&v=5.92"
private val accessToken = "_________"
val apiMethodInvoker = ApiMethodInvoker()

fun main() {
    println("Начинается исполнение запросов")

    // 20 штук тренировочных, чтобы убедиться, что ни одной ошибки не выпадает.
    for (i in 1..20) {
        println(usersGet("neverione"))
        println(usersGet("234"))
    }

//    val lines = FileReader("D:/Загрузки/bots.txt").readLines()
//    lines.forEach { id -> println(groupsRemoveUser(156077137, id.toLong())) }

}

private fun usersGet(userIds: String): String {
    return apiMethodInvoker.invoke(
        "https://api.vk.com/method/" +
                "users.get?" +
                "user_ids=$userIds" +
                "&v=5.92" +
                "&access_token=$accessToken"
    )
}


private fun groupsRemoveUser(groupId: Long, userId: Long): String {
    return apiMethodInvoker.invoke(
        "https://api.vk.com/method/" +
                "groups.removeUser?" +
                "group_id=$groupId" +
                "&user_id=$userId" +
                "&v=5.92" +
                "&access_token=$accessToken"
    )
}
