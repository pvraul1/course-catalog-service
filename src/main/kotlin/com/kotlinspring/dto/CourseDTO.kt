package com.kotlinspring.dto

import jakarta.validation.constraints.NotBlank

data class CourseDTO(
    val id: Int?,

    @get:NotBlank(message = "courseDTO.name cannot be blank")
    val name: String,

    @get:NotBlank(message = "courseDTO.category cannot be blank")
    val category: String
)