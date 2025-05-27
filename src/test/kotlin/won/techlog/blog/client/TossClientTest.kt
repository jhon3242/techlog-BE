package won.techlog.blog.client

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.blog.domain.client.company.toss.TossWebClient
import won.techlog.support.BaseControllerTest

class TossClientTest : BaseControllerTest() {
    @Autowired
    lateinit var tossWebClient: TossWebClient

//    @Test
    fun `블로그 전체를 가져온다`() {
        runBlocking {
            // given
            val result = tossWebClient.fetchBlogs()

            // when
            println(result)
            // then
            Assertions.assertThat(result).isNotEmpty()
            Assertions.assertThat(result.get(0).title).isNotBlank()
            Assertions.assertThat(result.get(0).content).isNotBlank()
            Assertions.assertThat(result.get(0).url).isNotBlank()
        }
    }
}
