package com.ead.course.controllers;

import com.ead.course.clients.CourseClient;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseUserService courseUserService;

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Page<UserDto>> getAllUsersCourse(@PageableDefault(
            page = 0, size = 10,sort = "userId", direction = Sort.Direction.ASC) Pageable pageable ,
                                                           @PathVariable(value = "courseId") UUID courseId) {

        var usersCourse = courseClient.getAllUsersByCourse(courseId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(usersCourse);

    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscripitionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
                                                                @RequestBody @Valid SubscriptionDto subscripitionDto) {

        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }
        if(courseUserService.existsByCourseAndUserId(courseModelOptional.get() , subscripitionDto.getUserId())) {
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("Error : subscripition already exists!");

        }
        //verificar no authuser de o userId existe.
       CourseUserModel courseUserModel = courseUserService.save(
               courseModelOptional.get().convertToCourseUserModel(subscripitionDto.getUserId()));

        return ResponseEntity.status(HttpStatus.OK).body("Subscrition created successfully.");
    }

}
















