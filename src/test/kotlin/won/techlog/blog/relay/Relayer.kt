package won.techlog.blog.relay

import org.springframework.beans.factory.annotation.Autowired
import won.techlog.blog.domain.relay.RelayService
import won.techlog.support.BaseServiceTest

class Relayer : BaseServiceTest() {
    @Autowired
    lateinit var relayService: RelayService

//    @Test
    fun `모든 블로그를 릴레이한다`() {
        relayService.relayAll("WOOWABRO")
    }

//    @Test
    fun `단일 블로그만 릴레이한다`() {
        val url = "https://techblog.woowahan.com/22043/"
        relayService.relay(url)
    }
}
