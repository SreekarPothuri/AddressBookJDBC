package com.blz.addressBook;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookDBService {

	private static AddressBookDBService addressBookDBService;
	private PreparedStatement addressBookDataStatement;
	AddressBookData addressBook = null;

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
		List<AddressBookData> addressBookList = new ArrayList<>();
		try (Connection connection = AddressBookConnection.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			addressBookList = this.getAddressBookData(resultSet);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return addressBookList;
	}

	private List<AddressBookData> getAddressBookData(ResultSet resultSet) throws AddressBookException {
		List<AddressBookData> addressBookList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("Id");
				String firstName = resultSet.getString("FirstName");
				String lastName = resultSet.getString("LastName");
				Date date = resultSet.getDate("Date_added");
				String addressType = resultSet.getString("AddressType");
				String address = resultSet.getString("Address");
				String city = resultSet.getString("City");
				String state = resultSet.getString("State");
				long zipCode = resultSet.getLong("ZipCode");
				String mobileNumber = resultSet.getString("MobileNumber");
				String emailId = resultSet.getString("EmailId");
				addressBookList.add(new AddressBookData(id, firstName, lastName, date, addressType, address, city,
						state, zipCode, mobileNumber, emailId));
			}
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return addressBookList;
	}

	public List<AddressBookData> getAddressBookData(String FirstName) throws AddressBookException {
		List<AddressBookData> addressBookList = null;
		if (this.addressBookDataStatement == null)
			this.prepareStatementForContactData();
		try {
			addressBookDataStatement.setString(1, FirstName);
			ResultSet resultSet = addressBookDataStatement.executeQuery();
			addressBookList = this.getAddressBookData(resultSet);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return addressBookList;
	}

	private void prepareStatementForContactData() throws AddressBookException {
		try {
			Connection connection = AddressBookConnection.getConnection();
			String sql = "SELECT * FROM addressBook WHERE FirstName = ?;";
			addressBookDataStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
	}

	public int updateContactData(String name, String city, String state) throws AddressBookException {
		try (Connection connection = AddressBookConnection.getConnection();) {
			String sql = "UPDATE addressBook SET City = ?, State = ? WHERE FirstName = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, city);
			preparedStatement.setString(2, state);
			preparedStatement.setString(3, name);
			int status = preparedStatement.executeUpdate();
			return status;
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
	}

	public List<AddressBookData> getContactForDateRange(LocalDate startDate, LocalDate endDate)
			throws AddressBookException {
		String sql = String.format("SELECT * FROM addressBook WHERE Date_added BETWEEN '%s' AND '%s';",
				Date.valueOf(startDate), Date.valueOf(endDate));
		return this.getAddressBookDataUsingDB(sql);
	}

	public Map<String, Integer> getCountContactsByCityOrState(String column) throws AddressBookException {
		Map<String, Integer> contactsCount = new HashMap<>();
		String query = String.format("SELECT %s , count(%s) FROM addressBook GROUP BY %s;", column, column, column);
		try (Connection con = AddressBookConnection.getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				contactsCount.put(resultSet.getString(1), resultSet.getInt(2));
			}
		} catch (Exception e) {
			throw new AddressBookException("SQL Exception", AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return contactsCount;
	}

	public AddressBookData addNewContactToAddressBook(int id, String fname, String lname, Date date, String addressType,
			String address, String city, String state, long zip, String mobileNum, String email)
			throws AddressBookException {
		AddressBookData addressBookData = null;
		String sql = String.format(
				"INSERT INTO addressBook(Id,FirstName,LastName,Date_added,AddressType,Address,City,State,Zipcode,MobileNumber,EmailId)"
						+ " VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s');",
				id, fname, lname, date, addressType, address, city, state, zip, mobileNum, email);
		try (Connection connection = AddressBookConnection.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			int result = preparedStatement.executeUpdate();
			if (result == 1)
				addressBookData = new AddressBookData(id, fname, lname, date, addressType, address, city, state, zip,
						mobileNum, email);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return addressBookData;
	}

	public void addMultipleContactsToDBUsingThreads(List<AddressBookData> record) {
		Map<Integer, Boolean> addStatus = new HashMap<>();
		for (AddressBookData contact : record) {
			Runnable task = () -> {
				addStatus.put(contact.hashCode(), false);
				try {
					addNewContactToAddressBook(contact.getId(), contact.getFirstName(), contact.getLastName(),
							contact.getDate(), contact.getAddressType(), contact.getAddress(), contact.getCity(),
							contact.getState(), contact.getZipCode(), contact.getMobileNum(), contact.getEmailId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				addStatus.put(contact.hashCode(), true);
			};
			Thread thread = new Thread(task, contact.getFirstName());
			thread.start();
		}
		while (addStatus.containsValue(false)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void addNewContactsUsingRestAPI(List<AddressBookData> contactList) throws AddressBookException {
		for (AddressBookData c : contactList) {
			addNewContactToAddressBook(c.getId(), c.getFirstName(), c.getLastName(), c.getDate(), c.getAddressType(),
					c.getAddress(), c.getCity(), c.getState(), c.getZipCode(), c.getMobileNum(), c.getEmailId());
		}
	}
}
