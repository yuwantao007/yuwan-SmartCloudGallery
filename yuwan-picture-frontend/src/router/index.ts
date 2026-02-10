import { createRouter, createWebHistory } from 'vue-router'
import UserLoginPage from '@/page/user/UserLoginPage.vue'
import UserManagePage from '@/page/admin/UserManagePage.vue'
import UserRegisterPage from '@/page/user/UserRegisterPage.vue'
import ACCESS_ENUM from '@/access/accessEnum.ts'
import NoAuth from '@/components/NoAuth.vue'
import HomePage from '@/page/HomePage.vue'
import AddPicturePage from '@/page/AddPicturePage.vue'
import PictureManagePage from '@/page/admin/PictureManagePage.vue'
import PictureDetailPage from '@/page/PictureDetailPage.vue'
import AddPictureBatchPage from '@/page/AddPictureBatchPage.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomePage,
      meta: {
        hideInMenu: false,
      },
    },
    {
      path: '/user/login',
      name: 'user login',
      component: UserLoginPage,
      meta: {
        hideInMenu: true,
      },
    },
    {
      path: '/add_picture',
      name: '创建图片',
      component: AddPicturePage,
      meta: {
        access: ACCESS_ENUM.ADMIN,
      },
    },

    {
      path: '/user/register',
      name: 'user register',
      component: UserRegisterPage,
      meta: {
        hideInMenu: true,
      },
    },
    {
      path: '/admin/userManage',
      name: 'user manage',
      component: UserManagePage,
      meta: {
        access: ACCESS_ENUM.ADMIN,
      },
    },
    {
      path: '/admin/pictureManage',
      name: '图片管理',
      component: PictureManagePage,
      meta: {
        access: ACCESS_ENUM.ADMIN,
      },
    },
    {
      path: '/picture/:id',
      name: '图片详情',
      component: PictureDetailPage,
      props: true,
    },
    {
      path: '/add_picture/batch',
      name: '批量创建图片',
      component: AddPictureBatchPage,
      meta: {
        access: ACCESS_ENUM.ADMIN,
      },
    },
    {
      path: '/noAuth',
      name: 'noAuth',
      component: NoAuth,
      meta: {
        hideInMenu: false,
      },
    },
  ],
})

export default router
