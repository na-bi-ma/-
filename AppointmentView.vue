<template>
  <div class="page">
    <AppHeader />

    <div class="content">
      <h2>Запись на приём</h2>

      <!-- Выбор услуги -->
      <div v-if="step === 1">
        <h3>Шаг 1: Выберите услугу</h3>
        <Spinner v-if="loadingUslugi" text="Загрузка услуг..." />
        <div v-else class="select-list">
          <div v-for="u in uslugi" :key="u.id" class="select-card" @click="selectUsluga(u)">
            <div class="select-name">{{ u.name }}</div>
            <div class="select-price">{{ u.price }} руб.</div>
          </div>
        </div>
      </div>

      <!-- Выбор врача -->
      <div v-if="step === 2">
        <h3>Шаг 2: Выберите врача</h3>
        <div class="selected-info">
          <span class="selected-label">Услуга:</span> {{ selectedUsluga.name }} ({{ selectedUsluga.price }} руб.)
        </div>
        <Spinner v-if="loadingVrachi" text="Загрузка врачей..." />
        <div v-else class="select-list">
          <div v-for="v in vrachi" :key="v.id" class="select-card" @click="selectVrach(v)">
            <div class="select-name">{{ v.fullName }}</div>
            <div class="select-spec">{{ v.categoryId ? getCategoryName(v.categoryId) : 'Категория не указана' }}</div>
          </div>
        </div>
        <div class="step-buttons">
          <button class="btn-back" @click="step = 1">← Назад</button>
        </div>
      </div>

      <!-- Выбор даты и времени -->
      <div v-if="step === 3">
        <h3>Шаг 3: Выберите дату и время</h3>
        <div class="selected-info">
          <div><span class="selected-label">Услуга:</span> {{ selectedUsluga.name }} ({{ selectedUsluga.price }} руб.)</div>
          <div><span class="selected-label">Врач:</span> {{ selectedVrach.fullName }}</div>
        </div>

        <input type="date" v-model="selectedDate" :min="today" class="date-input" />
        <button class="btn" @click="loadSlots" :disabled="!selectedDate">Показать время</button>

        <Spinner v-if="loadingSlots" text="Загрузка..." />
        <div v-else-if="availableSlots.length > 0" class="slots">
          <h4>Доступное время:</h4>
          <div class="slots-grid">
            <button
              v-for="slot in availableSlots"
              :key="slot"
              class="slot-btn"
              :class="{ active: selectedTime === slot }"
              @click="selectedTime = slot"
            >
              {{ slot }}
            </button>
          </div>
        </div>
        <div v-else-if="selectedDate && !loadingSlots" class="empty">Нет доступного времени на эту дату</div>

        <div class="step-buttons">
          <button class="btn-back" @click="step = 2">← Назад</button>
          <button class="btn" @click="confirmAppointment" :disabled="!selectedTime">Записаться</button>
        </div>
      </div>

      <!-- Успех -->
      <div v-if="step === 4">
        <div class="success-card">
          <h3>Вы записаны!</h3>
          <p><strong>Услуга:</strong> {{ selectedUsluga.name }}</p>
          <p><strong>Врач:</strong> {{ selectedVrach.fullName }}</p>
          <p><strong>Дата:</strong> {{ formatDate(selectedDate) }}</p>
          <p><strong>Время:</strong> {{ selectedTime }}</p>
          <p><strong>Цена:</strong> {{ selectedUsluga.price }} руб.</p>
          <div class="step-buttons" style="justify-content: center;">
            <router-link to="/profile" class="btn">В профиль</router-link>
            <button class="btn-back" @click="resetForm">Записаться ещё</button>
          </div>
        </div>
      </div>

      <div v-if="error" class="error">{{ error }}</div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue';
import AppHeader from '@/components/AppHeader.vue';
import Spinner from '@/components/Spinner.vue';

const API_URL = 'http://localhost:8080/api';

