<template>
  <div class="page">
    <AppHeader />

    <div class="content">
      <h2>Наши врачи</h2>

      <div class="search-bar">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Поиск по имени..."
          @input="handleSearch"
        />
      </div>

      <Spinner v-if="loading" text="Загрузка врачей..." />
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else-if="vrachi.length === 0" class="empty">Врачи не найдены</div>
      <div v-else class="cards">
        <div v-for="vrach in vrachi" :key="vrach.id" class="card">
          <h3>{{ vrach.fullName }}</h3>
          <div class="info">
            <span class="label">Категория:</span>
            <span class="value">{{ getCategoryName(vrach.categoryId) || 'Не указана' }}</span>
          </div>
          <div class="info">
            <span class="label">Телефон:</span>
            <span class="value">{{ vrach.phone || 'Не указан' }}</span>
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
  name: 'VrachiView',
  components: { AppHeader, Spinner },
  setup() {
    const vrachi = ref([]);
    const categories = ref([]);
    const loading = ref(true);
    const error = ref('');
    const searchQuery = ref('');
    let searchTimeout = null;

    const getCategoryName = (id) => {
      if (!id) return null;
      const cat = categories.value.find(c => c.id == id);
      return cat ? cat.name : null;
    };

    const fetchCategories = async () => {
      try {
        const res = await fetch(`${API_URL}/categories`);
        if (res.ok) categories.value = await res.json();
      } catch (e) {
        console.error('Ошибка загрузки категорий:', e);
      }
    };

    const fetchVrachi = async (search = '') => {
      loading.value = true;
      error.value = '';
      try {
        const url = search
          ? `${API_URL}/vrachi?q=${encodeURIComponent(search)}`
          : `${API_URL}/vrachi`;
        const response = await fetch(url);
        if (!response.ok) throw new Error('Ошибка загрузки');
        vrachi.value = await response.json();
      } catch (err) {
        error.value = 'Не удалось загрузить список врачей';
      } finally {
        loading.value = false;
      }
    };

    const handleSearch = () => {
      clearTimeout(searchTimeout);
      searchTimeout = setTimeout(() => {
        fetchVrachi(searchQuery.value);
      }, 300);
    };

    onMounted(() => {
      fetchVrachi();
      fetchCategories();
    });

    return { vrachi, loading, error, searchQuery, getCategoryName, handleSearch };
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
  max-width: 100%;
  width: 100%;
  margin: 0 auto;
  box-sizing: border-box;
}

.content h2 {
  font-size: 30px;
  margin-bottom: 25px;
  color: #222;
}

.search-bar {
  margin-bottom: 30px;
}

.search-bar input {
  width: 100%;
  max-width: 100%;
  padding: 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 17px;
  box-sizing: border-box;
}

.search-bar input:focus {
  outline: none;
  border-color: #4CAF50;
  box-shadow: 0 0 5px rgba(76,175,80,0.3);
}

.cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 1px 6px rgba(0,0,0,0.06);
  transition: box-shadow 0.2s;
}

.card:hover {
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.card h3 {
  font-size: 17px;
  color: #222;
  margin-bottom: 12px;
}

.info {
  display: flex;
  padding: 6px 0;
  font-size: 14px;
  gap: 8px;
}

.label {
  color: #888;
  min-width: 110px;
  flex-shrink: 0;
}

.value {
  color: #333;
  font-weight: 500;
  word-break: break-word;
}

.loading, .error, .empty {
  background: white;
  padding: 50px;
  border-radius: 12px;
  text-align: center;
  color: #888;
  font-size: 16px;
  width: 100%;
}

.error {
  color: #c62828;
}

@media (max-width: 900px) {
  .content {
    padding: 30px 5%;
  }

  .content h2 {
    font-size: 26px;
  }
}

@media (max-width: 600px) {
  .content {
    padding: 15px 3%;
  }

  .content h2 {
    font-size: 20px;
    margin-bottom: 15px;
  }

  .search-bar input {
    padding: 12px;
    font-size: 14px;
  }

  .cards {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .card {
    padding: 14px;
  }

  .card h3 {
    font-size: 15px;
    margin-bottom: 8px;
  }

  .info {
    font-size: 12px;
    padding: 4px 0;
    gap: 4px;
  }

  .label {
    min-width: 80px;
    font-size: 12px;
  }

  .value {
    font-size: 12px;
  }
}
</style>