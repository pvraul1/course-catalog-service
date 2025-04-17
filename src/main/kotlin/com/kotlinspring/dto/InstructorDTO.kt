package com.kotlinspring.dto

import jakarta.validation.constraints.NotNull

data class InstructorDTO(
    val id: Int?,

    @get:NotNull(message = "instructorDTO.name cannot be blank")
    var name: String
)