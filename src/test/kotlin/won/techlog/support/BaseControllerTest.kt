package won.techlog.support

import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import won.techlog.blog.domain.BlogRecommendationDao
import won.techlog.poster.domain.PosterDao
import won.techlog.tag.domain.TagDao

@ExtendWith(DatabaseCleanerExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
abstract class BaseControllerTest {
    @Autowired
    lateinit var posterDao: PosterDao

    @Autowired
    lateinit var tagDao: TagDao

    @Autowired
    lateinit var blogRecommendationDao: BlogRecommendationDao

    @LocalServerPort
    private val port: Int = 0

    @BeforeEach
    fun setPort() {
        RestAssured.port = port
    }
}
