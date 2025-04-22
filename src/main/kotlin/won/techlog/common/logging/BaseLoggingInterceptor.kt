package won.techlog.common.logging

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.servlet.HandlerInterceptor
import java.util.UUID

abstract class BaseLoggingInterceptor : HandlerInterceptor {
    companion object {
        const val REQUEST_ID = "requestId"
        const val REQUEST_TIME = "requestTime"
        const val IGNORE_URI = "/health"

    }

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        if (request.requestURI.contains(IGNORE_URI)) {
            return true
        }

        MDC.put(REQUEST_ID, UUID.randomUUID().toString().substring(0, 8))
        MDC.put(REQUEST_TIME, System.currentTimeMillis().toString())
        return true
    }
}
