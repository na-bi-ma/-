<template>
  <div class="auth-container">
    <h2>Восстановление пароля</h2>

    <form @submit.prevent="handleReset">
      <div class="form-group">
        <label for="email">Email</label>
        <input
          type="email"
          id="email"
          v-model="form.email"
          required
          placeholder="example@mail.com"
        >
      </div>

      <div class="form-group">
        <label for="motherMaidenName">Девичья фамилия матери</label>
        <input
          type="text"
          id="motherMaidenName"
          v-model="form.motherMaidenName"
          required
          placeholder="Иванова"
        >
      </div>

      <div class="form-group">
        <label for="newPassword">Новый пароль</label>
        <input
          type="password"
          id="newPassword"
          v-model="form.newPassword"
          required
          placeholder="Минимум 6 символов и 1 цифра"
        >
      </div>

      <button type="submit" :disabled="loading">
        {{ loading ? 'Восстановление...' : 'Восстановить пароль' }}
      </button>

      <div v-if="error" class="error">{{ error }}</div>
      <div v-if="success" class="success">{{ success }}</div>
    </form>

    <p class="link">
      <router-link to="/auth/login">Вернуться ко входу</router-link>
    </p>
  </div>
</template>

<script>
import { ref } from 'vue';
import { resetPassword } from '@/services/api';

export default {
  name: 'ResetPasswordView',
  setup() {
    const loading = ref(false);
    const error = ref('');
    const success = ref('');

    const form = ref({
      email: '',
      motherMaidenName: '',
      newPassword: ''
    });

    const handleReset = async () => {
      loading.value = true;
      error.value = '';
      success.value = '';

      try {
        await resetPassword(form.value);
        success.value = 'Пароль успешно изменён! Можете войти.';
      } catch (err) {
        error.value = err.message || 'Ошибка при восстановлении пароля';
      } finally {
        loading.value = false;
      }
    };

    return { form, loading, error, success, handleReset };
  }
};
</script>

<style scoped>
.auth-container {
  max-width: 420px;
  width: 400px;
  margin: 0;
  padding: 36px;
  border: 1px solid #ddd;
  border-radius: 12px;
  box-shadow: 0 2px 16px rgba(0,0,0,0.1);
  background: white;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  box-sizing: border-box;
}

h2 { text-align: center; color: #2c3e50; margin-bottom: 28px; font-size: 26px; }

.form-group { margin-bottom: 22px; }

label { display: block; margin-bottom: 6px; color: #555; font-weight: 500; font-size: 15px; }

input {
  width: 100%;
  padding: 14px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 16px;
  box-sizing: border-box;
}

input:focus { outline: none; border-color: #ff9800; box-shadow: 0 0 6px rgba(255, 152, 0, 0.3); }

button {
  width: 100%;
  padding: 14px;
  background-color: #ff9800;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 17px;
  cursor: pointer;
}

button:hover:not(:disabled) { background-color: #f57c00; }
button:disabled { background-color: #bbb; cursor: not-allowed; }

.error { margin-top: 16px; padding: 12px; background-color: #ffebee; color: #c62828; border-radius: 6px; border-left: 4px solid #c62828; font-size: 15px; }
.success { margin-top: 16px; padding: 12px; background-color: #e8f5e8; color: #2e7d32; border-radius: 6px; border-left: 4px solid #2e7d32; font-size: 15px; }

.link { text-align: center; margin-top: 22px; color: #777; font-size: 15px; }
.link a { color: #ff9800; text-decoration: none; }
.link a:hover { text-decoration: underline; }

@media (max-width: 768px) {
  .auth-container { width: 90%; padding: 28px 22px; border-radius: 10px; }
  h2 { font-size: 22px; margin-bottom: 22px; }
  input { padding: 12px; font-size: 15px; }
  button { padding: 12px; font-size: 16px; }
  label { font-size: 14px; }
  .link { font-size: 14px; }
}

@media (max-width: 400px) {
  .auth-container { padding: 20px 16px; }
  h2 { font-size: 20px; }
  input { padding: 10px; font-size: 14px; }
  button { padding: 10px; font-size: 14px; }
}
</style>