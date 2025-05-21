package won.techlog.blog.relay

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.blog.domain.relay.RelayService
import won.techlog.support.BaseServiceTest

class Relayer : BaseServiceTest() {
    @Autowired
    lateinit var relayService: RelayService

    @Test
    fun `릴레이`() {
        // given
        relayService.relay("WOOWABRO")

        // when

        // then
    }
}
