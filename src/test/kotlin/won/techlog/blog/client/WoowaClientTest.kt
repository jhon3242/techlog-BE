package won.techlog.blog.client

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.blog.domain.client.company.woowabro.WoowabroWebClient
import won.techlog.support.BaseServiceTest

class WoowaClientTest : BaseServiceTest() {
    @Autowired
    lateinit var woowabroWebClient: WoowabroWebClient

//    @Test
    fun `우형 블로그를 가져온다`() {
        runBlocking {
            // given
            val url = "https://techblog.woowahan.com/21905/"

            // when
            val result = woowabroWebClient.fetchBlog(url)

            // then
            println(result)
            Assertions.assertThat(result.url).isNotEmpty()
            Assertions.assertThat(result.content).isNotEmpty()
            Assertions.assertThat(result.title).isNotEmpty()
        }
    }

//    @Test
    fun `우형 블로그 리스트를 가져온다`() {
        runBlocking {
            // given
            // when
            val result = woowabroWebClient.fetchBlogs()

            println(result)
            println(result.size)

            // then
            Assertions.assertThat(result).isNotEmpty()
            Assertions.assertThat(result.get(0).title).isNotEmpty()
            Assertions.assertThat(result.get(0).content).isNotEmpty()
            Assertions.assertThat(result.get(0).url).isNotEmpty()
        }
    }
}
