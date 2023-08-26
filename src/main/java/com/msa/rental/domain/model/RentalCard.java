package com.msa.rental.domain.model;

import com.msa.rental.domain.model.vo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalCard {
    private RentalCardNo rentalCardNo;
    private IDName member;
    private RentStatus rentStatus;
    private LateFee totalLateFee;
    private List<RentItem> rentItemList = new ArrayList<RentItem>();
    private List<ReturnItem> returnItemList = new ArrayList<ReturnItem>();

    private void addRentItem(RentItem rentItem){
        this.rentItemList.add(rentItem);
    }

    private void removeRentItem(RentItem rentItem){
        this.rentItemList.remove(rentItem);
    }
    private void addReturnItem(ReturnItem returnItem)
    {
        this.returnItemList.add(returnItem);
    }
    public static RentalCard sample(){
        RentalCard rentalCard = new RentalCard();
        rentalCard.setRentalCardNo(RentalCardNo.createRentalCardNo());
        rentalCard.setMember(IDName.sample());
        rentalCard.setRentStatus(RentStatus.RENT_AVAILABLE);
        rentalCard.setTotalLateFee(LateFee.sample());
        return rentalCard;
    }

    public static RentalCard createRentalCard(IDName creater){
        RentalCard rentalCard = new RentalCard();
        rentalCard.setRentalCardNo(RentalCardNo.createRentalCardNo());
        rentalCard.setMember(creater);
        rentalCard.setRentStatus(RentStatus.RENT_AVAILABLE);
        rentalCard.setTotalLateFee(LateFee.creatLateFee());
        return rentalCard;
    }

    public  RentalCard rentItem(Item item) throws Exception {
        checkRentAvailable();
        this.addRentItem(RentItem.createRentItem(item));
        return this;
    }

    private  void checkRentAvailable() throws Exception {
        if (this.rentStatus == RentStatus.RENT_UNAVAILABLE) throw new IllegalAccessException("대여불가 상태입니다.");
        if (this.returnItemList.size() > 5) throw new Exception("이미 5권을 대여했습니다.");
    }

    public RentalCard returnItem(Item rentItem, LocalDate returnDate){
        RentItem rentedItem = this.rentItemList.stream().filter(i -> i.getItem().equals(rentItem)).findFirst().get();
        calcalateLateFee(rentedItem,returnDate);
        this.addReturnItem(ReturnItem.creatReturnItem(rentedItem));
        this.removeRentItem(rentedItem);
        return this;
    }

    private void calcalateLateFee(RentItem rentItem, LocalDate returnDate) {
        if(returnDate.compareTo(rentItem.getOverdueDate())>0){
            Integer latefee;
            latefee = Period.between(rentItem.getOverdueDate(), returnDate).getDays() * 10;
            LateFee lateFee = this.totalLateFee.addPoint(latefee);
            this.setTotalLateFee(lateFee);
        }
    }

    public RentalCard overdueItem(Item item){
        RentItem rentItem = this.rentItemList.stream().filter(i -> i.getItem().equals(item)).findFirst().get();
        rentItem.setOverdued(true);
        this.setRentStatus(RentStatus.RENT_UNAVAILABLE);
        rentItem.setOverdueDate(LocalDate.now().minusDays(1));
        return this;
    }

    public Integer makeAvailableRental(Integer point) throws Exception {
        if (this.rentItemList.size() != 0) throw new IllegalArgumentException("모든 도서가 반납되어야 정지를 해제 할 수 있습니다.");
        if (this.getTotalLateFee().getPoint() != point) throw  new IllegalArgumentException("해당 포인트로 연체를 해제할 수 없습니다.");
        this.setTotalLateFee(totalLateFee.removePoint(point));
        if (this.getTotalLateFee().getPoint() == 0)
        {
            this.rentStatus = RentStatus.RENT_AVAILABLE;
        }
        return this.getTotalLateFee().getPoint();
    }

}
