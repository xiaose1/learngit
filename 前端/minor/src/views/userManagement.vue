<template>
	<div class="page-header vertical-center">
		<span class="page-header-title">用户管理</span>
		<div class="flex-grow"></div>
		<el-space>
			<el-button type="success" color="var(--color-primary-green)" @click="switchShowNewDialog">新增</el-button>
			<el-button type="warning" :disabled="haveSelect" @click="batchBan">批量封禁</el-button>
		</el-space>
	</div>

	<el-dialog v-model="isShowNewDialog" title="新增用户" width="500">
		<el-form ref="ruleFormRef" :model="userNewForm" label-width="auto" style="padding: 20px;" :rules="newFormRules">
			<el-form-item label="昵称" prop="userNickname">
				<el-input v-model="userNewForm.userNickname" type="text" placeholder="请输入昵称" />
			</el-form-item>
			<el-form-item label="用户名" prop="userUsername">
				<el-input v-model="userNewForm.userUsername" type="text" placeholder="请输入用户名" />
			</el-form-item>
			<el-form-item label="手机号" prop="userPhone">
				<el-input v-model="userNewForm.userPhone" type="text" placeholder="请输入手机号" />
			</el-form-item>
			<el-form-item label="密码" prop="userPassword" required>
				<el-input v-model="userNewForm.userPassword" type="password" placeholder="请输入密码" autocomplete="off" />
			</el-form-item>
			<el-form-item label="确认密码" prop="userPasswordAgain" required>
				<el-input v-model="userNewForm.userPasswordAgain" type="password" placeholder="请再次输入密码"
					autocomplete="off" />
			</el-form-item>
			<el-form-item label="状态" required>
				<el-select v-model="userNewForm.userStatus" placeholder="请选择状态">
					<el-option v-for="status in statusOptions" :label="status.label" :value="status.value" />
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button style="width: 100%;" type="success" color="var(--color-primary-green)"
					@click="newUser(ruleFormRef)">新增</el-button>
			</el-form-item>
		</el-form>
	</el-dialog>

	<el-dialog v-model="isShowEditDialog" title="用户信息编辑" width="500">
		<el-form :model="userEditForm" label-width="auto" style="padding: 20px;">
			<el-form-item label="昵称">
				<el-input v-model="userEditForm.userNickname" type="text" placeholder="请输入昵称" />
			</el-form-item>
			<el-form-item label="手机号">
				<el-input v-model="userEditForm.userPhone" type="text" placeholder="请输入手机号" />
			</el-form-item>
			<el-form-item label="密码">
				<el-input style="width: 70%;" v-model="userEditForm.userPassword" type="password"
					placeholder="请输入要修改的密码" :disabled="!isUpdatePassword" />
				<div style="position: relative; width: 30%;">
					<el-button style="float: right;" type="danger" @click="switchUpdatePassword">{{isUpdatePassword ? "取消修改" : "修改密码"}}</el-button>
				</div>
			</el-form-item>
			<el-form-item label="状态">
				<el-select v-model="userEditForm.userStatus" placeholder="请选择状态">
					<el-option v-for="status in statusOptions" :label="status.label" :value="status.value" />
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button style="width: 100%;" type="success" color="var(--color-primary-green)"
					@click="saveEdit">保存更改</el-button>
			</el-form-item>
		</el-form>
	</el-dialog>

	<el-table ref="table" @selection-change="handleSelectChange" :data="userList"
		style="width: 100%; margin-top: 15px; height: 70vh;">
		<el-table-column type="selection" width="55" />
		<el-table-column fixed prop="userId" label="用户ID" width="80" />
		<el-table-column fixed prop="userNickname" label="昵称" width="100" />
		<el-table-column prop="userUsername" label="用户名" width="120" />
		<el-table-column prop="userRole" label="用户角色" width="80">
			<template #default="scope">
				{{roleOptions[scope.row.userRole]}}
			</template>
		</el-table-column>
		<el-table-column prop="userPhone" label="手机号" width="120" />
		<el-table-column prop="userStatus" label="状态" width="80">
			<template #default="scope">
				{{statusOptions[scope.row.userStatus].label}}
			</template>
		</el-table-column>
		<el-table-column prop="userCreateTime" label="用户创建时间" width="200" />
		<el-table-column prop="userUpdateTime" label="用户更新时间" width="200" />
		<el-table-column fixed="right" label="操作" min-width="150">
			<template #header>
				<el-input placeholder="搜索" size="small" v-model="searchStr">
					<template #append>
						<el-button size="small" :icon="Search" @click="toSearch()" />
					</template>
				</el-input>
			</template>
			<template #default="scope">
				<el-button type="primary" size="small" @click="switchShowEditDialog(scope.row)">
					编辑
				</el-button>
				<el-button type="danger" size="small" @click="removeUser(scope.row.userId)">
					删除
				</el-button>
			</template>
		</el-table-column>
	</el-table>
	<el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" layout="prev, pager, next, jumper"
		:total="total" :pageCount="pageCount" />

</template>

