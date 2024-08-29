<template>
  <div class="app">
    {{message}}
    <span>world</span>
    <comp></comp>
  </div>
</template>


<script>
export default {
  name: "test",
  data(){
    return {
      message: 'hello'
    }
  },
  created(){
    this.alertHello()
  }
}
</script>

<style scoped>
.app{
  color: blue;
}
.--this{
  background: lightseagreen;
}
.--this span{
  color: coral;
}
span{
  color: cyan;
}
</style>


