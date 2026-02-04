<template>
  <div id="app">
    <a-config-provider :locale="appLocale">
      <BasicLayout />
    </a-config-provider>
  </div>
</template>

<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn'; // 引入 dayjs 的中文语言包

dayjs.locale('zh-cn');
const appLocale = zhCN; // 设置当前应用的语言环境


</script>

<style scoped></style>
