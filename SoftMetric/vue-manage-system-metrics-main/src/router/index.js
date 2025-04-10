import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    mode: 'history',
    routes: [
        {
            path: '/',
            redirect: '/Class'
        },
        {
            path: '/',
            component: () => import(/* webpackChunkName: "home" */ '../components/common/Home.vue'),
            meta: { title: '自述文件' },
            children: [
                {
                    path: '/dashboard',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
                    meta: { title: '系统首页' }
                },
                {
                    path: '/Class',
                    component: () => import(/* webpackChunkName: "Class" */ '../components/page/metrics/Class.vue'),
                    meta: { title: '功能点度量' }
                },
                {
                    path: '/UserCase',
                    component: () => import(/* webpackChunkName: "UserCase" */ '../components/page/metrics/UserCase.vue'),
                    meta: { title: '用例点度量' }
                },
                {
                    path: '/SourceCode',
                    component: () => import(/* webpackChunkName: "SourceCode" */ '../components/page/metrics/SourceCode.vue'),
                    meta: { title: '源代码度量' }
                },
                {
                    path: '/InfoFlow',
                    component: () => import(/* webpackChunkName: "SourceCode" */ '../components/page/metrics/InfoFlow.vue'),
                    meta: { title: '信息流度量' }
                },
                {
                    path: '/ControlFlow',
                    component: () => import(/* webpackChunkName: "ControlFlow" */ '../components/page/metrics/ControlFlow.vue'),
                    meta: { title: '控制流结构度量' }
                },
                {
                    path: '/DataFlow',
                    component: () => import(/* webpackChunkName: "DataFlow" */ '../components/page/metrics/DataFlow.vue'),
                    meta: { title: '数据流结构度量' }
                },
                {
                    path: '/OOMMetrics',
                    component: () => import(/* webpackChunkName: "OOMMetrics" */ '../components/page/metrics/OOMMetrics.vue'),
                    meta: { title: '面向对象度量' }
                }
            ]
        },
        {
            path: '/login',
            component: () => import(/* webpackChunkName: "login" */ '../components/page/Login.vue'),
            meta: { title: '登录' }
        },
        {
            path: '*',
            redirect: '/404'
        }
    ]
});
