//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dao;

import model.Person;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection conn;

    public Database() {
    }

    public Connection openConnection() throws DataAccessException {
        try {
            Class.forName("org.sqlite.JDBC");
            String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";
            this.conn = DriverManager.getConnection("jdbc:sqlite:familymap.sqlite");
            this.conn.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException var2) {
            var2.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return this.conn;
    }

    public Connection getConnection() throws DataAccessException {
        return this.conn == null ? this.openConnection() : this.conn;
    }

    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                this.conn.commit();
            } else {
                this.conn.rollback();
            }

            this.conn.close();
            this.conn = null;
        } catch (SQLException var3) {
            var3.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    public void createTables(String tableName) throws DataAccessException {
        try {
            Statement stmt = conn.createStatement();
            try {
                String sql;
                if (tableName == "Events") {
                    sql = "CREATE TABLE IF NOT EXISTS EVENTS " +
                            "(EventID text not null unique, AssociatedUsername text not null, " +
                            "PersonID text not null, Latitude float not null, Longitude float not null, " +
                            "Country text not null, City text not null, EventType text not null, Year int not null, " +
                            "primary key (EventID), foreign key (AssociatedUsername) references Users(Username), " +
                            "foreign key (PersonID) references Persons(PersonID))";
                    stmt.executeUpdate(sql);
                } else if (tableName.equals("Users")) {
                    sql = "CREATE TABLE IF NOT EXISTS USERS " +
                            "(Username test not null unique,Password text not null)";
                    stmt.executeUpdate(sql);
                } else if (tableName == "Persons") {
                    sql = "CREATE TABLE IF NOT EXISTS PERSONS (Username text not null unique, " +
                            "Password text not null, FirstName text not null, LastName text not null, " +
                            "Email text not null, Gender text not null, PersonID text not null )";
                    stmt.executeUpdate(sql);
                } else {
                    sql = "CREATE TABLE IF NOT EXISTS AUTHTOKENS (Username test not null unique,UniqueKeys " +
                            "text not null)";
                    stmt.executeUpdate(sql);
                }
            } catch (Throwable var6) {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }

                throw var6;
            }

            if (stmt != null) {
                stmt.close();
            }

        } catch (SQLException var7) {
            throw new DataAccessException("SQL Error encountered while creating tables");
        }
    }

    public void clearTables(String table_name) throws DataAccessException {
        try {
            Statement stmt = this.conn.createStatement();

            try {
                String sql;
                if (table_name.equals("Events")) {
                    sql = "DELETE FROM Events";
                    stmt.executeUpdate(sql);
                } else if (table_name.equals("Users")) {
                    sql = "DELETE FROM Users";
                    stmt.executeUpdate(sql);
                } else if (table_name.equals("Persons")) {
                    sql = "DELETE FROM Persons";
                    stmt.executeUpdate(sql);
                } else {
                    sql = "DELETE FROM AuthTokens";
                    stmt.executeUpdate(sql);
                }
            } catch (Throwable var6) {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }

                throw var6;
            }

            if (stmt != null) {
                stmt.close();
            }

        } catch (SQLException var7) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

    public static void main(String[] args) {
        try {
            Database db = new Database();

            db.openConnection();
            Connection connection = db.getConnection();
            db.createTables("Persons");
            Person person = new Person("jame", "john", "mark", "is",
                    "we", "jgasa", "vjvhjvvj");
            PersonDAO personDAO = new PersonDAO(connection);
            personDAO.insert(person);
            db.closeConnection(true);

            System.out.println("OK");
        }
        catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
