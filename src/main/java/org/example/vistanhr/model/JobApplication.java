package org.example.vistanhr.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_REVIEWED = "REVIEWED";
    public static final String STATUS_INTERVIEW = "INTERVIEW";
    public static final String STATUS_ACCEPTED = "ACCEPTED";
    public static final String STATUS_REJECTED = "REJECTED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancy_id", nullable = false)
    private Vacancy vacancy;

    @NotBlank(message = "ФИО обязательно")
    @Size(min = 2, max = 100)
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Email(message = "Неверный формат email")
    @NotBlank(message = "Email обязателен")
    @Column(nullable = false)
    private String email;

    @Pattern(regexp = "^\\+?[0-9\\s\\-\\(\\)]{7,20}$", message = "Неверный формат телефона")
    @Column(nullable = false)
    private String phone;

    @Column(length = 1000)
    private String coverLetter;


    @Column(columnDefinition = "TEXT")
    private String resume;

    private Integer experience;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(name = "expected_salary")
    private Double expectedSalary;

    @Column(name = "application_date")
    private LocalDateTime applicationDate;

    @Column(name = "application_status")
    private String applicationStatus = STATUS_PENDING;

    @Column(name = "review_notes", length = 1000)
    private String reviewNotes;

}