package com.diploma.diplomaproject.repos;

import com.diploma.diplomaproject.models.students;
import org.springframework.data.repository.CrudRepository;

public interface StudentsRepository extends CrudRepository<students,Integer> {
}
