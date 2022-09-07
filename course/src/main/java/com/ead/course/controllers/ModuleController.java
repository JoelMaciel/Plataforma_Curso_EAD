package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ModuleService moduleService;

    @PostMapping("/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId") UUID courseId ,
                           @RequestBody @Valid ModuleDto moduleDto) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(!courseModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDto , moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(courseModelOptional.get());

        return  ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
    }

    @DeleteMapping("/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseId ,
                             @PathVariable(value = "moduleId") UUID moduleId) {
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(!moduleModelOptional.isPresent()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        }
        moduleService.delete(moduleModelOptional.get());

        return  ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully");

    }

    @PutMapping("/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId,
                                               @RequestBody @Valid ModuleDto moduleDto ) {
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(!moduleModelOptional.isPresent()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course");
        }
        var moduleModel = moduleModelOptional.get();
        moduleModel.setTitle(moduleDto.getTitle());
        moduleModel.setDescription(moduleDto.getDescription());

        moduleService.save(moduleModel);

        return ResponseEntity.status(HttpStatus.OK).body(moduleModel);

    }

    @GetMapping("/{courseId}/modules")
    public ResponseEntity<List<ModuleModel>> getAllModules(@PathVariable(value = "courseId") UUID courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findAllByCourse(courseId));
    }

    @GetMapping("/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable(value = "courseId") UUID courseId ,
                                               @PathVariable(value = "moduleId") UUID moduleId) {
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(!moduleModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course");
        }
        return ResponseEntity.status(HttpStatus.OK).body(moduleModelOptional.get());
    }
}


















