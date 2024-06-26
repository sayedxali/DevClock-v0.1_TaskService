package com.seyed.ali.taskservice.util.converter;

import com.seyed.ali.taskservice.model.domain.Task;
import com.seyed.ali.taskservice.model.payload.TaskDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskDTOToTaskConverter implements Converter<TaskDTO, Task> {

    @Override
    public Task convert(TaskDTO source) {
        return new Task(null, source.taskName(), source.taskDescription(), source.taskStatus(), source.taskOrder(), source.projectId());
    }

}
