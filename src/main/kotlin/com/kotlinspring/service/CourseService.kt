package com.kotlinspring.service

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
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

}
