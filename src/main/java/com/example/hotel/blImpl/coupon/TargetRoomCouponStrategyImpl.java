package com.example.hotel.blImpl.coupon;

import com.example.hotel.bl.coupon.CouponMatchStrategy;
import com.example.hotel.po.Coupon;
import com.example.hotel.util.CouponType;
import com.example.hotel.vo.OrderVO;
import org.springframework.stereotype.Service;

@Service
public class TargetRoomCouponStrategyImpl implements CouponMatchStrategy {


    /**
     * 判断某个订单是否满足某种多间优惠策略
     * @param orderVO
     * @param coupon
     * @return
     */
    @Override
    @CouponType(CouponType.TargetRoomCouponStrategy)
    public boolean isMatch(OrderVO orderVO, Coupon coupon) {

        return orderVO.getRoomNum() >= coupon.getTargetMoney();

    }
}
