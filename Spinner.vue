<template>
  <div class="spinner-container">
    <div class="spinner"></div>
    <p v-if="text" class="spinner-text">{{ text }}</p>
  </div>
</template>

<script>
export default {
  name: 'SpinnerView',
  props: {
    text: {
      type: String,
      default: 'Загрузка...'
    }
  }
};
</script>

<style scoped>
.spinner-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  background: white;
  border-radius: 12px;
  width: 100%;
  box-sizing: border-box;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #e0e0e0;
  border-top: 4px solid #1976D2;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

.spinner-text {
  margin-top: 16px;
  color: #888;
  font-size: 15px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>