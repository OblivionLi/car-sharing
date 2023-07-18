package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAOImpl implements CarDAO {
    private Connection connection;

    public Connection getConnection() {
        return this.connection;
    }

    public CarDAOImpl(String databaseName) {
        try {
            this.connection = DatabaseConnector.getConnection(databaseName);
            DatabaseConnector.createCarTable(this.connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Car> getAllCars(int companyId) {
        List<Car> cars = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM CAR WHERE COMPANY_ID = ? AND ID NOT IN (SELECT RENTED_CAR_ID FROM CUSTOMER WHERE RENTED_CAR_ID IS NOT NULL)";
            pstmt = this.connection.prepareStatement(sql);
            pstmt.setInt(1, companyId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int carId = rs.getInt("ID");
                String name = rs.getString("NAME");
                Car car = new Car(carId, name);
                cars.add(car);
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

        return cars;
    }

    @Override
    public void saveCar(String carName, int companyId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";
            pstmt = this.connection.prepareStatement(sql);
            pstmt.setString(1, carName);
            pstmt.setInt(2, companyId);
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
