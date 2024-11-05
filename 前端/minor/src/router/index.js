import {
	createRouter,
	createWebHistory
} from 'vue-router'

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: "/",
			component: () => import("../views/index.vue"),
			children: [
				{
					path: "/userManagement",
					component: ()=> import("../views/userManagement.vue")
				},
				{
					path: "/plantManagement",
					component: ()=> import("../views/plantManagement.vue")
				},
				{
					path: "/equipmentManagement",
					component: ()=> import("../views/equipmentManagement.vue")
				},
				{
					path: "/",
					component: ()=> import("../views/personalInformation.vue")
				}
			]
		},
		{
			path: "/login",
			component: () => import("../views/login.vue")
		}
	]
})
export default router