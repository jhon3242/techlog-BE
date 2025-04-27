package won.techlog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TechlogApplication

fun main(args: Array<String>) {
    runApplication<TechlogApplication>(*args)
}
