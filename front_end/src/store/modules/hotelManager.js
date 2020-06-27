import {
    addRoomAPI,
    addHotelAPI,
    mgrHotelListAPI,
} from '../../api/hotelManager'
import {
    checkOutAPI,
    getAllOrdersAPI,
    managedHotelOrdersAPI,
} from '../../api/order'
import {
    addCouponAPI,
    hotelAllCouponsAPI,
} from '../../api/coupon'
import { message } from 'ant-design-vue'

const hotelManager = {
    state: {
        orderList: [],
        managedOrders: [],
        mgrHotelList: [],
        addHotelParams: {
            name: '',
            address: '',
            bizRegion:'XiDan',
            hotelStar:'',
            rate: 0,
            description:'',
            phoneNum:'',
            managerId:'',
        },
        addHotelModalVisible: false,
        addRoomParams: {
            roomType: '',
            hotelId: '',
            price: '',
            total: 0,
            curNum: 0,
        },
        addRoomModalVisible: false,
        couponVisible: false,
        addCouponVisible: false,
        orderVisible:false,
        mgrOrderList: [],
        activeHotelId: 0,
        couponList: [],
    },
    mutations: {
        set_orderList: function(state, data) {
            state.orderList = data
        },
        set_addHotelModalVisible: function(state, data) {
            state.addHotelModalVisible = data
        },
        set_addHotelParams: function(state, data) {
            state.addHotelParams = {
                ...state.addHotelParams,
                ...data,
            }
        },
        set_addRoomModalVisible: function(state, data) {
            state.addRoomModalVisible = data
        },
        set_addRoomParams: function(state, data) {
            state.addRoomParams = {
                ...state.addRoomParams,
                ...data,
            }
        },
        set_couponVisible: function(state, data) {
            state.couponVisible = data
        },
        set_activeHotelId: function(state, data) {
            state.activeHotelId = data
        },
        set_couponList: function(state, data) {
            state.couponList = data
        },
        set_addCouponVisible: function(state, data) {
            state.addCouponVisible =data
        },
        set_orderVisible: function (state, data) {
            console.log('ordervisible')
            state.orderVisible = data
        },
        set_managedHotelOrders: function (state, data) {
            state.mgrOrderList = data
        },
        set_mgrHotelList: function (state, data) {
            state.mgrHotelList = data
            //console.log(data)
        },
    },
    actions: {
        getMgrHotelList: async ({state, commit}, id) => {
            console.log(id)
            const res = await mgrHotelListAPI(id)

            if (res) {
                commit('set_mgrHotelList', res)
            }
        },
        getAllOrders: async({ state, commit }) => {
            const res = await getAllOrdersAPI()
            if(res){
                commit('set_orderList', res)
            }
        },
        addHotel: async({ state, dispatch, commit }) => {
            const res = await addHotelAPI(state.addHotelParams)
            if(res){
                dispatch('getHotelList')
                commit('set_addHotelParams', {
                    name: '',
                    address: '',
                    bizRegion:'XiDan',
                    hotelStar:'',
                    rate: 0,
                    description:'',
                    phoneNum:'',
                    managerId:'',
                })
                commit('set_addHotelModalVisible', false)
                message.success('添加成功')
            }else{
                message.error('添加失败')
            }
        },
        addRoom: async({ state, dispatch, commit }) => {
            const res = await addRoomAPI(state.addRoomParams)
            if(res===null){
                commit('set_addRoomModalVisible', false)
                commit('set_addRoomParams', {
                    roomType: '',
                    hotelId: '',
                    price: '',
                    total: 0,
                    curNum: 0,
                });
                message.success('添加成功')
            }else{
                message.error('添加失败')
            }
        },
        getHotelCoupon: async({ state, commit }) => {
            const res = await hotelAllCouponsAPI(state.activeHotelId);
            if(res) {
                // 获取到酒店策略之后的操作（将获取到的数组赋值给couponList）
                commit('set_couponList',res);
                //commit 又是什么手法法啊，草
            }
        },
        addHotelCoupon: async({ commit, dispatch }, data) => {
            //todo
            console.log("enterAddCouponAPI");
            console.log("data",data);
            let res = await addCouponAPI(data);

            if(res){
                dispatch('getHotelCoupon');
                message.success("添加成功");
            }else{
                message.error("添加失败");
            }
        },
        checkOut: async({ state, dispatch }, id) => {

            let res = await checkOutAPI(id);
            dispatch('getAllOrders');
            if(res){
                message.success("已完成");
            }else{
                message.error("fail");
            }
        },
        getManagedOrders: async ({state, commit}) => {
            console.log(state.activeHotelId)
            const res = await managedHotelOrdersAPI(state.activeHotelId)
            if (res) {
                commit('set_managedHotelOrders', res)
            }
        }

    }
}
export default hotelManager