package won.techlog.tag.domain

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TagDao(
    private val tagRepository: TagRepository
) {
    @Transactional(readOnly = true)
    fun findByName(name: String): Tag? = tagRepository.findByNameAndIsDeletedIsFalse(name)

    @Transactional
    fun save(tag: Tag): Tag =
        findByName(tag.name)
            ?: tagRepository.save(tag)

    @Transactional
    fun save(name: String): Tag =
        findByName(name)
            ?: tagRepository.save(Tag(name = name))

    @Transactional(readOnly = true)
    fun getByName(name: String): Tag =
        findByName(name)
            ?: throw IllegalArgumentException("일치하는 태그가 없습니다.")

    @Transactional(readOnly = true)
    fun findAll(): List<Tag> =
        tagRepository.findAll()
            .filter { !it.isDeleted }

    @Transactional(readOnly = true)
    fun findAllByNames(tagNames: List<String>): List<Tag> {
        return tagNames.map { getByName(it) }
            .toList()
    }

    @Transactional
    fun delete(name: String) {
        findByName(name)?.let { it.isDeleted = true }
    }
}
