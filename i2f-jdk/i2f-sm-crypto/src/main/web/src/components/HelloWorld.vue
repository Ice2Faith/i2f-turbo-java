<template>
  <div>
    <div class="block">
      <div>text:{{test.text}}</div>
    </div>
    <div class="block">
      <div>sm3</div>
      <div>sign:{{test.sm3.sign}}</div>
    </div>
    <div class="block">
      <div>sm4</div>
      <div>key:{{test.sm4.key}}</div>
      <div>enc:{{test.sm4.enc}}</div>
      <div>dec:{{test.sm4.dec}}</div>
    </div>
    <div class="block">
      <div>sm2</div>
      <div>publicKey:{{test.sm2.keypair.publicKey}}</div>
      <div>privateKey:{{test.sm2.keypair.privateKey}}</div>
      <div>enc:{{test.sm2.enc}}</div>
      <div>dec:{{test.sm2.dec}}</div>
      <div>sign:{{test.sm2.sign}}</div>
      <div>verify:{{test.sm2.verify}}</div>
    </div>
  </div>
</template>

<script>
import {sm2,sm3,sm4}  from '../sm-crypto/index'
export default {
  data:()=>{
    return {
      test:{
        text: 'Hello你好 World世界!',
        sm3:{
          sign: ''
        },
        sm4:{
          key: '4eeb7d027804a89384671c8221398d4f',
          enc: '',
          dec: ''
        },
        sm2:{
          keypair:{
            publicKey: '045df2c1bb0718c41fdfd8765ad4fb6e4c62bced3c0b0ee0198072ebb4cf48633cb70dde25b7d8e9ca4f11fb1c2a8ae6c362ccc6b83ac32a6789bdb0d57d490204',
            privateKey: 'f22151d457b3c8e2142311616dd4972fc7f878e50a8f3974b423e5850ace98dc',
          },
          enc: '',
          dec: '',
          sign: '',
          verify: false
        }
      }
    }
  },
  created(){
    console.log({sm2,sm3,sm4})

    this.test.sm3.sign=sm3(this.test.text)

    this.test.sm4.enc=sm4.encrypt(this.test.text,this.test.sm4.key)
    this.test.sm4.dec=sm4.decrypt(this.test.sm4.enc,this.test.sm4.key)

    this.test.sm2.enc=sm2.doEncrypt(this.test.text,this.test.sm2.keypair.publicKey)
    this.test.sm2.dec=sm2.doDecrypt(this.test.sm2.enc,this.test.sm2.keypair.privateKey)

    this.test.sm2.sign=sm2.doSignature(this.test.text,this.test.sm2.keypair.privateKey)
    this.test.sm2.verify=sm2.doVerifySignature(this.test.text,this.test.sm2.sign,this.test.sm2.keypair.publicKey)
  },
  mounted(){

  },
  methods:{

  }
}
</script>



<style scoped>
.block{
  border-bottom: solid 1px #777;
  margin: 5px;
  width: 100%;
  overflow-wrap: anywhere;
}
.block div{
  margin-top: 3px;
}

</style>
