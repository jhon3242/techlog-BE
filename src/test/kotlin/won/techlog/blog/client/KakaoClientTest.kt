package won.techlog.blog.client

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.blog.domain.client.infrastructure.kakao.KakaoWebClient
import won.techlog.support.BaseServiceTest

class KakaoClientTest : BaseServiceTest() {
    @Autowired
    lateinit var kakaoWebClient: KakaoWebClient

    @Test
    fun `카카오 블로그를 가져온다`() {
        runBlocking {
            // given
            val url = "https://tech.kakao.com/api/v1/posts/369"
//            val url = "https://tech.kakao.com/api/v1/posts/629"

            // when
            val result = kakaoWebClient.fetchBlog(url)
            println(result)
            // then
            Assertions.assertThat(result.url).isNotEmpty()
            Assertions.assertThat(result.title).isNotEmpty()
            Assertions.assertThat(result.content).isNotEmpty()
            Assertions.assertThat(result.thumbnailUrl).isNotEmpty()
        }
    }

//    @Test
    fun `모든 카카오 블로그 글을 가져온다`() {
        runBlocking {
            // given & when
            val result = kakaoWebClient.fetchBlogs()

            // then
            println(result)
            Assertions.assertThat(result.isNotEmpty())
        }
    }
}