<script setup>
	import {
		reactive,
		ref,
		watch,
		getCurrentInstance
	} from 'vue';
	import {
		ElMessage,
		ElMessageBox
	} from 'element-plus';
	import {
		Search
	} from '@element-plus/icons-vue'
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
	//用户列表
	const userList = ref([])
	//搜索框
	const searchStr = ref('')
	//分页
	const currentPage = ref(1)
	const pageSize = ref(20)
	const total = ref(20)
	const pageCount = ref(20)

	//获取用户列表
	const getData = (searchStr) => {
		proxy.appContext.config.globalProperties.$api.getUsers(currentPage.value, pageSize.value, searchStr).then((
			res) => {
			console.log(res.data.data.records);
			res.data.data.records.forEach((user) => {

				userList.value.push(user);
			});
			total.value = res.data.data.total;
			pageCount.value = res.data.data.pages;
		});
	}
	getData("");
	//刷新列表数据
	const fleshUserList = () => {
		userList.value.splice(0, userList.value.length);
		getData(searchStr.value);
	}
	//操作分页
	const handleCurrentChange = (val) => {
		currentPage.value = val;
		fleshUserList();
	}
	//搜索
	const toSearch = () => {

		fleshUserList();
	}


	//编辑对话框
	const isShowEditDialog = ref(false)
	const isUpdatePassword = ref(false)
	const userEditForm = reactive({
		userId: "",
		userNickname: "",
		userPassword: "123456",
		userRole: "",
		userPhone: "",
		userStatus: "正常",
		userCreateTime: "",
		userUpdateTime: "",
	})
	const switchShowEditDialog = (user) => {
		userEditForm.userId = user.userId;
		userEditForm.userNickname = user.userNickname;
		userEditForm.userRole = user.userRole;
		userEditForm.userPhone = user.userPhone;
		userEditForm.userStatus = user.userStatus;

		isShowEditDialog.value = true;
	}
	const switchUpdatePassword = () => {
		userEditForm.userPassword = "";
		isUpdatePassword.value = !isUpdatePassword.value;
	}
	const saveEdit = () => {
		
		if(!isUpdatePassword.value) userEditForm.userPassword = "";
		proxy.appContext.config.globalProperties.$api.updateUsers(userEditForm).then((
			res) => {

			if (res.data.code == '200') {
				ElMessage({
					type: 'success',
					message: res.data.description
				});
				fleshUserList();
				isShowEditDialog.value = false;
			}
		});
	}

	//新增对话框
	const isShowNewDialog = ref(false)
	const userNewForm = reactive({
		userNickname: '',
		userUsername: '',
		userPhone: '',
		userPassword: '',
		userPasswordAgain: '',
		userStatus: 0
	})
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
	// 密码验证
	const validatePass = (rule, value, callback) => {
		if (value === '') {
			callback(new Error('请输入密码'))
		} else {
			if (userNewForm.userPasswordAgain !== '') {
				if (!ruleFormRef.value) return
				ruleFormRef.value.validateField('userPasswordAgain')
			}
			callback()
		}
	}
	// 确认密码验证
	const validatePass2 = (rule, value, callback) => {
		if (value === '') {
			callback(new Error('请再次输入密码'))
		} else if (value !== userNewForm.userPassword) {
			callback(new Error("两次密码并不一致"))
		} else {
			callback()
		}
	}
	const newFormRules = reactive({
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
		}],
		userPassword: [{
			validator: validatePass,
			trigger: 'blur'
		}],
		userPasswordAgain: [{
			validator: validatePass2,
			trigger: 'blur'
		}],
	})
	// 弹出新增框
	const switchShowNewDialog = () => {

		isShowNewDialog.value = true;
	}
	// 提交新增用户请求
	const newUser = async (formEl) => {
		if (!formEl) return
		await formEl.validate((valid, fields) => {
			if (valid) {
				console.log('submit!')
				proxy.appContext.config.globalProperties.$api.register(userNewForm).then((res) => {

					if (res.data.code == '200') {
						ElMessage({
							type: 'success',
							message: res.data.description
						});
						fleshUserList();
						isShowNewDialog.value = false;
					}
				})
			} else {
				console.log('error submit!', fields)
			}
		})
	}


	// 删除用户
	const removeUser = (userId) => {
		ElMessageBox.confirm(
			'一旦删除将不可恢复，是否确定删除？',
			'警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'error',
			}
		).then(() => {
			proxy.appContext.config.globalProperties.$api.deleteById(userId).then((res) => {

				if (res.data.code == '200') {
					ElMessage({
						type: 'success',
						message: res.data.description
					});
					fleshUserList();
				}
			})
		})
	}

	//被选中的用户
	const table = ref()
	const haveSelect = ref(true)
	const handleSelectChange = () => {
		if (table.value.getSelectionRows().length > 0) haveSelect.value = false;
		else haveSelect.value = true;
	}

	//批量封禁用户
	const batchBan = () => {
		ElMessageBox.confirm(
			'是否确定将选中用户进行封禁？',
			'警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
			}
		).then(() => {

			let userIds = [];
			table.value.getSelectionRows().forEach((user) => {

				userIds.push(user.userId);
			});

			console.log(userIds);
			proxy.appContext.config.globalProperties.$api.updateUsersStatus(userIds).then((res) => {

				if (res.data.code == '200') {
					ElMessage({
						type: 'success',
						message: res.data.description
					});
					fleshUserList();
				}
			})
		});
	}
</script>

<style scoped>
	.el-pagination {
		margin-top: 20px;
		display: flex;
		justify-content: center;
	}
</style>