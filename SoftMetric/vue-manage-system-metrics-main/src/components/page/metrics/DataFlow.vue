<template>
    <el-container class="body">
        <el-aside width="400px" class="aside">
            <el-upload
                class="upload-demo"
                drag
                :auto-upload="false"
                action="https://jsonplaceholder.typicode.com/posts/"
                :limit="1"
                :file-list="fileList"
                :on-change="onChange"
                :on-exceed="onExceed">
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <div class="el-upload__tip">请上传数据流图的oom文件</div>
            </el-upload>
            <el-row>
                <el-button class="submit-button" type="primary" round @click="uploadFile">上传文件</el-button>
                <el-button class="submit-button" round @click="openMessageBox()">了解更多</el-button>
            </el-row>

        </el-aside>
        <el-main style="overflow:auto; height:400px">

            <el-empty v-if="!tableData.length" description="请先上传文件"></el-empty>
            <el-table v-if="tableData.length"
                      :data="tableData"
                      style="width: 100%">

                <el-table-column
                    prop="decisionNodeCount"
                    label="判定节点数">
                </el-table-column>
                <el-table-column
                    prop="ordinaryNodeCount"
                    label="普通节点数">
                </el-table-column>
                <el-table-column
                    prop="totalNodeCount"
                    label="总节点数">
                </el-table-column>
                <el-table-column
                    prop="lineCount"
                    label="连线数量">
                </el-table-column>
                <el-table-column
                    prop="regionCount"
                    label="区域数量">
                </el-table-column>
                <el-table-column
                    prop="cyclomaticComplexity"
                    label="圈复杂度">
                </el-table-column>

            </el-table>

        </el-main>
    </el-container>
</template>

<script>
export default {
    name:'DataFlow',
    data() {
        return {
            fileList: [],
            tableData: []
        }
    },
    methods: {
        onChange(file,fileList){
            console.log(file);
            console.log(fileList);
            this.fileList = fileList;
        },
        async uploadFile(){
            let formData = new FormData();
            for (let i in this.fileList) {
                formData.append('file', this.fileList[i].raw);
            }
            let res = await this.axios({
                url:'http://localhost:8080/oom/uploadoom',
                method: 'post',
                data: formData
            });
            console.log(res);
            let {data} = await this.axios({
                url: 'http://localhost:8080/oom/controlFlow',
                method: 'get',
            })
            console.log('获得',data);
            this.tableData=[{...data.data}];
        },
        // 溢出则替换
        onExceed(files,fileList){
            this.fileList.pop();
            this.fileList.push(files[0]);
        },
        openMessageBox(){
            this.$notify({
                title: '提示帮助',
                dangerouslyUseHTMLString: true,
                message: '<p>我们规定圈复杂度在[1, 10]之间为良好，[11, +∞]为过大。</p>\n' +
                    '\n' +
                    '<p>圈复杂度良好提示：该模块圈复杂度良好，表示该模块复杂程度适中。</p>\n' +
                    '\n' +
                    '<p>圈复杂度过大提示：该模块圈复杂度过大。一个模块过于复杂可能会带来软件阅读和理解难度增加、维护成本提高、测试难度增加、可靠性下降等问题。您可以考虑通过重构代码、简化条件逻辑、增加代码的模块化等方式来降低模块的复杂度。详细的降低圈复杂度的方法见<a href="#">这里</a>。</p>'
            });
        }
    }
}
</script>

<style scoped>
.body {
    margin: 0 auto;
}
.aside {
    position: relative;
}
.submit-button {
    margin-top: 10px;
    margin-left: 70px;
}
</style>
