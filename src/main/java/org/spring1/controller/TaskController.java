package org.spring1.controller;

import org.spring1.entity.Task;
import org.spring1.exception.InvalidIdException;
import org.spring1.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

@Controller
@RequestMapping("/")
public class TaskController {

    private static final String TASKS_VIEW = "tasks";
    private static final String TASKS_ATTRIBUTE = "tasks";
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String tasks(Model model,
                            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit){

        addAttributeTasks(model, page, limit);

        addAttributePageNumbers(model, limit);

        addAttributeCurrentPage(model, page);

        return TASKS_VIEW;
    }

    private void addAttributeCurrentPage(Model model, int page) {
        model.addAttribute("current_page", page);
    }

    private void addAttributePageNumbers(Model model, int limit) {
        int totalPages = (int)Math.ceil(1.0 * taskService.getAllCount() / limit);
        if(totalPages > 1){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().toList();
            model.addAttribute("page_numbers", pageNumbers);
        }
    }

    private void addAttributeTasks(Model model, int page, int limit) {
        List<Task> tasks = taskService.getAllTasks((page - 1) * limit, limit);
        model.addAttribute(TASKS_ATTRIBUTE, tasks);
    }

    @PostMapping("/{id}")
    public String edit(Model model,
                     @PathVariable Integer id,
                     @RequestBody TaskInfo taskInfo){
        if(isNull(id) || id <= 0){
            throw new InvalidIdException("Invalid id!");
        }
        taskService.edit(id, taskInfo.getDescription(), taskInfo.getStatus());
        return tasks(model, 1, 10);
    }

    @PostMapping("/")
    public String add(Model model,
                     @RequestBody TaskInfo taskInfo){
        taskService.create(taskInfo.getDescription(), taskInfo.getStatus());
        return tasks(model, 1, 10);
    }

    @DeleteMapping("/{id}")
    public String delete(Model model,
                         @PathVariable Integer id){
        if(isNull(id) || id <= 0){
            throw new InvalidIdException("Invalid id!");
        }
        taskService.delete(id);
        return tasks(model, 1, 10);
    }
}
