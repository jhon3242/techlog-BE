package won.techlog.tag.domain

import org.springframework.data.repository.CrudRepository

interface TagRepository : CrudRepository<Tag, Long> {
    fun findByNameAndIsDeletedFalse(name: String): Tag?

    fun findByName(name: String): Tag?
}
