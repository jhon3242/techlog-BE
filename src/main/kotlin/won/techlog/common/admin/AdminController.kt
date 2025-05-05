package won.techlog.common.admin

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@AdminCheck
@RestController
@RequestMapping("/api/admin")
class AdminController {
    @GetMapping
    fun adminCheck(): String {
        return "OK"
    }
}
