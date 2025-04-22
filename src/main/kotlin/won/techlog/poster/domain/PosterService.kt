package won.techlog.poster.domain

import org.springframework.stereotype.Service

@Service
class PosterService(
    private val posterDao: PosterDao
) {
    fun createPoster(poster: Poster): Poster
        = posterDao.savePoster(poster)

    fun createPosters(posters: List<Poster>): List<Poster>
        = posters.map { createPoster(it) }

    fun getPoster(id: Long): Poster
        = posterDao.getPoster(id)

    fun getPosters(): List<Poster>
        = posterDao.getAllPosters()

    fun deletePoster(id: Long)
        = posterDao.deletePoster(id)

    fun recommend(id: Long) {
        posterDao.recommend(id)
    }

    fun increaseView(id: Long) {
        posterDao.increaseView(id)
    }
}
