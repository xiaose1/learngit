<template>
	<div class="page-header vertical-center">
		<span class="page-header-title">设备管理</span>
		<div class="flex-grow"></div>
		<el-space>
			<el-button type="success" color="var(--color-primary-green)" @click="switchShowNewDialog">新增</el-button>
			<el-button type="success" :disabled="haveSelect" @click="batchTurn(true)">批量开启</el-button>
			<el-button type="warning" :disabled="haveSelect" @click="batchTurn(false)">批量关闭</el-button>
		</el-space>
	</div>

	<el-dialog v-model="isShowDialog" title="设备信息" width="650">
		<el-row gutter="10">
			<el-col :md="16">
				<el-card style="margin-bottom: 10px;">
					<template #header>
						<strong>监控情况：</strong>
					</template>
					<el-row style="text-align: center;">
						<el-col :span="12">
							<el-progress type="circle" :percentage="monitoringStatus.temperature" stroke-width="15" />
						</el-col>
						<el-col :span="12">
							<el-progress type="circle" :percentage="monitoringStatus.humidity" stroke-width="15"
								color="var(--color-auxiliary-yellow)" />
						</el-col>
					</el-row>
				</el-card>
				<el-card>
					<template #header>
						<strong>信息指导：</strong>
					</template>
					<div class="information-guidance">
						<el-alert v-show="monitoringStatus.humidity < equipmentPlant.humidity - 10" title="湿度过低,需适当浇水"
							type="warning" show-icon />
						<el-alert v-show="monitoringStatus.temperature < equipmentPlant.temperature - 5"
							title="温度过低,需升温" type="warning" show-icon />
						<el-alert v-show="monitoringStatus.temperature > equipmentPlant.temperature + 5"
							title="温度过高,需降温,可适当浇水" type="warning" show-icon />
						<el-alert v-show="monitoringStatus.humidity > equipmentPlant.humidity + 10" title="湿度过高,可适当减少浇水"
							type="warning" show-icon />
						<el-alert v-show="monitoringStatus.temperature <= equipmentPlant.temperature + 5 
						&& monitoringStatus.temperature >= equipmentPlant.temperature - 5
						&& monitoringStatus.humidity >= equipmentPlant.humidity - 10
						&& monitoringStatus.humidity <= equipmentPlant.humidity + 10" title="目前状态良好" type="success" show-icon />
					</div>
				</el-card>
			</el-col>
			<el-col :md="8">
				<el-card>
					<template #header>
						<strong>{{equipmentPlant.plantName}}信息：</strong>
					</template>
					<el-image :src="equipmentPlant.plantImageUrl" style="width: 100%; height: 120px"
						fit="cover"></el-image>
					<el-text line-clamp="5">
						{{equipmentPlant.plantInformation}}
					</el-text>
					<el-divider></el-divider>
					<div style="margin-bottom: 7px;">
						适宜温度：<el-tag style="width: 70px;" effect="dark">{{equipmentPlant.temperature}}°C</el-tag>
					</div>
					<div>
						适宜湿度：<el-tag style="width: 70px;" type="warning" effect="dark"
							color="var(--color-auxiliary-yellow)">{{equipmentPlant.humidity}}%</el-tag>
					</div>
				</el-card>
			</el-col>
		</el-row>
	</el-dialog>

	<!-- 新增 -->
	<el-dialog v-model="isShowNewDialog" title="新增监听设备" width="500">
		<el-form :model="equipmentNewForm" label-width="auto" style="padding: 20px;">
			<el-form-item label="设备名">
				<el-input v-model="equipmentNewForm.equipmentName" type="text" placeholder="请输入设备名" />
			</el-form-item>
			<el-form-item label="绑定植物" required>
				<!-- <el-input v-model="equipmentNewForm.equipmentPlantName" type="text" placeholder="请选择植物" /> -->
				<el-select v-model="equipmentPlantIndex" filterable remote reserve-keyword placeholder="请选择植物"
					:remote-method="remoteMethod" :loading="loading">
					<el-option v-for="(item, index) in options" :key="index" :label="item.plantName" :value="index" />
				</el-select>
			</el-form-item>
			<el-form-item label="设备IP">
				<el-input v-model="equipmentNewForm.equipmentIp" type="text" placeholder="请输入设备IP地址" />
			</el-form-item>
			<el-form-item label="设备状态">
				<el-switch v-model="equipmentNewForm.equipmentStatus" class="ml-2"
					style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" :active-value="0"
					:inactive-value="1" inline-prompt active-text="开" inactive-text="关" />
			</el-form-item>
			<el-form-item>
				<el-button style="width: 100%;" type="success" color="var(--color-primary-green)"
					@click="saveNew">新增</el-button>
			</el-form-item>
		</el-form>
	</el-dialog>

	<!-- 编辑 -->
	<el-dialog v-model="isShowEditDialog" title="设备编辑" width="500">
		<el-form :model="equipmentEditForm" label-width="auto" style="padding: 20px;">
			<el-form-item label="设备名">
				<el-input v-model="equipmentEditForm.equipmentName" type="text" placeholder="请输入设备名" />
			</el-form-item>
			<el-form-item label="绑定植物" required>
				<!-- <el-input v-model="equipmentEditForm.equipmentPlantName" type="text" placeholder="请选择植物" /> -->
				<el-select v-model="equipmentEditPlantIndex" filterable remote reserve-keyword placeholder="请选择植物"
					:remote-method="remoteMethod" :loading="loading">
					<el-option v-for="(item, index) in options" :key="index" :label="item.plantName" :value="index" />
				</el-select>
			</el-form-item>
			<el-form-item label="设备IP">
				<el-input v-model="equipmentEditForm.equipmentIp" type="text" placeholder="请输入设备IP地址" />
			</el-form-item>
			<el-form-item label="设备状态">
				<el-switch v-model="equipmentEditForm.equipmentStatus" class="ml-2"
					style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" :active-value="0"
					:inactive-value="1" inline-prompt active-text="开" inactive-text="关" />
			</el-form-item>
			<el-form-item>
				<el-button style="width: 100%;" type="success" color="var(--color-primary-green)"
					@click="saveEdit">保存更改</el-button>
			</el-form-item>
		</el-form>
	</el-dialog>

	<el-table ref="table" @selection-change="handleSelectChange" :data="equipmentList"
		style="width: 100%; margin-top: 15px; height: 70vh;">
		<el-table-column fixed type="selection" width="55" />
		<el-table-column fixed prop="equipmentId" label="设备ID" width="80" />
		<el-table-column fixed prop="equipmentName" label="设备名" width="100" />
		<el-table-column prop="equipmentIp" label="设备IP" width="120" />
		<el-table-column label="设备状态" width="80">
			<template #default="scope">
				{{statusOptions[scope.row.equipmentStatus].label}}
			</template>
		</el-table-column>
		<el-table-column prop="equipmentPlantName" label="绑定植物" width="100" />
		<el-table-column prop="equipmentCreateTime" label="设备创建时间" width="200" />
		<el-table-column prop="equipmentUpdateTime" label="设备更新时间" width="200" />
		<el-table-column fixed="right" label="操作" min-width="200">
			<template #header>
				<el-input placeholder="搜索" size="small" v-model="searchStr">
					<template #append>
						<el-button size="small" :icon="Search" @click="toSearch" />
					</template>
				</el-input>
			</template>
			<template #default="scope">
				<el-button size="small" @click="switchShowDialog(scope.row.equipmentPlantId)">
					查看
				</el-button>
				<el-button type="primary" size="small" @click="switchShowEditDialog(scope.row)">
					编辑
				</el-button>
				<el-button type="danger" size="small" @click="removeEquipment(scope.row.equipmentId)">
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
	const baseUrl = proxy.appContext.config.globalProperties.$api.ServerURL

	const statusOptions = ref([{
		label: "开启",
		value: 0
	}, {
		label: "关闭",
		value: 1
	}])
	//设备所监听到的状态
	const monitoringStatus = ref({
		temperature: 25.00,
		humidity: 80.00
	})
	//搜索植物
	const options = ref([])
	const value = ref([])
	const loading = ref(false)
	const remoteMethod = (query) => {
		if (query) {
			loading.value = true
			proxy.appContext.config.globalProperties.$api.getPlantOptions(query).then((
				res) => {

				if (res.data.code == '200') {
					options.value.splice(0, options.value.length);
					res.data.data.forEach((plantOption) => {

						options.value.push(plantOption);
					});
				}
				loading.value = false
			})
		} else {
			options.value = []
		}
	}
	//设备列表
	const equipmentList = ref([])
	//搜索框
	const searchStr = ref('')
	//分页
	const currentPage = ref(1)
	const pageSize = ref(20)
	const total = ref(20)
	const pageCount = ref(20)
	//获取数据列表
	const getData = (searchStr) => {
		proxy.appContext.config.globalProperties.$api.getEquipment(currentPage.value, pageSize.value, searchStr).then((
			res) => {
			console.log(res.data.data.records);
			res.data.data.records.forEach((equipment) => {

				equipmentList.value.push(equipment);
			});
			total.value = res.data.data.total;
			pageCount.value = res.data.data.pages;
		});
	}
	getData("");
	//刷新列表数据
	const fleshList = () => {
		equipmentList.value.splice(0, equipmentList.value.length);
		getData(searchStr.value);
	}
	//操作分页
	const handleCurrentChange = (val) => {
		currentPage.value = val;
		fleshList();
	}
	//搜索
	const toSearch = () => {

		fleshList();
	}

	//新增对话框
	const isShowNewDialog = ref(false)
	const equipmentNewForm = reactive({
		equipmentName: "",
		equipmentStatus: 1,
		equipmentIp: "127.0.0.1",
		equipmentPlantName: "青草",
		equipmentPlantId: 1,
	})
	const equipmentPlantIndex = ref()
	const switchShowNewDialog = () => {

		isShowNewDialog.value = true;
	}
	const saveNew = () => {
		console.log(options.value)
		let plant = options.value[equipmentPlantIndex.value]
		equipmentNewForm.equipmentPlantId = plant.plantId
		equipmentNewForm.equipmentPlantName = plant.plantName

		proxy.appContext.config.globalProperties.$api.saveEquipment(equipmentNewForm).then((
			res) => {

			if (res.data.code == '200') {
				ElMessage({
					type: 'success',
					message: res.data.description
				});
				fleshList();
				isShowNewDialog.value = false;
			}
		});
	}

	//查看对话框
	const isShowDialog = ref(false)
	const equipmentPlant = reactive({
		plantName: "",
		plantImageUrl: "",
		plantInformation: "",
		temperature: 28.00,
		humidity: 88.00
	})
	const switchShowDialog = (equipmentPlantId) => {
		console.log(equipmentPlantId);
		proxy.appContext.config.globalProperties.$api.getPlantById(equipmentPlantId).then((
			res) => {

			if (res.data.code == '200') {
				let plant = res.data.data;
				equipmentPlant.plantName = plant.plantName;
				equipmentPlant.plantImageUrl = baseUrl + plant.plantImageUri;
				equipmentPlant.plantInformation = plant.plantInformation;
				equipmentPlant.temperature = plant.plantTemperature;
				equipmentPlant.humidity = plant.plantHumidity;
			}
		})
		isShowDialog.value = true;
	}

	//编辑对话框
	const isShowEditDialog = ref(false)
	const equipmentEditForm = reactive({
		equipmentId: 0,
		equipmentName: "",
		equipmentStatus: 0,
		equipmentIp: "127.0.0.1",
		equipmentPlantName: "青草",
		equipmentPlantId: 1,
	})
	const equipmentEditPlantIndex = ref()
	const switchShowEditDialog = (equipment) => {
		equipmentEditForm.equipmentId = equipment.equipmentId;
		equipmentEditForm.equipmentName = equipment.equipmentName;
		equipmentEditForm.equipmentStatus = equipment.equipmentStatus;
		equipmentEditForm.equipmentIp = equipment.equipmentIp;
		equipmentEditForm.equipmentPlantName = equipment.equipmentPlantName;
		equipmentEditForm.equipmentPlantId = equipment.equipmentPlantId;
		equipmentEditPlantIndex.value = null;

		isShowEditDialog.value = true;
	}
	const saveEdit = () => {

		let plant = options.value[equipmentEditPlantIndex.value]
		equipmentEditForm.equipmentPlantId = plant.plantId
		equipmentEditForm.equipmentPlantName = plant.plantName
		proxy.appContext.config.globalProperties.$api.updateEquipment(equipmentEditForm).then((
			res) => {

			if (res.data.code == '200') {
				ElMessage({
					type: 'success',
					message: res.data.description
				});
				fleshList();
				isShowEditDialog.value = false;
			}
		});
	}

	// 删除设备
	const removeEquipment = (equipmentId) => {
		ElMessageBox.confirm(
			'一旦删除将不可恢复，是否确定删除？',
			'警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'error',
			}
		).then(() => {
			proxy.appContext.config.globalProperties.$api.deleteEquipment(equipmentId).then((res) => {

				if (res.data.code == '200') {
					ElMessage({
						type: 'success',
						message: res.data.description
					});
					fleshList();
				}
			})
		})
	}

	//被选中的设备
	const table = ref()
	const haveSelect = ref(true)
	const handleSelectChange = () => {
		if (table.value.getSelectionRows().length > 0) haveSelect.value = false;
		else haveSelect.value = true;
	}

	const batchTurn = (isTurnOn) => {
		let message = isTurnOn ? "是否确定将选中设备进行开启？" : "是否确定将选中设备进行关闭？";
		ElMessageBox.confirm(
			message,
			'提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
			}
		).then(() => {

			let equipmentIds = [];
			table.value.getSelectionRows().forEach((equipment) => {

				equipmentIds.push(equipment.equipmentId);
			});

			console.log(equipmentIds);
			proxy.appContext.config.globalProperties.$api.updateEquipmentStatus(equipmentIds, isTurnOn).then((
				res) => {

				if (res.data.code == '200') {
					ElMessage({
						type: 'success',
						message: res.data.description
					});
					fleshList();
				}
			})
		});
	}
</script>

<style scoped>
	.information-guidance .el-alert {
		margin-bottom: 5px;
	}

	.el-pagination {
		margin-top: 20px;
		display: flex;
		justify-content: center;
	}
</style>