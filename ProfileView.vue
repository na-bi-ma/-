<template>
  <div class="page">
    <div class="profile-header">
      <h1>Медицинский центр</h1>
      <nav>
        <router-link to="/">Главная</router-link>
        <router-link to="/profile">Профиль</router-link>
        <a href="#" @click.prevent="handleLogout">Выйти</a>
      </nav>
    </div>

    <div class="profile-content">
      <h2>Личный кабинет</h2>

      <div v-if="loading" class="loading">Загрузка данных...</div>
      <div v-else-if="error" class="error">{{ error }}</div>

      <div v-else>
        <!-- Карточка пользователя -->
        <div class="card">
          <h3>Личные данные</h3>
          <div class="row">
            <span class="label">Имя:</span>
            <span class="value">{{ user.name }}</span>
          </div>
          <div class="row">
            <span class="label">Email:</span>
            <span class="value">{{ user.email }}</span>
          </div>
          <div class="row">
            <span class="label">Телефон:</span>
            <span class="value">{{ user.phone }}</span>
          </div>
          <div class="row">
            <span class="label">Роль:</span>
            <span class="value">{{ user.role === 'ADMIN' ? 'Администратор' : 'Пользователь' }}</span>
          </div>
          <div class="row">
            <span class="label">Дата регистрации:</span>
            <span class="value">{{ formatDate(user.createdAt) }}</span>
          </div>
        </div>

        <!-- Админ-панель -->
        <div v-if="user.role === 'ADMIN'" class="card admin-panel">
          <h3>Панель администратора</h3>
          <p>Здесь можно управлять врачами и услугами (будет добавлено позже).</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getProfile } from '@/services/api';

export default {
  name: 'ProfileView',
  setup() {
    const router = useRouter();
    const user = ref({});
    const loading = ref(true);
    const error = ref('');

    const loadProfile = async () => {
      try {
        const data = await getProfile();
        user.value = data;
      } catch (err) {
        error.value = 'Ошибка при загрузке профиля';
        if (err.message === 'Ошибка авторизации') {
          localStorage.removeItem('token');
          router.push('/auth/login');
        }
      } finally {
        loading.value = false;
      }
    };

    const handleLogout = () => {
      localStorage.removeItem('token');
      router.push('/auth/login');
    };

    const formatDate = (dateStr) => {
      if (!dateStr) return '';
      const date = new Date(dateStr);
      return date.toLocaleDateString('ru-RU', {
        day: '2-digit', month: '2-digit', year: 'numeric',
        hour: '2-digit', minute: '2-digit'
      });
    };

    onMounted(() => {
      loadProfile();
    });

    return { user, loading, error, handleLogout, formatDate };
  }
};
</script>

<style scoped>
* { box-sizing: border-box; }

.page {
  background: #f5f5f5;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 60px;
}

.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 10px;
  padding: 16px 28px;
  width: 700px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
  margin-bottom: 30px;
}

.profile-header h1 { font-size: 22px; color: #222; }

nav a { margin-left: 18px; text-decoration: none; color: #444; }
nav a:hover { color: #1976D2; }

.profile-content { width: 700px; }
.profile-content h2 { font-size: 24px; margin-bottom: 20px; color: #222; }

.card {
  background: white;
  border-radius: 10px;
  padding: 28px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
  margin-bottom: 24px;
}

.card h3 {
  font-size: 18px;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #2196F3;
  color: #222;
}

.row {
  display: flex;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
}
.row:last-child { border-bottom: none; }

.label { width: 200px; color: #666; }
.value { color: #222; }

.admin-panel {
  border-left: 4px solid #ff9800;
}

.admin-panel h3 {
  border-bottom-color: #ff9800;
}

.loading, .error {
  background: white;
  padding: 28px;
  border-radius: 10px;
}
.error { color: #c62828; }
</style>