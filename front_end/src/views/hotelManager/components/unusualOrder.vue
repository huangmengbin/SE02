<template>
    <div>
        <a-drawer
                title="当日异常订单"
                placement="top"
                :closable="true"
                height="666"
                :visible="unusualOrderVisible"
                @close="onClose"
        >
            <a-table
                    size="small"
                    :columns="columns1"
                    :dataSource="unusualOrderList"
                    bordered
            >
                <span slot="price" slot-scope="text">
                        <span>￥{{ text }}</span>
                </span>
                <span slot="roomType" slot-scope="text">
                        <span v-if="text === 'BigBed'">大床房</span>
                        <span v-if="text === 'DoubleBed'">双床房</span>
                        <span v-if="text === 'Family'">家庭房</span>
                </span>
                <span slot="action" slot-scope="record">
                    <a-popconfirm
                            title="确定恢复入住吗？"
                            @confirm="checkin(record.id)"
                            okText="确定"
                            cancelText="取消"
                    >
                        <a-button type="primary" size="small">恢复入住</a-button>
                    </a-popconfirm>
                </span>
            </a-table>
        </a-drawer>
    </div>
</template>

<script>
    import {mapActions, mapGetters, mapMutations} from "vuex";

    const columns1 = [
        {
            title: '用户Id',
            dataIndex: 'userId',
            scopedSlots: {customRender: 'userId'}
        },
        {
            title: '酒店Id',
            dataIndex: 'hotelId',
            //scopedSlots: {customRender: 'userId'}
        },
        {
            title: '入住时间',
            dataIndex: 'checkInDate',
            //scopedSlots: {customRender: 'checkInDate'}
        },
        {
            title: '退房时间 ',
            dataIndex: 'checkOutDate',
        },
        {
            title: '房间类型',
            dataIndex: 'roomType',
        },
        {
            title: '房间数量',
            dataIndex: 'roomNum',
        },
        // {
        //     title: '是否携带儿童',
        //     dataIndex: 'haveChild',
        // },
        {
            title: '创建时间',
            dataIndex: 'createDate',
        },

        {
            title: '客户姓名',
            dataIndex: 'tenantName',
        },
        {
            title: '电话号码',
            dataIndex: 'phoneNumber',
        },
        {
            title: '订单状态',
            filters: [{ text: '已预订', value: '已预订' },{ text: '未按时入住', value: '未按时入住' }, { text: '已撤销', value: '已撤销' },{ text: '已执行', value: '已执行' }, { text: '已退房', value: '已退房' },{text: '已评价', value: '已评价'}],
            onFilter: (value, record) => record.orderState.includes(value),
            dataIndex: 'orderState',
        },
        {
            title: '操作',
            key: 'action',
            scopedSlots: {customRender: 'action'},
        },

    ];
    export default {
        name: "unusualOrder",
        props: [
            'hotelId',
        ],
        data() {
            return {
                columns1,
                clickedRecord: {}
            }
        },
        computed: {
            ...mapGetters([
                'unusualOrderVisible',
                'unusualOrderList'
            ])
        },
        async mounted() {
            await this.getUnusualOrderList(this.hotelId)
        },
        methods: {
            ...mapActions([
                'getUnusualOrderList',
                'checkIn'
            ]),
            ...mapMutations([
                'set_unusualOrderVisible',
            ]),
            onClose() {
                this.set_unusualOrderVisible(false);
            },
            // afterVisibleChange(id) {
            //     this.getUnusualOrderList(id)
            // },
            checkin(id){
                this.checkIn(id)
            },
        }
    }
</script>

<style scoped>

</style>
