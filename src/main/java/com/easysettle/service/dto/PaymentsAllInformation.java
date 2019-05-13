package com.easysettle.service.dto;

import com.easysettle.domain.Payments;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentsAllInformation implements Serializable {

    private Double amount;

    private String name;

    private LocalDate date;

    private String payerName;

    private List<String> loanersNameList;

}
