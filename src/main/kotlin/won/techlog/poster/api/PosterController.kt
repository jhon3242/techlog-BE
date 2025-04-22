package won.techlog.poster.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import won.techlog.poster.api.request.PosterCreateRequest
import won.techlog.poster.api.request.PostersCreateRequest
import won.techlog.poster.api.response.PosterResponse
import won.techlog.poster.api.response.PostersResponse
import won.techlog.poster.domain.PosterService

@RestController
@RequestMapping("/api")
class PosterController(
    private val posterService: PosterService
) {
    @PostMapping("/poster")
    @ResponseStatus(HttpStatus.CREATED)
    fun createPoster(@RequestBody request: PosterCreateRequest): PosterResponse {
        val poster = posterService.createPoster(request.toPoster())
        return PosterResponse(poster)
    }

    @PostMapping("/posters")
    @ResponseStatus(HttpStatus.CREATED)
    fun createPosters(@RequestBody requests: PostersCreateRequest): PostersResponse {
        val posters = posterService.createPosters(requests.toPosters())
        return PostersResponse(posters.map { PosterResponse(it) })
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

    @PutMapping("/posters/{id}/recommend")
    fun recommend(@PathVariable id: Long) {
        posterService.recommend(id)
    }

    @PutMapping("/posters/{id}/view")
    fun increaseView(@PathVariable id: Long) {
        posterService.increaseView(id)
    }
}
