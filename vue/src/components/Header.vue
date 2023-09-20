<template>
  <div style="line-height: 60px;display: flex">
  <div style="flex: 1;font-size: 23px">
    <span :class="collapseBtnClass" style="cursor:pointer;font-size: 18px" @click="collapse"></span>
    <el-breadcrumb separator="/" style="display: inline-block;margin-left: 10px">
      <el-breadcrumb-item :to="'/'" >首页</el-breadcrumb-item>
      <el-breadcrumb-item >{{currentPathName}}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
  <el-dropdown style="width: 150px;cursor: pointer;text-align: right  ">
    <div>
      <img :src="user.avatarUrl"alt=""
        style="width: 30px;border-radius: 50%;position: relative; top:10px;right: 5px"
      >
    <span>{{ user.nickname}}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
    </div>
    <el-dropdown-menu slot="dropdown" style="width: 100px;text-align: center"  >
      <el-dropdown-item >
        <router-link style="text-decoration: none;color: black" to="/person">个人信息</router-link>
      </el-dropdown-item>
      <el-dropdown-item >
        <span style="text-decoration: none" @click="logout">退出</span>
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    collapse: Function,
    user: Object
  },
  data(){
    return{

    }
  },

  computed:{
    currentPathName(){
      return this.$store.state.currentPathName;
    },

  },
  methods:{
    logout(){
      this.$router.push("/")
      localStorage.removeItem("user")
      this.$message.success("退出成功！")
    }
  },
  watch:{
    currentPathName(newVal,oldVal){
      console.log(newVal)
    },


  },

  created() {
    if(localStorage.getItem("user")==null){
      this.$message.warning("请先登录")
      this.$router.push("/")
    }
    console.log(this.$route)
  }
}
</script>

<style scoped>

</style>
