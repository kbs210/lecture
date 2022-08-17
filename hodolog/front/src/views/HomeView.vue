<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import router from "@/router";

const posts = ref([])

const post = ref({
  id: 0,
  title: "",
  content: "",
});

axios.get("/api/posts2?page-1&size=10").then((response) => {
  response.data.forEach((r: any) => {
    posts.value.push(r);
  });
});

const moveToRead = () => {
  router.push({name: "read"});
}
</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <div class="title">
        <router-link :to="{name: 'read', params: {postId: post.id} }">{{
            post.title
          }}</router-link>
      </div>
      <div class="content">
        {{ post.content}}
      </div>
    </li>
  </ul>
</template>

<style scoped>
ul {
  list-style: none;
  paddiong: 0;
}

li {
  margin-bottom: 1.3rem;
}

li .title{
  background-color: black;
  font-size: 1.2rem;
  color: #303030;
  text-decoration: none;
}

li .content{
  font-size: 0.95rem;
  color: #5d5d5d;
}

li:last-child {
  margin-bottom: 0rem;
}

</style>
