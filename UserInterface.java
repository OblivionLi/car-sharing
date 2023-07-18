package carsharing;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner;
    private final CompanyDAOImpl companyDAO;
    private final CarDAOImpl carDAO;
    private final CustomerDAOImpl customerDAO;

    public UserInterface(Scanner scanner, String databaseName) {
        this.scanner = scanner;
        this.companyDAO = new CompanyDAOImpl(databaseName);
        this.carDAO = new CarDAOImpl(databaseName);
        this.customerDAO = new CustomerDAOImpl(databaseName);
    }

    public void boot() {
        while (true) {
            this.displayMainMenu();
            int userAction = this.scanner.nextInt();
            this.scanner.nextLine(); // consume line
            System.out.println();

            if (userAction == 0 || userAction > 3) {
                this.closeConnections();
                break;
            }

            switch (userAction) {
                case 1:
                    while (true) {
                        this.displayLoggedInMenu();
                        int userAction2 = this.scanner.nextInt();
                        this.scanner.nextLine(); // consume line
                        System.out.println();

                        if (userAction2 == 0 || userAction2 > 2) {
                            break;
                        }

                        if (userAction2 == 1) {
                            this.displayCompanies();
                        }

                        if (userAction2 == 2) {
                            this.createCompany();
                        }
                    }
                case 2:
                    this.displayCustomers();
                    break;
                case 3:
                    this.createCustomer();
                    break;
            }
        }
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");
        String customerName = this.scanner.nextLine();
        this.customerDAO.saveCustomer(customerName);
        System.out.println("The customer was added!");
        System.out.println();
    }

    private void displayCustomers() {
        List<Customer> customersList = this.customerDAO.getAllCustomers();
        if (customersList.isEmpty()) {
            System.out.println("The customer list is empty!");
            System.out.println();
            return;
        }

        System.out.println("Customer list:");
        for (int i = 0; i < customersList.size(); i++) {
            Customer customer = customersList.get(i);
            System.out.printf("%d. %s\n", i + 1, customer.customerName());
        }

        System.out.print("0. Back");
        System.out.println();

        int customerAction = this.scanner.nextInt();
        if (customerAction == 0) {
            return;
        }

        if (customerAction < 1 || customerAction > customersList.size()) {
            System.out.println("Invalid input. Please try again.");
            return;
        }

        Customer selectedCustomer = customersList.get(customerAction - 1);
        this.displayCustomerMenu(selectedCustomer);
    }

    private void displayCustomerMenu(Customer customer) {
        while (true) {
            System.out.println();
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");

            int customerAction = this.scanner.nextInt();
            this.scanner.nextLine(); // consume line
            System.out.println();

            if (customerAction == 0) {
                break;
            }

            switch (customerAction) {
                case 1 -> this.handleRentingCarCompany(customer);
                case 2 -> this.returnRentedCar(customer);
                case 3 -> this.displayRentedCar(customer);
                default -> System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private void returnRentedCar(Customer customer) {
        Customer customer1 = this.customerDAO.getCustomerById(customer.id());
        if (customer1.carId() == 0) {
            System.out.println("You didn't rent a car!");
            return;
        }

        this.customerDAO.returnRentedCar(customer1);
        System.out.println("You've returned a rented car!");
    }

    private void displayRentedCar(Customer customer) {
        Map<String, String> rentedCarData = this.customerDAO.retrieveRentedCarData(customer);

        if (rentedCarData.get("carName") == null || rentedCarData.isEmpty()) {
            System.out.println("You didn't rent a car!");
            return;
        }

        System.out.println("Your rented car:");
        System.out.println(rentedCarData.get("carName"));
        System.out.println("Company:");
        System.out.println(rentedCarData.get("companyName"));
    }

    private void handleRentingCarCompany(Customer customer) {
        Customer customer1 = this.customerDAO.getCustomerById(customer.id());
        if (customer1.carId() > 0) {
            System.out.println("You've already rented a car!");
            return;
        }

        while (true) {
            List<Company> companies = this.companyDAO.getAllCompanies();
            if (companies.isEmpty()) {
                System.out.println("The company list is empty!");
                break;
            }

            System.out.println("Choose a company:");
            for (int i = 0; i < companies.size(); i++) {
                Company company = companies.get(i);
                System.out.printf("%d. %s\n", i + 1, company.companyName());
            }

            System.out.print("0. Back\n");
            int companyAction = this.scanner.nextInt();
            this.scanner.nextLine(); // consume line
            System.out.println();

            if (companyAction == 0) {
                break;
            }

            if (companyAction < 1 || companyAction > companies.size()) {
                System.out.println("Invalid input. Please try again.");
                continue;
            }

            Company selectedCompany = companies.get(companyAction - 1);
            this.rentCar(selectedCompany, customer);
            break;
        }
    }

    private void rentCar(Company company, Customer customer) {
        List<Car> cars = this.carDAO.getAllCars(company.id());
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
            System.out.println();
            return;
        }

        System.out.println("Choose a car:");
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            System.out.printf("%d. %s\n", i + 1, car.carName());
        }

        System.out.print("0. Back\n");
        int carAction = this.scanner.nextInt();
        this.scanner.nextLine(); // consume line
        System.out.println();

        if (carAction == 0) {
            return;
        }

        if (carAction < 1 || carAction > cars.size()) {
            System.out.println("Invalid input. Please try again.");
            return;
        }

        Car selectedCar = cars.get(carAction - 1);
        this.customerDAO.addRentedCarToCustomer(customer, selectedCar);
        System.out.println("You rented '" + selectedCar.carName() + "'");
    }

    private void displayCompanies() {
        List<Company> companyList = this.companyDAO.getAllCompanies();
        if (companyList.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }

        System.out.println("Company list:");
        for (int i = 0; i < companyList.size(); i++) {
            Company company = companyList.get(i);
            System.out.printf("%d. %s\n", i + 1, company.companyName());
        }

        System.out.print("0. Back");
        System.out.println();

        this.handleCompanyMenu(companyList);
    }

    private void handleCompanyMenu(List<Company> companyList) {
        outerloop:
        while (true) {
            int companyIndex = this.scanner.nextInt();
            this.scanner.nextLine(); // consume line
            System.out.println();

            if (companyIndex == 0) {
                break;
            }

            if (companyIndex < 1 || companyIndex > companyList.size()) {
                continue;
            }

            Company company = companyList.get(companyIndex - 1);
            System.out.println("'" + company.companyName() + "' company");

            while (true) {
                this.displayCompanyMenu();
                int companyIndex2 = this.scanner.nextInt();
                this.scanner.nextLine();
                System.out.println();

                if (companyIndex2 == 0) {
                    break outerloop;
                }

                if (companyIndex2 > 2) {
                    continue;
                }

                if (companyIndex2 == 1) {
                    this.displayCars(company.id());
                }

                if (companyIndex2 == 2) {
                    this.createCar(company.id());
                }
            }
        }
    }

    private void createCar(int companyId) {
        System.out.println("Enter the car name:");
        String carName = this.scanner.nextLine();
        this.carDAO.saveCar(carName, companyId);
        System.out.println("The car was added!");
        System.out.println();
    }

    private void displayCars(int companyIndex) {
        List<Car> carList = this.carDAO.getAllCars(companyIndex);
        if (carList.isEmpty()) {
            System.out.println("The car list is empty!");
            System.out.println();
            return;
        }

        System.out.println("Car list:");
        for (int i = 0; i < carList.size(); i++) {
            Car car = carList.get(i);
            System.out.printf("%d. %s\n", i + 1, car.carName());
        }
    }

    private void displayCompanyMenu() {
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
    }

    private void createCompany() {
        System.out.println("Enter the company name:");
        String companyName = this.scanner.nextLine();
        this.companyDAO.saveCompany(companyName);
        System.out.println("The company was created!");
        System.out.println();
    }

    private void displayMainMenu() {
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");
    }

    private void displayLoggedInMenu() {
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
    }

    private void closeConnections() {
        DatabaseConnector.closeConnection(this.companyDAO.getConnection());
        DatabaseConnector.closeConnection(this.carDAO.getConnection());
        DatabaseConnector.closeConnection(this.customerDAO.getConnection());
    }
}
