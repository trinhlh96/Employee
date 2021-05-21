package entity;

import hannotation.Column;
import hannotation.Entity;
import hannotation.Id;
import java.sql.Date;


@Entity(tableName = "employee")
public class Employee {
    @Id(autoIncrement = true)
    @Column(columnName = "id", columnType = "int")
    private int id;
    @Column(columnName = "fullName", columnType = "VARCHAR(250)")
    private String fullName;
    @Column(columnName = "address", columnType = "VARCHAR(250)")
    private String address;
    @Column(columnName = "birthday", columnType = "DATE")
    private Date birthday;
    @Column(columnName = "position", columnType = "VARCHAR(250)")
    private String position ;
    @Column(columnName = "department", columnType = "VARCHAR(250)")
    private String department;

    public Employee() {
    }

    public Employee(int id, String fullName, String address, Date birthday, String position, String department) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.birthday = birthday;
        this.position = position;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}


