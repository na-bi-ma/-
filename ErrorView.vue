<template>
  <div class="page">
    <AppHeader />

    <div class="error-page">
      <div class="error-card">
        <div class="error-code">{{ code }}</div>
        <h2>{{ title }}</h2>
        <p>{{ description }}</p>
        <router-link to="/" class="btn">На главную</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import AppHeader from '@/components/AppHeader.vue';

export default {
  name: 'ErrorView',
  components: { AppHeader },
  setup() {
    const route = useRoute();
    const code = computed(() => route.params.code || '404');

    const errors = {
      '403': {
        title: 'Доступ запрещён :(',
        description: 'У вас нет прав для просмотра этой страницы. Войдите как администратор.'
      },
      '404': {
        title: 'Страница не найдена :(',
        description: 'Запрашиваемая страница не существует или была перемещена.'
      },
      '500': {
        title: 'Ошибка сервера :(',
        description: 'На сервере произошла ошибка. Попробуйте обновить страницу позже.'
      },
      '401': {
        title: 'Не авторизован :(',
        description: 'Для доступа к этой странице необходимо войти в систему.'
      }
    };

    const error = computed(() => errors[code.value] || errors['404']);

    return {
      code,
      title: computed(() => error.value.title),
      description: computed(() => error.value.description)
    };
  }
};
</script>

<style scoped>
.page {
  background: #f5f5f5;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  width: 100%;
  overflow-x: hidden;
}

.error-page {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30px 3%;
}

.error-card {
  background: white;
  border-radius: 16px;
  padding: 60px 40px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  max-width: 500px;
  width: 100%;
}

.error-code {
  font-size: 80px;
  font-weight: 700;
  color: #1976D2;
  line-height: 1;
  margin-bottom: 16px;
}

.error-card h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 12px;
}

.error-card p {
  font-size: 16px;
  color: #888;
  margin-bottom: 30px;
  line-height: 1.5;
}

.btn {
  display: inline-block;
  padding: 14px 36px;
  background: #1976D2;
  color: white;
  border-radius: 10px;
  text-decoration: none;
  font-size: 16px;
  font-weight: 500;
  transition: background 0.2s;
}

.btn:hover {
  background: #1565C0;
}
</style>