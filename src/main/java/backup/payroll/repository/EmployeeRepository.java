package backup.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import backup.payroll.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}