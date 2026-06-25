<template>
  <div class="page">
    <AppHeader />

    <div class="content">
      <h2>Немного о нас</h2>

      <div class="about-grid">
        <div class="about-card">
          <h3>Что для нас важно</h3>
          <p>
            Мы просто хотим помогать людям чувствовать себя лучше.
            Без лишнего пафоса и сложностей. Просто качественная медицина,
            когда она вам нужна.
          </p>
        </div>

        <div class="about-card">
          <h3>Как мы росли</h3>
          <p>
            Всё началось в 2018 году с маленькой уютной клиники.
            Сейчас нас уже больше, но мы по-прежнему стараемся относиться
            к каждому пациенту по-доброму. За год к нам приходит больше
            100 000 человек — и это очень приятно!
          </p>
        </div>

        <div class="about-card">
          <h3>На чём мы стоим</h3>
          <ul>
            <li>Быть честными с вами</li>
            <li>Постоянно учиться новому</li>
            <li>Слушать и слышать вас</li>
            <li>Использовать современные методы, но без фанатизма</li>
          </ul>
        </div>

        <div class="about-card">
          <h3>Как нас найти</h3>
          <div class="contact-row">
            <span class="label">Где мы:</span>
            <span>Брест, улица Врачебная, 15</span>
          </div>
          <div class="contact-row">
            <span class="label">Позвонить:</span>
            <span>+375 (17) 123-45-67</span>
          </div>
          <div class="contact-row">
            <span class="label">Написать:</span>
            <span>info@medcentre.by</span>
          </div>
          <div class="contact-row">
            <span class="label">Когда приходить:</span>
            <span>Пн–Пт: 7:30–21:00<br />Сб: 8:00–20:00<br />Вс: 8:30–19:00 </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AppHeader from '@/components/AppHeader.vue';
import Spinner from '@/components/Spinner.vue';

export default {
  name: 'AboutView',
  components: { AppHeader, Spinner }
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

.about-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
}

.about-card {
  background: white;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  transition: transform 0.2s, box-shadow 0.2s;
}

.about-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.about-card h3 {
  font-size: 20px;
  color: #1976D2;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid #e3f2fd;
  font-weight: 500;
}

.about-card p {
  font-size: 16px;
  line-height: 1.7;
  color: #555;
}

.about-card ul {
  list-style: none;
  padding: 0;
}

.about-card ul li {
  font-size: 16px;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
  color: #555;
  padding-left: 24px;
  position: relative;
}

.about-card ul li::before {
  content: "✓";
  color: #1976D2;
  font-weight: bold;
  position: absolute;
  left: 0;
}

.about-card ul li:last-child {
  border-bottom: none;
}

.contact-row {
  display: flex;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  font-size: 16px;
  color: #555;
  line-height: 1.5;
}

.contact-row:last-child {
  border-bottom: none;
}

.label {
  width: 130px;
  color: #999;
  font-weight: 400;
}

@media (max-width: 900px) {
  .content {
    padding: 40px 30px;
  }

  .content h2 {
    font-size: 26px;
  }

  .about-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
}

@media (max-width: 600px) {
  .content {
    padding: 25px 20px;
  }

  .content h2 {
    font-size: 22px;
  }

  .about-card {
    padding: 20px;
  }

  .contact-row {
    flex-direction: column;
  }

  .label {
    width: 100%;
    margin-bottom: 6px;
  }
}
</style>