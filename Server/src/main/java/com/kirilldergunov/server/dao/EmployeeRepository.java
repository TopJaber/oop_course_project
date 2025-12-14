package com.kirilldergunov.server.dao;

import com.kirilldergunov.server.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
