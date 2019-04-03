package ru.prudekb.drama

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import java.time.Duration
import java.time.LocalDateTime

/**
 * Выполняет вызовы запросов с учётом определённого количества запросов в секунду.
 * Если запросов в секунду делается больше, чем допущено значением maxCallsPerSec, то останавливает поток на оставшееся количество милисекунд.
 */
class ApiMethodInvoker {

    /** Самый ранний вызов. */
    var earliestCall = LocalDateTime.now()
    /** Самый поздний вызов. */
    var latestCall = LocalDateTime.now()
    /** Осталось вызовов в данный промежуток времени. */
    var callsLeft = maxCallsPerSec

    fun invoke(request: String): String {
        latestCall = LocalDateTime.now()
        pauseIfNeeded()
        return runWithApache(request)
    }

    /** Если вызовы израсходованы до истечения времени, ставит поток на паузу. */
    private fun pauseIfNeeded() {
        val betweenMilliseconds = Duration.between(earliestCall, latestCall).nano / 1000000
        if (callsLeft == 0) {
            if (betweenMilliseconds < 1000)
                Thread.sleep((1000 - betweenMilliseconds).toLong())
            // сброс значений
            callsLeft = maxCallsPerSec
            earliestCall = LocalDateTime.now()
            latestCall = LocalDateTime.now()
        }
        callsLeft--
    }

    private fun runWithApache(request: String): String {
        val httpclient = HttpClients.createDefault()
        val httpGet = HttpGet(request)
        httpclient.execute(httpGet).use { response1 ->
            val entity1 = response1.entity
            return EntityUtils.toString(entity1)
        }
    }


}

const val maxCallsPerSec = 3 // установлено опытным путём, что с 3 шт. не валится
