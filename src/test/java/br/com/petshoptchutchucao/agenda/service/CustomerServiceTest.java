package br.com.petshoptchutchucao.agenda.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import br.com.petshoptchutchucao.agenda.dto.CustomerFormDto;
import br.com.petshoptchutchucao.agenda.dto.CustomerOutputDto;
import br.com.petshoptchutchucao.agenda.dto.CustomerUpdateFormDto;
import br.com.petshoptchutchucao.agenda.infra.BusinessRulesException;
import br.com.petshoptchutchucao.agenda.model.Customer;
import br.com.petshoptchutchucao.agenda.model.Pet;
import br.com.petshoptchutchucao.agenda.model.Status;
import br.com.petshoptchutchucao.agenda.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerServiceTest {

	private List<String> contactNumbers = new ArrayList<>();
	private List<Pet> pets = new ArrayList<>();
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private CustomerRepository customerRepository;
	
	@InjectMocks
	private CustomerService service;
	
	@BeforeAll
	private void insertContactNumber() {
		contactNumbers.add("00 00000-0000");
	}
	
	@Test
	void couldRegisterACustomerWithCompleteData() {
		CustomerFormDto customerForm = new CustomerFormDto("Cliente Teste","Rua Teste, 00 - Bairro Teste. Teste/TE",contactNumbers);
		
		Customer customer = new Customer(customerForm.getName(), customerForm.getAddress(), customerForm.getContactNumbers(), Status.ATIVO);
		
		Mockito.when(modelMapper.map(customerForm, Customer.class)).thenReturn(customer);
		
		Mockito.when(modelMapper.map(customer, CustomerOutputDto.class)).thenReturn(new CustomerOutputDto(null,customer.getName(),customer.getAddress(), customer.getContactNumbers()));
		
		CustomerOutputDto customerOutput = service.register(customerForm);
		
		Mockito.verify(customerRepository).save(Mockito.any());

		assertEquals(customerForm.getName(), customerOutput.getName());
		assertEquals(customerForm.getAddress(), customerOutput.getAddress());
		assertEquals(customerForm.getContactNumbers(), customerOutput.getContactNumbers());
	}
	
	@Test
	void couldNotUpdateACustomerWithInexistentId() {
		CustomerUpdateFormDto customerUpdate = new CustomerUpdateFormDto("123456", "Cliente Teste", "Alguma Rua", contactNumbers, Status.ATIVO, pets);
		
		Mockito.when(customerRepository.findById(customerUpdate.getId())).thenThrow(BusinessRulesException.class);
		
		assertThrows(BusinessRulesException.class, () -> service.update(customerUpdate));
	}
	
	@Test
	void couldUpdateACustomerWithCorrectId() {
		CustomerUpdateFormDto customerUpdate = new CustomerUpdateFormDto("123456", "Cliente Teste", "Alguma Rua", contactNumbers, Status.ATIVO, pets);
		
		Customer customer = new Customer(customerUpdate.getId(),
										customerUpdate.getName(),
										customerUpdate.getAddress(),
										customerUpdate.getPets(),
										customerUpdate.getContactNumbers(),
										customerUpdate.getStatus());
		
		Mockito.when(customerRepository.findById(customerUpdate.getId())).thenReturn(Optional.of(customer));
		
		Mockito.when(modelMapper.map(customer, CustomerOutputDto.class)).thenReturn(new CustomerOutputDto(customer.getId(),
																										customer.getName(),
																										customer.getAddress(),
																										customer.getContactNumbers()));
		
		CustomerOutputDto customerDto = service.update(customerUpdate);
		
		Mockito.verify(customerRepository).save(Mockito.any());
		
		assertEquals(customerUpdate.getId(), customerDto.getId());
		assertEquals(customerUpdate.getName(), customerDto.getName());
		assertEquals(customerUpdate.getContactNumbers(), customerDto.getContactNumbers());
	}

}