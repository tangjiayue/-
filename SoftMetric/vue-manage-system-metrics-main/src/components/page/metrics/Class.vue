<template>
    <el-container style='display: block'>

        <div class="custom-card text-center custom-font">
            <h3>功能点度量页面</h3>
            <p class="paragraph-style">在本页面设置各类功能点数量和系统特征，将根据功能点度量的方法得出度量结果</p>
        </div>


        <el-card class="box-card">
            <el-steps :active="stepNumber" align-center>
                <el-step title="步骤1" description="填写功能点数量"></el-step>
                <el-step title="步骤2" description="填写技术因子影响程度"></el-step>
                <el-step title="步骤3" description="完成"></el-step>
            </el-steps>

            <div v-if='stepNumber == 1'>
                <p style='margin-top: 20px;font-size: 18px;margin-left: 40%;'>当前计算UFC的结果为：{{UFCResult}}</p>
                <el-divider></el-divider>
                <el-table :data="tableDataUFC" border style="width: 100%;border-radius: 10px;font-size: 14px">
                    <el-table-column prop="measurementElements" label="测量元素" width="180">
                        <template slot-scope="scope">
<!--                            <input type="text" v-model="scope.row.measurementElements" v-show="scope.row.iseditor" />-->
                            <span>{{scope.row.measurementElements}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="Simple" label="Simple" width="180">
                        <template slot-scope="scope">
                            <input type="text" v-model="scope.row.Simple" v-show="scope.row.iseditor" />
                            <span v-show="!scope.row.iseditor">{{scope.row.Simple}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="Average" label="Average">
                        <template slot-scope="scope">
                            <input type="text" v-model="scope.row.Average" v-show="scope.row.iseditor" />
                            <span v-show="!scope.row.iseditor">{{scope.row.Average}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="Complex" label="Complex">
                        <template slot-scope="scope">
                            <input type="text" v-model="scope.row.Complex" v-show="scope.row.iseditor" />
                            <span v-show="!scope.row.iseditor">{{scope.row.Complex}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="180">
                        <template slot-scope="scope">
                            <el-button type="warning" @click="edit(scope.row, scope)">编辑</el-button>
                            <el-button type="danger" @click="save(scope.row)">保存</el-button>
                        </template>
                    </el-table-column>
                </el-table>

                <el-divider></el-divider>
                <div  style='display: flex;justify-content:center'>
                    <el-button type="primary" @click="stepNumber = 2">下一步</el-button>
                    <el-button type="success" @click="calculateUFCResult()"  style='background-color: rgb(39 59 96);border-color: #1f2f3d'>计算结果</el-button>
                </div>
            </div>

            <div v-if='stepNumber == 2'>
                <p style='margin-top: 20px;font-size: 18px;margin-left: 40%;'>当前计算VAF的结果为：{{VAFResult}}</p>
                <el-divider></el-divider>
                <el-table :data="tableDataVAF" border style="width: 100%;border-radius: 10px;font-size: 14px">
                    <el-table-column prop="number" label="序号" width="300">
                        <template slot-scope="scope">
<!--                            <input type="text" v-model="scope.row.number" v-show="scope.row.iseditor" />-->
                            <span>{{scope.row.number}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="factor" label="技术复杂度因子" width="300">
                        <template slot-scope="scope">
<!--                            <input type="text" v-model="scope.row.factor" v-show="scope.row.iseditor" />-->
                            <span>{{scope.row.factor}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="degree" label="影响程度">
                        <template slot-scope="scope">
                            <input type="text" v-model="scope.row.degree" v-show="scope.row.iseditor" />
                            <span v-show="!scope.row.iseditor">{{scope.row.degree}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="180">
                        <template slot-scope="scope">
                            <el-button type="warning" @click="edit(scope.row, scope)">编辑</el-button>
                            <el-button type="danger" @click="save(scope.row)">保存</el-button>
                        </template>
                    </el-table-column>
                </el-table>

                <el-divider></el-divider>
                <div  style='display: flex;justify-content:center'>
                    <div style='display: flex;justify-content:center'>
                        <el-button type="primary" @click="stepNumber = 1">上一步</el-button>
                        <el-button type="primary" @click="calculateFPResult()">下一步</el-button>
                        <el-button type="success" @click="calculateVAFResult()" style='background-color: rgb(39 59 96);border-color: #1f2f3d'>计算结果</el-button>

                    </div>
                </div>
            </div>


            <div v-if='stepNumber == 3'>
                <div style='justify-content:center'>
                    <el-result icon="success" :title="'功能点度量结果为：' + FPResult " subTitle="可以点击“上一步”修改项目参数">
                    </el-result>
                </div>
                <div style='display: flex;justify-content: center;'>
                    <el-button type="primary" @click="stepNumber = 2">上一步</el-button>
                </div>

            </div>
        </el-card>



    </el-container>
</template>

<script>
export default {
    name:'Class',
    data() {
        return {
            fileList: [],
            activeNames: ['1'],
            activeName: 'first',
            CKresList: [],
            LKresList: [],
            classList: [],
            tableData: [],
            UFCResult: 0,
            VAFResult: 0,
            stepNumber: 1,
            tableDataUFC: [
                {
                    measurementElements: "外部输入",
                    Simple: "1",
                    Average: "2",
                    Complex: "3",
                    iseditor: false
                },
                {
                    measurementElements: "外部输出",
                    Simple: "2",
                    Average: "2",
                    Complex: "1",
                    iseditor: false
                },
                {
                    measurementElements: "外部查询",
                    Simple: "3",
                    Average: "1",
                    Complex: "2",
                    iseditor: false
                },
                {
                    measurementElements: "内部文件",
                    Simple: "5",
                    Average: "6",
                    Complex: "3",
                    iseditor: false
                },
                {
                    measurementElements: "外部接口",
                    Simple: "1",
                    Average: "2",
                    Complex: "1",
                    iseditor: false
                }
            ],
            tableDataVAF: [
                {
                    number: "1",
                    factor: "系统恢复",
                    degree: "4",
                    iseditor: false
                },
                {
                    number: "2",
                    factor: "数据通讯",
                    degree: "5",
                    iseditor: false
                },
                {
                    number: "3",
                    factor: "分布式数据处理",
                    degree: "2",
                    iseditor: false
                },
                {
                    number: "4",
                    factor: "性能",
                    degree: "3",
                    iseditor: false
                },
                {
                    number: "5",
                    factor: "资源需求",
                    degree: "4",
                    iseditor: false
                },
                {
                    number: "6",
                    factor: "在线数据输入",
                    degree: "5",
                    iseditor: false
                },
                {
                    number: "7",
                    factor: "多点运行",
                    degree: "2",
                    iseditor: false
                },
                {
                    number: "8",
                    factor: "在线升级",
                    degree: "5",
                    iseditor: false
                },
                {
                    number: "9",
                    factor: "复杂处理",
                    degree: "2",
                    iseditor: false
                },
                {
                    number: "10",
                    factor: "内部处理复杂",
                    degree: "0",
                    iseditor: false
                },
                {
                    number: "11",
                    factor: "可重用性",
                    degree: "3",
                    iseditor: false
                },
                {
                    number: "12",
                    factor: "易安装性",
                    degree: "3",
                    iseditor: false
                },
                {
                    number: "13",
                    factor: "多点运行",
                    degree: "4",
                    iseditor: false
                },
                {
                    number: "14",
                    factor: "易变性",
                    degree: "1",
                    iseditor: false
                }
            ],
            FPResult: 0,
            ufcObject: null,
            vafObject: null
        }
    },
    async mounted() {
        await this.calculateVAFResult()
        await this.calculateUFCResult()
    },
    methods: {
        async calculateFPResult(){
            this.stepNumber = 3
            // fpObject
            const fpObject = {...this.ufcObject,...this.vafObject};
            let res = await this.axios({
                url:'http://localhost:8080/fp/getFP',
                method: 'post',
                params: fpObject
            });
            console.log('FP',res);
            this.FPResult = res.data.data;
        },
        async calculateVAFResult(){
            let tableDataVAF = this.tableDataVAF;
            // 将 tableDataUFC 转换成 UFC 对象
            const vafObject = {
                recovery: parseInt(tableDataVAF[0].degree),
                communication: parseInt(tableDataVAF[1].degree),
                distribute: parseInt(tableDataVAF[2].degree),
                performance: parseInt(tableDataVAF[3].degree),
                operation: parseInt(tableDataVAF[4].degree),
                online: parseInt(tableDataVAF[5].degree),
                multiple: parseInt(tableDataVAF[6].degree),
                update: parseInt(tableDataVAF[7].degree),
                complex: parseInt(tableDataVAF[8].degree),
                interComplex: parseInt(tableDataVAF[9].degree),
                reuse: parseInt(tableDataVAF[10].degree),
                installation: parseInt(tableDataVAF[11].degree),
                multiplePoints: parseInt(tableDataVAF[12].degree),
                facilitation: parseInt(tableDataVAF[13].degree)
            };
            this.vafObject = vafObject;
            let res = await this.axios({
                url:'http://localhost:8080/fp/getVAF',
                method: 'post',
                params: vafObject
            });
            console.log('VAF',res);
            this.VAFResult = res.data.data;
        },
        async calculateUFCResult(){
            let tableDataUFC = this.tableDataUFC;
            // 将 tableDataUFC 转换成 UFC 对象
            const ufcObject = {
                EI1: parseInt(tableDataUFC[0].Simple),
                EO1: parseInt(tableDataUFC[1].Simple),
                EQ1: parseInt(tableDataUFC[2].Simple),
                EIF1: parseInt(tableDataUFC[3].Simple),
                ILF1: parseInt(tableDataUFC[4].Simple),

                EI2: parseInt(tableDataUFC[0].Average),
                EO2: parseInt(tableDataUFC[1].Average),
                EQ2: parseInt(tableDataUFC[2].Average),
                EIF2: parseInt(tableDataUFC[3].Average),
                ILF2: parseInt(tableDataUFC[4].Average),

                EI3: parseInt(tableDataUFC[0].Complex),
                EO3: parseInt(tableDataUFC[1].Complex),
                EQ3: parseInt(tableDataUFC[2].Complex),
                EIF3: parseInt(tableDataUFC[3].Complex),
                ILF3: parseInt(tableDataUFC[4].Complex)
            };
            this.ufcObject = ufcObject;
            let res = await this.axios({
                url:'http://localhost:8080/fp/getUFC',
                method: 'post',
                params: ufcObject
            });
            console.log('UFC',res);
            this.UFCResult = res.data.data;
        },
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
            // 上传文件
            let res = await this.axios({
                url:'http://localhost:8080/fp/getFP',
                method: 'post',
                data: formData
            });
            console.log('上传文件',res);
            // 请求后台处理结果
            // 1.请求CK度量结果
            let CKData = await this.axios({
                url:'http://localhost:8080/xml/getCKvalue',
                method: 'get',
            })
            this.CKresList = CKData.data.data;
            console.log('CK',this.CKresList);
            // 2.请求LK度量结果
            let LKData = await this.axios({
                url:'http://localhost:8080/xml/getLKvalue'
            })
            this.LKresList = LKData.data.data;
            console.log('LK',this.LKresList);
            // 3.请求类详情信息
            // let {data} = await this.axios({
            //                 url: 'http://localhost:8080/xml/getBasicInfo',
            //                 method: 'get',
            //             })
            // this.classList = data.data;
            // console.log('classInfo',this.classList);
            // 3.进行类图统计
            // 遍历ck度量中的数据，求和
            let complex=0, inherit=0, couple=0;
            this.CKresList.forEach(item=>{
                complex+=(item.wmc+item.dit);
                inherit+=(item.dit+item.noc);
                couple+=(item.cbo+item.rfc);
            })
            this.tableData = [{
                complex: complex,
                inherit: inherit,
                couple: couple
            }]
        },
        // 溢出则替换
        onExceed(files,fileList){
            this.fileList.pop();
            this.fileList.push(files[0]);
        },
        // 点击折叠面板出现的值
        handleChange(val){
            // console.log(val);
        },
        edit(row, index) {
            row.iseditor = true;
        },
        save(row, index) {
            row.iseditor = false;
        }
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
.custom-card {
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
    margin-bottom: 20px;
}

.text-center {
    text-align: center;
}

.custom-font {
    font-family: 'KaiTi', '楷体', sans-serif;
    font-size: 24px;
    font-weight: normal;
    color: #4f4d4d;
}

.paragraph-style {
    text-align: center;
    font-size: 19px;
    line-height: 1.5;
    padding: 10px 20px;
    color: #808080;
    font-family: 'KaiTi', '楷体', sans-serif;
}
</style>