package com.blz.addressBook;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blz.addressBook.AddressBookService.IOService;

public class AddressBookServiceTest {

	static AddressBookService addressBookService;
	static Map<String, Integer> count;

	@BeforeClass
	public static void AddressBookServiceObj() {
		addressBookService = new AddressBookService();
		count = new HashMap<>();
	}

	@Test
	public void givenAddressBookContactsInDB_WhenRetrieved_ShouldMatchContactsCount() throws AddressBookException {
		List<AddressBookData> addressBookData = addressBookService.readAddressBookData(IOService.DB_IO);
		Assert.assertEquals(3, addressBookData.size());
	}

	@Test
	public void givenNewCityAndStateForContact_WhenUpdated_ShouldMatch() throws AddressBookException {
		addressBookService.updateContactCityAndState("Abhi", "Mysore", "Karnataka");
		boolean result = addressBookService.checkAddressBookInSyncWithDB("Abhi");
		Assert.assertTrue(result);
	}

	@Test
	public void givenDateRange_WhenRetrieved_ShouldmatchEmployeeCount() throws AddressBookException {
		addressBookService.readAddressBookData(IOService.DB_IO);
		LocalDate startDate = LocalDate.of(2018, 01, 01);
		LocalDate endDate = LocalDate.now();
		List<AddressBookData> employeePayrollData = addressBookService.readAddressBookForDateRange(IOService.DB_IO,
				startDate, endDate);
		Assert.assertEquals(3, employeePayrollData.size());
	}

	@Test
	public void givenAddressBookDB_WhenRetrievedCountByState_ShouldReturnCountGroupedByState()
			throws AddressBookException {
		count = addressBookService.countContactsByState(IOService.DB_IO, "State");
		Assert.assertEquals(1, count.get("AndhraPradesh"), 0);
		Assert.assertEquals(1, count.get("Telangana"), 0);
		Assert.assertEquals(1, count.get("Karnataka"), 0);
	}

	@Test
	public void givenAddressBookDB_WhenRetrievedCountByCity_ShouldReturnCountGroupedByCity()
			throws AddressBookException {
		count = addressBookService.countContactsByCity(IOService.DB_IO, "City");
		Assert.assertEquals(1, count.get("Ponnur"), 0);
		Assert.assertEquals(1, count.get("Mysore"), 0);
		Assert.assertEquals(1, count.get("Hyderabad"), 0);
	}
}
