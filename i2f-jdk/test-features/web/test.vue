<template>
  <div class="app">
    {{message}}
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

<style>
.app{
  color: blue;
}
</style>


