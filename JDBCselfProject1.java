// package com.rudra.jdbc.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCselfProject1 {

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/smart", "root", "rudra@123456");

        Statement st = con.createStatement();
        Scanner src = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            System.out.println("1) Insert ");
            System.out.println("2) Update ");
            System.out.println("3) Delete ");
            System.out.println("4) Select ");
            System.out.println("5) Exit ");

            int n = src.nextInt();

            if (n == 1) {
                System.out.println("You want to insert the values of your table");
                System.out.println("Enter id");
                int id = src.nextInt();

                System.out.println("Enter name");
                String name = src.next();

                System.out.println("Enter email");
                String email = src.next();

                System.out.println("Enter password");
                String password = src.next();

                System.out.println("Enter phoneNo");
                String phone_no = src.next();

                int rowUpdate = st.executeUpdate("INSERT INTO deepaksir(id, name, email, password, phone_no) VALUES("
                        + id + ", '" + name + "', '" + email + "', '" + password + "', '" + phone_no + "')");

                if (rowUpdate > 0) {
                    System.out.println("Row has been inserted");
                } else {
                    System.out.println("Row has not been inserted");
                }
            } else if (n == 2) {
                System.out.println("You want to update your table, let's fill in the certain condition");
                System.out.println("Enter the ID of the row you want to update:");
                int id = src.nextInt();

                System.out.println("Enter the column name you want to update (name, email, password, phone_no):");
                String column = src.next();

                System.out.println("Enter the new value:");
                String newValue = src.next();

                int rowUpdate = st
                        .executeUpdate("UPDATE deepaksir SET " + column + " = '" + newValue + "' WHERE id = " + id);

                if (rowUpdate > 0) {
                    System.out.println("Row has been updated");
                } else {
                    System.out.println("Row has not been updated");
                }
            } else if (n == 3) {
                System.out.println("You want to delete a row from your table");
                System.out.println("Enter the ID of the row you want to delete:");
                int id = src.nextInt();

                int rowDelete = st.executeUpdate("DELETE FROM deepaksir WHERE id = " + id);

                if (rowDelete > 0) {
                    System.out.println("Row has been deleted");
                } else {
                    System.out.println("Row has not been deleted");
                }
            } else if (n == 4) {
                ResultSet rs = st.executeQuery("SELECT * FROM deepaksir");
                System.out.println("YOUR WHOLE TABLE CONTENTS:");
                while (rs.next()) {
                    System.out.print(rs.getString(1));
                    System.out.print(" , " + rs.getString(2));
                    System.out.print(" , " + rs.getString(3));
                    System.out.print(" , " + rs.getString(4));
                    System.out.print(" , " + rs.getString(5));
                    System.out.println();
                }
            } else if (n == 5) {
                keepRunning = false;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

        src.close();
        st.close();
        con.close();
    }
}
