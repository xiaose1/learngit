<template>
	<el-menu class="navbar" mode="horizontal" :ellipsis="false" background-color="var(--color-deep-1)"
		text-color="var(--color-primary-white)" active-text-color="var(--color-primary-green)">
		<el-menu-item index="0">
			<!-- <el-icon>
					<MostlyCloudy />
				</el-icon> -->
			<h3>植物生长监控系统</h3>
		</el-menu-item>
		<div class="flex-grow" />
		<el-menu-item index="1" @click="logout">
			<el-icon>
				<SwitchButton />
			</el-icon>
			安全注销
		</el-menu-item>
	</el-menu>
</template>

<script setup>
	import {
		MostlyCloudy,
		SwitchButton,
	} from '@element-plus/icons-vue'
	import {
		ElMessage
	} from 'element-plus';
	import {
		getCurrentInstance
	} from 'vue';
	import router from '@/router';
	//全局变量
	const proxy = getCurrentInstance()

	const logout = () => {
		proxy.appContext.config.globalProperties.$api.logout().then(res => {
			if (res.data.code == '200') {
				ElMessage({
					type: 'success',
					message: "注销成功"
				});
				router.push('/login');
			}
		})
	}
</script>

<style scoped>
	.navbar {
		/* Safari */
		/* position: -webkit-sticky; */
		position: fixed;
		width: 100%;
		height: 60px;
		/* 设置导航栏距离顶部的距离 */
		top: 0;
		/* 确保导航栏在最上层 */
		z-index: 1000;
		border-bottom: 1px solid var(--color-deep-2);
	}
</style>