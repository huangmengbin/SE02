<template>
  <div class="hotelList">
      <div>
          <a-input
                  size="large"
                  v-model="searchNames"
                  type="search"
                  placeholder="根据酒店名搜索"
                  v-decorator="[
                {validateTrigger: 'blur'}]">
              <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
          </a-input>
          <!--这里试试 把hmb的挤掉 -->
      </div>
      <div>
          <!--todo lz zjc 筛选栏 排序栏 -->
          <a-form
                  :label-col="labelCol"
                  :wrapper-col="wrapperCol"
          >
              <a-form-item label="房型" prop="roomType" class="roomType">
                  <a-checkbox-group v-model="screen.roomType">
                      <a-checkbox value="BigBed" name="roomType">
                          大床房
                      </a-checkbox>
                      <a-checkbox value="DoubleBed" name="roomType">
                          双床房
                      </a-checkbox>
                      <a-checkbox value="Family" name="roomType">
                          家庭房
                      </a-checkbox>
                  </a-checkbox-group>
              </a-form-item>
              <a-form-item label="评分" class="rate">
                  <a-input-group compact>
                      <a-input style=" width: 100px; text-align: center" placeholder="Minimum" v-model="screen.minRate"/>
                      <a-input
                              style=" width: 30px; border-left: 0; pointer-events: none; backgroundColor: #fff"
                              placeholder="~"
                              disabled
                      />
                      <a-input style="width: 100px; text-align: center; border-left: 0" placeholder="Maximum" v-model="screen.maxRate"/>
                  </a-input-group>
              </a-form-item>
              <a-form-item label="价格" class="price">
                  <a-input-group compact>
                      <a-input style=" width: 100px; text-align: center" placeholder="Minimum" v-model="screen.minPrice"/>
                      <a-input
                              style=" width: 30px; border-left: 0; pointer-events: none; backgroundColor: #fff"
                              placeholder="~"
                              disabled
                      />
                      <a-input style="width: 100px; text-align: center; border-left: 0" placeholder="Maximum" v-model="screen.maxPrice"/>
                  </a-input-group>
              </a-form-item>
          </a-form>
      </div>
    <a-layout>
        <a-layout-content style="min-width: 800px">
          <a-spin :spinning="hotelListLoading">
            <div class="card-wrapper">
                <HotelCard :hotel="item" v-for="item in currentHotelList" :key="item.index" @click.native="jumpToDetails(item.id)"></HotelCard>
                <div v-for="item in emptyBox" :key="item.name" class="emptyBox ant-col-xs-7 ant-col-lg-5 ant-col-xxl-3">
                </div>
                <!--<a-pagination showQuickJumper :total="hotelList.totalElements" :defaultCurrent="1" @change="pageChange"></a-pagination>a-pagination-->
            </div>
          </a-spin>
      </a-layout-content>
    </a-layout>
  </div>
</template>
<script>
import HotelCard from './components/hotelCard'
import { mapGetters, mapActions, mapMutations } from 'vuex'

export default {
  name: 'home',
  components: {
    HotelCard
  },
  data(){
    return {
        labelCol: { span: 4 ,offset:0},
        wrapperCol: { span: 14 },
        emptyBox: [{name: 'box1'}, {name: 'box2'}, {name: 'box3'}],
        searchNames: "",
        screen: {
            roomType: [],
            minRate:'',
            maxRate:'',
            minPrice:'',
            maxPrice:''
        }
    }
  },
    async mounted() {
        this.getAllRoomList();
        this.allRoomList=this.$store.getters.allRoomList;
        await this.getHotelList().then(response=>{
            //hotelList是加载下来所有的酒店，currentHotelList是处理后的，比如排序、筛选；最后才分页（可选
            this.currentHotelList=[...response];
            this.currentHotelList.reverse();
            console.log(this.currentHotelList);//
        })
    },
  computed: {
    ...mapGetters([
      'hotelList',
      'hotelListLoading',
      'allRoomList'
    ]),
      currentHotelList(){
          //hotelList是加载下来所有的酒店，currentHotelList是处理后的，比如排序、筛选；最后才分页（可选

        let {hotelList, searchNames} = this;
        let res = [...hotelList];

          if(searchNames.trim()){
              let words = searchNames.trim().split(/\s+/);
              res = res.filter(hotel => {
                  for(let word of words){
                      if(hotel.name.indexOf(word) === -1){
                          return false;
                      }
                  }
                  return true;
              })
          }//首先，把酒店的列表根据名字筛选一次。
          //接下来，各种高级筛选、排序的东西都写在这里

          console.log("名字",res);

          //todo lz zjc
          //筛选评分
          let minRate=0;
          let maxRate=5;
          if(this.screen.minRate.length!==0){
              minRate=this.screen.minRate;
          }
          if(this.screen.maxRate.length!==0){
              maxRate=this.screen.maxRate;
          }

          res=res.filter(hotel=>{
              return (hotel.rate >= minRate && hotel.rate <= maxRate);
          });

          console.log("评分",res);

          //筛选房型、价格
          //因为存在一个“有的酒店没有房间”的问题(即没有roomType和price)，所以弄得稍微复杂了一点 -.-|||
          let selectedRoom=[];
          let hasSelectedPrice=false;
          let minPrice=0;
          let maxPrice=99999999;
          if(this.screen.roomType.length!==0){
              selectedRoom=[...this.screen.roomType];
          }
          if(this.screen.minPrice.length!==0){
              minPrice=this.screen.minPrice;
              hasSelectedPrice=true;
          }
          if(this.screen.maxPrice.length!==0){
              maxPrice=this.screen.maxPrice;
          }


          res=res.filter(hotel=>{
              let room=[...this.allRoomList];
              if(selectedRoom.length===0 && !hasSelectedPrice){
                  return true;
              }else if(selectedRoom.length===0){
                  room=room.filter(room=>{
                      return (room.hotelId===hotel.id && room.price>=minPrice && room.price<=maxPrice)
                  });
              } else{
                  room=room.filter(room=>{
                      return (room.hotelId===hotel.id && selectedRoom.indexOf(room.roomType)!==-1 && room.price>=minPrice && room.price<=maxPrice)
                  });
              }
           return (room.length!==0);
          });
          console.log("房型/价格",res);

        return res;
      }
  },
  methods: {
    ...mapMutations([
      'set_hotelListParams',
      'set_hotelListLoading',
      'set_allRoomList'
    ]),
    ...mapActions([
      'getHotelList',
      'getAllRoomList'
    ]),

    pageChange(page, pageSize) {
       alert('居然可以翻页了?');
       console.log(this);
      const data = {
        pageNo: page - 1
      };
      this.set_hotelListParams(data)
      this.set_hotelListLoading(true)
      this.getHotelList()
    },
    jumpToDetails(id){
      this.$router.push({ name: 'hotelDetail', params: { hotelId: id }})
    }
  }
}
</script>
<style scoped lang="less">
  .hotelList {
    text-align: center;
    padding: 50px 0;
    .emptyBox {
      height: 0;
      margin: 10px 10px
    }
    .card-wrapper{
      display: flex;
      justify-content: space-around;
      flex-wrap: wrap;
      flex-grow: 3;
      min-height: 800px
    }
    .card-wrapper .card-item {
      margin: 30px;
      position: relative;
      height: 188px;
    }
  }
    .roomType{
        width: 500px;
    }
    .rate{
        width: 500px;
    }
    .price{
        width: 500px;
    }
</style>