package won.techlog.blog.client

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.blog.domain.client.infrastructure.KakaoWebClient
import won.techlog.support.BaseServiceTest

class KakaoClientTest: BaseServiceTest() {
    @Autowired
    lateinit var kakaoWebClient: KakaoWebClient

    @Test
    fun `카카오 블로그를 가져온다`() {
        runBlocking {
            // given
            val url = "https://tech.kakao.com/api/v1/posts/306"

            // when
            val result = kakaoWebClient.fetchBlog(url)

            // then
            Assertions.assertThat(result.url).isNotEmpty()
            Assertions.assertThat(result.title).isNotEmpty()
            Assertions.assertThat(result.content).isNotEmpty()
            Assertions.assertThat(result.thumbnailUrl).isNotEmpty()
        }
    }
}
