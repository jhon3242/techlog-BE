package won.techlog.blog.parser

import io.restassured.RestAssured
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import won.techlog.blog.api.request.BlogParseRequest
import won.techlog.blog.api.response.BlogResponse
import won.techlog.support.BaseControllerTest

class NaverBlogParserTest : BaseControllerTest() {
//    @Test
    fun `블로그 글 리스트를 파싱한다`() {
        // given
        val url = "https://d2.naver.com/helloworld?page=0"
        val request = BlogParseRequest(url)
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

//    @Test
    fun `블로그 글을 파싱한다`() {
        // given
        val url =
            "https://d2.naver.com/helloworld/1168674"
        val request = BlogParseRequest(url)
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
