package com.seyed.ali.taskservice.service;

import com.seyed.ali.taskservice.client.ProjectServiceClient;
import com.seyed.ali.taskservice.exceptions.OperationNotSupportedException;
import com.seyed.ali.taskservice.exceptions.ResourceNotFoundException;
import com.seyed.ali.taskservice.model.domain.Task;
import com.seyed.ali.taskservice.repository.TaskRepository;
import com.seyed.ali.taskservice.service.interfaces.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectServiceClient projectServiceClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public Task createTask(Task task) {
        task.setTaskId(UUID.randomUUID().toString());

        // Validate projectId existence
        if (this.projectServiceClient.validateProjectsExistence(task.getProjectId()))
            task.setProjectId(task.getProjectId());

        // If taskOrder is not provided, assign it based on the current maximum order for the project
        if (task.getTaskOrder() == null) {
            Integer maxOrder = this.taskRepository.findMaxTaskOrderByProjectId(task.getProjectId());
            int nextOrder = (maxOrder != null) ? maxOrder + 1 : 1;  // If maxOrder is null, set nextOrder as 1
            task.setTaskOrder(nextOrder);
        } else {
            // Check if the specified task order already exists for the project
            boolean orderExists = this.taskRepository.existsByProjectIdAndTaskOrder(task.getProjectId(), task.getTaskOrder());
            if (orderExists)
                throw new OperationNotSupportedException("Task order " + task.getTaskOrder() + " already exists for the project.");
        }
        return this.taskRepository.save(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> findAllTasksForProject(String projectId) {
        // Validate projectId existence
        this.projectServiceClient.validateProjectsExistence(projectId);

        // Fetch tasks for the project ordered by task order
        return taskRepository.findByProjectIdOrderByTaskOrderAsc(projectId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTask(Task task) throws ResourceNotFoundException {
        Task foundTask = this.taskRepository.findById(task.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + task.getTaskId() + " not found."));
        this.taskRepository.delete(foundTask);
    }

}
