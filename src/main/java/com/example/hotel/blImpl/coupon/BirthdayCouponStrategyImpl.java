package com.example.hotel.blImpl.coupon;

import com.example.hotel.bl.coupon.CouponMatchStrategy;
import com.example.hotel.po.Coupon;
import com.example.hotel.util.CouponMatchStrategyService;
import com.example.hotel.vo.OrderVO;

@CouponMatchStrategyService(CouponMatchStrategyService.BirthdayCouponStrategy)
public class BirthdayCouponStrategyImpl implements CouponMatchStrategy {

    /**
     * 判断某个订单是否满足某种生日优惠策略
     * @param orderVO
     * @return
     */

    @Override
    public boolean isMatch(OrderVO orderVO, Coupon coupon) {
        if(coupon.getEndTime()!=null){
            //todo lz
        }
        return true;
    }
}
