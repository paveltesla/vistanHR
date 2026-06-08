package org.example.vistanhr.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "vacancies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {
    @Enumerated(EnumType.STRING)
    @Column(name = "required_education")
    private EducationLevel requiredEducation = EducationLevel.NOT_SPECIFIED;

    public EducationLevel getRequiredEducation() {
        return requiredEducation;
    }
    public void setRequiredEducation(EducationLevel requiredEducation) {
        this.requiredEducation = requiredEducation;
    }

    public static final String STATUS_OPEN = "OPEN";
    public static final String STATUS_CLOSED = "CLOSED";
    public static final String STATUS_ON_HOLD = "ON_HOLD";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название обязательно")
    @Size(min = 2, max = 200)
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Отдел обязателен")
    @Column(nullable = false)
    private String department;

    @NotBlank(message = "Тип вакансии обязателен")
    @Column(name = "vacancy_type", nullable = false)
    private String vacancyType;

    @Column(length = 2000)
    private String description;

    @Column(length = 1000)
    private String requirements;

    @Column(length = 1000)
    private String responsibilities;

    @Column(name = "salary_min")
    private Double salaryMin;

    @Column(name = "salary_max")
    private Double salaryMax;

    @Column(name = "required_experience")
    private Integer requiredExperience;

    @Column(name = "work_schedule")
    private String workSchedule;

    @Column(name = "employment_type")
    private String employmentType;

    private String location;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "closing_date")
    private LocalDateTime closingDate;

    private String status = STATUS_OPEN;

    @Column(name = "applications_count")
    private Integer applicationsCount = 0;

    @Column(length = 500)
    private String notes;
}