package won.techlog.support.fixture

import won.techlog.tag.domain.Tag

object TagFixture {
    fun createList(): List<String> = listOf("백엔드", "레디스")

    fun create(name: String = "백엔드"): Tag = Tag(name = name)
}
