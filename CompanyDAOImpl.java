package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAOImpl implements CompanyDAO {
    private Connection connection;

    public Connection getConnection() {
        return this.connection;
    }

    public CompanyDAOImpl(String databaseName) {
        try {
            this.connection = DatabaseConnector.getConnection(databaseName);
            DatabaseConnector.createCompanyTable(this.connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = this.connection.createStatement();
            String sql = "SELECT * FROM COMPANY";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int companyId = rs.getInt("ID");
                String name = rs.getString("NAME");
                Company company = new Company(companyId, name);
                companies.add(company);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return companies;
    }

    @Override
    public void saveCompany(String companyName) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO COMPANY (NAME) VALUES (?)";
            pstmt = this.connection.prepareStatement(sql);
            pstmt.setString(1, companyName);
            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
