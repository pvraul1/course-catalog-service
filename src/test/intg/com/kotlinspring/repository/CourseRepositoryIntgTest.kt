package com.kotlinspring.repository

import com.kotlinspring.util.courseEntityList
import com.kotlinspring.util.instructorEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIntgTest {

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()

        val instructor = instructorEntity()
        instructorRepository.save(instructor)

        val courses = courseEntityList(instructor)
        courseRepository.saveAll(courses)
    }

    @Test
    fun findByNameContaining() {
        // Given
        val courseName = "SpringBoot"
        val expectedCourseCount = 2

        // When
        val courses = courseRepository.findByNameContaining(courseName)
        println("courses: $courses")

        // Then
        assert(courses.size == expectedCourseCount)
    }

    @Test
    fun findCoursesByName() {
        // Given
        val courseName = "SpringBoot"
        val expectedCourseCount = 2

        // When
        val courses = courseRepository.findCoursesByName(courseName)
        println("courses: $courses")

        // Then
        assert(courses.size == expectedCourseCount)
    }

    @ParameterizedTest
    @MethodSource("courseAndSize")
    fun findCoursesByName_approach2(courseName: String, expectedCourseCount: Int) {
        // When
        val courses = courseRepository.findByNameContaining(courseName)
        println("courses: $courses")

        // Then
        assert(courses.size == expectedCourseCount)
    }

    companion object {
        @JvmStatic
        fun courseAndSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("SpringBoot", 2),
                Arguments.of("Wiremock", 1)
            )
        }
    }

}