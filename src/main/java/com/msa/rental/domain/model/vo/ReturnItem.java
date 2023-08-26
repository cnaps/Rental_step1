package com.msa.rental.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnItem {
    private RentItem rentItem;
    private LocalDate returnDate;

    public static ReturnItem creatReturnItem(RentItem rentItem){
        return new ReturnItem(rentItem,LocalDate.now());
    }

    public static ReturnItem sample(){
        return ReturnItem.creatReturnItem(RentItem.sample());
    }


}
