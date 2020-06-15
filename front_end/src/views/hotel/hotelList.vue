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
    return{
        emptyBox: [{ name: 'box1' }, { name: 'box2'}, {name: 'box3'}],
        searchNames:"",
    }
  },
  async mounted() {
    await this.getHotelList().then(response=>{
        //hotelList是加载下来所有的酒店，currentHotelList是处理后的，比如排序、筛选；最后才分页（可选
    })

  },
  computed: {
    ...mapGetters([
      'hotelList',
      'hotelListLoading'
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

          //todo lz zjc

        return res;
      }
  },
  methods: {
    ...mapMutations([
      'set_hotelListParams',
      'set_hotelListLoading'
    ]),
    ...mapActions([
      'getHotelList'
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
</style>