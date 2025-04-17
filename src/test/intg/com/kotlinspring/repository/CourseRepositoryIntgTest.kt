package com.kotlinspring.repository

import com.kotlinspring.util.courseEntityList
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIntgTest {

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        val courses = courseEntityList()
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
}