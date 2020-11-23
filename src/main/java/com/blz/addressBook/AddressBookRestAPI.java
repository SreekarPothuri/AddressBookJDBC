package com.blz.addressBook;

import java.util.ArrayList;
import java.util.List;

public class AddressBookRestAPI {

	List<AddressBookData> contactsList;

	public AddressBookRestAPI(List<AddressBookData> contactList) {
		contactsList = new ArrayList<>(contactList);
	}

	public long countREST_IOEntries() {
		return contactsList.size();
	}
}
