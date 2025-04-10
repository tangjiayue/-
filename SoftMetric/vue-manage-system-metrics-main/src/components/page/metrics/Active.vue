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
            <div class="el-upload__tip">请上传流程图的oom文件</div>
            </el-upload>
            <el-button class="submit" type="primary" round @click="uploadFile">上传文件</el-button>
        </el-aside>
        <el-main style="overflow:auto; height:400px">
            
                <el-empty v-if="!tableData.length" description="请先上传文件"></el-empty>
                <el-table v-if="tableData.length" 
            :data="tableData"
            style="width: 100%">
            <el-table-column
              prop="node"
              label="节点数量">
            </el-table-column>
            <el-table-column
              prop="edge"
              label="边数量">
            </el-table-column>
            <el-table-column
              prop="circleComplex"
              label="圈复杂度">
            </el-table-column>
          </el-table>
            
        </el-main>
    </el-container>
</template>

<script>
export default {
    name:'Active',
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
                            url: 'http://localhost:8080/oom/dataFlow',
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
.submit {
    position: absolute;
    margin-top: 20px;
    left: 50%;
    transform: translateX(-70%);
}
.el-tabs--left {
    overflow: auto;
}
</style>