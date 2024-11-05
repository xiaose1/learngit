<template>
	<el-menu class="dashboard" :collapse="isCollapse" background-color="var(--color-deep-1)"
		text-color="var(--color-primary-white)" active-text-color="var(--color-primary-green)">
		<el-menu-item @click="switchCollapse" index="0">
			<el-icon>
				<ArrowRightBold v-show="isCollapse" />
				<ArrowLeftBold v-show="!isCollapse" />
			</el-icon>
			<span>收起</span>
		</el-menu-item>
		<!-- <el-menu-item index="1" @click="toPages('/')">
			<el-icon>
				<House />
			</el-icon>
			<span>网站首页</span>
		</el-menu-item> -->
		<el-menu-item index="2" @click="toPages('/')">
			<el-icon>
				<EditPen />
			</el-icon>
			<span>个人信息</span>
		</el-menu-item>
		<el-menu-item index="3" @click="toPages('/userManagement')" v-show="isAdmin">
			<el-icon>
				<User />
			</el-icon>
			<span>用户管理</span>
		</el-menu-item>
		<el-menu-item index="4" @click="toPages('/equipmentManagement')">
			<el-icon>
				<Operation />
			</el-icon>
			<span>设备管理</span>
		</el-menu-item>
		<el-menu-item index="5" @click="toPages('/plantManagement')" v-show="isAdmin">
			<el-icon>
				<Apple />
			</el-icon>
			<span>植物管理</span>
		</el-menu-item>
	</el-menu>
</template>

<script setup>
	import {
		ArrowRightBold,
		ArrowLeftBold,
		House,
		Tickets,
		User,
		Apple,
		EditPen,
		Operation
	} from '@element-plus/icons-vue'
	import {
		ref,
		getCurrentInstance
	} from 'vue'
	import router from '@/router';
	//全局变量
	const proxy = getCurrentInstance()
	const isAdmin = ref(false);
	const getRole = () => {
		proxy.appContext.config.globalProperties.$api.getUserRole().then((
			res) => {
		
			if(res.data.data == 1){
				isAdmin.value = true;
			}
		})
	}
	getRole();

	//用于网页响应式改变侧边栏
	const isCollapse = ref(false)
	const switchCollapse = () => {
		isCollapse.value = !isCollapse.value;
	}
	window.onresize = () => {
		if (window.innerWidth <= 768) {
			isCollapse.value = true;
		} else {
			isCollapse.value = false;
		}
	}

	//页面跳转
	const toPages = (pageHref) => {

		router.push(pageHref);
	}
</script>

<style scoped>
	.dashboard {
		height: 100%;
	}

	.dashboard:not(.el-menu--collapse) {
		width: 175px;
	}
</style>