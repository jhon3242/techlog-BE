package won.techlog.blog.client

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.client.BlogApiManager
import won.techlog.support.BaseServiceTest

class BlogApiManagerTest: BaseServiceTest() {

    @Autowired
    lateinit var blogApiManager: BlogApiManager

    @Test
    fun `지원 불가능한 블로그를 확인한다`() {
        // given
        val url = BlogType.KAKAO_PAY.baseUrl

        // when
        val result = blogApiManager.canHandle(url)

        // then
        Assertions.assertThat(result).isFalse()
    }

    @Test
    fun `지원 가능한 블로그를 확인한다`() {
        // given
        val url = BlogType.NAVER.baseUrl

        // when
        val result = blogApiManager.canHandle(url)

        // then
        Assertions.assertThat(result).isTrue()
    }

}
