package won.techlog.blog.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import won.techlog.blog.api.request.BlogRecommendationRequest
import won.techlog.blog.api.response.BlogRecommendationResponse
import won.techlog.blog.domain.BlogService
import won.techlog.common.admin.AdminCheck

@RestController
@RequestMapping("/api/blogs/recommendations")
class BlogRecommendController(
    private val blogService: BlogService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun recommendBlog(
        @RequestBody blogRecommendationRequest: BlogRecommendationRequest
    ): BlogRecommendationResponse =
        blogService.saveBlogRecommendation(blogRecommendationRequest.url)
            .let { BlogRecommendationResponse(it) }

    @AdminCheck
    @GetMapping
    fun getBlogRecommendations(): List<BlogRecommendationResponse> =
        blogService.findAllBlogRecommendations()
            .map { BlogRecommendationResponse(it) }

    @AdminCheck
    @DeleteMapping("/{id}")
    fun deleteBlogRecommendation(
        @PathVariable id: Long
    ) {
        blogService.deleteBlogRecommendation(id)
    }
}
