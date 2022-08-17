<script setup lang="ts">
import {defineProps, onMounted, ref} from "vue";
import axios from "axios";
import router from "@/router";

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true,
  },
})

const post = ref({
  id: 0,
  title: "",
  content: "",
});

onMounted(() => {
  axios.get(`/api/posts2/${props.postId}`).then((response) => {
    post.value = response.data;
  })
})

const moveToEdit = () => {
  router.push({name: "edit", params: { postId: props.postId }});
};
</script>

<template>
  <h2>{{ post.title }}</h2>
  <div>{{ post.content }}</div>
  <el-button type="warnin" @click="moveToEdit()">수정</el-button>
</template>