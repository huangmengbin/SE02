<template>
    <div>
        <a-drawer
                title="订单详情"
                height="400"
                @close="onClose"
                placement="top"
                :closable="false"
                :visible="orderVisible"
        >
            <a-table
                    :columns="columns"
                    size="small"
                    :dataSource="mgrOrderList"
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
                                title="确定想删除该订单吗？"
                                @confirm="deleteOrder(record)"
                                okText="确定"
                                cancelText="取消"
                        >
                            <a-button type="danger" size="small">删除订单</a-button>
                        </a-popconfirm>
                        <a-divider type="vertical"></a-divider>
                     <a-popconfirm
                             title="你确定ta来了吗？"
                             @confirm="checkin(record.id)"
                             okText="来了"
                             cancelText="再等等"
                             v-if="record.orderState === '已预订'"
                     >
                            <a-button type="primary" size="small">入住</a-button>
                        </a-popconfirm>


                     <a-divider type="vertical"></a-divider>
                      <a-popconfirm
                              title="你确定ta走了吗？"
                              @confirm="cOut(record.id)"
                              okText="走了"
                              cancelText="再等等"
                              v-if="record.orderState === '已执行'"
                      >
                            <a-button type="primary" size="small">退房</a-button>
                        </a-popconfirm>

                    </span>
            </a-table>
        </a-drawer>


    </div>

</template>
<script>
    import {mapGetters, mapMutations, mapActions} from 'vuex'
    //import checkOutDate from "./checkOutDate";

    const columns = [
        {
            title: '订单号',
            dataIndex: 'id',
        },
        {
            title: '酒店名',
            dataIndex: 'hotelName',
        },
        {
            title: '房型',
            dataIndex: 'roomType',
            scopedSlots: { customRender: 'roomType' }
        },
        {
            title: '入住时间',
            dataIndex: 'checkInDate',
            scopedSlots: { customRender: 'checkInDate' }
        },
        {
            title: '离店时间',
            dataIndex: 'checkOutDate',
            scopedSlots: { customRender: 'checkOutDate' }
        },
        {
            title: '入住人数',
            dataIndex: 'peopleNum',
        },
        {
            title: '房价',
            dataIndex: 'price',
        },
        {
            title: '状态',
            filters: [{ text: '已预订', value: '已预订' }, { text: '已撤销', value: '已撤销' },{ text: '已执行', value: '已执行' }, { text: '已退房', value: '已退房' },{text: '已评价', value: '已评价'}],
            onFilter: (value, record) => record.orderState.includes(value),
            dataIndex: 'orderState',
            scopedSlots: { customRender: 'orderState' }
        },
        {
            title: '操作',
            key: 'action',
            scopedSlots: { customRender: 'action' },
        },
    ];
    export default {
        name: 'manageOrder',
        props: [
            'hotelId',
        ],
        data() {
            return {
                columns,
                clickedRecord: {}
            }
        },
        components: {
            //checkOutDate,
        },
        computed: {
            ...mapGetters([
                'orderVisible',
                'mgrOrderList',
            ])
        },
        methods: {
            ...mapMutations([
                'set_orderVisible',
                'set_checkOutDateVisible',
            ]),
            ...mapActions([
                'getManagedOrders',
                'checkIn',
                'checkOut',
            ]),
            onClose() {
                console.log('cancel'),
                    this.set_orderVisible(false)
            },

            checkin(id) {
                this.checkIn(id)
            },

            checkRoomIn(record) {// 改变状态，自动渲染
                this.clickedRecord = record;
                this.set_checkOutDateVisible(true)
                console.log('23')
                this.set_orderVisible(false)

            },
            deleteOrder(){

            },
            cOut(id){
                this.checkOut(id);

            }
        },
    }
</script>
<style scoped>

</style>