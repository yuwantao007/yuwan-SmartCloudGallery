<template>
  <div class="no-auth-container">
    <a-result
      status="403"
      title="403"
      sub-title="抱歉，您没有访问此页面的权限"
    >
      <template #extra>
        <a-space size="middle">
          <a-button type="primary" @click="goHome">返回首页</a-button>
          <a-button @click="goBack">返回上一页</a-button>
        </a-space>
      </template>
    </a-result>
    <div class="auth-tips">
      <a-alert
        message="权限说明"
        description="如果您认为这是一个错误，请联系管理员为您分配相应权限"
        type="info"
        show-icon
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

const goHome = () => {
  router.push('/');
};

const goBack = () => {
  router.go(-1);
};
</script>

<style scoped>
.no-auth-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 80vh;
  padding: 24px;
}

.auth-tips {
  margin-top: 24px;
  max-width: 400px;
}
</style>