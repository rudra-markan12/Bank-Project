import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    
    static Connection con = null;

    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankapp", "root", "rudra@123456");
        } catch (Exception e) {
              e.printStackTrace();
        }
    }

    public static Connection get_connection(){
        return con;
    }
}
