package won.techlog.support

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import won.techlog.poster.domain.PosterDao
import won.techlog.poster.domain.PosterTagDao
import won.techlog.tag.domain.TagDao

@ExtendWith(DatabaseCleanerExtension::class)
@DataJpaTest
@Import(value = [PosterDao::class, TagDao::class, PosterTagDao::class])
abstract class BaseDaoTest {
    @Autowired
    lateinit var posterTagDao: PosterTagDao

    @Autowired
    lateinit var posterDao: PosterDao

    @Autowired
    lateinit var tagDao: TagDao
}
