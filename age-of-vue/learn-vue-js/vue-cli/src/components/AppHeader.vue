<template>
  <!-- HTML -->
  <header>
    <h1>{{ propsdata }}</h1>
    <button v-on:click="sendEvent">send</button>
  </header>
</template>


<script>
export default {
  //  Javascript
  props: ['propsdata'],
  methods: {
    sendEvent: function () {
      this.$emit('renew');
    }
  }
}
</script>


<style>
/* CSS */
</style>