package com.unistgympeople;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unistgympeople.model.Employee;

interface EmployeeRepository extends JpaRepository<Employee, Long> {

}