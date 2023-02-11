package it.interlogica.crm;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.interlogica.crm.model.PhoneNumber;
import it.interlogica.crm.service.PhoneNumberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PhoneNumberControllerTest {

    public static final String PHONE_NUMBER_NAME = "mobileNumber.csv";

    public static final String apiPhoneNumber = "/api/v1/phone-numbers/tmp/";
    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PhoneNumberService phoneNumberService;

    @Value("classpath:South_African_Mobile_Numbers.csv")
    Resource file;

    protected MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc =
                MockMvcBuilders.webAppContextSetup(applicationContext)
                        .build();
    }

    @Test
    void testResourceFileNotNull() {
        assertThat(file).isNotNull();
        assertThat(file.exists()).isTrue();
    }

    @Test
    void getAcceptableNumbersTest() throws Exception {

        PhoneNumber phoneNumber = PhoneNumber.builder().idNumber("103300640").originalNumber("27736529279 ").correctedNumber(null).status("VALID").build();
        PhoneNumber phoneNumber1 = PhoneNumber.builder().idNumber("103426000").originalNumber("27718159078").correctedNumber(null).status("VALID").build();
        List<PhoneNumber> response = Arrays.asList(phoneNumber, phoneNumber1);

        final String expectedResponse = objectMapper.writeValueAsString(response);
        Mockito.when(phoneNumberService.acceptablePhoneNumbers()).thenReturn(response);
        mvc.perform(
                        MockMvcRequestBuilders
                                .get(apiPhoneNumber+"acceptable")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful()).andExpect(content().string(expectedResponse));
    }

    @Test
    void getCorrectedNumbersTest() throws Exception {

        PhoneNumber phoneNumber = PhoneNumber.builder().idNumber("103300640").originalNumber("730276061").correctedNumber("+27730276061").status("CORRECTED").build();
        PhoneNumber phoneNumber1 = PhoneNumber.builder().idNumber("103343262").originalNumber("6478342944").correctedNumber("+27716125197").status("CORRECTED").build();
        List<PhoneNumber> response = Arrays.asList(phoneNumber, phoneNumber1);

        Mockito.when(phoneNumberService.correctPhoneNumbers()).thenReturn(response);
        mvc.perform(
                        MockMvcRequestBuilders
                                .get(apiPhoneNumber+"corrected")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[0].status").value(equalTo("CORRECTED")))
                .andExpect(jsonPath("$.[0].originalNumber").value(equalTo("730276061")))
                .andExpect(jsonPath("$.[1].idNumber").value(equalTo("103343262")));
    }

    @Test
    void getInvalidNumbersTest() throws Exception {

        PhoneNumber phoneNumber = PhoneNumber.builder().idNumber("103243034").originalNumber("81667273413").correctedNumber(null).status("INVALID").build();
        PhoneNumber phoneNumber1 = PhoneNumber.builder().idNumber("103343262").originalNumber("6478342944").correctedNumber(null).status("INVALID").build();
        PhoneNumber phoneNumber2 = PhoneNumber.builder().idNumber("103343262").originalNumber("6478342944").correctedNumber(null).status("INVALID").build();
        List<PhoneNumber> response = Arrays.asList(phoneNumber, phoneNumber1, phoneNumber2);

        Mockito.when(phoneNumberService.incorrectPhoneNumbers()).thenReturn(response);
        mvc.perform(
                        MockMvcRequestBuilders
                                .get(apiPhoneNumber+"invalid")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.[1].status").value(equalTo("INVALID")))
                .andExpect(jsonPath("$.[0].originalNumber").value(equalTo("81667273413")))
                .andExpect(jsonPath("$.[1].originalNumber").value(equalTo("6478342944")))
                .andExpect(jsonPath("$.[2].idNumber").value(equalTo("103343262")));
    }

    @Test
    void checkNumberTest() throws Exception {

        PhoneNumber phoneNumber = PhoneNumber.builder().idNumber("103427882").originalNumber("27845128204").correctedNumber(null).status("VALID").build();

        Mockito.when(phoneNumberService.checkPhoneNumber(Mockito.anyString())).thenReturn(phoneNumber);
        mvc.perform(
                        MockMvcRequestBuilders
                                .get(apiPhoneNumber+"checkNumber/27845128204")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(equalTo("VALID")))
                .andExpect(jsonPath("$.originalNumber").value(equalTo("27845128204")))
                .andExpect(jsonPath("$.idNumber").value(equalTo("103427882")));
    }

    @Test
    void uploadMobilePhoneTest() throws Exception {
        MockMultipartFile fileCSV = new MockMultipartFile("file", PHONE_NUMBER_NAME,
                "text/csv", file.getInputStream());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilderCert =
                multipart(apiPhoneNumber+"uploadFileCsv")
                        .file(fileCSV)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE);

        ResultActions performUpdatePhoneNumber = mvc.perform(mockHttpServletRequestBuilderCert);
        performUpdatePhoneNumber
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("File processed and data saved successfully"));


    }

    @Test
    void uploadMobilePhoneErrorTest() throws Exception {
        MockMultipartFile file2 = new MockMultipartFile("file", "test.csv", PHONE_NUMBER_NAME, file.getInputStream());

        Mockito.doThrow(new IOException("error processing file"))
                .when(phoneNumberService).uploadFile(Mockito.any());

        mvc.perform(MockMvcRequestBuilders.multipart(apiPhoneNumber+"uploadFileCsv")
                        .file(file2))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("An error occurred while processing the file: error processing file"));
    }

}
