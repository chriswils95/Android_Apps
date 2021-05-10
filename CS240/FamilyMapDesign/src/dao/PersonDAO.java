//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dao;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Person;

public class PersonDAO {
    Connection conn;

    public PersonDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Person person) throws DataAccessException {
        try {
            String query = "INSERT INTO PERSONS VALUES(?,?,?,?,?,?,?)";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setObject(1, person.getUsername());
            stmt.setObject(2, person.getPassword());
            stmt.setObject(3, person.getFirst_name());
            stmt.setObject(4, person.getLast_name());
            stmt.setObject(5, person.getEmail());
            stmt.setObject(6, person.getGender());
            stmt.setObject(7, person.getPersonID());
            stmt.executeUpdate();
        } catch (SQLException var4) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    public Person find(String uniqueId) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE PersonID = ?;";

        Person var6;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            label147: {
                try {
                    stmt.setString(1, uniqueId);
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        Person person = new Person(rs.getString("Username"), rs.getString("Password"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Email"), rs.getString("Gender"), rs.getString("PersonID"));
                        var6 = person;
                        break label147;
                    }
                } catch (Throwable var19) {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (Throwable var18) {
                            var19.addSuppressed(var18);
                        }
                    }

                    throw var19;
                }

                if (stmt != null) {
                    stmt.close();
                }

                return null;
            }

            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException var20) {
            var20.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException var17) {
                    var17.printStackTrace();
                }
            }

        }

        return var6;
    }

    public void deleteUserFromDatabase(Connection connection, String username) {
        String deleteUser = "DELETE FROM PERSONS WHERE USERNAME LIKE '" + username + "'";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(deleteUser);
            if (!rs.next()) {
                System.out.println("User is not found, please register user");
            } else {
                System.out.println("user successful deleted");
            }
        } catch (SQLException var6) {
            System.out.println(var6.getMessage());
        }

    }

    public void deleteAllUsersFromDatabase(Connection connection) {
        String deleteUser = "DELETE FROM USERS";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(deleteUser);
            if (!rs.next()) {
                System.out.println("Deletion fails");
            } else {
                System.out.println("All users deleted");
            }
        } catch (SQLException var5) {
            System.out.println(var5.getMessage());
        }

    }

    public void selectAll(Connection connection) {
        String sql = "SELECT * FROM PERSONS";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                System.out.println("Table is empty");
            }

            while(rs.next()) {
                PrintStream var10000 = System.out;
                String var10001 = rs.getString("username");
                var10000.println(var10001 + "\t" + rs.getString("password") + "\t" + rs.getString("first_name") + "\t" + rs.getString("last_name") + "\t" + rs.getString("email") + "\t" + rs.getString("person_id"));
            }
        } catch (SQLException var5) {
            System.out.println(var5.getMessage());
        }

    }
}
