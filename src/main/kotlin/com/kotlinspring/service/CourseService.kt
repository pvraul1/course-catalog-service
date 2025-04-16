package com.kotlinspring.service

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.exception.CourseNotFoundException
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(val courseRespository: CourseRepository) {

    companion object: KLogging()

    fun addCourse(courseDTO: CourseDTO): CourseDTO {
        val courseEntity = courseDTO.let {
            // Logic to convert CourseDTO to Course entity
            Course(null, it.name, it.category)
        }

        // Logic to save course entity to the database
        courseRespository.save(courseEntity)

        logger.info { "Saved course is: $courseEntity" }

        return courseEntity.let {
            // Logic to convert Course entity back to CourseDTO
            CourseDTO(it.id, it.name, it.category)
        }
    }

    fun retrieveAllCourses(): List<CourseDTO> {
        return courseRespository.findAll()
            .map {
                // Logic to convert Course entity to CourseDTO
                CourseDTO(it.id, it.name, it.category)
            }
    }

    fun updateCourse(courseId: Int, courseDTO: CourseDTO): CourseDTO {
        val existingCourse = courseRespository.findById(courseId)
        return if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    it.name = courseDTO.name
                    it.category = courseDTO.category

                    // Logic to save updated course entity to the database
                    courseRespository.save(it)

                    // Logic to convert Course entity back to CourseDTO
                    CourseDTO(it.id, it.name, it.category)
                }
        } else {
            throw CourseNotFoundException("Not course found for the passed id: $courseId")
        }
    }

    fun deleteCourse(courseId: Int) {
        val existingCourse = courseRespository.findById(courseId)
        if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    // Logic to delete course entity from the database
                    courseRespository.deleteById(courseId)
                }
        } else {
            throw CourseNotFoundException("Not course found for the passed id: $courseId")
        }
    }

}
