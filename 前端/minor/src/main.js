import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './assets/base.css'

const app = createApp(App)

app.use(ElementPlus)
app.use(router);
app.mount('#app')


// 导入封装的api对象
import api from './components/api.js'
// 将api对象设置为app的全局属性
app.config.globalProperties.$api = api