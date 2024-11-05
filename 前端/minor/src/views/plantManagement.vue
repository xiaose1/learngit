<template>
	<div class="page-header vertical-center">
		<span class="page-header-title">植物管理</span>
		<div class="flex-grow"></div>
		<el-space>
			<el-button type="success" color="var(--color-primary-green)" @click="switchShowNewDialog">新增</el-button>
			<el-button type="danger" :disabled="haveSelect" @click="batchDelete">批量删除</el-button>
		</el-space>
	</div>

	<el-dialog v-model="isShowNewDialog" title="新增植物" width="650">
		<el-row>
			<el-col :span="18">
				<el-form ref="ruleNewFormRef" :rules="newFormRules" :model="plantNewForm" label-width="auto"
					style="padding: 20px;">
					<el-form-item prop="plantName" label="植物名">
						<el-input v-model="plantNewForm.plantName" type="text" placeholder="请输入植物名" />
					</el-form-item>
					<el-form-item prop="plantInformation" label="植物简介">
						<el-input v-model="plantNewForm.plantInformation" type="textarea" :rows="3"
							placeholder="请输入植物简介" />
					</el-form-item>
					<el-row :gutter="10">
						<el-col :span="12">
							<el-form-item label="适宜温度">
								<el-input-number v-model="plantNewForm.plantTemperature" :precision="2" :step="1"
									:max="100" :min="0" />
							</el-form-item>
						</el-col>
						<el-col :span="12" style="text-align: right;">
							<el-form-item label="适宜湿度">
								<el-input-number v-model="plantNewForm.plantHumidity" :precision="2" :step="1"
									:max="100" :min="0" />
							</el-form-item>
						</el-col>
					</el-row>
				</el-form>
			</el-col>
			<el-col :span="6" style="padding-top: 20px; text-align: center;">
				<el-text>植物图片上传</el-text>
				<el-upload class="avatar-uploader" :action="uploadUrl"
					:with-credentials="true" :show-file-list="false" :on-success="handleAvatarSuccess"
					:before-upload="beforeAvatarUpload">
					<el-image v-if="imageUrl" :src="imageUrl" style="width: 100%; height: 120px" fit="cover" />
					<el-icon v-else class="avatar-uploader-icon">
						<Plus />
					</el-icon>
				</el-upload>
			</el-col>
		</el-row>

		<el-button style="width: 100%; margin-bottom: 20px;" type="success" color="var(--color-primary-green)"
			@click="saveNew(ruleNewFormRef)">新增</el-button>
	</el-dialog>

	<el-dialog v-model="isShowEditDialog" title="植物信息编辑" width="500">
		<el-form ref="ruleEditorFormRef" :rules="newFormRules" :model="plantEditForm" label-width="auto"
			style="padding: 20px;">
			<el-form-item prop="plantName" label="植物名">
				<el-input v-model="plantEditForm.plantName" type="text" placeholder="请输入植物名" />
			</el-form-item>
			<el-form-item prop="plantInformation" label="植物简介">
				<el-input v-model="plantEditForm.plantInformation" type="textarea" :rows="3" placeholder="请输入植物简介" />
			</el-form-item>
			<el-row :gutter="10">
				<el-col :span="12">
					<el-form-item label="适宜温度">
						<el-input-number v-model="plantEditForm.plantTemperature" :precision="2" :step="1" :max="100"
							:min="0" />
					</el-form-item>
				</el-col>
				<el-col :span="12" style="text-align: right;">
					<el-form-item label="适宜湿度">
						<el-input-number v-model="plantEditForm.plantHumidity" :precision="2" :step="1" :max="100"
							:min="0" />
					</el-form-item>
				</el-col>
			</el-row>
			<el-form-item>
				<el-button style="width: 100%;" type="success" color="var(--color-primary-green)"
					@click="saveEdit(ruleEditorFormRef)">保存更改</el-button>
			</el-form-item>
		</el-form>
	</el-dialog>

	<el-table ref="table" @selection-change="handleSelectChange" :data="plantList"
		style="width: 100%; margin-top: 15px; height: 70vh;">
		<el-table-column type="selection" width="55" />
		<el-table-column fixed prop="plantId" label="植物ID" width="80" />
		<el-table-column fixed prop="plantName" label="植物名" width="100" />
		<el-table-column prop="plantImage" label="植物图片" width="100">
			<template #default="scope">
				<el-avatar :src="baseUrl + scope.row.plantImageUri"></el-avatar>
			</template>
		</el-table-column>
		<el-table-column prop="plantInformation" label="植物信息" width="150">
			<template #default="scope">
				<el-text line-clamp="2">{{ scope.row.plantInformation }}</el-text>
			</template>
		</el-table-column>
		<el-table-column prop="plantTemperature" label="植物温度" width="80">
			<template #default="scope">
				{{scope.row.plantTemperature}}°C
			</template>
		</el-table-column>
		<el-table-column prop="plantHumidity" label="植物湿度" width="80">
			<template #default="scope">
				{{scope.row.plantHumidity}}%
			</template>
		</el-table-column>
		<el-table-column prop="plantCreateTime" label="植物创建时间" width="200" />
		<el-table-column prop="plantUpdateTime" label="植物更新时间" width="200" />
		<el-table-column fixed="right" label="操作" min-width="150">
			<template #header>
				<el-input placeholder="搜索" size="small" v-model="searchStr">
					<template #append>
						<el-button size="small" :icon="Search" @click="toSearch" />
					</template>
				</el-input>
			</template>
			<template #default="scope">
				<el-button type="primary" size="small" @click="switchShowEditDialog(scope.row)">
					编辑
				</el-button>
				<el-button type="danger" size="small" @click="deletePlant(scope.row.plantId)">
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
		Search,
		Plus,
		Upload
	} from '@element-plus/icons-vue'
	import {
		genFileId,
		ElMessage,
		ElMessageBox
	} from 'element-plus'
	//全局变量
	const proxy = getCurrentInstance()
	const baseUrl = proxy.appContext.config.globalProperties.$api.ServerURL
	// const baseUrl = 'http://local.java.lin037.cn'
	const uploadUrl = baseUrl + '/public/uploadPicture'

	//植物列表
	const plantList = ref([])
	//搜索框
	const searchStr = ref('')
	//分页
	const currentPage = ref(1)
	const pageSize = ref(20)
	const total = ref(20)
	const pageCount = ref(20)
	const getData = (searchStr) => {
		proxy.appContext.config.globalProperties.$api.getPlants(currentPage.value, pageSize.value, searchStr).then((
			res) => {
			console.log(res.data.data.records);
			res.data.data.records.forEach((plant) => {

				plantList.value.push(plant);
			});
			total.value = res.data.data.total;
			pageCount.value = res.data.data.pages;
		});
	}
	getData("");
	//刷新列表数据
	const fleshPlantList = () => {
		plantList.value.splice(0, plantList.value.length);
		getData(searchStr.value);
	}
	//操作分页
	const handleCurrentChange = (val) => {
		currentPage.value = val;
		fleshPlantList();
	}
	//搜索
	const toSearch = () => {

		fleshPlantList();
	}

	//新增对话框
	const isShowNewDialog = ref(false)
	const plantNewForm = reactive({
		plantName: "",
		plantImageUri: "",
		plantInformation: "",
		plantTemperature: 0,
		plantHumidity: 0,
	})
	const switchShowNewDialog = (plant) => {

		isShowNewDialog.value = true;
	}
	//图片上传
	const imageUrl = ref('')
	const handleAvatarSuccess = (response, uploadFile) => {
		plantNewForm.plantImageUri = response.data;
		imageUrl.value = URL.createObjectURL(uploadFile.raw);
	}
	const beforeAvatarUpload = (rawFile) => {
		if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
			ElMessage.error('图片必须为JPG或PNG格式!')
			return false
		} else if (rawFile.size / 1024 / 1024 > 2) {
			ElMessage.error('图片大小不能超过2MB!')
			return false
		}
		return true
	}
	const newFormRules = reactive({
		plantName: [{
			required: true,
			message: '请输入植物名',
			trigger: 'blur'
		}],
		plantInformation: [{
			required: true,
			message: '请输入植物简介',
			trigger: 'blur'
		}]
	})
	const ruleNewFormRef = ref()
	const saveNew = async (formEl) => {
		if (!formEl) return
		await formEl.validate((valid, fields) => {
			if (valid) {
				console.log('submit!')
				if (plantNewForm.plantImageUri === '') ElMessage.error("请上传图片！");
				else {
					proxy.appContext.config.globalProperties.$api.savePlant(plantNewForm).then((res) => {

						if (res.data.code == '200') {
							ElMessage({
								type: 'success',
								message: res.data.description
							});
							fleshPlantList();
							isShowNewDialog.value = false;
						}
					})
				}
			} else {
				console.log('error submit!', fields)
			}
		})
	}

	//编辑对话框
	const isShowEditDialog = ref(false)
	const plantEditForm = reactive({
		plantId: 0,
		plantName: "",
		plantImageUri: "",
		plantInformation: "",
		plantTemperature: 0,
		plantHumidity: 0,
	})
	const switchShowEditDialog = (plant) => {
		plantEditForm.plantId = plant.plantId;
		plantEditForm.plantName = plant.plantName;
		plantEditForm.plantImageUri = plant.plantImageUri;
		plantEditForm.plantInformation = plant.plantInformation;
		plantEditForm.plantTemperature = plant.plantTemperature;
		plantEditForm.plantHumidity = plant.plantHumidity;

		isShowEditDialog.value = true;
	}
	const ruleEditorFormRef = ref()
	const saveEdit = async (formEl) => {
		if (!formEl) return
		await formEl.validate((valid, fields) => {
			if (valid) {
				proxy.appContext.config.globalProperties.$api.updatePlant(plantEditForm).then((
					res) => {

					if (res.data.code == '200') {
						ElMessage({
							type: 'success',
							message: res.data.description
						});
						fleshPlantList();
						isShowEditDialog.value = false;
					}
				})
			} else {
				console.log('error submit!', fields)
			}
		})
	}
	
	// 删除植物 
	const deletePlant = (plantId) => {
		ElMessageBox.confirm(
			'一旦删除将不可恢复，是否确定删除？',
			'警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'error',
			}
		).then(() => {
			proxy.appContext.config.globalProperties.$api.deletePlant(plantId).then((res) => {
	
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

	//被选中的植物
	const table = ref()
	const haveSelect = ref(true)
	const handleSelectChange = () => {
		if (table.value.getSelectionRows().length > 0) haveSelect.value = false;
		else haveSelect.value = true;
	}
	const batchDelete = () => {
		console.log(table.value.getSelectionRows());
		ElMessageBox.confirm(
			'是否确定将选中植物删除？',
			'警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'error',
			}
		).then(() => {
		
			let plantIds = [];
			table.value.getSelectionRows().forEach((plant) => {
		
				plantIds.push(plant.plantId);
			});
		
			console.log(plantIds);
			proxy.appContext.config.globalProperties.$api.deletePlants(plantIds).then((res) => {
		
				if (res.data.code == '200') {
					ElMessage({
						type: 'success',
						message: res.data.description
					});
					fleshPlantList();
				}
			})
		});
	}
</script>

<style scoped>
	.avatar-uploader {
		border: 2px dashed var(--el-border-color);
		border-radius: 6px;
		margin: 5px 0;
	}

	:deep(.avatar-uploader .el-upload) {
		width: 100%;
	}

	.el-icon.avatar-uploader-icon {
		font-size: 28px;
		color: #8c939d;
		width: 100%;
		height: 120px;
		text-align: center;
	}

	.el-pagination {
		margin-top: 20px;
		display: flex;
		justify-content: center;
	}
</style>