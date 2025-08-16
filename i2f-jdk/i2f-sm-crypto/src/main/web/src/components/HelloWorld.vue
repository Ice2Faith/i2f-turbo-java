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
          sign: 'fd5b6e69ef806dfac2c6dbf7dadc413a13878a129f1db8c671e26ea0122bd91d'
        },
        sm4:{
          key: '4eeb7d027804a89384671c8221398d4f',
          enc: 'e070c424be6186aff8a18ead90bf7be97ff017fb9d59dcdf1a4a6d085b6d9518',
          dec: ''
        },
        sm2:{
          keypair:{
            publicKey: '045df2c1bb0718c41fdfd8765ad4fb6e4c62bced3c0b0ee0198072ebb4cf48633cb70dde25b7d8e9ca4f11fb1c2a8ae6c362ccc6b83ac32a6789bdb0d57d490204',
            privateKey: 'f22151d457b3c8e2142311616dd4972fc7f878e50a8f3974b423e5850ace98dc',
          },
          enc: '2c45756dae7c0e2e8a2e9392570500cd90748116ba2be8e144a93737d8bf38175bdc7d181ebae5ec79fe7b6f80b51e561e6d51f93de8bac67484aa740780fa2bf9004656eeb3f564a2d8bc5cae3f9586feba912a375bcda1ed072d3b3278e56d9c1bf53f67b815b53fd5ae8e0dd248c00749a09b50dde71a',
          dec: '',
          sign: '4c6caad740f6f8860e62ea0ed28f0ff071936fa77559aad76f6ba9eeea5e5ac40086f565493f9fe79373d8aebf114320bc53cf24cfd67980f1a1ca011ba9dbab',
          verify: false
        }
      }
    }
  },
  created(){
    console.log({sm2,sm3,sm4})

    this.test.sm3.sign=sm3.digest(this.test.text)

    // this.test.sm4.enc=sm4.encrypt(this.test.text,this.test.sm4.key)
    this.test.sm4.dec=sm4.decrypt(this.test.sm4.enc,this.test.sm4.key)

    // this.test.sm2.enc=sm2.doEncrypt(this.test.text,this.test.sm2.keypair.publicKey)
    this.test.sm2.dec=sm2.doDecrypt(this.test.sm2.enc,this.test.sm2.keypair.privateKey)

    // this.test.sm2.sign=sm2.doSignature(this.test.text,this.test.sm2.keypair.privateKey)
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
