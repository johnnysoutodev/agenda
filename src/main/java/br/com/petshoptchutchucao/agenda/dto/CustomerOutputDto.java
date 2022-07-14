package br.com.petshoptchutchucao.agenda.dto;

import java.util.List;

public class CustomerOutputDto {

	private String id;
	private String name;
	private String address;
	private List<String> contactNumbers;
	
	public CustomerOutputDto() {}

	public CustomerOutputDto(String id,String name, String address, List<String> contactNumbers) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.contactNumbers = contactNumbers;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<String> getContactNumbers() {
		return contactNumbers;
	}

	public void setContactNumbers(List<String> contactNumbers) {
		this.contactNumbers = contactNumbers;
	}
}
