<template>
    <a-modal
            :visible="commentVisible"
            title="评价"
            width="900px"
            @cancel="cancelComment"
            @ok="commentSubmit"
    >
            <a-input>
                size="large"
                v-model="aaaa"
                type="search"
                placeholder="请输入评论",
                v-decorator="[
                {rules: [{ required: true, message: '宁还没输入评论内容' }],validateTrigger: 'blur'}]">
                <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>

    </a-modal>
</template>

<script>
    import { mapGetters, mapMutations } from 'vuex'
    export default {
        name: "comment",
        props:{
            order:{}
        },

        data(){
          return{
              bbb:"d",
              aaaa:"",
          }
        },

        computed:{
            ...mapGetters([
                'commentVisible',
            ])
        },

        methods:{
            cancelComment(){
                this.set_CommentVisible(false);
            },
            commentSubmit(){
                alert(this.aaaa);
                this.set_CommentVisible(false);
            },
            ...mapMutations([
                'set_CommentVisible'
            ]),
        },

    }
</script>

<style scoped>

</style>