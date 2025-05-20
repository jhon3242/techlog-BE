package won.techlog.common.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {
    @GetMapping("/check/health")
    fun healthCheck(): String {
        return "I'm OK"
    }
}
