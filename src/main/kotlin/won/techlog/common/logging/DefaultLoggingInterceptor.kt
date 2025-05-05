package won.techlog.common.logging

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingRequestWrapper

@Component
class DefaultLoggingInterceptor : BaseLoggingInterceptor() {
    private val logger = KotlinLogging.logger {}

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        if (isIgnore(request)) {
            return
        }
        val requestId = MDC.get(REQUEST_ID) ?: "N/A"
        val duration = System.currentTimeMillis() - MDC.get(REQUEST_TIME).toLong()
        val status =
            when (response.status / 100) {
                2, 3 -> "SUCCESS ✅"
                else -> "FAIL ❌"
            }

        // 요청 헤더
        val headers = request.headerNames?.toList()?.associateWith { request.getHeader(it) } ?: emptyMap()

        // 요청 바디
        val requestBody =
            if (request is ContentCachingRequestWrapper) {
                try {
                    java.lang.String(request.contentAsByteArray, request.characterEncoding ?: "UTF-8")
                } catch (e: Exception) {
                    "[Failed to read body: ${e.message}]"
                }
            } else {
                "[Request not wrapped. Cannot read body.]"
            }

        logger.info {
            """
            📦 RESPONSE $status [$requestId]
            ▶ URI: ${request.method} ${request.requestURI}${getParameters(request)}
            ▶ Status: [${response.status}]
            ▶ Duration: ${duration}ms
            ▶ Headers: $headers
            ▶ Body: $requestBody
            ▶ Exception: ${ex?.message}
            """.trimIndent()
        }
    }

    private fun isIgnore(request: HttpServletRequest): Boolean {
        return request.requestURI.contains(IGNORE_URI) || request.method == "OPTIONS"
    }

    private fun getParameters(request: HttpServletRequest): String {
        val parameters =
            request.parameterNames
                .toList()
        if (parameters.isEmpty()) {
            return ""
        }
        return "?" +
            parameters
                .joinToString("&") { "$it=${request.getParameter(it)}" }
    }
}
