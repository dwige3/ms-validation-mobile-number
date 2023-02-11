package it.interlogica.crm.service;

import it.interlogica.crm.model.PhoneNumber;
import it.interlogica.crm.model.enumerator.PhoneNumberStatus;
import it.interlogica.crm.repository.PhoneNumberRepository;
import it.interlogica.crm.utils.UserModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private static final String TEMP_FILE_NAME = "interlogica-phoneNumber.tmp";

    @Autowired
    private PhoneNumberRepository phoneNumberRepository;


    /**
     * @param inputStream
     */
/*    @Override
    public void insertPhoneNumber(InputStream inputStream) throws IOException {

        List<PhoneNumberEntity> mobilePhoneEntities = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            // skip first line
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 2) {
                    throw new IllegalArgumentException("Invalid number of columns in CSV file. Expected 2, found " + values.length);
                }
                PhoneNumberEntity mobilePhone = new PhoneNumberEntity();
                mobilePhone.setIdNumber(values[0]);
                mobilePhone.setOriginalNumber(values[1]);
                if (UserModule.isValid(values[1])) {
                    mobilePhone.setStatus(PhoneNumberStatus.VALID);
                } else {
                    String correctedPhoneNumber = correctPhoneNumber(values[1]);
                    if (correctedPhoneNumber != null) {
                        mobilePhone.setCorrectedNumber(correctedPhoneNumber);
                        mobilePhone.setStatus(PhoneNumberStatus.CORRECTED);
                    } else {
                        mobilePhone.setStatus(PhoneNumberStatus.INVALID);
                    }
                }
                mobilePhoneEntities.add(mobilePhone);
            }
        }
        log.info("Read {} mobilePhone from CSV file", mobilePhoneEntities.size());
        phoneNumberRepository.saveAll(mobilePhoneEntities);
        log.info("Saved {} mobilePhone to database", mobilePhoneEntities.size());
    }
*/

    /**
     * @return
     */
 /*   @Override
    public List<PhoneNumber> getValidNumbers() {
        return phoneNumberRepository.findAll().stream()
                .filter(phone -> phone.getStatus().equals(PhoneNumberStatus.VALID))
                .map(phoneNumberValid -> PhoneNumber.builder()
                        .idNumber(phoneNumberValid.getIdNumber())
                        .originalNumber(phoneNumberValid.getOriginalNumber())
                        .status(phoneNumberValid.getStatus().name())
                        .build()).collect(Collectors.toList());
    }
*/
    /**
     * @return
     */
 /*   @Override
    public List<PhoneNumber> getCorrectedNumbers() {
        return phoneNumberRepository.findAll().stream()
                .filter(phone -> phone.getStatus().equals(PhoneNumberStatus.CORRECTED))
                .map(phoneNumberCorrected -> PhoneNumber.builder()
                        .idNumber(phoneNumberCorrected.getIdNumber())
                        .correctedNumber(phoneNumberCorrected.getCorrectedNumber())
                        .originalNumber(phoneNumberCorrected.getOriginalNumber())
                        .status(phoneNumberCorrected.getStatus().name())
                        .build()).collect(Collectors.toList());
    }
*/
    /**
     * @return
     */
 /*   @Override
    public List<PhoneNumber> getInvalidNumbers() {
        return phoneNumberRepository.findAll().stream()
                .filter(phone -> phone.getStatus().equals(PhoneNumberStatus.INVALID))
                .map(phoneNumberInvalid -> PhoneNumber.builder()
                        .idNumber(phoneNumberInvalid.getIdNumber())
                        .originalNumber(phoneNumberInvalid.getOriginalNumber())
                        .status(phoneNumberInvalid.getStatus().name())
                        .build()).collect(Collectors.toList());
    }
*/
    /**
     * @param number
     * @return
     */
  /*  @Override
    @Transactional
    public PhoneNumber checkNumber(String number) {

        PhoneNumberEntity phoneNumberEntity = phoneNumberRepository.findByOriginalNumber(number)
                .orElseThrow(() -> new EntityNotFoundException(String.format("PhoneNumber not found with originalNumber [%s]", number)));
        return PhoneNumber.builder().idNumber(phoneNumberEntity.getIdNumber())
                .originalNumber(phoneNumberEntity.getOriginalNumber())
                .correctedNumber(phoneNumberEntity.getCorrectedNumber())
                .status(phoneNumberEntity.getStatus().name())
                .build();
//        return PhoneNumberMapper.INSTANCE.toPhoneNumber(phoneNumberEntity);
    }
*/
    /*************************************** Store to Temporary File ************************************************************/

    /**
     * @param file
     * @return
     */
    @Override
    public void uploadFile(MultipartFile file) throws IOException {
        List<PhoneNumber> phoneNumberList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 2) {
                    throw new IllegalArgumentException("Invalid number of columns in CSV file. Expected 2, found " + values.length);
                }
                PhoneNumber mobilePhone = new PhoneNumber();
                mobilePhone.setIdNumber(values[0]);
                mobilePhone.setOriginalNumber(values[1]);
                if (UserModule.isValid(values[1])) {
                    mobilePhone.setStatus(PhoneNumberStatus.VALID.name());
                } else {
                    mobilePhone = UserModule.validateNumber(values[0], values[1]);
                }
                phoneNumberList.add(mobilePhone);
            }
        }
        saveFile(phoneNumberList);
    }

    /**
     * @param originalNumber
     * @return
     */
    @Override
    public PhoneNumber checkPhoneNumber(String originalNumber) throws IOException {
        List<PhoneNumber> phoneNumbers = loadData();
        return phoneNumbers.stream().filter(phoneNumber -> phoneNumber
                        .getOriginalNumber().equals(originalNumber)).findFirst()
                .orElse(null);
    }

    /**
     * @return
     */
    @Override
    public List<PhoneNumber> correctPhoneNumbers() throws IOException {
        return loadData().stream()
                .filter(x -> PhoneNumberStatus.CORRECTED.name().equals(x.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * @return
     */
    @Override
    public List<PhoneNumber> incorrectPhoneNumbers() throws IOException {
        return loadData().stream()
                .filter(x -> PhoneNumberStatus.INVALID.name().equals(x.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * @return
     */
    @Override
    public List<PhoneNumber> acceptablePhoneNumbers() throws IOException {
        return loadData().stream()
                .filter(x -> PhoneNumberStatus.VALID.name().equals(x.getStatus()))
                .collect(Collectors.toList());
    }

    private void saveFile(List<PhoneNumber> phoneNumbers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEMP_FILE_NAME))) {
            for (PhoneNumber phoneNumber : phoneNumbers) {
                writer.write(phoneNumber.getIdNumber() + "," + phoneNumber.getOriginalNumber()
                        + "," + phoneNumber.getCorrectedNumber() + "," + phoneNumber.getStatus() + "," + phoneNumber.getReason());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("An error occurred while saving data to the temporary file", e);
        }
    }

    private List<PhoneNumber> loadData() throws IOException {
        List<PhoneNumber> phoneNumberList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TEMP_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                PhoneNumber mobilePhone = new PhoneNumber();
                mobilePhone.setIdNumber(parts[0]);
                mobilePhone.setOriginalNumber(parts[1]);
                mobilePhone.setCorrectedNumber(parts[2]);
                mobilePhone.setStatus(parts[3]);
                mobilePhone.setReason(parts[4]);

                phoneNumberList.add(mobilePhone);
            }
        }
        return phoneNumberList;
    }

}
