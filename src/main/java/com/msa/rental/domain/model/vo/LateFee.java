package com.msa.rental.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LateFee {
    private Integer point;

    public LateFee addPoint(Integer point){
        return new LateFee(this.point + point);
    }

    public LateFee removePoint(Integer point) throws Exception {
        if(point > this.point){
            throw new Exception("보유한 포이트보다 커서 삭제할수 없습니다.");
        }
        return new LateFee(this.point-point);
    }

    public static LateFee sample(){
        return new LateFee(100);
    }

    public static LateFee creatLateFee(){
        return new LateFee(0);
    }
}
