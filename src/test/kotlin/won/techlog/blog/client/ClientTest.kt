package won.techlog.blog.client

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.blog.domain.client.company.nhn.NhnWebClient
import won.techlog.support.BaseServiceTest

class ClientTest: BaseServiceTest() {
    @Autowired
    lateinit var client: NhnWebClient

    @Test
    fun `블로그를 전부 가져온다`() {
        runBlocking {
            // given
            val result = client.fetchBlogs()

            // when
            TODO()

            // then

        }
    }
}
