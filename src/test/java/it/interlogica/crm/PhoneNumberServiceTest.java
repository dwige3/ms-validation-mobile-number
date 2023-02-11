package it.interlogica.crm;

import it.interlogica.crm.model.PhoneNumber;
import it.interlogica.crm.model.entity.PhoneNumberEntity;
import it.interlogica.crm.model.enumerator.PhoneNumberStatus;
import it.interlogica.crm.repository.PhoneNumberRepository;
import it.interlogica.crm.service.PhoneNumberServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PhoneNumberServiceTest {

    @Autowired
    private PhoneNumberServiceImpl service;

    @MockBean
    private PhoneNumberRepository phoneNumberRepository;

    @Test
    void getValidNumbersTest() throws Exception {

        PhoneNumberEntity phoneNumber = PhoneNumberEntity.builder().idNumber("103243034").originalNumber("81667273413").correctedNumber(null).status(PhoneNumberStatus.VALID).build();
        PhoneNumberEntity phoneNumber1 = PhoneNumberEntity.builder().idNumber("103343262").originalNumber("6478342944").correctedNumber(null).status(PhoneNumberStatus.VALID).build();
        PhoneNumberEntity phoneNumber2 = PhoneNumberEntity.builder().idNumber("103343262").originalNumber("6478342944").correctedNumber(null).status(PhoneNumberStatus.INVALID).build();
        List<PhoneNumberEntity> response = Arrays.asList(phoneNumber, phoneNumber1, phoneNumber2);

        Mockito.when(phoneNumberRepository.findAll()).thenReturn(response);
        List<PhoneNumber> phoneNumberList = service.acceptablePhoneNumbers();
        assertEquals(phoneNumberList.size(), 465);
        assertEquals(phoneNumberList.get(15).getIdNumber(), "103426147");
        assertEquals(phoneNumberList.get(15).getOriginalNumber(), "27815580331");

    }

    @Test
    void getCorrectedNumbersTest() throws Exception {

        PhoneNumberEntity phoneNumber = PhoneNumberEntity.builder().idNumber("103243034").originalNumber("81667273413").correctedNumber(null).status(PhoneNumberStatus.VALID).build();
        PhoneNumberEntity phoneNumber1 = PhoneNumberEntity.builder().idNumber("103343262").originalNumber("6478342944").correctedNumber(null).status(PhoneNumberStatus.VALID).build();
        PhoneNumberEntity phoneNumber2 = PhoneNumberEntity.builder().idNumber("103343262").originalNumber("6478342944").correctedNumber("+27716125197").status(PhoneNumberStatus.CORRECTED).build();
        List<PhoneNumberEntity> response = Arrays.asList(phoneNumber, phoneNumber1, phoneNumber2);

        Mockito.when(phoneNumberRepository.findAll()).thenReturn(response);
        List<PhoneNumber> phoneNumberList = service.correctPhoneNumbers();
        assertEquals(phoneNumberList.size(), 73);
        assertEquals(phoneNumberList.get(3).getIdNumber(), "103391374");
        assertEquals(phoneNumberList.get(3).getOriginalNumber(), "714573497");
        assertEquals(phoneNumberList.get(3).getCorrectedNumber(), "+27714573497");
        assertEquals(phoneNumberList.get(3).getReason(), "Added prefix to the number");
    }

    @Test
    void getInvalidNumbersTest() throws Exception {

        PhoneNumberEntity phoneNumber = PhoneNumberEntity.builder().idNumber("103243034").originalNumber("81667273413").correctedNumber(null).status(PhoneNumberStatus.VALID).build();
        PhoneNumberEntity phoneNumber1 = PhoneNumberEntity.builder().idNumber("103426000").originalNumber("6478342944").correctedNumber(null).status(PhoneNumberStatus.INVALID).build();
        PhoneNumberEntity phoneNumber2 = PhoneNumberEntity.builder().idNumber("103343262").originalNumber("6478342944").correctedNumber("+27716125197").status(PhoneNumberStatus.CORRECTED).build();
        List<PhoneNumberEntity> response = Arrays.asList(phoneNumber, phoneNumber1, phoneNumber2);

        Mockito.when(phoneNumberRepository.findAll()).thenReturn(response);
        List<PhoneNumber> phoneNumberList = service.incorrectPhoneNumbers();
        assertEquals(phoneNumberList.size(), 462);
        assertEquals(phoneNumberList.get(10).getIdNumber(), "103269768");
        assertEquals(phoneNumberList.get(10).getOriginalNumber(), "14013477302");
    }

    @Test
    void checkNumberTest() throws Exception {

        PhoneNumberEntity phoneNumber = PhoneNumberEntity.builder().idNumber("103243034").originalNumber("81667273413").correctedNumber(null).status(PhoneNumberStatus.VALID).build();

        Mockito.when(phoneNumberRepository.findByOriginalNumber(Mockito.anyString())).thenReturn(Optional.of(phoneNumber));
        PhoneNumber phone = service.checkPhoneNumber("27815580331");
        assertEquals(phone.getOriginalNumber(), "27815580331");
        assertEquals(phone.getIdNumber(), "103426147");
        assertEquals(phone.getStatus(), "VALID");
    }
}
