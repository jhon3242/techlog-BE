package won.techlog.blog.parser

import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import won.techlog.blog.api.request.BlogParseRequest
import won.techlog.blog.api.response.BlogResponse
import won.techlog.blog.domain.crawler.WoowabroBlogCrawler
import won.techlog.support.BaseControllerTest

class WoowabroBlogParserTest : BaseControllerTest() {
    @Autowired
    lateinit var woowabroBlogCrawler: WoowabroBlogCrawler

//    @Test
    fun `블로그 글을 찾는다`() {
        // given
        val url = "https://techblog.woowahan.com/?paged=1"
        val request = BlogParseRequest(url)
        val result =
            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(Companion.ADMIN_HEADER, adminHeaderKey)
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
//        val url = "https://techblog.woowahan.com/21905/"
        val url = "https://techblog.woowahan.com/13539/"
        val request = BlogParseRequest(url)
        val response =
            RestAssured.given().log().all()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(Companion.ADMIN_HEADER, adminHeaderKey)
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
    fun `이미지 url이 uri만 있는 블로그를 파싱한다`() {
        // given
        val url = "https://techblog.woowahan.com/13539/"
        val validUrlPrefix = "https://techblog.woowahan.com/wp-content/"

        // when
        val result = woowabroBlogCrawler.crawlBlog(url)

        // then
        Assertions.assertThat(result.thumbnailUrl?.startsWith(validUrlPrefix)).isTrue
    }

    @Test
    fun `이미지 url이 base url로 시작하는 블로그를 파싱한다`() {
        // given
        val url = "https://techblog.woowahan.com/22127/"
        val validUrlPrefix = "https://techblog.woowahan.com/wp-content/"

        // when
        val result = woowabroBlogCrawler.crawlBlog(url)

        // then
        Assertions.assertThat(result.thumbnailUrl?.startsWith(validUrlPrefix)).isTrue
    }
}
