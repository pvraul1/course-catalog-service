package com.kotlinspring.repository

import com.kotlinspring.entity.Course
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CourseRepository : CrudRepository<Course, Int> {

    fun findByNameContaining(courseName: String): List<Course>

    @Query("SELECT c.* FROM COURSES c WHERE c.name LIKE %?1%", nativeQuery = true)
    fun findCoursesByName(courseName: String): List<Course>

}