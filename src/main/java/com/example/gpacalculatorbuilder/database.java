package com.example.gpacalculatorbuilder;

import java.sql.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class database {


    private static volatile String DB_PATH;
    private static volatile String DB_URL;

    static {
        Path root = Paths.get(System.getProperty("user.dir"));
        Path legacy = root.resolve("gpa.db");
        Path target = root.resolve("gpa_app.db");

        try {
            if (Files.exists(legacy) && !Files.exists(target)) {
                Files.copy(legacy, target);
                System.out.println("Migrated legacy gpa.db to gpa_app.db");
            }
        } catch (Exception e) {
            System.out.println("Legacy migration failed: " + e.getMessage());
        }
        DB_PATH = target.toAbsolutePath().toString();
        DB_URL = "jdbc:sqlite:" + DB_PATH;
    }



    private static Connection connection;


    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS courses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "code TEXT NOT NULL," +
                    "credit INTEGER NOT NULL," +
                    "teacher1 TEXT," +
                    "teacher2 TEXT," +
                    "grade TEXT NOT NULL" +
                    ");";


    private static void ensureConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Connecting to: " + DB_URL);
                try {
                    Class.forName("org.sqlite.JDBC");
                } catch (ClassNotFoundException ignore) {

                }
                connection = DriverManager.getConnection(DB_URL);
                try { connection.setAutoCommit(true); } catch (SQLException ignore) {}
                createTableIfNeeded();
                migrateSchemaIfNeeded();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static void createTableIfNeeded() {
        try (Statement st = connection.createStatement()) {
            st.execute(CREATE_TABLE_SQL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void insertCourse(Course c) {
        ensureConnection();
        String sql = "INSERT INTO courses(name, code, credit, teacher1, teacher2, grade) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getCode());
            ps.setInt(3, c.getCredit());
            ps.setString(4, c.getTeacher1());
            ps.setString(5, c.getTeacher2());
            ps.setString(6, c.getGrade());
            ps.executeUpdate();
            logOp("INSERT", c.getCode());
        } catch (SQLException e) {
            if (isMissingColumnError(e)) {
                migrateSchemaIfNeeded();
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, c.getName());
                    ps.setString(2, c.getCode());
                    ps.setInt(3, c.getCredit());
                    ps.setString(4, c.getTeacher1());
                    ps.setString(5, c.getTeacher2());
                    ps.setString(6, c.getGrade());
                    ps.executeUpdate();
                    logOp("INSERT_AFTER_MIGRATE", c.getCode());
                    return;
                } catch (SQLException e2) {
                    throw new RuntimeException("Insert failed after migration: " + e2.getMessage() + " (DB: " + DB_PATH + ")", e2);
                }
            }
            throw new RuntimeException("Insert failed: " + e.getMessage() + " (DB: " + DB_PATH + ")", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void updateCourse(Course c) {
        ensureConnection();

        String sql = "UPDATE courses SET name=?, code=?, credit=?, teacher1=?, teacher2=?, grade=? WHERE id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getCode());
            ps.setInt(3, c.getCredit());
            ps.setString(4, c.getTeacher1());
            ps.setString(5, c.getTeacher2());
            ps.setString(6, c.getGrade());
            ps.setInt(7, c.getId());
            ps.executeUpdate();
            logOp("UPDATE", c.getCode());
        } catch (SQLException e) {
            if (isMissingColumnError(e)) {
                migrateSchemaIfNeeded();
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, c.getName());
                    ps.setString(2, c.getCode());
                    ps.setInt(3, c.getCredit());
                    ps.setString(4, c.getTeacher1());
                    ps.setString(5, c.getTeacher2());
                    ps.setString(6, c.getGrade());
                    ps.setInt(7, c.getId());
                    ps.executeUpdate();
                    logOp("UPDATE_AFTER_MIGRATE", c.getCode());
                    return;
                } catch (SQLException e2) {
                    throw new RuntimeException("Update failed after migration: " + e2.getMessage() + " (DB: " + DB_PATH + ")", e2);
                }
            }
            throw new RuntimeException("Update failed: " + e.getMessage() + " (DB: " + DB_PATH + ")", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void deleteCourse(int id) {
        ensureConnection();

        String sql = "DELETE FROM courses WHERE id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            logOp("DELETE", String.valueOf(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static List<Course> getAllCourses() {
        ensureConnection();

        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY id DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("code"),
                        rs.getInt("credit"),
                        rs.getString("teacher1"),
                        rs.getString("teacher2"),
                        rs.getString("grade")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }




                private static void migrateSchemaIfNeeded() {
                    try (Statement st = connection.createStatement()) {

                        java.util.Set<String> cols = new java.util.HashSet<>();
                        try (ResultSet rs = st.executeQuery("PRAGMA table_info(courses)")) {
                            while (rs.next()) {
                                cols.add(rs.getString("name"));
                            }
                        }


                        if (!cols.contains("name")) {
                            st.executeUpdate("ALTER TABLE courses ADD COLUMN name TEXT");
                            System.out.println("Migrated: added column 'name'");
                        }
                        if (!cols.contains("code")) {
                            st.executeUpdate("ALTER TABLE courses ADD COLUMN code TEXT");
                            System.out.println("Migrated: added column 'code'");
                        }
                        if (!cols.contains("credit")) {
                            st.executeUpdate("ALTER TABLE courses ADD COLUMN credit INTEGER DEFAULT 0");
                            System.out.println("Migrated: added column 'credit'");
                        }
                        if (!cols.contains("teacher1")) {
                            st.executeUpdate("ALTER TABLE courses ADD COLUMN teacher1 TEXT");
                            System.out.println("Migrated: added column 'teacher1'");
                        }
                        if (!cols.contains("teacher2")) {
                            st.executeUpdate("ALTER TABLE courses ADD COLUMN teacher2 TEXT");
                            System.out.println("Migrated: added column 'teacher2'");
                        }
                        if (!cols.contains("grade")) {
                            st.executeUpdate("ALTER TABLE courses ADD COLUMN grade TEXT DEFAULT 'F'");
                            System.out.println("Migrated: added column 'grade'");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException("Schema migration failed: " + e.getMessage(), e);
                    }
                }
    public static void deleteAll() {
        ensureConnection();
        try (Statement st = connection.createStatement()) {
            st.execute("DELETE FROM courses");
            logOp("DELETE_ALL", "*");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String getDatabaseFilePath() {
        return DB_PATH;
    }


    public static synchronized void setDatabaseFileName(String fileName) {
        Path newPath = Paths.get(System.getProperty("user.dir"), fileName).toAbsolutePath();
        switchToDatabase(newPath);
    }


    public static synchronized void setDatabaseAbsolutePath(String absolutePath) {
        Path newPath = Paths.get(absolutePath).toAbsolutePath();
        switchToDatabase(newPath);
    }

    private static void switchToDatabase(Path newPath) {
        try {
            if (connection != null && !connection.isClosed()) {
                try { connection.close(); } catch (SQLException ignore) {}
            }
        } catch (SQLException ignore) {}
        DB_PATH = newPath.toString();
        DB_URL = "jdbc:sqlite:" + DB_PATH;
        connection = null;
        ensureConnection();
    }


    public static synchronized void resetDatabaseFile() {
        try {
            if (connection != null && !connection.isClosed()) {
                try { connection.close(); } catch (SQLException ignore) {}
            }
        } catch (SQLException ignore) {}
        connection = null;
        try {
            Files.deleteIfExists(Paths.get(DB_PATH));
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete DB file: " + DB_PATH + ": " + e.getMessage(), e);
        }
        ensureConnection();
    }

    private static boolean isMissingColumnError(SQLException e) {
        String msg = e.getMessage();
        if (msg == null) return false;
        String m = msg.toLowerCase();
        return m.contains("no such column") || m.contains("has no column") || m.contains("no column named");
    }

    public static int getRowCount() {
        ensureConnection();
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) AS c FROM courses")) {
            return rs.next() ? rs.getInt("c") : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Row count failed: " + e.getMessage(), e);
        }
    }

    private static void logOp(String op, String ref) {
        try {
            int count = getRowCount();
            System.out.println("[DB] " + op + " ref=" + ref + " rows=" + count + " file=" + DB_PATH);
        } catch (Exception e) {
            System.out.println("[DB] logOp failed: " + e.getMessage());
        }
    }
}
