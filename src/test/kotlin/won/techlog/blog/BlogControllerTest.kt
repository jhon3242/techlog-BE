package won.techlog.blog

import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import won.techlog.poster.api.response.PosterResponse
import won.techlog.support.BaseControllerTest

class BlogControllerTest: BaseControllerTest() {

    @Test
    fun `우아한형제들 블로그 글을 찾는다`() {
        // given
        val result = RestAssured.given().log().all()
            .param("url", "https://techblog.woowahan.com/?paged=1")
            .`when`().get("/api/blog/posters")
            .then().log().all()
            .extract()

        // when
        println(result)

        // then
    }

    @Test
    fun `우아한형제들 블로그 글을 파싱한다`() {
        // given
        val response = RestAssured.given().log().all()
            .param("url", "https://techblog.woowahan.com/21604/")
            .`when`().get("/api/blog/poster")
            .then().log().all()
            .extract().`as`(PosterResponse::class.java)

        // when & then
        Assertions.assertThat(response.url).isNotEmpty()
        Assertions.assertThat(response.title).isNotEmpty()
        Assertions.assertThat(response.thumbnail).isNotEmpty()
        Assertions.assertThat(response.content).isNotEmpty()
    }

}
