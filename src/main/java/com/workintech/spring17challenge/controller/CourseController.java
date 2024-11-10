package com.workintech.spring17challenge.controller;

import com.workintech.spring17challenge.dto.ApiResponse;
import com.workintech.spring17challenge.entity.Course;
import com.workintech.spring17challenge.entity.CourseGpa;
import com.workintech.spring17challenge.exceptions.ApiErrorResponse;
import com.workintech.spring17challenge.exceptions.ApiException;
import com.workintech.spring17challenge.validation.CourseValidation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseGpa lowCourseGpa;
    private final CourseGpa mediumCourseGpa;
    private final CourseGpa highCourseGpa;
    private List<Course> courses;

    public CourseController(@Qualifier("lowCourseGpa") CourseGpa lowCourseGpa,
                            @Qualifier("mediumCourseGpa") CourseGpa mediumCourseGpa,
                            @Qualifier("highCourseGpa") CourseGpa highCourseGpa) {
        this.lowCourseGpa = lowCourseGpa;
        this.mediumCourseGpa = mediumCourseGpa;
        this.highCourseGpa = highCourseGpa;
    }


    @PostConstruct
    public void init() {
        this.courses = new ArrayList<>();
    }

    @GetMapping
    public List<Course> getAll() {
        return this.courses;
    }


    @GetMapping("/{name}")
    public Course getByName(@PathVariable String name) {
        CourseValidation.checkName(name);
        return getExistingCourseByName(name);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addCourse(@RequestBody Course course){
        //checkCourseExistsByName(course.getName()); // this part is wrong
        CourseValidation.checkCredit(course.getCredit());
        CourseValidation.checkName(course.getName());

        courses.add(course);
        Integer totalGpa = getTotalGpa(course);
        System.out.println("Updated Total GPA: " + totalGpa);
        ApiResponse apiResponse = new ApiResponse(course, totalGpa);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCourse(@PathVariable Integer id,@RequestBody Course updatedcourse){
        CourseValidation.checkId(id);
        CourseValidation.checkCredit(updatedcourse.getCredit());
        CourseValidation.checkName(updatedcourse.getName());

        Course existingCourse = getExistingCourseById(id);
        int indexOfExisting = courses.indexOf(existingCourse);
        updatedcourse.setId(id);
        courses.set(indexOfExisting,updatedcourse);

        Integer totalGpa = getTotalGpa(updatedcourse);
        System.out.println("Updated Total GPA: " + totalGpa);
        ApiResponse apiResponse = new ApiResponse(courses.get(indexOfExisting), totalGpa);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id){
        //CourseValidation.checkId(id);

        Course existingCourse =  getExistingCourseById(id);
        courses.remove(existingCourse);
    }



    private Course getExistingCourseById(int id) {
        return courses.stream()
                .filter(course -> course.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ApiException("Course not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    private Course getExistingCourseByName(String name) {
        return courses.stream()
                .filter(course -> course.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new ApiException("Course not found with name: " + name,HttpStatus.NOT_FOUND));
    }

    private void checkCourseExistsByName(String name) {
        boolean exists = courses.stream()
                .anyMatch(course -> course.getName().equalsIgnoreCase(name));
        if (exists) {
            throw new ApiException("Course with name '" + name + "' already exists.",HttpStatus.NOT_FOUND);
        }
    }

    private Integer getTotalGpa(Course course){
        if(course.getCredit()<=2){
            return course.getGrade().getCoefficient() * course.getCredit() * lowCourseGpa.getGpa();
        }
        else if(course.getCredit()==3){
            return course.getGrade().getCoefficient() * course.getCredit() * mediumCourseGpa.getGpa();
        }
        else {
            return course.getGrade().getCoefficient() * course.getCredit() * highCourseGpa.getGpa();
        }
    }



}
