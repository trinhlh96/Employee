package helper;

import hannotation.Column;
import hannotation.Entity;
import hannotation.Id;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Set;

public class HMigration {
    public static void main(String[] args) throws SQLException {
        // quét trong package entity.
        Reflections reflections = new Reflections("entity");
        // tìm ra tất cả các class có annotation là @Entity.
        Set<Class<?>> allClasses =
                reflections.getTypesAnnotatedWith(Entity.class);
        for (Class<?> c :
                allClasses) {
            registerClass(c);
        }
    }
    public static void registerClass(Class clazz) throws SQLException {
        try {
            // kiểm tra class có được đánh dấu là @Entity hay không?
            if (!clazz.isAnnotationPresent(Entity.class)) {
                return;
            }
            // Lấy ra giá trị của annotation entity.
            Entity entityInfor = (Entity) clazz.getAnnotation(Entity.class);
            StringBuilder strQuery = new StringBuilder();
            strQuery.append(SQLConstant.CREATE_TABLE);
            strQuery.append(SQLConstant.SPACE);
            strQuery.append(entityInfor.tableName());
            strQuery.append(SQLConstant.SPACE);
            strQuery.append(SQLConstant.OPEN_PARENTHESES);
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                // check xem trường có phải là column không.
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }
                // lấy thông tin column để check tên trường, kiểu giá trị của trường.
                Column columnInfor = (Column) field.getAnnotation(Column.class);
                strQuery.append(columnInfor.columnName());
                strQuery.append(SQLConstant.SPACE);
                strQuery.append(columnInfor.columnType());
                // check xem trường có phải là id không.
                if (field.isAnnotationPresent(Id.class)) {
                    // lấy thông tin id.
                    Id idInfor = (Id) field.getAnnotation(Id.class);
                    strQuery.append(SQLConstant.SPACE);
                    strQuery.append(SQLConstant.PRIMARY_KEY);
                    if (idInfor.autoIncrement()) {
                        strQuery.append(SQLConstant.SPACE);
                        strQuery.append(SQLConstant.AUTO_INCREMENT);
                    }
                }
                strQuery.append(SQLConstant.COMMON);
                strQuery.append(SQLConstant.SPACE);
            }
            strQuery.setLength(strQuery.length() - 2);
            strQuery.append(SQLConstant.CLOSE_PARENTHESES);
            ConnectionHelper.getConnection().createStatement().execute(strQuery.toString());
            System.out.printf("Tạo bảng %s thành công.\n", entityInfor.tableName());
        } catch (java.sql.SQLSyntaxErrorException sqlSyntaxErrorException) {
            // sqlSyntaxErrorException.printStackTrace();
            System.err.printf("Có lỗi xảy ra trong quá trình tạo bảng. Error %s.\n", sqlSyntaxErrorException.getMessage());
        }
    }
}
