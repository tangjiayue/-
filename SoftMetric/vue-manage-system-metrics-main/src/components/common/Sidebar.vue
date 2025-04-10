<template>
    <div class="sidebar">
        <div class="sidebar-header">
            <div class="logo">
                <i class="el-icon-menu"></i>
                Metrics
            </div>
            <div class="header-right">
                <div class="header-user-con">
                    <!-- 用户头像 -->
                    <div class="user-avator">
                        <img src="../../assets/img/img.jpg" />
                    </div>
                    <!-- 用户名下拉菜单 -->
                    <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                    <span class="el-dropdown-link">
                        {{username}}
                        <i class="el-icon-caret-bottom"></i>
                    </span>
                        <el-dropdown-menu slot="dropdown">
                            <a href="https://github.com/lin-xin/vue-manage-system" target="_blank">
                                <el-dropdown-item>项目仓库</el-dropdown-item>
                            </a>
                            <el-dropdown-item divided command="loginout">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </div>
            <div class="divider"></div>

            <div class="logo1">工具栏</div>
            <div class="menu-grid">
                <router-link
                    v-for="(item, index) in items"
                    :key="index"
                    :to="'/' + item.index"
                    class="menu-card"
                    :class="{'is-active': onRoutes === '/' + item.index}"
                >
                    <i :class="item.icon"></i>
                    <span>{{ item.title }}</span>
                </router-link>
            </div>
        </div>

    </div>

</template>

<script>
import bus from '../common/bus';
export default {
    data() {
        return {
            collapse: false,
            items: [
                {
                    icon: 'el-icon-office-building', // 更精致的图标
                    index: 'Class',
                    title: '功能点度量'
                },
                {
                    icon: 'el-icon-notebook-2', // 更精致的图标
                    index: 'UserCase',
                    title: '用例点度量'
                },
                {
                    icon: 'el-icon-tickets', // 更精致的图标
                    index: 'SourceCode',
                    title: '源代码度量'
                },
                {
                    icon: 'el-icon-link', // 更精致的图标
                    index: 'InfoFlow',
                    title: '信息流度量'
                },
                {
                    icon: 'el-icon-cpu', // 更精致的图标
                    index: 'ControlFlow',
                    title: '控制流结构度量'
                },
                // {
                //     icon: 'el-icon-pie-chart',
                //     index: 'DataFlow',
                //     title: '数据流结构度量'
                // },
                {
                    icon: 'el-icon-data-analysis', // 更精致的图标
                    index: 'OOMMetrics',
                    title: '面向对象度量'
                }
            ]
        };
    },
    computed: {
        onRoutes() {
            return this.$route.path.startsWith('/') ? this.$route.path.substring(1) : this.$route.path;
        },
        username() {
            let username = localStorage.getItem('ms_username');
            return username ? username : this.name;
        }
    },
    methods: {
        // 用户名下拉菜单选择事件
        handleCommand(command) {
            if (command == 'loginout') {
                localStorage.removeItem('ms_username');
                this.$router.push('/login');
            }
        }
    },
    created() {
        // 通过 Event Bus 进行组件间通信，来折叠侧边栏
        bus.$on('collapse', msg => {
            this.collapse = msg;
            bus.$emit('collapse-content', msg);
        });
    }

};
</script>

<style scoped>
.sidebar {
    display: block;
    position: absolute;
    left: 10px;
    top: 10px;
    bottom: 0;
    overflow-y: scroll;
}
.sidebar::-webkit-scrollbar {
    width: 0;
}
.sidebar > ul {
    height: 100%;
}
.sidebar {
    display: flex;
    flex-direction: column;
    width: 250px;
    height: 95vh;
    overflow-y: auto;
    border-radius: 20px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    background-color: #f8f9fa;
}

.sidebar-header {
    padding: 20px;
    background-color: #f8f9fa; /* 更改头部背景色 */
    color: #333333;
    font-size: 20px;
    text-align: center;
}
.logo {
    font-weight: bold;
    color: #333333;
    font-size: 25px;
    margin-left: -61px;
}
.logo i {
    margin-right: 8px;
    font-size: 28px;
    color: #1890ff; /* 蓝色图标 */
}
.user-avator img {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    margin-top: 10px;
}
.divider {
    width: 175px;
    height: 1px;
    background-color: #ddd;
    margin: 9px 20px;
}
.menu-grid {
    display: grid;
    grid-template-columns: repeat(2, 90px);
    gap: 15px;
    padding: 15px;
    margin-left: -8px;
}

.menu-card {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 15px;
    border-radius: 8px;
    background-color: #f8f9fa;
    text-align: center;
    transition: all .3s;
    text-decoration: none;
    color: inherit;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.menu-card i {
    font-size: 24px;
    margin-bottom: 10px;
}

.menu-card span {
    font-size: 14px;
    color: #333;
}

.menu-card:hover {
    background-color: #e0e6ed;
}
</style>
