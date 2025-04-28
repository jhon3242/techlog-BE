package won.techlog.tag.domain

import org.springframework.stereotype.Service

@Service
class TagService(
    private val tagDao: TagDao
) {
    fun save(name: String): Tag = tagDao.save(name)

    fun findAll(): List<Tag> = tagDao.findAll()

    fun delete(name: String) = tagDao.delete(name)
}
