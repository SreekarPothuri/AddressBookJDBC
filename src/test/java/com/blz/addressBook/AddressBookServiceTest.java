package com.blz.addressBook;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
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

	@Test
	public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() throws AddressBookException {
		addressBookService.readAddressBookData(IOService.DB_IO);
		addressBookService.addContactToAddressBook(4,"Anudeep", "Betha", Date.valueOf("2020-05-12"), "Office",
				"Electronic City", "Bangalore", "Karnataka", 536429, "8796589899", "deepu123@gmail.com");
		boolean result = addressBookService.checkAddressBookInSyncWithDB("Anudeep");
		Assert.assertTrue(result);
	}
	
	@Test
	public void givenMultipleContact_WhenAdded_ShouldSyncWithDB() throws AddressBookException {
		AddressBookData[] contactArray = {
								new AddressBookData(5,"Latha","Pothuri",Date.valueOf("2020-06-12"),"Home","VidyaNagar",
										"Ponnur","AndhraPradesh",887622,"8465216975", "latha@gmail.com"),
								new AddressBookData(6,"Sandeep","Vanga",Date.valueOf("2019-06-22"),"Office","gandhiNagar","Vijayawada",
										"AndhraPradesh",882002,"9976549999","pm@gmail.com"),
								new AddressBookData(7,"Gaurav","Khede",Date.valueOf("2018-08-02"),"Home","Dumka","Kolkata",
										"WestBengal", 820056,"9648515621","rkboi@yahoo.com")
		};
		addressBookService.addMultipleContactsToDBUsingThreads(Arrays.asList(contactArray));
		boolean isSynced1 = addressBookService.checkAddressBookInSyncWithDB("Latha");
		boolean isSynced2 = addressBookService.checkAddressBookInSyncWithDB("Sandeep");
		boolean isSynced3 = addressBookService.checkAddressBookInSyncWithDB("Gaurav");
		Assert.assertTrue(isSynced1);
		Assert.assertTrue(isSynced2);
		Assert.assertTrue(isSynced3);
	}
}