export default {
  name: 'AppointmentView',
  components: { AppHeader, Spinner },
  setup() {
    const step = ref(1);
    const uslugi = ref([]);
    const vrachi = ref([]);
    const categories = ref([]);
    const selectedUsluga = ref({});
    const selectedVrach = ref({});
    const selectedDate = ref('');
    const selectedTime = ref('');
    const loadingUslugi = ref(true);
    const loadingVrachi = ref(false);
    const loadingSlots = ref(false);
    const error = ref('');
    const busySlots = ref([]);

    const today = computed(() => new Date().toISOString().split('T')[0]);

    const allSlots = [];
    for (let h = 8; h <= 18; h++) {
      allSlots.push(`${String(h).padStart(2, '0')}:00`);
      if (h < 18) allSlots.push(`${String(h).padStart(2, '0')}:30`);
    }

    const availableSlots = computed(() => {
      const busy = busySlots.value.map(s => s.appointmentTime?.substring(0, 5));
      return allSlots.filter(s => !busy.includes(s));
    });

    const getCategoryName = (id) => {
      const cat = categories.value.find(c => c.id == id);
      return cat ? cat.name : null;
    };

    const getToken = () => localStorage.getItem('token');

    const loadCategories = async () => {
      try {
        const res = await fetch(`${API_URL}/categories`);
        if (res.ok) categories.value = await res.json();
      } catch (e) {
        console.error('Ошибка загрузки категорий:', e);
      }
    };

    const loadUslugi = async () => {
      try {
        const res = await fetch(`${API_URL}/uslugi`);
        uslugi.value = await res.json();
      } catch (e) {
        error.value = 'Ошибка загрузки услуг';
      } finally {
        loadingUslugi.value = false;
      }
    };

    const selectUsluga = (usluga) => {
      selectedUsluga.value = usluga;
      step.value = 2;
      loadVrachi();
    };

    const loadVrachi = async () => {
      loadingVrachi.value = true;
      try {
        const res = await fetch(`${API_URL}/vrachi`);
        const allVrachi = await res.json();
        if (selectedUsluga.value.categoryId) {
          vrachi.value = allVrachi.filter(v => v.categoryId == selectedUsluga.value.categoryId);
        } else {
          vrachi.value = allVrachi;
        }
      } catch (e) {
        error.value = 'Ошибка загрузки врачей';
      } finally {
        loadingVrachi.value = false;
      }
    };

    const selectVrach = (vrach) => {
      selectedVrach.value = vrach;
      step.value = 3;
    };

    const loadSlots = async () => {
      loadingSlots.value = true;
      selectedTime.value = '';
      try {
        const res = await fetch(`${API_URL}/appointments/slots?vrachId=${selectedVrach.value.id}&date=${selectedDate.value}`);
        busySlots.value = await res.json();
      } catch (e) {
        error.value = 'Ошибка загрузки слотов';
      } finally {
        loadingSlots.value = false;
      }
    };

    const confirmAppointment = async () => {
      try {
        const res = await fetch(`${API_URL}/appointments`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}`
          },
          body: JSON.stringify({
            uslugaId: selectedUsluga.value.id,
            vrachId: selectedVrach.value.id,
            appointmentDate: selectedDate.value,
            appointmentTime: selectedTime.value
          })
        });

        if (!res.ok) {
          const data = await res.json().catch(() => ({}));
          throw new Error(data.error || 'Ошибка');
        }
        step.value = 4;
      } catch (e) {
        error.value = e.message || 'Ошибка при записи';
      }
    };

    const resetForm = () => {
      step.value = 1;
      selectedUsluga.value = {};
      selectedVrach.value = {};
      selectedDate.value = '';
      selectedTime.value = '';
      error.value = '';
    };

    const formatDate = (dateStr) => {
      if (!dateStr) return '';
      const parts = dateStr.split('-');
      return `${parts[2]}.${parts[1]}.${parts[0]}`;
    };

    onMounted(() => {
      loadUslugi();
      loadCategories();
    });

    return {
      step, uslugi, vrachi, selectedUsluga, selectedVrach,
      selectedDate, selectedTime, loadingUslugi, loadingVrachi, loadingSlots,
      error, today, availableSlots,
      selectUsluga, selectVrach, loadSlots, confirmAppointment, resetForm, formatDate,
      getCategoryName
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
  max-width: 800px;
  width: 100%;
  margin: 0 auto;
  box-sizing: border-box;
}

.content h2 { font-size: 30px; margin-bottom: 20px; color: #222; }
.content h3 { font-size: 22px; margin-bottom: 16px; color: #333; }
.content h4 { font-size: 18px; margin-bottom: 12px; color: #555; }

.selected-info {
  background: #e3f2fd;
  padding: 16px;
  border-radius: 10px;
  margin-bottom: 20px;
  font-size: 15px;
  color: #333;
  line-height: 1.6;
}

.selected-label { font-weight: 600; color: #1976D2; }

.select-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.select-card {
  background: white;
  padding: 20px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
}

.select-card:hover { background: #e3f2fd; transform: translateY(-1px); }

.select-name { font-size: 18px; font-weight: 600; color: #222; margin-bottom: 4px; }
.select-price { font-size: 16px; color: #1976D2; font-weight: 600; }
.select-spec { font-size: 14px; color: #888; margin-top: 4px; }

.date-input {
  padding: 14px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  margin-bottom: 16px;
  width: 100%;
  box-sizing: border-box;
}

.slots-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
  gap: 8px;
  margin-bottom: 24px;
}

.slot-btn {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: white;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.slot-btn:hover { border-color: #1976D2; background: #e3f2fd; }
.slot-btn.active { background: #1976D2; color: white; border-color: #1976D2; }

.btn {
  padding: 14px 32px;
  background: #1976D2;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  text-decoration: none;
  display: inline-block;
}

.btn:hover { background: #1565C0; }
.btn:disabled { background: #bbb; cursor: not-allowed; }

.btn-back {
  padding: 14px 32px;
  background: #eee;
  color: #555;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
}

.btn-back:hover { background: #ddd; }

.step-buttons {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

.success-card {
  background: white;
  padding: 32px;
  border-radius: 16px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}

.success-card p { font-size: 16px; margin: 8px 0; color: #555; }

.loading, .error, .empty {
  background: white;
  padding: 40px;
  border-radius: 12px;
  text-align: center;
  color: #888;
}
.error { color: #c62828; }

@media (max-width: 600px) {
  .content { padding: 20px 4%; }
  .content h2 { font-size: 22px; }
  .content h3 { font-size: 18px; }
  .select-name { font-size: 16px; }
}
</style>