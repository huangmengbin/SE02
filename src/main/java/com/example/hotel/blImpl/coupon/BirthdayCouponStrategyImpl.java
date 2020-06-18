package com.example.hotel.blImpl.coupon;

import com.example.hotel.bl.coupon.CouponMatchStrategy;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.po.Coupon;
import com.example.hotel.util.CouponMatchStrategyService;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.po.User;
import org.springframework.beans.factory.annotation.Autowired;

@CouponMatchStrategyService(CouponMatchStrategyService.BirthdayCouponStrategy)
public class BirthdayCouponStrategyImpl implements CouponMatchStrategy {

    @Autowired
    AccountService accountService;

    /**
     * 判断某个订单是否满足某种生日优惠策略
     * @param orderVO
     * @return
     */

    @Override
    public boolean isMatch(OrderVO orderVO, Coupon coupon) {
        User user = accountService.getUserInfo(orderVO.getUserId());
    //todo
        return true;
    }
}
