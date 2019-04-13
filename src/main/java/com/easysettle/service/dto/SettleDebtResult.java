package com.easysettle.service.dto;

import com.easysettle.domain.Members;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SettleDebtResult {

    private Members paymentFrom;
    private Members paymentTo;
    private Double amount;

}
