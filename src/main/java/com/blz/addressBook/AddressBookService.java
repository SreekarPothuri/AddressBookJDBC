package com.blz.addressBook;

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

	private AddressBookData getAddressBookData(String firstName) {
		return this.addressBookList.stream()
				.filter(addressBookDataItem -> addressBookDataItem.firstName.equals(firstName)).findFirst()
				.orElse(null);
	}

	public boolean checkAddressBookInSyncWithDB(String firstName) throws AddressBookException {
		List<AddressBookData> addressBookDataList = addressBookDBService.getAddressBookData(firstName);
		return addressBookDataList.get(0).equals(getAddressBookData(firstName));
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
}
