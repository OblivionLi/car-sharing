package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDAOImpl implements CustomerDAO {
    private Connection connection;

    public Connection getConnection() {
        return this.connection;
    }

    public CustomerDAOImpl(String databaseName) {
        try {
            this.connection = DatabaseConnector.getConnection(databaseName);
            DatabaseConnector.createCustomerTable(this.connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = this.connection.createStatement();
            String sql = "SELECT * FROM CUSTOMER";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int customerId = rs.getInt("ID");
                String name = rs.getString("NAME");
                int rentedCarId = rs.getInt("RENTED_CAR_ID");
                Customer customer = new Customer(customerId, name, rentedCarId);
                customers.add(customer);
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

        return customers;
    }

    @Override
    public Customer getCustomerById(int id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM CUSTOMER WHERE ID = ?";
            pstmt = this.connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int customerId = rs.getInt("ID");
                String name = rs.getString("NAME");
                int rentedCarId = rs.getInt("RENTED_CAR_ID");
                return new Customer(customerId, name, rentedCarId);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void addRentedCarToCustomer(Customer customer, Car car) {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";
            pstmt = this.connection.prepareStatement(sql);
            pstmt.setInt(1, car.id());
            pstmt.setInt(2, customer.id());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    @Override
    public void saveCustomer(String customerName) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO CUSTOMER (NAME) VALUES (?)";
            pstmt = this.connection.prepareStatement(sql);
            pstmt.setString(1, customerName);
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

    @Override
    public Map<String, String> retrieveRentedCarData(Customer customer) {
        Map<String, String> customerRentedCarData = new HashMap<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT CAR.NAME AS CAR_NAME, COMPANY.NAME AS COMPANY_NAME " +
                    "FROM CUSTOMER " +
                    "JOIN CAR ON CUSTOMER.RENTED_CAR_ID = CAR.ID " +
                    "JOIN COMPANY ON CAR.COMPANY_ID = COMPANY.ID " +
                    "WHERE CUSTOMER.ID = ?";
            pstmt = this.connection.prepareStatement(sql);
            pstmt.setInt(1, customer.id());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String carName = rs.getString("CAR_NAME");
                String companyName = rs.getString("COMPANY_NAME");
                customerRentedCarData.put("carName", carName);
                customerRentedCarData.put("companyName", companyName);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return customerRentedCarData;
    }

    @Override
    public void returnRentedCar(Customer customer) {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";
            pstmt = this.connection.prepareStatement(sql);
            pstmt.setNull(1, Types.INTEGER);
            pstmt.setInt(2, customer.id());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
