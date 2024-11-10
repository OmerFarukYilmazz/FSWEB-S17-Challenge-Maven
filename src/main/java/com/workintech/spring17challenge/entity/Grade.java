package com.workintech.spring17challenge.entity;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Grade {
    private int coefficient;
    private String note;

}
