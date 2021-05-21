package controller;

import entity.Employee;
import service.EmployeeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "create", urlPatterns = "/admin/create")
public class CreateServlet extends HttpServlet {
    private EmployeeService employeeService = new EmployeeService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/admin/form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8"); // lỗi Utf8 ngoài view
        Employee employee = new Employee();
        employee.setFullName(req.getParameter("fullName"));
        employee.setAddress(req.getParameter("address"));
        employee.setDepartment(req.getParameter("department"));
        employee.setPosition(req.getParameter("position"));
        employee.setBirthday(Date.valueOf(req.getParameter("birthday")));
        employeeService.create(employee);
        resp.sendRedirect("/admin/list");
    }
}
