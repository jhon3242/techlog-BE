package won.techlog.blog.client

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.blog.domain.client.infrastructure.line.LineOldWebClient
import won.techlog.blog.domain.client.infrastructure.line.LineWebClient
import won.techlog.support.BaseServiceTest

class LineClientTest: BaseServiceTest() {
    @Autowired
    lateinit var lineOldWebClient: LineOldWebClient

    @Autowired
    lateinit var lineWebClient: LineWebClient

//    @Test
    fun `과거 라인 블로그 리스트를 가져온다`() {
        runBlocking {
            // given
            // when
            val result = lineOldWebClient.fetchBlogs()

            println(result)

            // then
            Assertions.assertThat(result).isNotEmpty()
            Assertions.assertThat(result.get(0).title).isNotEmpty()
            Assertions.assertThat(result.get(0).content).isNotEmpty()
            Assertions.assertThat(result.get(0).url).isNotEmpty()
        }
    }

//    @Test
    fun `현재 라인 블로그 리스트를 가져온다`() {
        runBlocking {
            // given
            // when
            val result = lineWebClient.fetchBlogs()

            println(result.size)

            // then
            Assertions.assertThat(result).isNotEmpty()
            Assertions.assertThat(result.get(0).title).isNotEmpty()
            Assertions.assertThat(result.get(0).content).isNotEmpty()
            Assertions.assertThat(result.get(0).url).isNotEmpty()
        }
    }

    @Test
    fun `썸네일을 조회할 수 없는 경우 null이 들어간다`() {
        runBlocking {
            // given
            // when
            val result = lineWebClient.fetchBlog("techniques-for-improving-code-quality-4")

            println(result)

            // then
            Assertions.assertThat(result.title).isNotEmpty()
            Assertions.assertThat(result.url).isNotEmpty()
            Assertions.assertThat(result.content).isNotEmpty()
            Assertions.assertThat(result.thumbnailUrl).isNull()
        }
    }

}
