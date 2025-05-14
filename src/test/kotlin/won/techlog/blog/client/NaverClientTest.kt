package won.techlog.blog.client

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.blog.domain.client.infrastructure.naver.NaverWebClient
import won.techlog.support.BaseControllerTest

class NaverClientTest : BaseControllerTest() {
    @Autowired
    lateinit var naverWebClient: NaverWebClient

//    @Test
    fun `네이버 블로그를 가져온다`() {
        runBlocking {
            // given
            val url = "https://d2.naver.com/helloworld/977"

            // when
            val result = naverWebClient.fetchBlog(url)

            // then
            println(result)
            Assertions.assertThat(result.url).isNotEmpty()
            Assertions.assertThat(result.content).isNotEmpty()
            Assertions.assertThat(result.title).isNotEmpty()
        }
    }

//    @Test
    fun `네이버 블로그 리스트를 가져온다`() {
        runBlocking {
            // given
            // when
            val result = naverWebClient.fetchBlogs()

            println(result)

            // then
            Assertions.assertThat(result).isNotEmpty()
            Assertions.assertThat(result.get(0).title).isNotEmpty()
            Assertions.assertThat(result.get(0).content).isNotEmpty()
            Assertions.assertThat(result.get(0).url).isNotEmpty()
        }
    }
}
