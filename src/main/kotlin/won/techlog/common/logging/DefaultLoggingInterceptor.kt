package won.techlog.common.logging

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingRequestWrapper
import java.io.Serializable
import java.util.UUID

@Component
class DefaultLoggingInterceptor : BaseLoggingInterceptor() {
    private val logger = KotlinLogging.logger {}

    companion object {
        const val IGNORE_URI = "/health"
        const val REQUEST_ID = "requestId"
        const val REQUEST_TIME = "requestTime"
    }

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        if (shouldIgnore(request)) return true
        MDC.put(REQUEST_ID, UUID.randomUUID().toString().substring(0, 8))
        MDC.put(REQUEST_TIME, System.currentTimeMillis().toString())

        val requestId = MDC.get(REQUEST_ID) ?: "N/A"
        val originalUri = request.requestURI
        val headers = extractHeaders(request)
        val body = extractRequestBody(request)

        logger.info {
            """{"type":"REQUEST ", "requestId":"$requestId", "method":"${request.method}", "uri":"$originalUri${
                getRequestParameters(
                    request
                )
            }", "body":$body}"""
        }
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        if (shouldIgnore(request)) return

        val requestId = MDC.get(REQUEST_ID) ?: "N/A"
        val originalUri =
            (request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI) as? String)
                ?: request.requestURI
        val duration = calculateDuration()
        val statusText = getStatusText(response.status)
        val body = extractRequestBody(request)

        logger.info {
            """{"type":"RESPONSE", "requestId":"$requestId", "method":"${request.method}", "uri":"$originalUri${
                getRequestParameters(
                    request
                )
            }", "status":"$statusText", "statusCode":${response.status}, "duration":$duration, "body":$body}"""
        }
    }

    private fun shouldIgnore(request: HttpServletRequest): Boolean {
        return request.method.equals("OPTIONS", ignoreCase = true) ||
            request.requestURI.contains(IGNORE_URI)
    }

    private fun calculateDuration(): Long {
        val startTime = MDC.get(REQUEST_TIME)?.toLongOrNull() ?: return -1
        return System.currentTimeMillis() - startTime
    }

    private fun getStatusText(statusCode: Int): String {
        return when (statusCode / 100) {
            2, 3 -> "SUCCESS ✅"
            else -> "FAIL ❌"
        }
    }

    private fun extractHeaders(request: HttpServletRequest): Map<String, String> {
        return request.headerNames?.toList()
            ?.associateWith { request.getHeader(it) }
            ?: emptyMap()
    }

    private fun extractRequestBody(request: HttpServletRequest): Serializable {
        return if (request is ContentCachingRequestWrapper) {
            try {
                java.lang.String(request.contentAsByteArray, request.characterEncoding ?: "UTF-8")
            } catch (e: Exception) {
                "[Body 조회를 실패했습니다: ${e.message}]"
            }
        } else {
            "[Request가 ContentCachingRequestWrapper로 변환되지 않았습니다]"
        }
    }

    private fun getRequestParameters(request: HttpServletRequest): String {
        val params = request.parameterNames.toList()
        return if (params.isEmpty()) "" else "?" + params.joinToString("&") { "$it=${request.getParameter(it)}" }
    }
}
