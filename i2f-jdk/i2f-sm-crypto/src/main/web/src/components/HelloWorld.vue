<template>
  <div>
    {{msg}}
  </div>
</template>

<script>
import {sm2,sm3,sm4}  from '../sm-crypto/index'
export default {
  data:()=>{
    return {
      msg: sm2.generateKeyPairHex()
    }
  },
  created(){
    console.log({sm2,sm3,sm4})
  },
  mounted(){

  },
  methods:{

  }
}
</script>



<style scoped>

</style>
