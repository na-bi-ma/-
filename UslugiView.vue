<template>
  <div class="page">
    <AppHeader />

    <div class="content">
      <h2>Наши услуги</h2>

      <div class="search-bar">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Поиск по услугам..."
          @input="handleSearch"
        />
      </div>

      <Spinner v-if="loading" text="Загрузка услуг..." />
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else>
        <div v-if="displayedUslugi.length === 0" class="empty">Услуги не найдены</div>
        <div v-else>
          <div v-for="group in displayedUslugi" :key="group.categoryId" class="category-section">
            <h3 class="category-title">{{ group.categoryName }}</h3>
            <div class="uslugi-list">
              <div v-for="usluga in group.uslugi" :key="usluga.id" class="usluga-row">
                <span class="usluga-name">{{ usluga.name }}</span>
                <div class="usluga-right">
                  <span class="usluga-price">{{ usluga.price }} руб.</span>
                  <button
                    v-if="isLoggedIn"
                    class="fav-btn"
                    :class="{ active: favoriteIds.includes(usluga.id) }"
                    @click="toggleFavorite(usluga.id)"
                    :title="favoriteIds.includes(usluga.id) ? 'Убрать из избранного' : 'Добавить в избранное'"
                  >
                    {{ favoriteIds.includes(usluga.id) ? '❤️' : '🤍' }}
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div v-if="hasMore || visibleCount > PAGE_SIZE" class="load-more">
            <button v-if="hasMore" class="load-more-btn" @click="loadMore" :disabled="loadingMore">
              {{ loadingMore ? 'Загрузка...' : `Загрузить ещё (${remainingCount})` }}
            </button>
            <button v-if="visibleCount > PAGE_SIZE" class="load-more-btn collapse-btn" @click="collapse">
              Свернуть
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import AppHeader from '@/components/AppHeader.vue';
import Spinner from '@/components/Spinner.vue';

const API_URL = 'http://localhost:8080/api';
const PAGE_SIZE = 5;

