<template>
	<!-- 头部布局 -->
	<Navbar></Navbar>
	<!-- <el-row class="main-container">
		<el-col :span="6" :md="5" :lg="4" :xl="3">
			<DashBoard></DashBoard>
		</el-col>
		<el-col :span="18" :md="19" :lg="20" :xl="21" style="padding: 15px;">
			<router-view></router-view>
			<div class="page-footer" style="position: relative; bottom: 0;">
				2023
			</div>
		</el-col>
	</el-row> -->
	<el-container>
		<el-aside style="width: auto; padding-top: 60px;">
			<DashBoard></DashBoard>
		</el-aside>
		<el-main class="main-container">
			<div style="padding: 15px;">
				<router-view></router-view>
			</div>
			<div class="page-footer" style="position: absolute; bottom: 0; left: 50%; text-align: center;">
				<el-text>Copyright © 2024-2025</el-text>
			</div>
		</el-main>
	</el-container>
</template>

<script setup>
	import Navbar from '../components/Navbar.vue'
	import DashBoard from '../components/DashBoard.vue'
	import router from '@/router';
</script>

<style scoped>
	.main-container {
		height: 100vh;
		padding-top: 60px;
	}
</style>