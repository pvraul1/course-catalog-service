package com.kotlinspring.service

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.exception.CourseNotFoundException
import com.kotlinspring.exception.InstructorNotValidException
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(val courseRespository: CourseRepository,
    val instructorService: InstructorService) {

    companion object: KLogging()

    fun addCourse(courseDTO: CourseDTO): CourseDTO {
        val instructorOptional = instructorService.findByInstructorId(courseDTO.instructorId!!)
        if (instructorOptional.isEmpty) {
            throw InstructorNotValidException("Instructor not found for the passed id: ${courseDTO.instructorId}")
        }

        val courseEntity = courseDTO.let {
            // Logic to convert CourseDTO to Course entity
            Course(null, it.name, it.category, instructorOptional.get())
        }

        // Logic to save course entity to the database
        courseRespository.save(courseEntity)

        logger.info { "Saved course is: $courseEntity" }

        return courseEntity.let {
            // Logic to convert Course entity back to CourseDTO
            CourseDTO(it.id, it.name, it.category, it.instructor!!.id)
        }
    }

    fun retrieveAllCourses(courseName: String?): List<CourseDTO> {
        val courses = courseName?.let {
            courseRespository.findCoursesByName(courseName)
        } ?: courseRespository.findAll()

        return courses
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
