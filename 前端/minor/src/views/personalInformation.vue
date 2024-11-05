<template>
	<div class="page-header vertical-center">
		<span class="page-header-title">个人信息</span>
	</div>

	<el-row :gutter="15" style=" margin-top: 15px;">
		<el-col :md="18">
			<el-card>
				<el-row justify="center">
					<el-col :md="18">
						<el-form ref="ruleFormRef" :rules="formRules" :model="userForm" label-width="auto" style="min-width: 200px; padding: 20px 40px;">
							<h3 style="text-align: center;">个人信息</h3>
							<el-form-item prop="userNickname" label="昵称">
								<el-input v-model="userForm.userNickname" type="text" placeholder="请输入昵称" />
							</el-form-item>
							<el-form-item prop="userUsername" label="用户名">
								<el-input v-model="userForm.userUsername" type="text" placeholder="请输入用户名" />
							</el-form-item>
							<el-form-item prop="userPhone" label="手机号">
								<el-input v-model="userForm.userPhone" type="text" placeholder="请输入手机号" />
							</el-form-item>
							<el-form-item label="密码">
								<el-input style="width: 70%;" v-model="userForm.userUsername" type="password"
									placeholder="请输入要修改的密码" :disabled="!isUpdatePassword" />
								<div style="position: relative; width: 30%;">
									<el-button style="float: right;" type="danger"
										@click="switchUpdatePassword">修改密码</el-button>
								</div>
							</el-form-item>
							<el-row :gutter="20">
								<el-col :span="12">
									<el-form-item label="角色">
										<el-tag type="success" effect="dark">{{roleOptions[userForm.userRole]}}</el-tag>
									</el-form-item>
								</el-col>
								<el-col :span="12">
									<el-form-item label="状态">
										<el-tag type="success"
											effect="dark">{{statusOptions[userForm.userStatus].label}}</el-tag>
									</el-form-item>
								</el-col>
							</el-row>
							<el-form-item>
								<el-button style="width: 100%;" type="success" color="var(--color-primary-green)"
									@click="saveEdit(ruleFormRef)">保存更改</el-button>
							</el-form-item>
						</el-form>
					</el-col>
				</el-row>
			</el-card>
		</el-col>
		<el-col :md="6">
			<el-card style="text-align: center;">
				<el-statistic :value="status.equipmentOpenCount">
					<template #title>
						<div>
							<el-text size="large">设备数</el-text>
						</div>
					</template>
					<template #suffix>/ {{status.equipmentAllCount ? status.equipmentAllCount : 0}}</template>
				</el-statistic>
				<el-divider></el-divider>
				<el-statistic :value="status.plantCount">
					<template #title>
						<div>
							<el-text size="large">已收录植物</el-text>
						</div>
					</template>
					<template #suffix></template>
				</el-statistic>
				<el-divider></el-divider>
				<el-statistic :value="userForm.userCreateTime.split(' ')[0]">
					<template #title>
						<div>
							<el-text size="large">注册时间</el-text>
						</div>
					</template>
				</el-statistic>
			</el-card>
		</el-col>
	</el-row>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance,
		reactive
	} from 'vue';
	import {
		ElMessage,
	} from 'element-plus';

	//全局变量
	const proxy = getCurrentInstance()
	const roleOptions = ref(["普通用户", "管理员"])
	const statusOptions = ref([{
		label: "正常",
		value: 0
	}, {
		label: "封禁",
		value: 1
	}])


	const userForm = reactive({
		userId: "1",
		userNickname: "林叁柒",
		userUsername: "lin037",
		userRole: 1,
		userPhone: "18476095076",
		userStatus: 0,
		userCreateTime: "2024-05-27 21:22:56",
		userUpdateTime: "2024-05-27 21:22:56",
	})
	const getData = () => {

		proxy.appContext.config.globalProperties.$api.getSelfMsg().then(res => {
			userForm.userId = res.data.data.userId;
			userForm.userNickname = res.data.data.userNickname;
			userForm.userUsername = res.data.data.userUsername;
			userForm.userRole = res.data.data.userRole;
			userForm.userPhone = res.data.data.userPhone;
			userForm.userStatus = res.data.data.userStatus;
			userForm.userCreateTime = res.data.data.userCreateTime;
		})
	}
	getData()

	const status = reactive({
		equipmentOpenCount: 0,
		equipmentAllCount: 0,
		plantCount: 0
	})
	const getEquipmentCount = () => {
		proxy.appContext.config.globalProperties.$api.getEquipmentCount().then(res => {

			status.equipmentOpenCount = res.data.data.equipmentOpenCount;
			status.equipmentAllCount = res.data.data.equipmentAllCount;
		})
	}
	getEquipmentCount()
	const getPlantCount = () => {
		proxy.appContext.config.globalProperties.$api.getPlantCount().then(res => {

			status.plantCount = res.data.data;
		})
	}
	getPlantCount()


	const isUpdatePassword = ref(false)
	const switchUpdatePassword = () => {
		userForm.userPassword = "";
		isUpdatePassword.value = !isUpdatePassword.value;
	}
	// 表单格式验证
	const ruleFormRef = ref()
	// 手机号验证
	const validatePhone = (rule, value, callback) => {
		let str = new RegExp("^1[3578]\\d{9}$");
		if (value === '') {
			callback(new Error('请输入手机号'))
		} else if (!str.test(value)) {
			callback(new Error('手机号错误'))
		} else {
			callback()
		}
	}
	const formRules = reactive({
		userNickname: [{
				required: true,
				message: '请输入昵称',
				trigger: 'blur'
			},
			{
				min: 2,
				max: 7,
				message: '长度应在2到7之间',
				trigger: 'blur'
			},
		],
		userUsername: [{
				required: true,
				message: '请输入用户名',
				trigger: 'blur'
			},
			{
				min: 5,
				max: 10,
				message: '长度应在5到10之间',
				trigger: 'blur'
			},
		],
		userPhone: [{
			validator: validatePhone,
			trigger: 'blur'
		}]
	})
	const saveEdit = async (formEl) => {
		if (!formEl) return
		await formEl.validate((valid, fields) => {
			if (valid) {
				console.log('submit!')
				if (!isUpdatePassword.value) userForm.userPassword = "";
				proxy.appContext.config.globalProperties.$api.updateSelf(userForm).then((
					res) => {

					if (res.data.code == '200') {
						ElMessage({
							type: 'success',
							message: res.data.description
						});
						getData();
						isShowEditDialog.value = false;
					}
				});
			} else {
				console.log('error submit!', fields)
			}
		})
	}
</script>

<style scoped>
</style>