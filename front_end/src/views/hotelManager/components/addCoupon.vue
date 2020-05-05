<template>
    <a-modal
        :visible="addCouponVisible"
        title="添加优惠策略"
        cancelText="取消"
        okText="确定"
        @cancel="cancel"
        @ok="handleSubmit"
    >
        <!-- 这里是添加策略模态框区域，请编写表单 -->
        <a-form  v-bind:form="form" >
            <a-form-item label="优惠券类型" v-bind="formItemLayout">
                <a-select
                        size="large"
                        placeholder="请选择优惠券类型"
                        v-decorator="[
                            'couponType',
                    { rules: [{ required: true, message: '宁还没选择优惠券类型' }] }]"
                >
                    <!--1生日特惠 2多间特惠 3满减优惠 4限时优惠； 感觉后端应该统一整成枚举量，拿一个数字来记录是什么东西-->
                    <a-select-option value="1">生日特惠</a-select-option>
                    <a-select-option value="2">多间特惠</a-select-option>
                    <a-select-option value="3">满减优惠</a-select-option>
                    <a-select-option value="4">限时优惠</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="优惠券名称" v-bind="formItemLayout">
                <a-input
                        size="large"
                        type="text"
                        placeholder="请输入优惠券名称"
                        v-decorator="[
                            'couponName',
                    {rules: [{ required: true, message: '宁还没输入优惠券名称' }], validateTrigger: 'blur'}
                  ]"
                >
                </a-input>
            </a-form-item>
            <a-form-item label="优惠券简介" v-bind="formItemLayout">
                <a-input
                        size="large"
                        type="text"
                        placeholder="请输入优惠券简介"
                        v-decorator="[
                            'description',
                    {rules: [{ required: true, message: '宁还没输入优惠券简介' }], validateTrigger: 'blur'}
                  ]"
                >
                </a-input>
            </a-form-item>
            <a-form-item label="达标金额" v-bind="formItemLayout">
                <a-input
                        size="large"
                        type="text"
                        placeholder="请输入达标金额"
                        v-decorator="[
                            'targetMoney',
                    {rules: [{ required: true, message: '宁还没输入达标金额' }, { validator: isMoney}], validateTrigger: 'blur'}
                  ]"
                >
                </a-input>
            </a-form-item >
            <a-form-item label="优惠金额" v-bind="formItemLayout">
                <a-input
                        size="large"
                        type="text"
                        placeholder="请输入优惠金额"
                        v-decorator="[
                            'discountMoney',
                    {rules: [{ required: true, message: '宁还没输入优惠金额' }, { validator: isMoney}], validateTrigger: 'blur'}
                  ]"
                >
                </a-input>
            </a-form-item>
        </a-form>
    </a-modal>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
export default {
    name: 'addCouponModal',
    data() {
        return {
            formItemLayout: {
                labelCol: {
                    xs: { span: 12 },
                    sm: { span: 6 },
                },
                wrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 },
                },
            },
            form: this.$form.createForm(this),
        }
    },
    computed: {
        ...mapGetters([
            'activeHotelId',
            'addCouponVisible',
        ])
    },
    beforeCreate() {
        // 表单名默认为“form”
        this.form = this.$form.createForm(this, { name: 'addCouponModal' });
    },
    mounted() {

    },
    methods: {
        ...mapMutations([
            'set_couponVisible',
            'set_addCouponVisible',
        ]),
        ...mapActions([
            // addHotelCoupon：添加酒店策略接口
            'addHotelCoupon'
        ]),
        isMoney(rule, value, callback){
            const regex = /^\+?[1-9][0-9]*$/;
            if(value===undefined || value===""){
                callback()
            }
            if (regex.test(value)) {
                callback()
            }
            else{
                callback(new Error('\t请输入一个正整数'));
            }
        },
        cancel() {
            this.set_addCouponVisible(false);
        },

        changeType(v){  //todo zjc
            if( v === '3') {

            }else{
                this.$message.warning('请实现')
            }
        },

        handleSubmit(e) {
            e.preventDefault();
            this.form.validateFieldsAndScroll((err, values) => {
                if (!err) {
                    const data = {
                        // 这里添加接口参数
                        //id:null, 我觉得... 优惠劵id应该是后端数据层生成的 ？？？
                        //status又是什么意思
                        hotelId:this.activeHotelId,
                        name:this.form.getFieldValue('couponName'),
                        type:this.form.getFieldValue('couponType'),
                        description:this.form.getFieldValue('description'),
                        targetMoney:this.form.getFieldValue('targetMoney'),
                        discountMoney:this.form.getFieldValue('discountMoney'),
                    };
                    this.addHotelCoupon(data);
                    this.form.setFieldsValue({
                        'couponName':'',
                        'couponType':'',
                        'description':'',
                        'targetMoney':'',
                        'discountMoney':'',
                    });
                    this.set_addCouponVisible(false);//设置成不可见
                    this.set_couponVisible(true);
                }
            });
        },
    }
}
</script>