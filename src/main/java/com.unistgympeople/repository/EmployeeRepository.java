package com.unistgympeople.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unistgympeople.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}