package com.lucasangelo.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucasangelo.todosimple.models.User;
import com.lucasangelo.todosimple.repositories.TaskRepository;
import com.lucasangelo.todosimple.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service 
public class UserService {
    
    private final UserRepository userRepository;

    private final TaskRepository taskRepository ;

    UserService(UserRepository userRepository, TaskRepository taskRepository ) {
        this.userRepository = userRepository;
        this.taskRepository  = taskRepository ;
    }

    public User findById(Long id) {
        Optional<User> user = this.userRepository.findById(id);

        return user.orElseThrow(() -> new RuntimeException(
                "Usuário não encontrado: Id: " + id + ", Tipo: " + User.class.getName()));
    }

    @Transactional 
    public User create(User obj) {
        obj.setId(null);
        obj = this.userRepository.save(obj);
        this.taskRepository.saveAll(obj.getTasks());

        return obj;
    }

    @Transactional 
    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());

        return this.userRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não é possível excluir pois há entidades relacionadas");
        }
    }

}
