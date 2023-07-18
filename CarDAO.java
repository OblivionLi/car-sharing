package carsharing;

import java.util.List;

public interface CarDAO {
    List<Car> getAllCars(int companyId);
    void saveCar(String carName, int companyId);
}
