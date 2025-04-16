package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.service.CourseService
import com.kotlinspring.util.courseDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.assertTrue

@WebMvcTest(controllers = [CourseController::class])
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMock: CourseService

    @Test
    fun addCourse() {
        val courseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin", "Development")

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1)

        val savedCourseDTO = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertTrue {
            savedCourseDTO!!.id != null
        }

    }

    @Test
    fun retrieveAllCourses() {
        every { courseServiceMock.retrieveAllCourses() }.returnsMany(
            listOf(
                courseDTO(id = 1),
                courseDTO(id = 2, name = "Build Reactive Microservices using Spring WebFlux/SpringBoot"),
                courseDTO(id = 3, name = "Wiremock for Java Developers")
            )
        )

        val courses = webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("courses: $courses")

        assertTrue {
            courses!!.size == 3
        }
    }

}