<template>
    <el-container style='display: block'>
        <div class="custom-card text-center custom-font">
            <h3>控制流度量页面</h3>
            <p class="paragraph-style">在本页面中上传源代码，我们将根据控制流度量的方法得出度量结果，并为您提供优化控制流结构的建议</p>
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

        <el-card class='box-card'>
            <el-empty v-if="!tableData.length" description="请先上传文件"></el-empty>
            <el-row v-if='tableData.length' type="flex" justify="space-around">
                <!-- 区域数量 -->
                <el-col :span="7" style="margin-right: 10px;">
                    <el-card :body-style="{ padding: '20px', textAlign: 'center', height: '100%' }" shadow="hover">
                        <i class="el-icon-s-marketing" style='font-size: 24px;color: #409EFF'></i>
                        <div style='font-size: 20px;font-weight: bold;margin-top: 10px;'>区域数量</div>
                        <div class="bottom clearfix" style="margin-top: 20px;font-size: 18px;">
                            {{tableData[0].cycleComplexity}}
                        </div>
                    </el-card>
                </el-col>

                <!-- 判断节点 -->
                <el-col :span="7" style="margin-right: 10px;">
                    <el-card :body-style="{ padding: '20px', textAlign: 'center', height: '100%' }" shadow="hover">
                        <i class="el-icon-s-flag" style='font-size: 24px;color: #67C23A'></i>
                        <div style='font-size: 20px;font-weight: bold;margin-top: 10px;'>判断节点</div>
                        <div class="bottom clearfix" style="margin-top: 20px;font-size: 18px;">
                            {{tableData[0].cycleComplexity - 1}}
                        </div>
                    </el-card>
                </el-col>

                <!-- 圈复杂度 -->
                <el-col :span="7">
                    <el-card :body-style="{ padding: '20px', textAlign: 'center', height: '100%' }" shadow="hover">
                        <i class="el-icon-s-opportunity" style='font-size: 24px;color: #E6A23C'></i>
                        <div style='font-size: 20px;font-weight: bold;margin-top: 10px;'>圈复杂度</div>
                        <div class="bottom clearfix" style="margin-top: 20px;font-size: 18px;">
                            {{tableData[0].cycleComplexity}}
                        </div>
                    </el-card>
                </el-col>
            </el-row>

            <el-card class="box-card" v-if='tableData.length' style="margin-top: 20px; border-left: 5px solid #409EFF;">
                <div v-if='tableData[0].cycleComplexity >= 11' style="padding: 15px; background-color: #fef0f0; color: #f56c6c;">
                    <p>该模块圈复杂度过大。一个模块过于复杂可能会带来软件阅读和理解难度增加、维护成本提高、测试难度增加、可靠性下降等问题。您可以考虑通过重构代码、简化条件逻辑、增加代码的模块化等方式来降低模块的复杂度。</p>
                </div>
                <div v-else style="padding: 15px; background-color: #f0f9eb; color: #67c23a;">
                    <p>该模块圈复杂度良好，表示该模块复杂程度适中。</p>
                </div>
            </el-card>
        </el-card>

        <el-card class="box-card" v-if='tableData.length'>
            <el-collapse @change="handleChange">
                <el-collapse-item title="HELP" name="1">
                    <div>
                        <h2>降低圈复杂度的方法</h2>
                        <ol>
                            <li>
                                <h3>1. 重构代码</h3>
                                <ul>
                                    <li><b>a) 分解复杂函数：</b>将复杂的函数分解成几个更小、更专一的函数。</li>
                                    <li><b>b) 使用多态替代条件语句：</b>在适用的情况下，可以通过多态来替代过多的if-else或switch-case语句。</li>
                                    <li></li>
                                </ul>
                            </li>
                            <li>
                                <h3>2. 简化条件逻辑</h3>
                                <ul>
                                    <li><b>a) 简化条件表达式：</b>通过逻辑运算简化复杂的条件表达式。</li>
                                    <li><b>b) 避免嵌套控制流：</b>减少if、for、while等控制流结构的嵌套深度。</li>
                                    <li></li>
                                </ul>
                            </li>
                            <li>
                                <h3>3. 使用设计模式</h3>
                                <ul>
                                    <li><b>a) 策略模式：</b>对于有多个分支的决策逻辑，可以使用策略模式将每个分支的逻辑封装到单独的类中。</li>
                                    <li><b>b) 状态模式：</b>对于复杂的状态机逻辑，使用状态模式可以帮助管理不同状态下的行为，降低复杂度。</li>
                                    <li></li>
                                </ul>
                            </li>
                            <li>
                                <h3>4. 优化循环和递归</h3>
                                <ul>
                                    <li><b>a) 减少循环内部的决策逻辑：</b>尽量保持循环体简洁，将复杂的逻辑移出循环。</li>
                                    <li><b>b) 谨慎使用递归：</b>递归可以使某些算法看起来更简洁，但也可能增加复杂度和资源消耗。在可能的情况下，考虑使用迭代代替递归。</li>
                                    <li></li>
                                </ul>
                            </li>
                            <li>
                                <h3>5. 增加代码的模块化</h3>
                                <ul>
                                    <li><b>a) 模块化设计：</b>通过模块化设计，将程序划分成高内聚、低耦合的模块，每个模块专注于完成一个具体的任务。</li>
                                    <li></li>
                                </ul>
                            </li>
                        </ol>
                    </div>
                </el-collapse-item>
            </el-collapse>
        </el-card>

    </el-container>
</template>

<script>
import * as echarts from 'echarts';
export default {
    name:'ControlFlow',
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
            var option = {
                title: {
                    text: '注释密度：'+ (this.tableData[0].commentDensity * 100).toFixed(3) + '%',
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    top: '5%'
                },
                label: {
                    show: false
                },
                series: [
                    {
                        name: '源代码度量',
                        type: 'pie',
                        radius: ['40%', '70%'],
                        avoidLabelOverlap: false,
                        itemStyle: {
                            borderRadius: 10,
                            borderColor: '#fff',
                            borderWidth: 2
                        },
                        label: {
                            show: false,
                            position: 'center'
                        },
                        emphasis: {
                            label: {
                                show: true,
                                fontSize: 40,
                                fontWeight: 'bold'
                            }
                        },
                        labelLine: {
                            show: false
                        },
                        data: [
                            { value: this.tableData[0].codeLines - this.tableData[0].commentLines, name: '未注释行数' },
                            { value: this.tableData[0].commentLines, name: '注释行数' },
                        ]
                    }
                ]
            };
            myChart.setOption(option)
        },
        onChange(file,fileList){
            console.log(file);
            console.log(fileList);
            this.fileList = fileList;
        },
        async uploadFile(){
            if(this.textarea.length!=0){
                console.log('上传输入');
                let res = await this.axios({
                    url:'http://localhost:8080/txt/getRawCode',
                    method:'POST',
                    data: {code:this.textarea}
                })
                console.log('上传代码',res);
                this.tableData=[{...res.data.data}];
                return;
            }
            console.log('上传文件');
            let formData = new FormData();
            for (let i in this.fileList) {
                formData.append('txt', this.fileList[i].raw);
            }
            let res = await this.axios({
                url:'http://localhost:8080/txt/uploadtxt',
                method: 'post',
                data: formData
            });
            console.log(res);
            let {data} = await this.axios({
                url: 'http://localhost:8080/txt/getCycleComplexity',
                method: 'get',
            })
            this.tableData=[{...data.data}];
            console.log('tableData',this.tableData);
            // 雷达图
            // this.$nextTick(()=>{
            //     this.drawLine();
            // })
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

</style>