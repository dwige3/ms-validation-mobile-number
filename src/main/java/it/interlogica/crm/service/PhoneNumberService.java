package it.interlogica.crm.service;

import it.interlogica.crm.model.PhoneNumber;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface PhoneNumberService {
   /* void insertPhoneNumber(InputStream inputStream) throws IOException;

    List<PhoneNumber> getValidNumbers();

    List<PhoneNumber> getCorrectedNumbers();

    List<PhoneNumber> getInvalidNumbers();

    PhoneNumber checkNumber(String number);*/

    void uploadFile(MultipartFile file) throws IOException;

    PhoneNumber checkPhoneNumber(String originalNumber) throws IOException;

    List<PhoneNumber> correctPhoneNumbers() throws IOException;

    List<PhoneNumber> incorrectPhoneNumbers() throws IOException;

    List<PhoneNumber> acceptablePhoneNumbers() throws IOException;
}
