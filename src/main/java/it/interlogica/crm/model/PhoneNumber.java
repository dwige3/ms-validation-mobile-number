package it.interlogica.crm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumber implements Serializable {

    private String idNumber;

    private String originalNumber;

    private String correctedNumber;

    private String reason;

    private String status;
}
