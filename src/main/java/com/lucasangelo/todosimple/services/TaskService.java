package com.lucasangelo.todosimple.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lucasangelo.todosimple.models.Task;
import com.lucasangelo.todosimple.models.User;
import com.lucasangelo.todosimple.repositories.TaskRepository;

import jakarta.transaction.Transactional;

@Service 
public class TaskService {
    
    private TaskRepository taskRepository;
    private UserService userService;


    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);

        return task.orElseThrow(
                () -> new RuntimeException(
                        "Tarefa não encontrada não encontrado: Id: " + id + ", Tipo: " + Task.class.getName()));
    }

    @Transactional 
    public Task create(Task obj) {
        User user = this.userService.findById(obj.getUser().getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);

        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = findById(obj.getId());
         newObj.setDescription(obj.getDescription());
         return this.taskRepository.save(newObj);
    }

    public void deleter(Long id) {
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não é possível excluir pois há entidades relacionadas");
        }
    }

}
