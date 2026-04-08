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
import CodeUtil from "@/i2f-turbo-web/i2f-core/util/CodeUtil";
import Base64Util from "@/i2f-turbo-web/i2f-core/codec/Base64Util";
import SwlDto from "@/i2f-turbo-web/i2f-swl/core/data/SwlDto";

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
      TestSwlTransfer.main();
      // TestSwlExchanger.main();
      // TestSwlCertTransfer.main();
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
      if(this.$axios.$filter.config.enable==false){
        return;
      }
      debugger
      let clientTransfer=this.$axios.$filter.transfer
      let swapKeyPair = clientTransfer.getSelfSwapKey();

      let clientKeyPair = clientTransfer.generateKeyPair();

      let payload = CodeUtil.makeCheckCode(32,false);
      let reqHandleShake = clientTransfer.sendByRaw("swap",
          swapKeyPair.getPublicKey(),
          clientKeyPair.getPrivateKey(),
          [payload],
          [clientTransfer.obfuscateEncode(clientKeyPair.getPublicKey())]
      );
      reqHandleShake.context=null;

      let reqJson = JSON.stringify(reqHandleShake);
      let reqPayload=Base64Util.encrypt(reqJson);
      let reqData=new SwlDto();
      reqData.payload=reqPayload;
      this.$axios({
        url: 'swl/swapKey',
        method: 'post',
        data: reqData,
      }).then(({data}) => {
        debugger
        /**
         * @type {SwlDto}
         */
        let respDto=data;
        let respPayload=respDto.payload;
        let respJson=Base64Util.decrypt(respPayload);
        let respHandleShake=JSON.parse(respJson);

        let recvRespHandleShake = clientTransfer.receiveByRaw("swap", respHandleShake, swapKeyPair.getPublicKey(), clientKeyPair.getPrivateKey());

        let serverPublicKey = recvRespHandleShake.parts[0];

        let clientCertId = clientTransfer.acceptOtherPublicKeyRaw(recvRespHandleShake.header.certId, clientKeyPair, serverPublicKey);

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
