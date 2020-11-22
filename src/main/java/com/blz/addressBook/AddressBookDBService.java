package com.blz.addressBook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {

	private static AddressBookDBService addressBookDBService;

	private AddressBookDBService() {
	}

	public static AddressBookDBService getInstance() {
		if (addressBookDBService == null)
			addressBookDBService = new AddressBookDBService();
		return addressBookDBService;
	}

	public List<AddressBookData> readData() throws AddressBookException {
		String sql = "SELECT * FROM addressBook; ";
		return this.getAddressBookDataUsingDB(sql);
	}

	private List<AddressBookData> getAddressBookDataUsingDB(String sql) throws AddressBookException {
		List<AddressBookData> employeePayrollList = new ArrayList<>();
		try (Connection connection = AddressBookConnection.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			employeePayrollList = this.getAddressBookData(resultSet);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return employeePayrollList;
	}

	private List<AddressBookData> getAddressBookData(ResultSet resultSet) throws AddressBookException {
		List<AddressBookData> addressBookList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("Id");
				String firstName = resultSet.getString("Firstname");
				String lastName = resultSet.getString("Lastname");
				String addressType = resultSet.getString("AddressType");
				String address = resultSet.getString("Address");
				String city = resultSet.getString("City");
				String state = resultSet.getString("State");
				long zipCode = resultSet.getLong("ZipCode");
				String mobileNumber = resultSet.getString("MobileNumber");
				String emailId = resultSet.getString("EmailId");
				addressBookList.add(new AddressBookData(id, firstName, lastName, addressType, address, city, state,
						zipCode, mobileNumber, emailId));
			}
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return addressBookList;
	}

}
