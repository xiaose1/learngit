<template>
	<div class="login-background">
		<div class="vertical-center horizontally-center" style="height: 100vh;
		backdrop-filter: blur(5px);">
			<!-- 登录界面 -->
			<el-card v-show="isLogin" style="max-width: 600px;">
				<el-form :model="form" label-width="auto" style=" padding: 20px 40px;">
					<h3 style="text-align: center;">系统登录</h3>
					<el-form-item label="账号">
						<el-input v-model="form.userUsername" type="text" placeholder="请输入用户名或手机号" />
					</el-form-item>
					<el-form-item label="密码">
						<el-input v-model="form.userPassword" type="password" placeholder="请输入密码" />
					</el-form-item>
					<el-form-item>
						<el-button style="width: 100%;" type="success" color="var(--color-primary-green)"
							@click="submit">登录</el-button>
						<div style="text-align: center; width: 100%;">
							<el-link type="success" color="var(--color-primary-green)"
								@click="switchLoginOrRegister">没有账号？点击注册</el-link>
						</div>
					</el-form-item>
				</el-form>
			</el-card>

			<!-- 注册界面 -->
			<el-card v-show="!isLogin" style="max-width: 600px;">
				<el-form :model="form" label-width="auto"
					style="min-width: 300px; max-width: 600px; padding: 20px 40px;">
					<h3 style="text-align: center;">系统注册</h3>
					<el-form-item label="昵称">
						<el-input v-model="form.userNickname" type="text" placeholder="请输入昵称" />
					</el-form-item>
					<el-form-item label="用户名">
						<el-input v-model="form.userUsername" type="text" placeholder="请输入用户名" />
					</el-form-item>
					<el-form-item label="手机号">
						<el-input v-model="form.userPhone" type="text" placeholder="请输入手机号" />
					</el-form-item>
					<el-form-item label="密码">
						<el-input v-model="form.userPassword" type="password" placeholder="请输入密码" />
					</el-form-item>
					<el-form-item label="确认密码">
						<el-input v-model="form.userPasswordAgain" type="password" placeholder="请再次输入密码" />
					</el-form-item>
					<el-form-item>
						<el-button style="width: 100%;" type="success" color="var(--color-primary-green)"
							@click="submit">注册</el-button>
						<div style="text-align: center; width: 100%;">
							<el-link type="success" color="var(--color-primary-green)"
								@click="switchLoginOrRegister">已有账号？点击登录</el-link>
						</div>
					</el-form-item>
				</el-form>
			</el-card>
		</div>
	</div>
</template>

<script setup>
	import {
		ElMessage
	} from 'element-plus';
	import {
		reactive,
		ref,
		getCurrentInstance
	} from 'vue'
	import router from '@/router';

	//全局变量
	const proxy = getCurrentInstance()

	const isLogin = ref(true)
	const switchLoginOrRegister = () => {
		isLogin.value = !isLogin.value;
	}

	const form = reactive({
		userNickname: '',
		userUsername: '',
		userPhone: '',
		userPassword: '',
		userPasswordAgain: '',
		userStatus: 0
	})

	const submit = () => {

		if (isLogin.value) {
			//登录
			proxy.appContext.config.globalProperties.$api.login(form).then((res) => {

				if (res.data.code == '200') {
					ElMessage({
						type: 'success',
						message: "登录成功"
					});
					router.push('/');
				}
			});
		} else {
			//注册
			proxy.appContext.config.globalProperties.$api.register(form).then((res) => {

				if (res.data.code == '200') {
					ElMessage({
						type: 'success',
						message: "注册成功"
					});
					isLogin.value = true;
				}
			});
		}
	}
</script>

<style scoped>
	.login-background {
		background-image: url('/img/login-background.jpeg');
		background-size: cover;
		background-repeat: no-repeat;
		background-position: center;
		background-attachment: fixed;
	}
</style>