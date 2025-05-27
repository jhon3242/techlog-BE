package won.techlog.tag.domain

import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long> {
    fun findByNameAndIsDeletedFalse(name: String): Tag?

    fun findByName(name: String): Tag?
}
