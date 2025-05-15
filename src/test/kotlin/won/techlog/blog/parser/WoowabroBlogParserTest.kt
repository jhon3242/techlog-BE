package won.techlog.blog.parser

import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import won.techlog.blog.api.request.BlogParseRequest
import won.techlog.blog.api.response.BlogResponse
import won.techlog.support.BaseControllerTest

class WoowabroBlogParserTest : BaseControllerTest() {
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

//    @Test
    fun `블로그 글을 파싱한다`() {
        // given
        val url = "https://techblog.woowahan.com/21905/"
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
}
