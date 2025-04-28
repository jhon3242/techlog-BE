package won.techlog.tag.domain

import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long> {
    fun findByNameAndIsDeletedIsFalse(name: String): Tag?
}
