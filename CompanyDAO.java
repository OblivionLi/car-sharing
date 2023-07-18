package carsharing;

import java.util.List;

public interface CompanyDAO {
    List<Company> getAllCompanies();
    void saveCompany(String companyName);
}
