package carsharing;

import java.util.List;
import java.util.Map;

public interface CustomerDAO {
    List<Customer> getAllCustomers();
    Customer getCustomerById(int id);
    void addRentedCarToCustomer(Customer customer, Car car);
    void saveCustomer(String customerName);
    Map<String, String> retrieveRentedCarData(Customer customer);
    void returnRentedCar(Customer customer);
}
