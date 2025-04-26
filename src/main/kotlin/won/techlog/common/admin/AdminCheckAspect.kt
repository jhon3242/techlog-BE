package won.techlog.common.admin

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

private const val ADMIN_HEADER = "X-Admin-Header"

@Aspect
@Component
class AdminCheckAspect(
    @Value("\${admin.header}")
    private val adminHeaderKey: String
) {
    @Before("@annotation(AdminCheck)")
    fun adminCheck(joinPoint: JoinPoint) {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val headerValue = request.getHeader(ADMIN_HEADER) ?: throw IllegalArgumentException("Missing admin token header")

        if (headerValue != adminHeaderKey) {
            throw IllegalArgumentException("Invalid admin token")
        }
    }
}
