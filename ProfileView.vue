<template>
  <div class="page">
    <AppHeader />

    <div class="profile-content">
      <h2>Личный кабинет</h2>

      <Spinner v-if="loading" text="Загрузка профиля..." />
      <div v-else-if="error" class="error">{{ error }}</div>

      <div v-else>
        <!-- Личные данные -->
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

        <!-- Избранные услуги -->
        <div v-if="favorites.length > 0" class="card">
          <h3>Избранные услуги</h3>
          <div class="fav-list">
            <div v-for="fav in favorites" :key="fav.id" class="fav-row">
              <div class="fav-info">
                <span class="fav-name">{{ fav.uslugaName || 'Услуга #' + fav.uslugaId }}</span>
                <span v-if="fav.uslugaPrice" class="fav-price">{{ fav.uslugaPrice }} руб.</span>
              </div>
              <button class="fav-remove-btn" @click="removeFavorite(fav.uslugaId)" title="Удалить из избранного">✕</button>
            </div>
          </div>
        </div>
        <div v-else class="card">
          <h3>Избранные услуги</h3>
          <p class="empty-fav">У вас пока нет избранных услуг</p>
        </div>

        <!-- Мои записи -->
        <div v-if="appointments.length > 0" class="card">
          <h3>Мои записи на приём</h3>
          <div class="fav-list">
            <div v-for="a in appointments" :key="a.id" class="fav-row">
              <div class="fav-info">
                <span class="fav-name">{{ a.uslugaName || 'Услуга #' + a.uslugaId }}</span>
                <span class="fav-detail">Врач: {{ a.vrachName || 'Врач #' + a.vrachId }}</span>
                <span class="fav-price">{{ a.appointmentDate }} в {{ a.appointmentTime?.substring(0, 5) }}</span>
              </div>
              <button class="fav-remove-btn" @click="deleteAppointment(a.id)" title="Отменить запись">✕</button>
            </div>
          </div>
        </div>
        <div v-else class="card">
          <h3>Мои записи на приём</h3>
          <p class="empty-fav">У вас пока нет записей</p>
        </div>

        <!-- Админка -->
        <div v-if="user.role === 'ADMIN'" class="admin-section">
          <!-- Управление категориями -->
          <div class="card admin-card">
            <h3>Управление категориями</h3>
            <div class="admin-form">
              <input v-model="newCategory.name" placeholder="Название категории" />
              <button @click="addCategory" :disabled="!newCategory.name">Добавить категорию</button>
              <div v-if="categoryMsg" class="msg">{{ categoryMsg }}</div>
            </div>
            <div class="admin-list">
              <div v-for="c in categoriesList" :key="c.id" class="admin-item">
                <span>{{ c.name }}</span>
                <button class="del-btn" @click="deleteCategory(c.id)">Удалить</button>
              </div>
            </div>
          </div>

          <!-- Управление услугами -->
          <div class="card admin-card">
            <h3>Управление услугами</h3>
            <div class="admin-form">
              <input v-model="newUsluga.name" placeholder="Название услуги" />
              <input v-model="newUsluga.price" placeholder="Цена" type="number" step="0.01" />
              <select v-model="newUsluga.categoryId" class="category-select">
                <option value="">Без категории</option>
                <option v-for="c in categoriesList" :key="c.id" :value="c.id">{{ c.name }}</option>
              </select>
              <button @click="editingUsluga ? saveEditUsluga() : addUsluga()" :disabled="!newUsluga.name">
                {{ editingUsluga ? 'Сохранить' : 'Добавить услугу' }}
              </button>
              <button v-if="editingUsluga" @click="cancelEditUsluga" class="cancel-btn">Отмена</button>
              <div v-if="uslugaMsg" class="msg">{{ uslugaMsg }}</div>
            </div>
            <div class="admin-list">
              <div v-for="u in uslugiList" :key="u.id" class="admin-item">
                <span>{{ u.name }} — {{ u.price }} руб.</span>
                <div class="admin-actions">
                  <button class="edit-btn" @click="startEditUsluga(u)">Ред.</button>
                  <button class="del-btn" @click="deleteUsluga(u.id)">Удалить</button>
                </div>
              </div>
            </div>
          </div>

          <!-- Управление врачами -->
          <div class="card admin-card">
            <h3>Управление врачами</h3>
            <div class="admin-form">
              <input v-model="newVrach.fullName" placeholder="ФИО врача" />
              <select v-model="newVrach.categoryId" class="category-select">
                <option value="">Без категории</option>
                <option v-for="c in categoriesList" :key="c.id" :value="c.id">{{ c.name }}</option>
              </select>
              <input v-model="newVrach.phone" placeholder="Телефон" />
              <button @click="editingVrach ? saveEditVrach() : addVrach()" :disabled="!newVrach.fullName">
                {{ editingVrach ? 'Сохранить' : 'Добавить врача' }}
              </button>
              <button v-if="editingVrach" @click="cancelEditVrach" class="cancel-btn">Отмена</button>
              <div v-if="vrachMsg" class="msg">{{ vrachMsg }}</div>
            </div>
            <div class="admin-list">
              <div v-for="v in vrachiList" :key="v.id" class="admin-item">
                <span>{{ v.fullName }} — {{ getCategoryName(v.categoryId) }}</span>
                <div class="admin-actions">
                  <button class="edit-btn" @click="startEditVrach(v)">Ред.</button>
                  <button class="del-btn" @click="deleteVrach(v.id)">Удалить</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getProfile } from '@/services/api';
