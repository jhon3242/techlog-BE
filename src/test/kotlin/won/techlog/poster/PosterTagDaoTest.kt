package won.techlog.poster

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import won.techlog.support.BaseDaoTest
import won.techlog.support.fixture.PosterFixture
import won.techlog.support.fixture.TagFixture

class PosterTagDaoTest : BaseDaoTest() {
    @Test
    fun `태그를 추가한다`() {
        // given
        val poster = posterDao.save(PosterFixture.create())
        val tag = tagDao.save(TagFixture.create(name = "redis"))
        posterTagDao.save(poster = poster, tag = tag)

        // when
        val findTags = posterTagDao.findTags(poster)

        // then
        Assertions.assertThat(findTags).containsExactly(tag)
    }

    @Test
    fun `태그를 새롭게 수정한다`() {
        // given
        val poster = posterDao.save(PosterFixture.create())
        val tag = tagDao.save(TagFixture.create(name = "redis"))
        val newTagA = tagDao.save(TagFixture.create(name = "new tag a"))
        val newTagB = tagDao.save(TagFixture.create(name = "new tag b"))
        posterTagDao.save(poster = poster, tag = tag)

        // when
        posterTagDao.save(poster = poster, tags = listOf(newTagA, newTagB))
        val result = posterTagDao.findTags(poster)

        // then
        val expected = listOf(newTagA, newTagB)
        Assertions.assertThat(result).containsExactlyElementsOf(expected)
    }
}