export default {
  name: 'UslugiView',
  components: { AppHeader, Spinner },
  setup() {
    const uslugi = ref([]);
    const categories = ref([]);
    const favoriteIds = ref([]);
    const loading = ref(true);
    const loadingMore = ref(false);
    const error = ref('');
    const searchQuery = ref('');
    const visibleCount = ref(PAGE_SIZE);
    let searchTimeout = null;

    const isLoggedIn = computed(() => !!localStorage.getItem('token'));
    const getToken = () => localStorage.getItem('token');

    const groupedUslugi = computed(() => {
      const groups = [];
      const catMap = {};
      categories.value.forEach(c => { catMap[c.id] = c.name; });

      const noCategory = uslugi.value.filter(u => !u.categoryId);
      if (noCategory.length > 0) {
        groups.push({ categoryId: 'none', categoryName: 'Без категории', uslugi: noCategory });
      }

      const ids = [...new Set(uslugi.value.filter(u => u.categoryId).map(u => u.categoryId))];
      ids.forEach(cid => {
        groups.push({
          categoryId: cid,
          categoryName: catMap[cid] || 'Категория ' + cid,
          uslugi: uslugi.value.filter(u => u.categoryId === cid)
        });
      });
      return groups;
    });

    const displayedUslugi = computed(() => {
      const result = [];
      let count = 0;
      for (let group of groupedUslugi.value) {
        if (count >= visibleCount.value) break;
        const take = Math.min(group.uslugi.length, visibleCount.value - count);
        result.push({
          ...group,
          uslugi: group.uslugi.slice(0, take)
        });
        count += take;
      }
      return result;
    });

    const totalCount = computed(() => uslugi.value.length);
    const hasMore = computed(() => visibleCount.value < totalCount.value);
    const remainingCount = computed(() => totalCount.value - visibleCount.value);

    const loadMore = () => {
      loadingMore.value = true;
      setTimeout(() => {
        visibleCount.value += PAGE_SIZE;
        loadingMore.value = false;
      }, 300);
    };

    const collapse = () => {
      visibleCount.value = PAGE_SIZE;
    };

    const fetchUslugi = async (search = '') => {
      loading.value = true;
      error.value = '';
      visibleCount.value = PAGE_SIZE;
      try {
        const url = search
          ? `${API_URL}/uslugi?name=${encodeURIComponent(search)}`
          : `${API_URL}/uslugi`;
        const response = await fetch(url);
        if (!response.ok) throw new Error('Ошибка загрузки');
        uslugi.value = await response.json();
      } catch (err) {
        error.value = 'Не удалось загрузить услуги';
      } finally {
        loading.value = false;
      }
    };

    const fetchCategories = async () => {
      try {
        const res = await fetch(`${API_URL}/categories`);
        if (res.ok) categories.value = await res.json();
      } catch (e) {
        console.error('Ошибка загрузки категорий:', e);
      }
    };

    const fetchFavorites = async () => {
      if (!isLoggedIn.value) return;
      try {
        const res = await fetch(`${API_URL}/favorites`, {
          headers: { 'Authorization': `Bearer ${getToken()}` }
        });
        if (res.ok) {
          const favs = await res.json();
          favoriteIds.value = favs.map(f => f.uslugaId);
        }
      } catch (e) {
        console.error('Ошибка загрузки избранного:', e);
      }
    };

    const toggleFavorite = async (uslugaId) => {
      const token = getToken();
      if (!token) return;
      try {
        if (favoriteIds.value.includes(uslugaId)) {
          await fetch(`${API_URL}/favorites/${uslugaId}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
          });
          favoriteIds.value = favoriteIds.value.filter(id => id !== uslugaId);
        } else {
          await fetch(`${API_URL}/favorites/${uslugaId}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${token}` }
          });
          favoriteIds.value.push(uslugaId);
        }
      } catch (e) {
        console.error('Ошибка избранного:', e);
      }
    };

    const handleSearch = () => {
      clearTimeout(searchTimeout);
      searchTimeout = setTimeout(() => {
        visibleCount.value = PAGE_SIZE;
        fetchUslugi(searchQuery.value);
      }, 300);
    };

    onMounted(() => {
      fetchUslugi();
      fetchCategories();
      fetchFavorites();
    });

    return {
      uslugi, loading, loadingMore, error, searchQuery,
      displayedUslugi, favoriteIds, isLoggedIn,
      hasMore, remainingCount, visibleCount, PAGE_SIZE,
      loadMore, collapse,
      toggleFavorite, handleSearch
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
  border-color: #2196F3;
  box-shadow: 0 0 5px rgba(33,150,243,0.3);
}

.category-section {
  margin-bottom: 32px;
}

.category-title {
  font-size: 22px;
  color: #1976D2;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 2px solid #e3f2fd;
  font-weight: 500;
}

.uslugi-list {
  background: white;
  border-radius: 12px;
  box-shadow: 0 1px 6px rgba(0,0,0,0.06);
  overflow: hidden;
}

.usluga-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #f0f0f0;
  transition: background 0.15s;
}

.usluga-row:last-child {
  border-bottom: none;
}

.usluga-row:hover {
  background: #f8f9fa;
}

.usluga-name {
  font-size: 16px;
  color: #333;
}

.usluga-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.usluga-price {
  font-size: 16px;
  color: #1976D2;
  font-weight: 600;
  white-space: nowrap;
}

.fav-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  padding: 4px;
  transition: transform 0.2s;
  line-height: 1;
}

.fav-btn:hover {
  transform: scale(1.2);
}

.fav-btn.active {
  animation: heartBeat 0.3s ease;
}

@keyframes heartBeat {
  0% { transform: scale(1); }
  50% { transform: scale(1.3); }
  100% { transform: scale(1); }
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
  .content { padding: 30px 5%; }
  .content h2 { font-size: 26px; }
}

@media (max-width: 600px) {
  .content { padding: 15px 3%; }
  .content h2 { font-size: 20px; margin-bottom: 15px; }
  .search-bar input { padding: 12px; font-size: 14px; }
  .usluga-row { padding: 12px 14px; }
  .usluga-name { font-size: 13px; }
  .usluga-price { font-size: 13px; }
  .fav-btn { font-size: 18px; }
  .usluga-right { gap: 8px; }
  .category-title { font-size: 18px; }
}

.load-more {
  text-align: center;
  padding: 30px 0;
}

.load-more-btn {
  background: white;
  color: #1976D2;
  border: 2px solid #1976D2;
  padding: 14px 36px;
  border-radius: 12px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-weight: 500;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
}

.load-more-btn:hover:not(:disabled) {
  background: #1976D2;
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(25, 118, 210, 0.3);
}

.load-more-btn:disabled {
  background: #f5f5f5;
  color: #bbb;
  border-color: #ddd;
  cursor: not-allowed;
  box-shadow: none;
}

.collapse-btn {
  border-color: #ff9800;
  color: #ff9800;
  margin-left: 12px;
}

.collapse-btn:hover:not(:disabled) {
  background: #ff9800;
  color: white;
  box-shadow: 0 2px 8px rgba(255, 152, 0, 0.3);
}
</style>