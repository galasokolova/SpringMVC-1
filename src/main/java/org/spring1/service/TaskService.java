package org.spring1.service;

import jakarta.persistence.EntityNotFoundException;
import org.spring1.entity.Status;
import org.spring1.entity.Task;
import org.spring1.repository.TaskRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class TaskService {
    private final TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }
    public Task getById(int id){
        return taskRepo.getById(id);
    }

    public List<Task> getAllTasks(int offset, int count){
        return taskRepo.getAllTasks(offset,count);
    }

    public int getAllCount(){
        return taskRepo.getAllCount();
    }

    @Transactional
    public Task edit(int id, String description, Status status){
        Task task = taskRepo.getById(id);
        if(isNull(task)){
            throw new EntityNotFoundException("Not found!");
        }
        task.setDescription(description);
        task.setStatus(status);
        taskRepo.saveOrUpdate(task);
        return task;
    }

    public Task create(String description, Status status){
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(status);
        taskRepo.saveOrUpdate(task);
        return task;
    }

    @Transactional
    public void delete(int id){
        Task task = taskRepo.getById(id);
        if(isNull(task)){
            throw new EntityNotFoundException("Not found!");
        }
        taskRepo.delete(task);
    }

}
