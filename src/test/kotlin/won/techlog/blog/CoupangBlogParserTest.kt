package won.techlog.blog

import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import won.techlog.blog.api.request.BlogRequest
import won.techlog.blog.api.response.BlogResponse
import won.techlog.support.BaseControllerTest

class CoupangBlogParserTest: BaseControllerTest() {

    @Test
    fun `블로그 글을 파싱한다`() {
        // given
        val url = "https://medium.com/coupang-engineering/%EA%B8%B0%EA%B3%84-%ED%95%99%" +
            "EC%8A%B5-%EB%AA%A8%EB%8D%B8%EC%9D%84-%ED%99%9C%EC%9A%A9%ED%95%9C-%EB%" +
            "AC%BC%EB%A5%98-%EC%9E%85%EA%B3%A0-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4-%EC%B5" +
            "%9C%EC%A0%81%ED%99%94-fe4490e44514"
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
