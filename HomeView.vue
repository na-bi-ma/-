<template>
  <div class="page">
    <AppHeader />

    <div class="hero">
      <div class="hero-text">
        <h2>Забота о вашем здоровье</h2>
        <p>
          Современное оборудование, опытные врачи и индивидуальный подход
          к каждому пациенту. Мы рядом, когда вам нужна помощь.
        </p>
        <router-link to="/uslugi" class="btn">Наши услуги</router-link>
      </div>
    </div>

    <div class="features-wrapper">
      <div class="features">
        <div class="feature-card">
          <h3>Опытные врачи</h3>
          <p>Специалисты с многолетним стажем и высокой квалификацией.</p>
        </div>
        <div class="feature-card">
          <h3>Современное оборудование</h3>
          <p>Диагностика и лечение на аппаратах экспертного класса.</p>
        </div>
        <div class="feature-card">
          <h3>Доступные цены</h3>
          <p>Гибкая система скидок и регулярные акции для пациентов.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AppHeader from '@/components/AppHeader.vue';
import Spinner from '@/components/Spinner.vue';

export default {
  name: 'HomeView',
  components: { AppHeader, Spinner },
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

.hero {
  background: linear-gradient(135deg, #1976D2, #42a5f5);
  color: white;
  padding: 100px 80px;
  text-align: center;
}

.hero-text h2 {
  font-size: 42px;
  margin-bottom: 20px;
}

.hero-text p {
  font-size: 20px;
  max-width: 800px;
  margin: 0 auto 35px;
  opacity: 0.9;
  line-height: 1.6;
}

.btn {
  display: inline-block;
  background: white;
  color: #1976D2;
  padding: 16px 40px;
  border-radius: 40px;
  text-decoration: none;
  font-weight: 600;
  font-size: 18px;
  transition: all 0.3s ease;
}

.btn:hover {
  background: #e3f2fd;
  transform: translateY(-2px);
}

.features-wrapper {
  padding: 50px 80px;
  background: #f5f5f5;
}

.features {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
}

.feature-card {
  background: white;
  border-radius: 16px;
  padding: 40px 30px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.12);
}

.feature-card h3 {
  color: #1976D2;
  margin-bottom: 15px;
  font-size: 22px;
}

.feature-card p {
  color: #666;
  font-size: 16px;
  line-height: 1.6;
}

@media (max-width: 900px) {
  .hero {
    padding: 60px 30px;
  }

  .hero-text h2 {
    font-size: 32px;
  }

  .hero-text p {
    font-size: 17px;
  }

  .features-wrapper {
    padding: 40px 30px;
  }

  .features {
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  }
}

@media (max-width: 600px) {
  .hero {
    padding: 50px 20px;
  }

  .hero-text h2 {
    font-size: 24px;
  }

  .hero-text p {
    font-size: 14px;
  }

  .btn {
    padding: 12px 28px;
    font-size: 15px;
  }

  .features-wrapper {
    padding: 30px 20px;
  }

  .features {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .feature-card {
    padding: 30px 20px;
  }
}
</style>