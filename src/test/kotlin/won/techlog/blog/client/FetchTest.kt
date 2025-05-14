package won.techlog.blog.client

import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import won.techlog.blog.api.request.BlogFetchRequest
import won.techlog.support.BaseControllerTest

class FetchTest: BaseControllerTest() {
    @Test
    fun `블로그를 파싱하고 저장한다`() {
        // given
        val url = "https://tech.kakao.com/api/v1/posts/703"
        val request = BlogFetchRequest(url = url)

        // when
        RestAssured.given().log().all()
            .body(request)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header(ADMIN_HEADER, adminHeaderKey)
            .`when`().post("/api/blog/fetch")
            .then().log().all()
            .statusCode(201)

        // then
        val result = posterDao.getAllPosters()
        Assertions.assertThat(result).isNotEmpty()
    }
}
