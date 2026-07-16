import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class FixDB {
    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/whitefox", "mahimahi", "1234");
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE IF EXISTS service_catalog CASCADE");
        stmt.execute("DROP TABLE IF EXISTS service_categories CASCADE");
        System.out.println("Tables dropped");
    }
}
