package com.ead.course.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Entity
@Table(name = "TB_LESSONS")
public class LessonModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private UUID lessonId;

    @Column(nullable = false, length = 150)
    private  String title;

    @Column(nullable = false, length = 250)
    private String description;

    @Column(nullable = false)
    private  String videoUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "dd-MM-YYYY HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime creationDate;

}