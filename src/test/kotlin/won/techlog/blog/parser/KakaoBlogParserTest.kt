package won.techlog.blog.parser

import io.restassured.RestAssured
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import won.techlog.blog.api.request.BlogRequest
import won.techlog.blog.api.response.BlogResponse
import won.techlog.blog.domain.parser.KakaoBlogAsyncParser
import won.techlog.support.BaseControllerTest

class KakaoBlogParserTest : BaseControllerTest() {
    @Autowired
    lateinit var kakaoBlogAsyncParser: KakaoBlogAsyncParser

    @Test
    fun `블로그 글 리스트를 파싱한다`() {
        // given
        val url = "https://tech.kakao.com/tag/tech"
        val request = BlogRequest(url)
        val result =
            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .`when`().post("/api/blogs")
                .then().log().all()
                .statusCode(200)
                .extract()
        println(result)
    }

    @Test
    fun `블로그 글을 파싱한다`() {
        // given
        val url = "https://tech.kakaopay.com/post/kakaopayins-opensearch-analyzer/"
        val request = BlogRequest(url)
        val response =
            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .`when`().post("/api/blog")
                .then().log().all()
                .extract().`as`(BlogResponse::class.java)

        // when & then
        Assertions.assertThat(response.url).isNotEmpty()
        Assertions.assertThat(response.title).isNotEmpty()
        Assertions.assertThat(response.content).isNotEmpty()
        Assertions.assertThat(response.blogType).isNotEmpty()
    }

    @Test
    fun `블로그 글 리스트를 비동기로 파싱한다`() {
        runBlocking {
            val parseBlogs = kakaoBlogAsyncParser.parseBlogs("https://tech.kakao.com/tag/tech")
            println(parseBlogs)
        }
    }
}
