<template>
  <div class="auth-container">
    <h2>Регистрация</h2>

    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label for="name">ФИО</label>
        <input
          type="text"
          id="name"
          v-model="form.name"
          required
          placeholder="Иванов Иван Иванович"
        >
      </div>

      <div class="form-group">
        <label for="email">E-mail</label>
        <input
          type="email"
          id="email"
          v-model="form.email"
          required
          placeholder="example@mail.com"
        >
      </div>

      <div class="form-group">
        <label for="phone">Номер телефона</label>
        <input
          type="tel"
          id="phone"
          v-model="form.phone"
          required
          placeholder="+375000000000"
        >
      </div>

      <div class="form-group">
        <label for="password">Пароль</label>
        <input
          type="password"
          id="password"
          v-model="form.password"
          required
          minlength="6"
          placeholder="Минимум 6 символов"
        >
      </div>

      <button type="submit" :disabled="loading">
        {{ loading ? 'Регистрация...' : 'Зарегистрироваться' }}
      </button>

      <div v-if="error" class="error">{{ error }}</div>
      <div v-if="success" class="success">{{ success }}</div>
    </form>

    <p class="link">
      Уже есть аккаунт? <router-link to="/auth/login">Войти</router-link>
    </p>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { register } from '@/services/api';

export default {
  name: 'RegisterView',
  setup() {
    const router = useRouter();
    const loading = ref(false);
    const error = ref('');
    const success = ref('');

    const form = ref({
      name: '',
      email: '',
      phone: '',
      password: ''
    });

    const handleRegister = async () => {
      loading.value = true;
      error.value = '';
      success.value = '';

      try {
        await register(form.value);
        success.value = 'Регистрация успешна! Сейчас вы будете перенаправлены на страницу входа...';

        setTimeout(() => {
          router.push('/auth/login');
        }, 2000);
      } catch (err) {
        error.value = err.message || 'Ошибка при регистрации';
      } finally {
        loading.value = false;
      }
    };

    return {
      form,
      loading,
      error,
      success,
      handleRegister
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
  border-color: #4CAF50;
  box-shadow: 0 0 5px rgba(76, 175, 80, 0.3);
}

button {
  width: 100%;
  padding: 12px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
}

button:hover:not(:disabled) {
  background-color: #45a049;
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

.success {
  margin-top: 15px;
  padding: 10px;
  background-color: #e8f5e8;
  color: #2e7d32;
  border-radius: 5px;
  border-left: 4px solid #2e7d32;
}

.link {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.link a {
  color: #4CAF50;
  text-decoration: none;
}

.link a:hover {
  text-decoration: underline;
}
</style>