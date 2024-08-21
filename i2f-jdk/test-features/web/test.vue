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
  }
}
</script>

<style>
.app{
  color: blue;
}
</style>


