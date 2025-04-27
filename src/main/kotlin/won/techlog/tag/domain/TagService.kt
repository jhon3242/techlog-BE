package won.techlog.tag.domain

import org.springframework.stereotype.Service

@Service
class TagService(
    private val tagDao: TagDao
) {
    fun save(name: String): Tag {
        return tagDao.save(name)
    }

    fun findAll(): List<Tag> = tagDao.findAll()
}
