package com.cybr406.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@RestController
public class TodoRestController {

    @Autowired
    TodoJpaRepository todoJpaRepository;

    @Autowired
    TaskJpaRepository taskJpaRepository;

    @PostMapping("/todos")
    public ResponseEntity<Todo> register(@Valid @RequestBody Todo todo) {

        if(todo.getAuthor().isEmpty() || todo.getDetails().isEmpty())
        {
            return new ResponseEntity<>(todoJpaRepository.save(todo), HttpStatus.BAD_REQUEST);
        }
        else
        {
            return new ResponseEntity<>(todoJpaRepository.save(todo), HttpStatus.CREATED);
        }
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getID(@PathVariable long id) {

        //Optional<Todo> listID = inMemoryTodoRepository.find(id);
        Optional<Todo> listID = todoJpaRepository.findById(id);

        if(listID.isPresent())
        {
            Todo newID = listID.get();
            return new ResponseEntity<>(newID, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/todos")
    public Page<Todo> findAll(Pageable pageable) {

        return todoJpaRepository.findAll(pageable);
    }

    @PostMapping("/todos/{id}/tasks")
    public ResponseEntity<Todo> addATask(@PathVariable long id, @RequestBody Task task)
    {
        Optional<Todo> todo = todoJpaRepository.findById(id);

        if(todo.isPresent())
        {
            Todo todo1 = todo.get();
            todo1.getTasks().add(task);
            task.setTodo(todo1);

            taskJpaRepository.save(task);

            return new ResponseEntity<>(todo1, HttpStatus.CREATED);
        }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<Todo> deleteTodo(@PathVariable long id) {

        if(todoJpaRepository.existsById(id))
        {
            todoJpaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Todo> deleteTask(@PathVariable long id) {

        if(taskJpaRepository.existsById(id))
        {
            taskJpaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