import AppHeader from '@/components/AppHeader.vue';
import Spinner from '@/components/Spinner.vue';

const API_URL = 'http://localhost:8080/api';

export default {
  name: 'ProfileView',
  components: { AppHeader, Spinner },
  setup() {
    const router = useRouter();
    const user = ref({});
    const loading = ref(true);
    const error = ref('');

    const favorites = ref([]);
    const appointments = ref([]);
    const categoriesList = ref([]);
    const newCategory = ref({ name: '' });
    const categoryMsg = ref('');
    const uslugiList = ref([]);
    const newUsluga = ref({ name: '', price: '', categoryId: '' });
    const uslugaMsg = ref('');
    const editingUsluga = ref(false);
    const vrachiList = ref([]);
    const newVrach = ref({ fullName: '', phone: '', categoryId: '' });
    const vrachMsg = ref('');
    const editingVrach = ref(false);

    const getToken = () => localStorage.getItem('token');

    const getCategoryName = (id) => {
      if (!id) return '—';
      const cat = categoriesList.value.find(c => c.id == id);
      return cat ? cat.name : '—';
    };

    const loadProfile = async () => {
      try {
        const data = await getProfile();
        user.value = data;
        await loadFavorites();
        await loadAppointments();
        if (data.role === 'ADMIN') {
          await loadCategories();
          await loadUslugi();
          await loadVrachi();
        }
      } catch (err) {
        error.value = 'Ошибка при загрузке профиля';
        if (err.message === 'Ошибка авторизации' || err.message === 'Ошибка запроса') {
          localStorage.removeItem('token');
          router.push('/auth/login');
        }
      } finally {
        loading.value = false;
      }
    };

    // ИЗБРАННОЕ
    const loadFavorites = async () => {
      try {
        const favRes = await fetch(`${API_URL}/favorites`, {
          headers: { 'Authorization': `Bearer ${getToken()}` }
        });
        if (!favRes.ok) return;
        const favs = await favRes.json();
        const uslugiRes = await fetch(`${API_URL}/uslugi`);
        if (!uslugiRes.ok) return;
        const allUslugi = await uslugiRes.json();
        for (let fav of favs) {
          const usluga = allUslugi.find(u => u.id === fav.uslugaId);
          fav.uslugaName = usluga ? usluga.name : 'Услуга #' + fav.uslugaId;
          fav.uslugaPrice = usluga ? usluga.price : null;
        }
        favorites.value = favs;
      } catch (e) {
        console.error('Ошибка загрузки избранного:', e);
      }
    };

    const removeFavorite = async (uslugaId) => {
      try {
        await fetch(`${API_URL}/favorites/${uslugaId}`, {
          method: 'DELETE',
          headers: { 'Authorization': `Bearer ${getToken()}` }
        });
        favorites.value = favorites.value.filter(f => f.uslugaId !== uslugaId);
      } catch (e) {
        console.error('Ошибка удаления из избранного:', e);
      }
    };

    // ЗАПИСЬ
    const loadAppointments = async () => {
      try {
        const res = await fetch(`${API_URL}/appointments`, {
          headers: { 'Authorization': `Bearer ${getToken()}` }
        });
        if (res.ok) {
          const apps = await res.json();
          const [uslugiRes, vrachiRes] = await Promise.all([
            fetch(`${API_URL}/uslugi`),
            fetch(`${API_URL}/vrachi`)
          ]);
          const allUslugi = uslugiRes.ok ? await uslugiRes.json() : [];
          const allVrachi = vrachiRes.ok ? await vrachiRes.json() : [];
          for (let a of apps) {
            const u = allUslugi.find(u => u.id === a.uslugaId);
            const v = allVrachi.find(v => v.id === a.vrachId);
            a.uslugaName = u ? u.name : null;
            a.vrachName = v ? v.fullName : null;
          }
          appointments.value = apps;
        }
      } catch (e) {
        console.error('Ошибка загрузки записей:', e);
      }
    };

    const deleteAppointment = async (id) => {
      try {
        await fetch(`${API_URL}/appointments/${id}`, {
          method: 'DELETE',
          headers: { 'Authorization': `Bearer ${getToken()}` }
        });
        appointments.value = appointments.value.filter(a => a.id !== id);
      } catch (e) {
        console.error('Ошибка удаления записи:', e);
      }
    };

    // КАТЕГОРИИ
    const loadCategories = async () => {
      try {
        const res = await fetch(`${API_URL}/categories`);
        if (res.ok) categoriesList.value = await res.json();
      } catch (e) {
        console.error('Ошибка загрузки категорий:', e);
      }
    };

    const addCategory = async () => {
      categoryMsg.value = '';
      try {
        const res = await fetch(`${API_URL}/categories`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}`
          },
          body: JSON.stringify({ name: newCategory.value.name })
        });
        if (!res.ok) throw new Error('Ошибка');
        newCategory.value = { name: '' };
        categoryMsg.value = 'Категория добавлена!';
        setTimeout(() => { categoryMsg.value = ''; }, 3000);
        await loadCategories();
      } catch (e) {
        categoryMsg.value = 'Ошибка';
        setTimeout(() => { categoryMsg.value = ''; }, 3000);
      }
    };

    const deleteCategory = async (id) => {
      try {
        await fetch(`${API_URL}/categories/${id}`, {
          method: 'DELETE',
          headers: { 'Authorization': `Bearer ${getToken()}` }
        });
        await loadCategories();
      } catch (e) {
        console.error('Ошибка удаления категории:', e);
        alert('Не удалось удалить категорию');
      }
    };

    // УСЛУГИ
    const loadUslugi = async () => {
      try {
        const res = await fetch(`${API_URL}/uslugi`);
        if (res.ok) uslugiList.value = await res.json();
      } catch (e) {
        console.error('Ошибка загрузки услуг:', e);
      }
    };

    const addUsluga = async () => {
      uslugaMsg.value = '';
      try {
        const body = {
          name: newUsluga.value.name,
          price: parseFloat(newUsluga.value.price) || 0,
          categoryId: newUsluga.value.categoryId || null
        };
        const res = await fetch(`${API_URL}/uslugi`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}`
          },
          body: JSON.stringify(body)
        });
        if (!res.ok) throw new Error('Ошибка');
        newUsluga.value = { name: '', price: '', categoryId: '' };
        uslugaMsg.value = 'Услуга добавлена!';
        setTimeout(() => { uslugaMsg.value = ''; }, 3000);
        await loadUslugi();
      } catch (e) {
        uslugaMsg.value = 'Ошибка';
        setTimeout(() => { uslugaMsg.value = ''; }, 3000);
      }
    };

    const startEditUsluga = (usluga) => {
      editingUsluga.value = usluga.id;
      newUsluga.value = {
        name: usluga.name,
        price: usluga.price,
        categoryId: usluga.categoryId || ''
      };
    };

    const saveEditUsluga = async () => {
      uslugaMsg.value = '';
      try {
        const body = {
          name: newUsluga.value.name,
          price: parseFloat(newUsluga.value.price) || 0,
          categoryId: newUsluga.value.categoryId || null
        };
        const res = await fetch(`${API_URL}/uslugi/${editingUsluga.value}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}`
          },
          body: JSON.stringify(body)
        });
        if (!res.ok) throw new Error('Ошибка');
        editingUsluga.value = false;
        newUsluga.value = { name: '', price: '', categoryId: '' };
        uslugaMsg.value = 'Услуга обновлена!';
        setTimeout(() => { uslugaMsg.value = ''; }, 3000);
        await loadUslugi();
      } catch (e) {
        uslugaMsg.value = 'Ошибка';
        setTimeout(() => { uslugaMsg.value = ''; }, 3000);
      }
    };

    const cancelEditUsluga = () => {
      editingUsluga.value = false;
      newUsluga.value = { name: '', price: '', categoryId: '' };
    };

    const deleteUsluga = async (id) => {
      try {
        await fetch(`${API_URL}/uslugi/${id}`, {
          method: 'DELETE',
          headers: { 'Authorization': `Bearer ${getToken()}` }
        });
        await loadUslugi();
      } catch (e) {
        alert('Не удалось удалить услугу');
      }
    };

    // ВРАЧИ
    const loadVrachi = async () => {
      try {
        const res = await fetch(`${API_URL}/vrachi`);
        if (res.ok) vrachiList.value = await res.json();
      } catch (e) {
        console.error('Ошибка загрузки врачей:', e);
      }
    };

    const addVrach = async () => {
      vrachMsg.value = '';
      try {
        const body = {
          fullName: newVrach.value.fullName,
          phone: newVrach.value.phone,
          categoryId: newVrach.value.categoryId || null
        };
        const res = await fetch(`${API_URL}/vrachi`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}`
          },
          body: JSON.stringify(body)
        });
        if (!res.ok) throw new Error('Ошибка');
        newVrach.value = { fullName: '', phone: '', categoryId: '' };
        vrachMsg.value = 'Врач добавлен!';
        setTimeout(() => { vrachMsg.value = ''; }, 3000);
        await loadVrachi();
      } catch (e) {
        vrachMsg.value = 'Ошибка';
        setTimeout(() => { vrachMsg.value = ''; }, 3000);
      }
    };

    const startEditVrach = (vrach) => {
      editingVrach.value = vrach.id;
      newVrach.value = {
        fullName: vrach.fullName,
        phone: vrach.phone,
        categoryId: vrach.categoryId || ''
      };
    };

    const saveEditVrach = async () => {
      vrachMsg.value = '';
      try {
        const body = {
          fullName: newVrach.value.fullName,
          phone: newVrach.value.phone,
          categoryId: newVrach.value.categoryId || null
        };
        const res = await fetch(`${API_URL}/vrachi/${editingVrach.value}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}`
          },
          body: JSON.stringify(body)
        });
        if (!res.ok) throw new Error('Ошибка');
        editingVrach.value = false;
        newVrach.value = { fullName: '', phone: '', categoryId: '' };
        vrachMsg.value = 'Врач обновлён!';
        setTimeout(() => { vrachMsg.value = ''; }, 3000);
        await loadVrachi();
      } catch (e) {
        vrachMsg.value = 'Ошибка';
        setTimeout(() => { vrachMsg.value = ''; }, 3000);
      }
    };

    const cancelEditVrach = () => {
      editingVrach.value = false;
      newVrach.value = { fullName: '', phone: '', categoryId: '' };
    };

    const deleteVrach = async (id) => {
      try {
        await fetch(`${API_URL}/vrachi/${id}`, {
          method: 'DELETE',
          headers: { 'Authorization': `Bearer ${getToken()}` }
        });
        await loadVrachi();
      } catch (e) {
        alert('Не удалось удалить врача');
      }
    };

    const formatDate = (dateStr) => {
      if (!dateStr) return '';
      const date = new Date(dateStr);
      return date.toLocaleDateString('ru-RU', {
        day: '2-digit', month: '2-digit', year: 'numeric',
        hour: '2-digit', minute: '2-digit'
      });
    };

    onMounted(() => loadProfile());

    return {
      user, loading, error, formatDate,
      favorites, removeFavorite,
      appointments, deleteAppointment,
      categoriesList, newCategory, categoryMsg, addCategory, deleteCategory, getCategoryName,
      uslugiList, newUsluga, uslugaMsg, editingUsluga,
      addUsluga, startEditUsluga, saveEditUsluga, cancelEditUsluga, deleteUsluga,
      vrachiList, newVrach, vrachMsg, editingVrach,
      addVrach, startEditVrach, saveEditVrach, cancelEditVrach, deleteVrach
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

.profile-content {
  flex: 1;
  padding: 40px 5%;
  max-width: 100%;
  width: 100%;
  margin: 0 auto;
  box-sizing: border-box;
}

.profile-content h2 {
  font-size: 30px;
  margin-bottom: 25px;
  color: #222;
}

.card {
  background: white;
  border-radius: 12px;
  padding: 28px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
  margin-bottom: 24px;
}

.card h3 {
  font-size: 20px;
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

.row:last-child {
  border-bottom: none;
}

.label {
  width: 200px;
  color: #666;
  font-weight: 500;
}

.value {
  color: #222;
}

/* Избранное */
.fav-list {
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.fav-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.fav-row:last-child {
  border-bottom: none;
}

.fav-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.fav-name {
  font-size: 16px;
  color: #333;
}

.fav-price {
  font-size: 14px;
  color: #1976D2;
  font-weight: 600;
}

.fav-remove-btn {
  background: none;
  border: 1px solid #ddd;
  border-radius: 50%;
  width: 28px;
  height: 28px;
  font-size: 14px;
  cursor: pointer;
  color: #999;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  padding: 0;
  line-height: 1;
}

.fav-remove-btn:hover {
  color: #e53935;
  border-color: #e53935;
}

.empty-fav {
  color: #888;
  font-size: 15px;
  text-align: center;
  padding: 10px 0;
}

/* Админка */
.admin-card {
  border-left: 4px solid #ff9800;
}

.admin-card h3 {
  border-bottom-color: #ff9800;
}

.admin-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 16px;
  width: 100%;
}

.admin-form input,
.category-select {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  min-width: 120px;
  max-width: 100%;
  box-sizing: border-box;
}

.category-select {
  background: white;
}

.admin-form button {
  padding: 10px 20px;
  background: #ff9800;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.2s;
}

.admin-form button:hover:not(:disabled) {
  background: #f57c00;
}

.admin-form button:disabled {
  background: #bbb;
  cursor: not-allowed;
}

.cancel-btn {
  background: #999;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.cancel-btn:hover {
  background: #777;
}

.msg {
  width: 100%;
  font-size: 14px;
  color: #2e7d32;
}

.admin-list {
  max-height: 200px;
  overflow-y: auto;
}

.admin-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
  font-size: 14px;
}

.admin-actions {
  display: flex;
  gap: 8px;
}

.edit-btn {
  background: #ff9800;
  color: white;
  border: none;
  padding: 5px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: background 0.2s;
}

.edit-btn:hover {
  background: #f57c00;
}

.del-btn {
  background: #e53935;
  color: white;
  border: none;
  padding: 5px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: background 0.2s;
}

.del-btn:hover {
  background: #c62828;
}

.loading, .error {
  background: white;
  padding: 50px;
  border-radius: 12px;
  text-align: center;
}

.error {
  color: #c62828;
}

@media (max-width: 900px) {
  .profile-content {
    padding: 30px 5%;
  }
  .profile-content h2 {
    font-size: 26px;
  }
}

@media (max-width: 600px) {
  .profile-content {
    padding: 20px 4%;
  }
  .profile-content h2 {
    font-size: 20px;
  }
  .card {
    padding: 18px;
  }
  .row {
    flex-direction: column;
  }
  .label {
    width: 100%;
    margin-bottom: 5px;
  }
  .admin-form input,
  .category-select {
    min-width: 100%;
  }
}
</style>