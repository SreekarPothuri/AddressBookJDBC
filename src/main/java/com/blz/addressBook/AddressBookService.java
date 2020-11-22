package com.blz.addressBook;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AddressBookService {

	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}

	private List<AddressBookData> addressBookList;
	private static AddressBookDBService addressBookDBService;

	public AddressBookService() {
		addressBookDBService = AddressBookDBService.getInstance();
	}

	public AddressBookService(List<AddressBookData> addresBookList) {
		this();
		this.addressBookList = addressBookList;
	}

	public List<AddressBookData> readAddressBookData(IOService ioservice) throws AddressBookException {
		if (ioservice.equals(IOService.DB_IO))
			this.addressBookList = addressBookDBService.readData();
		return this.addressBookList;
	}

	private AddressBookData getAddressBookData(String FirstName) {
		return this.addressBookList.stream()
				.filter(addressBookDataItem -> addressBookDataItem.getFirstName().equals(FirstName)).findFirst()
				.orElse(null);
	}

	public boolean checkAddressBookInSyncWithDB(String FirstName) throws AddressBookException {
		List<AddressBookData> addressBookDataList = addressBookDBService.getAddressBookData(FirstName);
		return addressBookDataList.get(0).equals(getAddressBookData(FirstName));
	}

	public void updateContactCityAndState(String name, String city, String state) throws AddressBookException {
		int result = addressBookDBService.updateContactData(name, city, state);
		if (result == 0)
			return;
		AddressBookData addressBookData = this.getAddressBookData(name);
		if (addressBookData != null) {
			addressBookData.city = city;
			addressBookData.state = state;
		}
	}

	public List<AddressBookData> readAddressBookForDateRange(IOService ioService, LocalDate startDate,
			LocalDate endDate) throws AddressBookException {
		if (ioService.equals(IOService.DB_IO))
			return addressBookDBService.getContactForDateRange(startDate, endDate);
		return null;
	}

	public Map<String, Integer> countContactsByState(IOService ioService, String state) throws AddressBookException {
		if (ioService.equals(IOService.DB_IO))
			return addressBookDBService.getCountContactsByCityOrState(state);
		return null;
	}

	public Map<String, Integer> countContactsByCity(IOService ioService, String city) throws AddressBookException {
		if (ioService.equals(IOService.DB_IO))
			return addressBookDBService.getCountContactsByCityOrState(city);
		return null;
	}

	public void addContactToAddressBook(int id, String fname, String lname, Date date, String addressType,
			String address, String City, String State, long zip, String mobileNum, String email)
			throws AddressBookException {
		addressBookList.add(addressBookDBService.addNewContactToAddressBook(id, fname, lname, date, addressType,
				address, City, State, zip, mobileNum, email));
	}
	
	public void addMultipleContactsToDBUsingThreads(List<AddressBookData> record) {
		addressBookDBService.addMultipleContactsToDBUsingThreads(record);
	}
}
