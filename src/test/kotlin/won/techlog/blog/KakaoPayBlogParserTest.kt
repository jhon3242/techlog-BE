package won.techlog.blog

import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import won.techlog.blog.api.request.BlogRequest
import won.techlog.blog.api.response.BlogResponse
import won.techlog.support.BaseControllerTest

class KakaoPayBlogParserTest : BaseControllerTest() {
    @Test
    fun `블로그 글 리스트를 파싱한다`() {
        // given
        val url = "https://tech.kakaopay.com/page/1/"
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
}
