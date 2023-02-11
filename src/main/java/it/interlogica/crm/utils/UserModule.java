package it.interlogica.crm.utils;

import it.interlogica.crm.model.PhoneNumber;
import it.interlogica.crm.model.enumerator.PhoneNumberStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserModule {

    private static final String PHONE_NUMBER_PATTERN = "^27[0-9]{9}$";
    private static final String INTERNATIONAL_PREFIX = "+27";
    private static final int LENGTH = 9;

    private UserModule() {
        throw new IllegalStateException("Utility class");
    }

    public static String testSingleNumber(String inputNumber) {

        String message = null;

        PhoneNumber mobileNumber = validateNumber(null, inputNumber);
        String correctedNumber = mobileNumber.getCorrectedNumber();
        String reason = mobileNumber.getReason();
        String status = mobileNumber.getStatus();

        if (isValid(inputNumber)) {
            log.info("Il numero di cellulare " + inputNumber + " è corretto.");
            message = "Successo: Il numero di cellulare " + inputNumber + " è corretto.";
        } else if (status.equals(PhoneNumberStatus.INVALID.name())) {
            message = "Errore: Il numero di cellulare " + inputNumber + " è errato. " + reason + ". Non puo essere corretto .";
        } else {
            log.info("Il numero di cellulare " + inputNumber + " non è corretto. " + reason + ". Il formato corretto è " + correctedNumber + ".");
            message = "Errore: Il numero di cellulare " + inputNumber + " non è corretto. " + reason + ". Il formato corretto è " + correctedNumber + ".";
        }
        return message;
    }

    public static boolean isValid(String phoneNumber) {
        return phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }

    public static PhoneNumber validateNumber(String idNumber, String originalNumber) {

        PhoneNumber phoneNumber = PhoneNumber.builder()
                .idNumber(idNumber)
                .originalNumber(originalNumber)
                .build();

        log.info("Remove any spaces or dashes");
        String strippedNumber = originalNumber.replaceAll("[^\\d]", "");
        if (strippedNumber.length() == LENGTH) {

            phoneNumber.setCorrectedNumber(INTERNATIONAL_PREFIX + strippedNumber);
            phoneNumber.setReason("Added prefix to the number");
            phoneNumber.setStatus(PhoneNumberStatus.CORRECTED.name());
            return phoneNumber;
        }
        if (strippedNumber.length() == LENGTH + 1 && strippedNumber.startsWith("0")) {

            phoneNumber.setCorrectedNumber(INTERNATIONAL_PREFIX + strippedNumber.substring(1));
            phoneNumber.setReason("Added missing 3. and Removed leading 0.");
            phoneNumber.setStatus("CORRECTED");
            return phoneNumber;
        }
        if (!strippedNumber.startsWith("27") || strippedNumber.length() != 10) {
            phoneNumber.setStatus(PhoneNumberStatus.INVALID.name());
            phoneNumber.setReason("Invalid format. Must start with 27 and have 10 digits.");
            return phoneNumber;
        }
        phoneNumber.setStatus(PhoneNumberStatus.VALID.name());

        return phoneNumber;
    }
}
