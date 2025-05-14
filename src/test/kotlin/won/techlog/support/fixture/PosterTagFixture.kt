package won.techlog.support.fixture

import won.techlog.poster.domain.PosterTag

object PosterTagFixture {
    fun create(tagName: String): PosterTag {
        val tag = TagFixture.create(tagName)
        val poster = PosterFixture.create(title = "${tagName} title test", content = "${tagName} content test")
        return PosterTag(poster = poster, tag = tag)
    }
}
