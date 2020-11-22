package com.blz.addressBook;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blz.addressBook.AddressBookService.IOService;

public class AddressBookServiceTest {

	static AddressBookService addressBookService;
	
	@BeforeClass
	public static void AddressBookServiceObj() {
		addressBookService = new AddressBookService();
	}

	@Test
	public void givenAddressBookContactsInDB_WhenRetrieved_ShouldMatchContactsCount() throws AddressBookException {
		List<AddressBookData> addressBookData = addressBookService.readAddressBookData(IOService.DB_IO);
		Assert.assertEquals(3, addressBookData.size());
	}
	
	@Test
	public void givenNewCityAndStateForContact_WhenUpdated_ShouldMatch() throws AddressBookException {
		addressBookService.updateContactCityAndState("Abhi","Mysore", "Karnataka");
		boolean result = addressBookService.checkAddressBookInSyncWithDB("Abhi");
		Assert.assertTrue(result);
	}
}
