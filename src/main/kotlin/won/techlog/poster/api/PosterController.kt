package won.techlog.poster.api

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import won.techlog.poster.api.request.PosterCreateRequest
import won.techlog.poster.api.response.PosterResponse
import won.techlog.poster.domain.PosterService

@RestController
@RequestMapping("/api")
class PosterController(
    private val posterService: PosterService
) {
    @PostMapping("/poster")
    fun createPoster(@RequestBody request: PosterCreateRequest): PosterResponse {
        return PosterResponse(posterService.createPoster(request.toPoster()))
    }

    @GetMapping("/posters")
    fun getPosters() : List<PosterResponse>
    = posterService.getPosters()
        .map { PosterResponse(it) }

    @GetMapping("/posters/{id}")
    fun getPoster(@PathVariable id: Long) : PosterResponse
    = PosterResponse(posterService.getPoster(id))

    @DeleteMapping("posters/{id}")
    fun deletePoster(@PathVariable id: Long)
    = posterService.deletePoster(id)
}
