package com.seyed.ali.taskservice.util.converter;

import com.seyed.ali.taskservice.model.domain.Task;
import com.seyed.ali.taskservice.model.payload.TaskDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskConverter {

    private final TaskDTOToTaskConverter taskDTOToTaskConverter;
    private final TaskToTaskDTOConverter taskToTaskDTOConverter;

    public Task convertToTask(TaskDTO taskDTO) {
        return this.taskDTOToTaskConverter.convert(taskDTO);
    }

    public TaskDTO convertToTaskDTO(Task createdTask) {
        return this.taskToTaskDTOConverter.convert(createdTask);
    }

    public List<TaskDTO> convertTaskDTOList(List<Task> taskList) {
        List<TaskDTO> taskDTOList = new ArrayList<>();
        for (Task task : taskList)
            taskDTOList.add(this.convertToTaskDTO(task));
        return taskDTOList;
    }

}
