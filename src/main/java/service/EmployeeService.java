package service;

import entity.Employee;
import repository.GenericRepository;

import java.sql.Date;
import java.util.List;

// function chính của Student trong hệ thống.
// những thao tác liên quan đến business.
public class EmployeeService {
    private GenericRepository<Employee> genericRepository;
    public EmployeeService() {
        this.genericRepository = new GenericRepository<>(Employee.class);
    }
    Date date=new Date(System.currentTimeMillis());
    public boolean create(Employee obj) {
        return genericRepository.save(obj);
    }

    public List<Employee> getList() {
        return genericRepository.findAll();
    }


}