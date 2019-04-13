package com.easysettle.service.dto;

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
public class NewPaymentRequest implements Serializable {

    private Double amount;

    private LocalDate date;

    private Long group_id;

    private Long payer_id;

    private String name;

    private List<Long> loanersList;

}
