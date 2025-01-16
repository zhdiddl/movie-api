package com.example.infrastructure.db;

import com.example.domain.model.entity.Movie;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieJpaRepository extends JpaRepository<Movie, Long> {

    @EntityGraph(attributePaths = {"screenings", "screenings.theater"})
    List<Movie> findAll(Sort sort);

}
