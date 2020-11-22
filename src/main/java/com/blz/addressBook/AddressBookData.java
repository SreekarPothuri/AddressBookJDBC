package com.blz.addressBook;

public class AddressBookData {
	private int id;
	private String firstName;
	private String lastName;
	private String emailId;
	private String address;
	private String city;
	private String state;
	private String mobileNum;
	private long zipCode;
	private String addressType;

	public AddressBookData(int id, String firstName, String lastName, String addressType, String address, String city,
			String state, long zipCode, String mobileNum, String emailId) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.addressType = addressType;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.mobileNum = mobileNum;
		this.emailId = emailId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public long getZipCode() {
		return zipCode;
	}

	public void setZipCode(long zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String toString() {
		return "Contacts [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId
				+ ", cityName=" + city + ", address=" + address + ", zipCode=" + zipCode + ", stateName=" + state
				+ ", phoneNumber=" + mobileNum + ", addressType=" + addressType + "]";
	}
}
