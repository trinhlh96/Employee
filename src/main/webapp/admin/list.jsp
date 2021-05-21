<%@ page import="java.util.ArrayList" %>
<%@ page import="entity.Employee" %>
<%--
  Created by IntelliJ IDEA.
  User: Win10Pro
  Date: 4/23/2021
  Time: 19:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% ArrayList<Employee> employees = (ArrayList<Employee>) request.getAttribute("list"); %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
            integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
            integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
            crossorigin="anonymous"></script>
</head>
<body>
<div class="container">
    <div class="">
        <table class="table">
            <thead class="thead-dark">
            <tr>
                <th scope="col">Full Name</th>
                <th scope="col">Address</th>
                <th scope="col">Position</th>
                <th scope="col">Birthday</th>
                <th scope="col">Department</th>
                <th scope="col"></th>
            </tr>
            </thead>
            <tbody>
            <% if (employees != null && employees.size() > 0) {
                for (int i = 0; i < employees.size(); i++) {
            %>
            <tr>
                <th scope="row"><%= employees.get(i).getFullName() %>
                </th>
                <td><%= employees.get(i).getAddress() %>
                </td>
                <td><%= employees.get(i).getPosition() %>
                </td>
                <td><%= employees.get(i).getBirthday() %>
                </td>
                <td><%= employees.get(i).getDepartment() %>
                </td>
            </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
    </div>
        <div>
            <a href="/admin/create" class="badge badge-primary">Create</a>
        </div>
    </div>
</div>
</body>
</html>
