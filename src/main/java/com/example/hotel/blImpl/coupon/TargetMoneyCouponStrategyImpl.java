package com.example.hotel.blImpl.coupon;

import com.example.hotel.bl.coupon.CouponMatchStrategy;
import com.example.hotel.po.Coupon;
import com.example.hotel.util.CouponMatchStrategyService;
import com.example.hotel.vo.OrderVO;

@CouponMatchStrategyService(CouponMatchStrategyService.TargetMoneyCouponStrategy)
public class TargetMoneyCouponStrategyImpl implements CouponMatchStrategy {


    /**
     * 判断某个订单是否满足某种满减金额优惠策略
     * @param orderVO
     * @param coupon
     * @return
     */
    @Override
    public boolean isMatch(OrderVO orderVO, Coupon coupon) {

        return orderVO.getPrice() >= coupon.getTargetMoney();

    }
}
