package com.unistgympeople.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unistgympeople.payroll.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}