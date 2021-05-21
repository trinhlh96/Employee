package repository;

import hannotation.Column;
import hannotation.Entity;
import hannotation.Id;
import helper.ConnectionHelper;
import helper.SQLConstant;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenericRepository<T> {
    private Class<T> clazz;

    public GenericRepository(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            System.err.printf("Class %s không được đăng ký làm việc với database.", clazz.getSimpleName());
            return;
        }
        this.clazz = clazz;
    }

    // lưu thông tin đối tượng kiểu T
    public boolean save(T obj) {
        try {
            // Lấy ra giá trị của annotation @Entity vì cần những thông tin liên quan đến tableName.
            Entity entityInfor = clazz.getAnnotation(Entity.class);
            // Build lên câu query string.
            StringBuilder strQuery = new StringBuilder();
            // Build chuỗi chứa giá trị các trường tương ứng.
            StringBuilder fieldValues = new StringBuilder();
            fieldValues.append(SQLConstant.OPEN_PARENTHESES);
            // Xây dựng câu lệnh insert theo tên bảng, theo tên các field cùa đối tượng truyền vào.
            strQuery.append(SQLConstant.INSERT_INTO); // insert into
            strQuery.append(SQLConstant.SPACE); //
            strQuery.append(entityInfor.tableName()); // giangvien
            strQuery.append(SQLConstant.SPACE); //
            strQuery.append(SQLConstant.OPEN_PARENTHESES); // (
            for (Field field : clazz.getDeclaredFields()) {
                // check xem trường có phải là @Column không.
                if (!field.isAnnotationPresent(Column.class)) {
                    // bỏ qua trong trường hợp không được đánh là @Column.
                    continue;
                }
                // cần set bằng true để có thể set, get giá trị của field trong một object nào đó.
                field.setAccessible(true);
                // lấy thông tin column để check tên trường, kiểu giá trị của trường.
                // Không lấy danh sách column theo tên field mà lấy theo annotation đặt tại field đó.
                Column columnInfor = field.getAnnotation(Column.class);
                // check xem trường có phải là id không.
                if (field.isAnnotationPresent(Id.class)) {
                    // lấy thông tin id.
                    Id idInfor = field.getAnnotation(Id.class);
                    if (idInfor.autoIncrement()) {
                        // trường hợp đây là trường tự tăng, thì next sang trường tiếp theo.
                        continue;
                    }
                }
                strQuery.append(columnInfor.columnName()); // nối tên trường.
                strQuery.append(SQLConstant.COMMON); //,
                strQuery.append(SQLConstant.SPACE); //
                // nhanh trí, xử lý luôn phần value, tránh sử dụng 2 vòng lặp.
                // check kiểu của trường, nếu là string thì thêm dấu '
                if (field.getType().getSimpleName().equals(String.class.getSimpleName()) ||
                        field.getType().getSimpleName().equals(Date.class.getSimpleName())) {
                    fieldValues.append(SQLConstant.QUOTE);
                }
                // lấy ra thông tin giá trị của trường đó tại obj truyền vào.
                fieldValues.append(field.get(obj)); // field.setAccessible(true);
                // check kiểu của trường, nếu là string thì thêm dấu '
                if (field.getType().getSimpleName().equals(String.class.getSimpleName())||
                        field.getType().getSimpleName().equals(Date.class.getSimpleName())) {
                    fieldValues.append(SQLConstant.QUOTE);
                }
                fieldValues.append(SQLConstant.COMMON); //,
                fieldValues.append(SQLConstant.SPACE); //
            }
            strQuery.setLength(strQuery.length() - 2); // trường hợp là field cuối cùng thì bỏ dấu , và khoảng trắng đi.
            fieldValues.setLength(fieldValues.length() - 2);
            strQuery.append(SQLConstant.CLOSE_PARENTHESES); // )
            fieldValues.append(SQLConstant.CLOSE_PARENTHESES); // )
            strQuery.append(SQLConstant.SPACE);
            strQuery.append(SQLConstant.VALUES); // values
            strQuery.append(SQLConstant.SPACE);
            strQuery.append(fieldValues); // nối giá trị các trường vào.
            System.out.println("lệnh insert \n");
            System.out.println(strQuery.toString());
            return ConnectionHelper.getConnection().createStatement().execute(strQuery.toString());
        } catch (IllegalAccessException | SQLException e) {
            System.err.printf("Có lỗi xảy ra trong quá trình làm việc với database. Error %s.\n", e.getMessage());
        }
        return true;
    }

    public List<T> findAll() {
        try {
            List<T> listObj = new ArrayList<>(); // khởi tạo một danh sách rỗng.
            String tableName = clazz.getAnnotation(Entity.class).tableName();
            StringBuilder stringCmd = new StringBuilder();
            stringCmd.append(SQLConstant.SELECT_ASTERISK);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(SQLConstant.FROM);
            stringCmd.append(SQLConstant.SPACE);
            stringCmd.append(tableName);
            //execute command
            PreparedStatement preparedStatement = ConnectionHelper.getConnection().prepareStatement(stringCmd.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                T obj = clazz.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    if (!field.isAnnotationPresent(Column.class)) {
                        continue;
                    }
                    field.setAccessible(true);
                    Column columnInfor = field.getAnnotation(Column.class);
                    switch (field.getType().getSimpleName()) {
                        case SQLConstant.PRIMITIVE_INT:
                            // set giá trị của trường đó cho đối tượng mới tạo ở trên.
                            field.set(obj, resultSet.getInt(columnInfor.columnName()));
                            break;
                        case SQLConstant.PRIMITIVE_STRING:
                            field.set(obj, resultSet.getString(columnInfor.columnName()));
                            break;
                        case SQLConstant.PRIMITIVE_DOUBLE:
                            field.set(obj, resultSet.getDouble(columnInfor.columnName()));
                            break;
                        case SQLConstant.PRIMITIVE_DATE:
                            field.set(obj, resultSet.getDate(columnInfor.columnName()));
                            break;
                    }
                }
                System.out.println(stringCmd.toString());
                listObj.add(obj);
            }
            return listObj;
        } catch (InstantiationException | IllegalAccessException | SQLException error) {
            System.err.printf("Find all error %s\n", error.getMessage());
        }
        return null;
    }






}
