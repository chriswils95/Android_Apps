//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
/**
 * The Database class implements my connections and create and clear tables
 * It will be used priliminary by my other dao classes to access a connection that
 * will be used to do some implementations to my database
 */
package files.dao;

import java.sql.*;

/**
 * The Database class implements my connections and create and clear tables
 * It will be used priliminary by my other dao classes to access a connection that
 * will be used to do some implementations to my database
 */
public class Database {
    /**
     * This is the connection fields used in linking the JBDC to my database
     */
    Connection conn;


    public Database(){
        loadDriver();
    }

    public void loadDriver()
    {
        try
        {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch (ClassNotFoundException e)
        {
            System.out.print("Class Not found error\n");
        }
    }
    /**
     * The open connection opens a connectio to my database bybusing the
     * sqlite JDBC driver and creating a sqlite database for me.
     * It will returns a secure connection if the database is created and link
     * correctly or throw an error if there was
     * @return connection
     * @throws DataAccessException
     */
    public Connection openConnection() throws DataAccessException {
         conn = null;
        try {
            String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";
            this.conn = DriverManager.getConnection(CONNECTION_URL);
            createIfNotExist();
            //createTables();
            System.out.println("opening");

        } catch (SQLException var2) {
            var2.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return this.conn;
    }

    /**
     * The getConnection class returns the secured connection to my database
     * that was created
     * @return connection
     * @throws DataAccessException
     */
    public Connection getConnection() throws DataAccessException {
        return this.conn;
    }


    public void startTransaction() throws DataAccessException {
        try
        {
            openConnection();
            conn.setAutoCommit(false);
        }
        catch (SQLException e)
        {
            System.out.print("turn off auto commit error");
            e.printStackTrace();
        }

    }


    /**
     * The closeConnection close the connection to my database is the commit
     * is true and update the intended opearations that was done to it when it was open,
     *If commit is false, it stills close it, but all necessary data opeartions doesnt take
     * effects. It throws an error if there was any error while closing the database
     * @param commit
     * @throws DataAccessException
     */
    public void closeConnection(boolean commit) throws DataAccessException, SQLException {

        try {

            if (commit) {
                this.conn.commit();
            } else {
                this.conn.rollback();
            }
            System.out.println("closing");
        } catch (SQLException var3) {
            var3.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
        finally
        {
            try
            {
                conn.close();
            }
            catch (SQLException e) {
                System.out.println("Cant close connection");
                e.printStackTrace();
            }
        }
        conn = null;
    }



    public void createTables() throws DataAccessException {
        try {
            Statement stmt = null;
            Statement stmt1 = null;
            Statement stmt2 = null;
            Statement stmt3 = null;
            try {
                String sql;
                    sql = "CREATE TABLE IF NOT EXISTS EVENTS " +
                            "(EventID text not null unique, AssociatedUsername text not null, " +
                            "PersonID text not null, Latitude float not null, Longitude float not null, " +
                            "Country text not null, City text not null, EventType text not null, Year int not null, " +
                            "primary key (EventID), foreign key (AssociatedUsername) references Users(Username), " +
                            "foreign key (PersonID) references Persons(PersonID))";
                    stmt.executeUpdate(sql);
                    sql = "CREATE TABLE IF NOT EXISTS USERS " +
                            "(Username text not null unique,Password text not null," +
                            "FirstName text not null, LastName text not null," +
                            "Email text not null, Gender not null," +
                            "PersonID text not null)";

                    stmt1.executeUpdate(sql);
                    sql = "CREATE TABLE IF NOT EXISTS PERSONS " +
                            "(PersonID text not null, AssociatedUsername text not null, " +
                            "FirstName text not null, LastName text not null, " +
                            "Gender text not null, FatherID text, MotherID text," +
                            "SpouseID text)";
                    stmt2.executeUpdate(sql);
                    sql = "CREATE TABLE IF NOT EXISTS AUTHTOKENS (Username text not null ,UniqueKeys " +
                            "text not null)";
                    stmt3.executeUpdate(sql);
                }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }

                if (stmt1 != null) {
                    stmt1.close();
                    stmt1 = null;
                }

                if (stmt2 != null) {
                    stmt2.close();
                    stmt2 = null;
                }

                if (stmt3 != null) {
                    stmt3.close();
                    stmt3 = null;
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Failed");
        }
    }


    /**
     * This function as the name suggests creates table if it doesnt exist
     * It pretty much create the tables needed for this lab
     * @throws SQLException
     * @throws DataAccessException
     */
    public void createIfNotExist() throws SQLException, DataAccessException {


        String sql =
                "CREATE TABLE IF NOT EXISTS EVENTS"+
                        "("+
                        "AssociatedUsername varchar(64),"+
                        "eventid varchar(64) primary key,"+
                        "personid varchar(64),"+
                        "latitude real,"+
                        "longitude real,"+
                        "country varchar(25),"+
                        "city varchar(25),"+
                        "EventType varchar(50),"+
                        "year varchar(5)" +
                        ");";


        String sql1 = "CREATE TABLE IF NOT EXISTS USERS" +
                "(Username text not null unique,Password text not null," +
                "FirstName text not null, LastName text not null," +
                "Email text not null, Gender not null," +
                "PersonID text not null)";

        String sql2 = "CREATE TABLE IF NOT EXISTS PERSONS" +
                "(PersonID text not null, AssociatedUsername text not null, " +
                "FirstName text not null, LastName text not null, " +
                "Gender text not null, FatherID text, MotherID text," +
                "SpouseID text)";

        String sql3 = "CREATE TABLE IF NOT EXISTS AUTHTOKENS (Username test not null,UniqueKeys " +
                "text not null)";

        PreparedStatement stmt = this.conn.prepareStatement(sql);
        stmt.executeUpdate();

        PreparedStatement stmt1 = this.conn.prepareStatement(sql1);
        stmt1.executeUpdate();

        PreparedStatement stmt3 = this.conn.prepareStatement(sql3);
        stmt3.executeUpdate();

        PreparedStatement stmt2 = this.conn.prepareStatement(sql2);
        stmt2.executeUpdate();

    }


    /**
     * The clear tables function classes clears or empty out all  contents of a table that
     * corresponds to the given table name, it throws an error if there bad connection or
     * linking problem with the database
     * @param table_name specifies the table name to cleared
     * @throws DataAccessException
     */
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
                }
                else if(table_name.equals("Years")){
                    sql = "DELETE FROM Years";
                    stmt.executeUpdate(sql);
                }
                else {
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
}
