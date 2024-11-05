/*全局配置：针对axios对后端请求进行统一封装，方便后续维护管理*/
import axios from "axios"
import router from '@/router'
import {
	ElMessage
} from 'element-plus'
import {
	reactive
} from 'vue'
const ServerURL = 'http://local.java.lin037.cn'
const http = axios.create({
	// 配置baseURL
	baseURL: ServerURL
	// 配置响应成功时状态码
	// validateStatus:function(status) {
	//     return true
	//  }
})

// 请求拦截器，在每个请求时都会执行该方法，并把请求配置中的对象传入到该方法中
// 扩展说明：在后续的非登录请求时，都需要带上鉴权
http.interceptors.request.use((config) => {
	//对于所有非/login请求中添加jwt_token请求头
	// store = BaseStore();
	// if (config.url != 'users/login') {
	// 	config.headers['jwt_token'] = store.jwt_token
	// }
	config.withCredentials = true
	// console.log(sessionStorage.getItem('userLoginStatus'));
	// config.headers['Authorization'] = sessionStorage.getItem('userLoginStatus'); // 在请求头中发送Session数据

	return config
});

// 响应拦截器
http.interceptors.response.use((response) => {

	// 2xx 范围内的状态码都会触发该函数。
	console.log('响应数据：', response);

	let tip = response.data.description === null ? response.data.message : response.data.description
	//如果为报错信息，则统一发出提示
	if (response.data.code >= 40000) {

		console.log(response.data);
		ElMessage({
			type: 'error',
			message: tip
		});

		//如果用户未登录访问该api，则跳转到登录界面
		if (response.data.code == '40100') router.push("/login");
	}
	return response;
})


export default {
	ServerURL,
	//用户users接口
	//登录接口
	login(users) {
		return http.post('/users/login', users);
	},
	//注销接口
	logout() {
		return http.delete('/users/logout');
	},
	//注册接口
	register(users) {
		return http.post('/users/register', users);
	},
	//用户查看个人信息接口
	getSelfMsg() {
		return http.get('/users/selfMsg');
	},
	//用户修改个人信息接口
	updateSelf(users) {
		return http.put('/users/updateSelf', users);
	},
	//修改用户信息接口
	updateUsers(users) {
		return http.put('/users/', users);
	},
	//删除用户接口
	deleteById(usersId) {
		// let uri = '/users/?';
		// usersIds.foreach((usersId) => {
		// 	uri += 'userId=' + usersId + '&';
		// });
		// console.log(uri);
		return http.delete('/users/' + usersId);
	},
	//批量封禁用户接口
	updateUsersStatus(userIds) {
		return http.put('/users/ban', userIds);
	},
	//分页查询用户接口
	getUsers(pageNum, usersNum, searchStr) {
		if (searchStr === '') return http.get('/users/' + pageNum + '/' + usersNum);
		else return http.get('/users/' + pageNum + '/' + usersNum + '/' + searchStr);
	},
	getUserRole() {
		return http.get('/users/getAuthority')
	},

	//设备equipment接口
	saveEquipment(equipment) {
		return http.post('/equipment/', equipment);
	},
	deleteEquipment(equipmentId) {
		return http.delete('/equipment/' + equipmentId);
	},
	updateEquipment(equipment) {
		return http.put('/equipment/', equipment);
	},
	updateEquipmentStatus(equipmentIds, isOpen) {
		let uri = '/equipment/' + isOpen;
		return http.put(uri, equipmentIds);
	},
	getEquipmentCount() {
		return http.get('/equipment/count');
	},
	getEquipment(pageNum, equipmentNum, searchStr) {
		if (searchStr === '') return http.get('/equipment/' + pageNum + '/' + equipmentNum);
		else return http.get('/equipment/' + pageNum + '/' + equipmentNum + '/' + searchStr);
	},

	//植物plant接口
	savePlant(plant) {
		return http.post('/plants/', plant);
	},
	deletePlant(plantId) {
		let uri = '/plants/?';
		uri += 'plantId=' + plantId;
		return http.delete(uri);
	},
	deletePlants(plantIds) {
		let uri = '/plants/?';
		plantIds.forEach((plantId) => {
			uri += 'plantId=' + plantId + '&';
		});
		// console.log(uri);
		return http.delete(uri);
	},
	updatePlant(plant) {
		return http.put('/plants/', plant);
	},
	getPlantCount() {
		return http.get('/plants/count');
	},
	getPlantOptions(searchStr) {
		return http.get('/plants/search/' + searchStr);
	},
	getPlants(pageNum, usersNum, searchStr) {
		if (searchStr === '') return http.get('/plants/' + pageNum + '/' + usersNum);
		else return http.get('/plants/' + pageNum + '/' + usersNum + '/' + searchStr);
	},

	uploadPicture(file) {
		return http.post('/public/uploadPicture', file);
	},
	getPlantById(plantId) {
		return http.get('/plants/get/' + plantId);
	}
}