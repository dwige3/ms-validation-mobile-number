package it.interlogica.crm.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.interlogica.crm.model.PhoneNumber;
import it.interlogica.crm.service.PhoneNumberService;
import it.interlogica.crm.utils.UserModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/phone-numbers")
@Api(value = "Mobile Number Validation", description = "Operations for validating South African mobile numbers")
public class PhoneNumberController {

    @Autowired
    private PhoneNumberService phoneNumberService;

    /*************************************** Store to DB sqlServer ************************************************************/
 /*   @ApiOperation(
            value = "upload file csv ",
            notes = " file Mobile Phone upload",
            response = String.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value =
            {@ApiResponse(code = 200, message = "File uploaded con successo"),
                    @ApiResponse(code = 401, message = "Non sei AUTENTICATO"),
                    @ApiResponse(code = 500, message = "Internal Server Error")
            })
    @PostMapping(path = "/interlogica/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadMobilePhone(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Received file for uploading: {}", file.getOriginalFilename());
            phoneNumberService.insertPhoneNumber(file.getInputStream());
            log.info("File MobilePhone uploaded successfully");
            return ResponseEntity.ok("File processed and data saved successfully");
        } catch (IOException e) {
            log.error("Error reading CSV file", e);
            return ResponseEntity.badRequest().body("Error reading CSV file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Error parsing CSV file", e);
            return ResponseEntity.badRequest().body("Error parsing CSV file: " + e.getMessage());
        } catch (DataAccessException e) {
            log.error("Error saving MobilePhone to database", e);
            return ResponseEntity.badRequest().body("Error saving MobilePhone to database: " + e.getMessage());
        }

    }

    @ApiOperation(
            value = "Servizio per il recupero dei numeri telefoni sud africani validi",
            notes = " Restitiure un collection di PhoneNumber validi in formato JSON",
            response = PhoneNumber.class,
            produces = "application/json")
    @ApiResponses(value =
            {@ApiResponse(code = 200, message = "Successfully Valid PhoneNumber", response = PhoneNumber.class),
                    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
                    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 500, message = "Application failed to process the request")
            })
    @GetMapping(path = "/acceptable", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PhoneNumber>> getAcceptableNumbers() {
        log.info("Init request: /api/v1/phone-numbers/acceptable");
        List<PhoneNumber> response = phoneNumberService.getValidNumbers();
        log.info("Success: /api/v1/phone-numbers/acceptable");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Servizio per il recupero dei numeri telefoni sud africani corretti",
            notes = " Restitiure un collection di numeri corretti in formato JSON",
            response = PhoneNumber.class,
            produces = "application/json")
    @ApiResponses(value =
            {@ApiResponse(code = 200, message = "Successfully Corrected PhoneNumber", response = PhoneNumber.class),
                    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
                    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 500, message = "Application failed to process the request")
            })
    @GetMapping(path = "/corrected", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PhoneNumber>> getCorrectedNumbers() {
        log.info("Init request: /api/v1/phone-numbers/corrected");
        List<PhoneNumber> response = phoneNumberService.getCorrectedNumbers();
        log.info("Success: /api/v1/phone-numbers/corrected");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Servizio per il recupero dei numeri telefoni sud africani invalidi",
            notes = " Restitiure un collection di numeri invalidi in formato JSON",
            response = PhoneNumber.class,
            produces = "application/json")
    @ApiResponses(value =
            {@ApiResponse(code = 200, message = "Successfully Invalid PhoneNumber", response = PhoneNumber.class),
                    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
                    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 500, message = "Application failed to process the request")
            })
    @GetMapping(path = "/invalid", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PhoneNumber>> getInvalidNumbers() {
        log.info("Init request: /api/v1/phone-numbers/invalid");
        List<PhoneNumber> response = phoneNumberService.getInvalidNumbers();
        log.info("Success: /api/v1/phone-numbers/invalid");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Servizio per i controllo dei numeri telefoni sud africani",
            notes = " Restitiure un numero di telefono in formato JSON",
            response = PhoneNumber.class,
            produces = "application/json")
    @ApiResponses(value =
            {@ApiResponse(code = 200, message = "Successfully checkNumber PhoneNumber", response = PhoneNumber.class),
                    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
                    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 500, message = "Application failed to process the request")
            })
    @GetMapping(path = "/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhoneNumber> checkNumber(@PathVariable String number) {
        PhoneNumber phoneNumber = phoneNumberService.checkNumber(number);
        if (phoneNumber == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(phoneNumber, HttpStatus.OK);
    }*/

