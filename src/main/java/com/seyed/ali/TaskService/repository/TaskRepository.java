package com.seyed.ali.TaskService.repository;

import com.seyed.ali.TaskService.model.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findByProjectIdOrderByTaskOrderAsc(String projectId);

    // Custom query to find the maximum task order for a given project ID
    @Query("""
            SELECT MAX(t.taskOrder)
            FROM Task t
            WHERE t.projectId = :projectId
            """)
    Integer findMaxTaskOrderByProjectId(String projectId);

    boolean existsByProjectIdAndTaskOrder(String projectId, Integer taskOrder);

}