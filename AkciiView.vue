<template>
  <div class="page">
    <AppHeader />

    <div class="content">
      <h2>Действующие акции</h2>

      <Spinner v-if="loading" text="Загрузка..." />
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else class="cards">
        <div v-if="akcii.length === 0" class="empty">Сейчас нет активных акций</div>
        <div v-for="akcija in akcii" :key="akcija.id" class="card">
          <h3>{{ akcija.title }}</h3>
          <p class="description">{{ akcija.description }}</p>
          <div class="info-row" v-if="akcija.price && akcija.price > 0">
            <span class="label">Цена:</span>
            <span class="price">{{ akcija.price }} руб.</span>
          </div>
          <div class="dates">
            <span>{{ formatDate(akcija.startDate) }} – {{ formatDate(akcija.endDate) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import AppHeader from '@/components/AppHeader.vue';
import Spinner from '@/components/Spinner.vue';

const API_URL = 'http://localhost:8080/api';

export default {
  name: 'AkciiView',
  components: { AppHeader, Spinner },
  setup() {
    const akcii = ref([]);
    const loading = ref(true);
    const error = ref('');

    const fetchAkcii = async () => {
      try {
        const res = await fetch(`${API_URL}/akcii`);
        if (!res.ok) throw new Error('Ошибка загрузки');
        akcii.value = await res.json();
      } catch (err) {
        error.value = 'Не удалось загрузить акции';
      } finally {
        loading.value = false;
      }
    };

    const formatDate = (dateStr) => {
      if (!dateStr) return '';
      const date = new Date(dateStr);
      return date.toLocaleDateString('ru-RU', {
        day: '2-digit', month: '2-digit', year: 'numeric'
      });
    };

    onMounted(() => fetchAkcii());

    return { akcii, loading, error, formatDate };
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

.content {
  flex: 1;
  padding: 30px 3%;
  max-width: 1400px;
  width: 100%;
  margin: 0 auto;
}

.content h2 {
  font-size: 30px;
  margin-bottom: 30px;
  color: #222;
  font-weight: 500;
}

.cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 24px;
}

.card {
  background: white;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  border-left: 4px solid #ff9800;
  transition: transform 0.2s, box-shadow 0.2s;
}

.card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.card h3 {
  font-size: 20px;
  color: #222;
  margin-bottom: 12px;
  font-weight: 500;
}

.description {
  font-size: 16px;
  color: #555;
  line-height: 1.6;
  margin-bottom: 16px;
}

.info-row {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  font-size: 16px;
}

.label {
  color: #888;
  width: 60px;
}

.price {
  color: #e65100;
  font-weight: 600;
  font-size: 18px;
}

.dates {
  font-size: 14px;
  color: #999;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.loading, .error, .empty {
  background: white;
  padding: 50px;
  border-radius: 12px;
  text-align: center;
  color: #888;
  font-size: 16px;
}

.error { color: #c62828; }

@media (max-width: 900px) {
  .content { padding: 40px 30px; }
  .content h2 { font-size: 26px; }
  .cards { grid-template-columns: 1fr; }
}

@media (max-width: 600px) {
  .content { padding: 25px 20px; }
  .content h2 { font-size: 22px; }
  .card { padding: 20px; }
}
</style>