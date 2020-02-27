package com.cybr406.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskJpaRepository extends JpaRepository<Task, Long>
{

}

    //Optional<Todo> findByUsername(@Param("todo") String idk);}