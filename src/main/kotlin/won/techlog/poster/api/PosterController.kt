package won.techlog.poster.api

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import won.techlog.poster.api.response.PosterResponse
import won.techlog.poster.domain.PosterService

@RestController
@RequestMapping("/api/posters")
class PosterController(
    private val posterService: PosterService
) {
    @GetMapping
    fun getPosters() : List<PosterResponse>
    = posterService.getPosters()
        .map { PosterResponse(it) }

    @GetMapping("/{id}")
    fun getPoster(@PathVariable id: Long) : PosterResponse
    = PosterResponse(posterService.getPoster(id))

    @DeleteMapping("/{id}")
    fun deletePoster(@PathVariable id: Long)
    = posterService.deletePoster(id)
}
