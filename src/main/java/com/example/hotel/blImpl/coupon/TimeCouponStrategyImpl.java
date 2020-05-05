package com.example.hotel.blImpl.coupon;

import com.example.hotel.bl.coupon.CouponMatchStrategy;
import com.example.hotel.po.Coupon;
import com.example.hotel.util.CouponType;
import com.example.hotel.vo.OrderVO;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class TimeCouponStrategyImpl implements CouponMatchStrategy {


    /**
     * 判断某个订单是否满足某种限时优惠策略
     * @param orderVO
     * @param coupon
     * @return
     */
    @Override
    @CouponType(CouponType.TimeCouponStrategy)
    public boolean isMatch(OrderVO orderVO, Coupon coupon) {

        String orderDate = orderVO.getCreateDate(); //"yyyy-mm-dd"
        LocalDateTime orderTime = LocalDateTime.of(
                Integer.valueOf(orderDate.substring(0, 4)),
                Integer.valueOf(orderDate.substring(5, 7)),
                Integer.valueOf(orderDate.substring(8, 10)),
                0, 0, 0, 0);

        return orderTime.compareTo(coupon.getStartTime()) >= 0 &&
                orderTime.compareTo(coupon.getEndTime()) <= 0;
    }
}
