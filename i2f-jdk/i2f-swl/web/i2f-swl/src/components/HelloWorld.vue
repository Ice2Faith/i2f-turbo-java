<template>
  <div>
    <button @click="testTransfer">transfer</button>
    <button @click="testMatcher">matcher</button>

    <button @click="testSwapKey">swapKey</button>

    <button @click="invokeInt">int</button>
    <button @click="invokeStr">str</button>
    <button @click="invokeMap">map</button>

    <button @click="invokeEcho">echo</button>

    <button @click="invokeObj">obj</button>

    <button @click="invokeParam">param</button>
  </div>
</template>

<script>


import TestSwlTransfer from "@/i2f-turbo-web/i2f-swl/test/TestSwlTransfer";
import AntMatcher from "@/i2f-turbo-web/i2f-core/match/impl/AntMatcher";
import Random from "@/i2f-turbo-web/i2f-core/util/Random";
import TestSwlExchanger from "@/i2f-turbo-web/i2f-swl/test/TestSwlExchanger";
import TestSwlCertTransfer from "@/i2f-turbo-web/i2f-swl/test/TestSwlCertExchanger";
import TestSwlKeyExchanger from "@/i2f-turbo-web/i2f-swl/test/TestSwlKeyExchanger";
import TestSwlTtlKeyExchanger from "@/i2f-turbo-web/i2f-swl/test/TestSwlTtlKeyExchanger";
import TestSwlTransferRefactor from "@/i2f-turbo-web/i2f-swl/test/TestSwlTransferRefactor";

export default {
  name: 'HelloWorld',
  props: {},
  data: () => {
    return {
      msg: 'aaa'
    }
  },
  created() {
  },
  methods: {
    testTransfer() {
      // TestSwlTransfer.main();
      // TestSwlExchanger.main();
      // TestSwlCertTransfer.main();
      // TestSwlKeyExchanger.main();
      // TestSwlTtlKeyExchanger.main()
      TestSwlTransferRefactor.main()
    },
    testMatcher(){
      let matcher=new AntMatcher()
      debugger
      let rate=matcher.match('/user/rel/role/list','/user/**')
      console.log(rate)
      rate=matcher.match('/user/rel/role/list','/**')
      console.log(rate)
      rate=matcher.match('/user/rel/role/list','/user/rel/role/*')
      console.log(rate)
      rate=matcher.match('/user/rel/role/list','/user/rel/*/list')
      console.log(rate)
      rate=matcher.match('/user/rel/role/list','/user/**/list')
      console.log(rate)
      rate=matcher.match('/user/rel/role/list','/u*/**/l*t')
      console.log(rate)
    },
    testSwapKey(){
      debugger
      let transfer=this.$axios.$filter.transfer
      let selfSwapKey = transfer.getSelfSwapKey()
      this.$axios({
        url: 'swl/swapKey',
        method: 'post',
        data: selfSwapKey,
      }).then(({data}) => {
        debugger
        transfer.acceptOtherSwapKey(data)
        console.log('echo', data)
      })
    },
    invokeParam() {
      let obj = {
        name: "张三",
        age: 25,
        time: new Date().getTime(),
        index: 1,
        size: 30
      }
      this.$axios({
        url: 'test/param',
        method: 'post',
        params: obj,
        data: obj,
        // headers: SecureTransfer.getSecureHeader(true, true)
      }).then(({data}) => {
        console.log('echo', data)
      })
    },
    invokeObj() {
      let obj = {
        username: "张三",
        age: 25,
        roles: ['admin', 'log', 'data']
      }
      this.$axios({
        url: 'test/obj',
        method: 'post',
        data: obj,
        params:{
          username:'admin',
          age: 12
        }
        // headers: SecureTransfer.getSecureHeader(false, true)
      }).then(({data}) => {
        console.log('echo', data)
      })
    },
    invokeEcho() {
      this.$axios({
        url: 'test/echo',
        method: 'post',
        data: Random.nextInt(),
        // headers: SecureTransfer.getSecureHeader(false)
      }).then(({data}) => {
        console.log('echo', data)
      })
    },
    invokeInt() {
      this.$axios({
        url: 'test/int',
        method: 'get',
        params:{
          age: 22
        }
        // headers: SecureTransfer.getSecureHeader(false, false)
      }).then(({data}) => {
        console.log('int', data)
      })
    },
    invokeStr() {
      this.$axios({
        url: 'test/str',
        method: 'get',
        // headers: SecureTransfer.getSecureHeader(false, false)
      }).then((res) => {
        let data = res.data
        console.log('str', data)
      })
    },
    invokeMap() {
      this.$axios({
        url: 'test/map',
        method: 'get',
        // headers: SecureTransfer.getSecureHeader(false, false)
      }).then(({data}) => {
        console.log('map', data)
      })
    }
  }
}
</script>

<style scoped>
</style>
