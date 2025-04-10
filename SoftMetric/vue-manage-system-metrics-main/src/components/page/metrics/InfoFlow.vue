<template>
    <el-container style='display: block'>
        <div class="custom-card text-center custom-font">
            <h3>信息流度量页面</h3>
            <p class="paragraph-style">在本页面中上传源代码，我们将根据信息流度量的方法得出度量结果</p>
            <el-card class="box-card" style='width: 100%;display: flex'>
                <el-upload
                    class="upload-demo"
                    :auto-upload="false"
                    action="https://jsonplaceholder.typicode.com/posts/"
                    :limit="1"
                    :file-list="fileList"
                    :on-change="onChange"
                    :on-exceed="onExceed">
                    <div style='display: flex'>
                        <i class="el-icon-document-checked" style='font-size: 20px;margin-top: 10px;margin-left: 20px;margin-right: 10px'></i>
                        <div>点击选择要上传的文件（java 文件）</div>
                    </div>
                </el-upload>
                <el-button style='margin-top: 10px' class="submit" type="primary" @click="uploadFile">上传文件</el-button>
            </el-card>
        </div>

<!--        <el-card class='box-card'>-->
<!--            <el-empty v-if="!tableData.length" description="请先上传文件"></el-empty>-->
<!--            <el-table v-if="tableData.length" :data="tableData" style="width: 100%">-->
<!--                <el-table-column prop="methodName" label="方法名" width="180"></el-table-column>-->
<!--                <el-table-column prop="flowIn" label="扇入"></el-table-column>-->
<!--                <el-table-column prop="flowOut" label="扇出"></el-table-column>-->
<!--                <el-table-column prop="length" label="方法长度"></el-table-column>-->
<!--                <el-table-column prop="complexity" label="复杂度"></el-table-column>-->
<!--                <el-table-column prop="shepperdComplexity" label="Shepperd复杂度"></el-table-column>-->
<!--            </el-table>-->

<!--        </el-card>-->
        <el-card class="box-card">
            <el-empty v-if="!tableData.length" description="请先上传文件"></el-empty>
            <el-table v-if="tableData.length" :data="tableData" style="width: 100%" stripe border>
                <el-table-column prop="methodName" label="方法名" width="180" header-align="center" align="center">
                </el-table-column>
                <el-table-column prop="flowIn" label="扇入" header-align="center" align="center"></el-table-column>
                <el-table-column prop="flowOut" label="扇出" header-align="center" align="center"></el-table-column>
                <el-table-column prop="length" label="方法长度" header-align="center" align="center"></el-table-column>
                <el-table-column prop="complexity" label="复杂度" header-align="center" align="center"></el-table-column>
                <el-table-column prop="shepperdComplexity" label="Shepperd复杂度" header-align="center" align="center"></el-table-column>
            </el-table>
        </el-card>
        <el-card class="box-card" v-if='tableData.length' style='width: 100%'>
            <div style="height:500px;width: 100%;" ref="main"></div>
        </el-card>


    </el-container>
</template>

