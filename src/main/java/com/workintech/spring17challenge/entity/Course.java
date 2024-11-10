package com.workintech.spring17challenge.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Course {
    private Integer id;
    private String name;
    private Integer credit;
    private Grade grade;
}