    /*************************************** Store to Temporary File ************************************************************/
    @ApiOperation(
            value = "File csv contenente l'elenco dei numeri",
            notes = "file csv Mobile Phone",
            response = String.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value =
            {@ApiResponse(code = 200, message = "File uploaded con successo"),
                    @ApiResponse(code = 401, message = "Non sei AUTENTICATO"),
                    @ApiResponse(code = 500, message = "Internal Server Error")
            })
    @PostMapping(value = "/tmp/uploadFileCsv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> processCsv(@RequestParam("file") MultipartFile file) {
        try {
            phoneNumberService.uploadFile(file);
            return ResponseEntity.ok().body("File processed and data saved successfully");
        } catch (IOException e) {
            log.error("Error reading CSV file", e);
            return ResponseEntity.badRequest().body("An error occurred while processing the file: " + e.getMessage());
        }
    }

    @ApiOperation(value = "Validate a mobile number", response = PhoneNumber.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully validated the mobile number"),
            @ApiResponse(code = 400, message = "Bad request, invalid mobile number format"),
            @ApiResponse(code = 404, message = "The mobile number is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(path = "/tmp/checkNumber/{originalNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhoneNumber> getPhoneByOriginalNumber(@PathVariable String originalNumber) throws IOException {
        PhoneNumber phoneNumber = phoneNumberService.checkPhoneNumber(originalNumber);
        if (phoneNumber == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(phoneNumber, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Servizio per il recupero dei numeri telefoni sud africani corretti",
            notes = " Restitiure un collection di numeri corretti in formato JSON",
            response = PhoneNumber.class,
            produces = "application/json")
    @ApiResponses(value =
            {@ApiResponse(code = 200, message = "Successfully Corrected PhoneNumber", response = PhoneNumber.class),
                    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
                    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
                    @ApiResponse(code = 500, message = "Application failed to process the request")
            })
    @GetMapping(path = "/tmp/corrected", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PhoneNumber>> getCorrectedPhoneNumbers() throws IOException {
        log.info("Init request: /api/v1/phone-numbers/tmp/corrected");
        List<PhoneNumber> response = phoneNumberService.correctPhoneNumbers();
        log.info("Success: /api/v1/phone-numbers/tmp/corrected");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Servizio per il recupero dei numeri telefoni sud africani invalidi",
            notes = "Get all invalid Phone Number", response = PhoneNumber.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved data"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(path = "/tmp/invalid", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PhoneNumber>> getIncorrectPhoneNumbers() throws IOException {
        log.info("Init request: /api/v1/phone-numbers/tmp/invalid");
        List<PhoneNumber> response = phoneNumberService.incorrectPhoneNumbers();
        log.info("Success: /api/v1/phone-numbers/tmp/invalid");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Servizio per il recupero dei numeri telefoni sud africani validi",
            notes = "Get all acceptable Phone Number", response = PhoneNumber.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved data"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(path = "/tmp/acceptable", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PhoneNumber>> getAcceptablePhoneNumbers() throws IOException {
        log.info("Init request: /api/v1/phone-numbers/tmp/acceptable");
        List<PhoneNumber> response = phoneNumberService.acceptablePhoneNumbers();
        log.info("Success: /api/v1/phone-numbers/tmp/acceptable");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Servizio per la validazione di un numero sud africani",
            notes = "Validate a mobile number", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully validated the mobile number"),
            @ApiResponse(code = 400, message = "Bad request, invalid mobile number format"),
            @ApiResponse(code = 404, message = "The mobile number is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/validate/{mobileNumber}")
    public ResponseEntity<String> validateMobileNumber(@PathVariable String mobileNumber) {
        log.info("Inserisci il numero di cellulare da testare: {}", mobileNumber);
        String response = UserModule.testSingleNumber(mobileNumber);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}