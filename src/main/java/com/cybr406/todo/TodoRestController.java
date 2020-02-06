package com.cybr406.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class TodoRestController {

    @Autowired
    InMemoryTodoRepository inMemoryTodoRepository;





    @PostMapping("/todos")
    public ResponseEntity<Todo> register(@Valid @RequestBody Todo todo) {

        if(todo.getAuthor().isEmpty() || todo.getDetails().isEmpty())
        {
            return new ResponseEntity<>(inMemoryTodoRepository.create(todo), HttpStatus.BAD_REQUEST);
        }
        else
        {
            return new ResponseEntity<>(inMemoryTodoRepository.create(todo), HttpStatus.CREATED);
        }


    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getID(@PathVariable long id) {

        Optional<Todo> listID = inMemoryTodoRepository.find(id);

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
    public ResponseEntity<List<Todo>> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

        List<Todo> listOfTodos = inMemoryTodoRepository.findAll(page, size);

        return new ResponseEntity<>(listOfTodos, HttpStatus.OK);
    }

    @PostMapping("/todos/{id}/tasks")
    public ResponseEntity<Todo> addATask(@PathVariable long id, @RequestBody Task task)
    {
        Todo todo = inMemoryTodoRepository.addTask(id, task);

        List<Task> taskList = todo.getTasks();

        if(!taskList.isEmpty())
        {
            return new ResponseEntity<>(todo, HttpStatus.CREATED);
        }
        else
        {
            return new ResponseEntity<>(todo, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<Todo> deleteTodo(@PathVariable long id) {

        try {
            inMemoryTodoRepository.delete(id);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Todo> deleteTask(@PathVariable long id) {

        try {
            inMemoryTodoRepository.deleteTask(id);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    }
