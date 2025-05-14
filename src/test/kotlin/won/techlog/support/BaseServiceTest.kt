package won.techlog.support

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import won.techlog.poster.domain.PosterDao
import won.techlog.poster.domain.PosterTagDao
import won.techlog.tag.domain.TagDao

@ExtendWith(DatabaseCleanerExtension::class)
@SpringBootTest
abstract class BaseServiceTest {
    @Autowired
    lateinit var posterDao: PosterDao

    @Autowired
    lateinit var tagDao: TagDao

    @Autowired
    lateinit var posterTagDao: PosterTagDao
}
