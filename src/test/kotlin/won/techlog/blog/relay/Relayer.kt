package won.techlog.blog.relay

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.relay.RelayService
import won.techlog.support.BaseServiceTest

class Relayer : BaseServiceTest() {
    @Autowired
    lateinit var relayService: RelayService

    @Test
    fun `모든 블로그를 릴레이한다`() {
        val blogs =
            listOf(
                BlogType.KURLY,
                BlogType.YEOGI
            )

        blogs.forEach { blog ->
            relayService.relayByBlogName(blog.name)
        }
    }

//    @Test
    fun `단일 블로그만 릴레이한다`() {
        val url = "https://techblog.woowahan.com/22043/"
        relayService.relayByUrl(url)
    }
}