<script>
import * as echarts from 'echarts';
export default {
    name:'InfoFlow',
    data() {
        return {
            fileList: [],
            tableData: [],
            activeName: 'first',
            textarea: ''
        }
    },
    methods: {
        drawLine() {
            const myChart = echarts.init(this.$refs.main);
            let legendData = this.tableData.map(item => item.methodName);
            let seriesData = this.tableData.map(item => ({
                value: [item.flowIn, item.flowOut, item.length, item.complexity, item.shepperdComplexity, item.startLine, item.endLine],
                name: item.methodName
            }));

            var option = {
                color: ['#5470C6', '#91CC75', '#FAC858', '#EE6666', '#73C0DE', '#3BA272', '#FC8452', '#9A60B4', '#EA7CCC'], // 自定义颜色
                title: {
                    text: '信息流度量',
                    left: 'center', // 标题居中显示
                    textStyle: {
                        fontSize: 18,
                        fontWeight: 'bold'
                    }
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    data: legendData,
                    orient: 'vertical', // 图例垂直排列
                    left: '20%', // 图例位置
                    top: 'top',
                    itemWidth: 16, // 图例标记的图形宽度
                    itemHeight: 16, // 图例标记的图形高度
                    textStyle: {
                        fontSize: 14
                    }
                },
                radar: {
                    // shape: 'circle',
                    indicator: [
                        { name: 'flowIn', max: 10 },
                        { name: 'flowOut', max: 10 },
                        { name: 'length', max: 10 },
                        { name: 'complexity', max: 150 },
                        { name: 'shepperdComplexity', max: 100 },
                        { name: 'startLine', max: 40 },
                        { name: 'endLine', max: 40 },
                    ],
                    splitNumber: 5, // 雷达图圈数
                    axisName: {
                        formatter: '{value}',
                        color: '#444' // 指示器标签颜色
                    },
                    splitLine: {
                        lineStyle: {
                            color: ['#777'], // 网格线颜色
                            opacity: 0.5 // 网格线透明度
                        }
                    },
                    splitArea: {
                        areaStyle: {
                            color: ['rgba(250, 250, 250, 0.3)', 'rgba(200, 200, 200, 0.3)'], // 区域填充颜色
                            shadowColor: 'rgba(0, 0, 0, 0.1)', // 区域阴影颜色
                            shadowBlur: 10 // 区域阴影模糊大小
                        }
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#444' // 坐标轴线颜色
                        }
                    }
                },
                series: [
                    {
                        name: '',
                        type: 'radar',
                        data: seriesData,
                        symbol: 'circle', // 数据点形状
                        symbolSize: 6, // 数据点大小
                        lineStyle: {
                            width: 2 // 折线宽度
                        },
                        areaStyle: {
                            opacity: 0.5 // 区域填充透明度
                        }
                    }
                ]
            };
            myChart.setOption(option);
        },
        onChange(file,fileList){
            console.log(file);
            console.log(fileList);
            this.fileList = fileList;
        },
        async uploadFile(){
            console.log('上传文件');
            let formData = new FormData();
            for (let i in this.fileList) {
                formData.append('txt', this.fileList[i].raw);
            }
            let res = await this.axios({
                url:'http://localhost:8080/flow/uploadtxt',
                method: 'post',
                data: formData
            });
            console.log(res);
            let {data} = await this.axios({
                url: 'http://localhost:8080/flow/getInfoFlow',
                method: 'get',
            })
            console.log('data',data.data);
            this.tableData = data.data;
            console.log('tableData',this.tableData);
            // 雷达图
            this.$nextTick(()=>{
                this.drawLine();
            })
        },
        // 溢出则替换
        onExceed(files,fileList){
            this.fileList.pop();
            this.fileList.push(files[0]);
        },
    }
}
</script>

<style scoped>

.box-card {
    margin-top: 20px;
}

/deep/ .el-upload{
    width: 320px;
    height: 40px;
    line-height: 40px;
}
.col-box{
    margin-left: 50px;
    margin-right: 50px;
    width: 17%;
}

.bottom {
    margin-top: 13px;
    line-height: 12px;
    font-size: 25px;
    font-weight: bold;
    text-align: center;
}


.clearfix:before,
.clearfix:after {
    display: table;
    content: "";
}

.clearfix:after {
    clear: both
}

.el-table th, .el-table tr {
    background-color: #f5f7fa;
    font-weight: bold;
    color: #333;
    font-size: 18px!important;
    font-family: 'Arial', sans-serif;
}

.el-table td, .el-table th.is-leaf {
    border-bottom: 1px solid #ebeef5; /* 底部边框 */
}

/* 悬停行背景色 */
.el-table--enable-row-hover .el-table__body tr:hover>td {
    background-color: #f5f7fa !important; /* 悬停背景颜色 */
}

/* 表格整体边框 */
.el-table--border, .el-table--group {
    border: 1px solid #ebeef5; /* 外边框 */
}
</style>