package com.msa.rental.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentItem {
    private Item item;
    private LocalDate rentDate;

    private boolean overdued;

    private LocalDate overdueDate;

    public static RentItem createRentItem(Item item){
        return new RentItem(item,LocalDate.now(),false,LocalDate.now().plusDays(14));
    }

    public static RentItem sample(){
        return RentItem.createRentItem(Item.sample());
    }
}
