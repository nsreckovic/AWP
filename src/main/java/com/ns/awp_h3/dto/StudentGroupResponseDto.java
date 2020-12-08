package com.ns.awp_h3.dto;

import com.ns.awp_h3.models.StudentGroup;
import lombok.Data;

@Data
public class StudentGroupResponseDto {
    private int id;
    private String name;

    public StudentGroupResponseDto(StudentGroup studentGroup) {
        this.id = studentGroup.getId();
        this.name = studentGroup.getName();
    }
}
