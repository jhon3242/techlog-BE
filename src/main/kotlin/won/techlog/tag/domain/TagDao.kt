package won.techlog.tag.domain

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TagDao(
    private val tagRepository: TagRepository
) {
    @Transactional
    fun save(tag: Tag): Tag
    = tagRepository.findByName(tag.name)
        ?: tagRepository.save(tag)

    @Transactional
    fun save(name: String): Tag
        = tagRepository.findByName(name)
        ?: tagRepository.save(Tag(name = name))

    @Transactional(readOnly = true)
    fun findAll(): List<Tag> = tagRepository.findAll()

    @Transactional(readOnly = true)
    fun getByName(name: String): Tag
    = tagRepository.findByName(name)
        ?: throw IllegalArgumentException("일치하는 태그가 없습니다.")
}
