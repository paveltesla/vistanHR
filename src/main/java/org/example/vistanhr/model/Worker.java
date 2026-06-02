package org.example.vistanhr.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workers")
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String profession;
    private String team;

    // Этот блок автоматически создаст вспомогательную таблицу в H2/PostgreSQL для хранения строк
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "worker_tasks", joinColumns = @JoinColumn(name = "worker_id"))
    @Column(name = "task_description")
    private List<String> assignedTasks = new ArrayList<>();

    // Геттеры, сеттеры, конструкторы...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }
    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }
    public List<String> getAssignedTasks() { return assignedTasks; }
    public void setAssignedTasks(List<String> assignedTasks) { this.assignedTasks = assignedTasks; }
}