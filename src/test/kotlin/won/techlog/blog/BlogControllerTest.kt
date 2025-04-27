package won.techlog.blog

import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import won.techlog.blog.api.request.BlogRequest
import won.techlog.blog.api.response.BlogResponse
import won.techlog.support.BaseControllerTest

class BlogControllerTest : BaseControllerTest() {
    //    @ParameterizedTest
//    @ValueSource(strings = ["https://techblog.woowahan.com/?paged=1", "https://d2.naver.com/helloworld?page=0"])
    fun `블로그 글을 찾는다`(url: String) {
        // given
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

//    @ParameterizedTest
//    @ValueSource(strings = ["https://techblog.woowahan.com/21604/", "https://d2.naver.com/helloworld/1168674"])
    fun `블로그 글을 파싱한다`(url: String) {
        // given
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
        Assertions.assertThat(response.thumbnail).isNotEmpty()
        Assertions.assertThat(response.content).isNotEmpty()
        Assertions.assertThat(response.blogType).isNotEmpty()
    }
}
