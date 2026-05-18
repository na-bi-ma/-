<template>
  <div class="auth-container">
    <h2>Вход в личный кабинет</h2>

    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="login">Логин (email или телефон)</label>
        <input
          type="text"
          id="login"
          v-model="form.login"
          required
          placeholder="example@mail.com или +375000000000"
        >
      </div>

      <div class="form-group">
        <label for="password">Пароль</label>
        <input
          type="password"
          id="password"
          v-model="form.password"
          required
          placeholder="Введите пароль"
        >
      </div>

      <button type="submit" :disabled="loading">
        {{ loading ? 'Вход...' : 'Войти' }}
      </button>

      <div v-if="error" class="error">{{ error }}</div>
    </form>

    <p class="link">
      Нет аккаунта? <router-link to="/auth/register">Зарегистрироваться</router-link>
    </p>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { login } from '@/services/api';

export default {
  name: 'LoginView',
  setup() {
    const router = useRouter();
    const loading = ref(false);
    const error = ref('');

    const form = ref({
      login: '',
      password: ''
    });

    const handleLogin = async () => {
      loading.value = true;
      error.value = '';

      try {
        const response = await login(form.value);
        localStorage.setItem('token', response.token);
        router.push('/profile');
      } catch (err) {
        error.value = err.message || 'Ошибка при входе';
      } finally {
        loading.value = false;
      }
    };

    return {
      form,
      loading,
      error,
      handleLogin
    };
  }
};
</script>

<style scoped>
.auth-container {
  max-width: 400px;
  margin: 0;
  padding: 30px;
  border: 1px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  background: white;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 90%;
}

h2 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 25px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
  color: #555;
  font-weight: 500;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
  box-sizing: border-box;
}

input:focus {
  outline: none;
  border-color: #2196F3;
  box-shadow: 0 0 5px rgba(33, 150, 243, 0.3);
}

button {
  width: 100%;
  padding: 12px;
  background-color: #2196F3;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
}

button:hover:not(:disabled) {
  background-color: #1976D2;
}

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.error {
  margin-top: 15px;
  padding: 10px;
  background-color: #ffebee;
  color: #c62828;
  border-radius: 5px;
  border-left: 4px solid #c62828;
}

.link {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.link a {
  color: #2196F3;
  text-decoration: none;
}

.link a:hover {
  text-decoration: underline;
}
</style>