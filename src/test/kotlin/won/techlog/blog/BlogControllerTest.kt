package won.techlog.blog

import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import won.techlog.blog.api.request.BlogRequest
import won.techlog.blog.api.response.BlogResponse
import won.techlog.poster.api.response.PosterResponse
import won.techlog.support.BaseControllerTest

class BlogControllerTest: BaseControllerTest() {

    @Test
    fun `우아한형제들 블로그 글을 찾는다`() {
        // given
        val request = BlogRequest("https://techblog.woowahan.com/?paged=1")
        val result = RestAssured.given().log().all()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .`when`().post("/api/blogs")
            .then().log().all()
            .extract()

        // when
        println(result)

        // then
    }

    @Test
    fun `우아한형제들 블로그 글을 파싱한다`() {
        // given
        val request = BlogRequest("https://techblog.woowahan.com/21604/")
        val response = RestAssured.given().log().all()
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
