import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class BANKproject {

    static Scanner src = new Scanner(System.in);

    public static void main(String[] args) {

        BANKproject b = new BANKproject();
        b.setBankdetail();

    }

    void setBankdetail() {

        System.out.println("-----------------");
        System.out.println("Select any one option");
        System.out.println("1) toal amount");
        System.out.println("2) users");
        System.out.println("3) mini_statment");
        System.out.println("4) exit");
        getInput();

    }

    void getInput() {
        int n = src.nextInt();
        if (n == 1) {
            InsertDetalis();
        } else if (n == 2) {
            Fund_transfer();
        } else if (n == 3) {
            Mini_statement();
        } else {
            return;
        }
    }

    void InsertDetalis() {
        System.out.println("You want to insert yourr details");
        System.out.println("Enter id");
        int id = src.nextInt();
        System.out.println("Enter name ");
        String name = src.next();

        System.out.println("Enter email");
        String email = src.next();

        System.out.println("Enter phone");
        String phone_no = src.next();

        System.out.println("Enter account");
        int account_no = src.nextInt();
        Connection con = null;
        try {
//             jidhr connection method bani hai uski class name then method name
            con = DatabaseConnection.get_connection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("INSERT INTO user VALUES(? , ? , ? , ? , ?)");
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, phone_no);
            ps.setInt(5, account_no);

            int row_update = ps.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement("INSERT INTO total_amount VALUES(? , ? , ?)");

            ps2.setInt(1, id);
            ps2.setInt(2, account_no);
            ps2.setInt(3, 50000);

            int row_Count = ps2.executeUpdate();

            if (row_Count > 0 && row_update > 0) {
                System.out.println("Your value inserted and account created successfully");
                con.commit();
            } else {
                con.rollback();
                System.out.println("Accoutn creation failed due to some error");
            }
        } catch (Exception e) {
            e.printStackTrace();

            try {
                con.rollback();
            } catch (Exception s) {
                s.printStackTrace();
            }
            e.printStackTrace();
        }

        setBankdetail();
    }

    void Fund_transfer() {
        System.out.println("Sender account no");
        int sender_acc_no = src.nextInt();

        System.out.println("Receiver account no");
        int receiver_acc_no = src.nextInt();

        System.out.println("Enter amount");
        int amount = src.nextInt();

        int from_bal = 0;
        int to_acc = 0;
        Connection con = null;
        try {
            con = DatabaseConnection.get_connection();
            con.setAutoCommit(false);

            // Fetch sender's balance
            PreparedStatement ps = con.prepareStatement("SELECT balance FROM total_amount WHERE account_no = ?");
            ps.setInt(1, sender_acc_no);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                from_bal = rs.getInt(1);
            } else {
                System.out.println("Sender account not found.");
                con.rollback();
                setBankdetail();
                return;
            }

            // Fetch receiver's balance
            PreparedStatement ps1 = con.prepareStatement("SELECT balance FROM total_amount WHERE account_no = ?");
            ps1.setInt(1, receiver_acc_no);
            ResultSet rs2 = ps1.executeQuery();
            if (rs2.next()) {
                to_acc = rs2.getInt(1);
            } else {
                System.out.println("Receiver account not found.");
                con.rollback();
                setBankdetail();
                return;
            }

            // Check if the sender has sufficient balance
            if (from_bal < amount) {
                System.out.println("Insufficient balance in sender's account.");
                con.rollback();
                setBankdetail();
                return;
            }

            // Calculate new balances
            int new_from_bal = from_bal - amount;
            int new_to_bal = to_acc + amount;

            // Update sender's balance
            PreparedStatement ps2 = con.prepareStatement("UPDATE total_amount SET balance = ? WHERE account_no = ?");
            ps2.setInt(1, new_from_bal);
            ps2.setInt(2, sender_acc_no);
            int row_count1 = ps2.executeUpdate();

            // Update receiver's balance
            PreparedStatement ps3 = con.prepareStatement("UPDATE total_amount SET balance = ? WHERE account_no = ?");
            ps3.setInt(1, new_to_bal);
            ps3.setInt(2, receiver_acc_no);
            int row_count2 = ps3.executeUpdate();
//         --------------------------------------------------------
            Date d = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            String date1 = sdf1.format(d);

            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            String time1 = sdf2.format(d);
//         ---------------------------------------------------------------   
            PreparedStatement ps4 = con.prepareStatement("INSERT INTO mini_statement VALUES( ? , ? , ? , ? , ?)");

            ps4.setInt(1, sender_acc_no);
            ps4.setInt(2, amount);
            ps4.setString(3, "d");
            ps4.setString(4, date1);
            ps4.setString(5, time1);

            int row_count3 = ps4.executeUpdate();

            PreparedStatement ps5 = con.prepareStatement("INSERT INTO mini_statement VALUES(? , ? , ? , ? , ?)");

            ps5.setInt(1, receiver_acc_no);
            ps5.setInt(2, amount);
            ps5.setString(3, "c");
            ps5.setString(4, date1);
            ps5.setString(5, time1);

            int row_count4 = ps5.executeUpdate();

            if (row_count1 > 0 && row_count2 > 0 && row_count3 > 0 && row_count4 > 0) {
                con.commit();
                System.out.println("Amount deposit successfully");
            } else {
                con.rollback();
                System.out.println("Transaction failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }

        setBankdetail();
    }

    void Mini_statement() {
 
        System.out.println("Enter acocunt");
        int account_no = src.nextInt();
        Connection con = null;
        try {
            con = DatabaseConnection.get_connection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM mini_statement WHERE account_no =?");
            ps.setInt(1, account_no);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                System.out.print(rs.getInt(2));
             

                System.out.print("\t" + rs.getString(3));
              

                System.out.print("\t" + rs.getString(4));
             

                System.out.print("\t" + rs.getString(5));
               

                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setBankdetail();
    }
}
