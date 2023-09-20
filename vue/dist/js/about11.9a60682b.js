(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["about11"],{1511:function(e,t,l){"use strict";l.r(t);var a=function(){var e=this,t=e._self._c;return t("div",[t("div",{staticStyle:{margin:"10px 0"}},[t("el-input",{staticStyle:{width:"200px"},attrs:{placeholder:"请输入名称","suffix-icon":"el-icon-search"},model:{value:e.username,callback:function(t){e.username=t},expression:"username"}}),t("el-input",{staticStyle:{width:"200px"},attrs:{placeholder:"请输入邮箱","suffix-icon":"el-icon-message"},model:{value:e.email,callback:function(t){e.email=t},expression:"email"}}),t("el-input",{staticStyle:{width:"200px"},attrs:{placeholder:"请输入地址","suffix-icon":"el-icon-position"},model:{value:e.address,callback:function(t){e.address=t},expression:"address"}}),t("el-button",{staticClass:"ml-5",attrs:{type:"primary"},on:{click:function(t){return e.load()}}},[e._v("搜索")]),t("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.reset()}}},[e._v("重置")])],1),t("div",{staticStyle:{margin:"10px 0"}},[t("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.handleAdd()}}},[e._v("新增"),t("i",{staticClass:"el-icon-circle-plus-outline",staticStyle:{"margin-left":"5px"}})]),t("el-button",{attrs:{type:"danger"},on:{click:function(t){return e.delbath()}}},[e._v("批量删除"),t("i",{staticClass:"el-icon-remove",staticStyle:{"margin-left":"5px"}})]),t("el-upload",{staticClass:"upload-demo ml-10",staticStyle:{display:"inline-block"},attrs:{action:"http://"+e.serverIp+":9090/user/import","show-file-list":!1,accept:".xlsx","on-success":e.handleExcelImportSuccess}},[t("el-button",{attrs:{type:"primary"}},[e._v(" 导入"),t("i",{staticClass:"el-icon-bottom",staticStyle:{"margin-left":"5px"}})])],1),t("el-button",{staticClass:"ml-10",attrs:{type:"primary"},on:{click:function(t){return e.exp()}}},[e._v("导出"),t("i",{staticClass:"el-icon-top",staticStyle:{"margin-left":"5px"}})])],1),t("el-table",{attrs:{data:e.tableData,border:"",stripe:"","header-cell-class-name":e.headerBg},on:{"selection-change":e.handleSelectionChange}},[t("el-table-column",{attrs:{type:"selection",width:"55"}}),t("el-table-column",{attrs:{prop:"id",label:"ID",width:"80"}}),t("el-table-column",{attrs:{prop:"username",label:"用户名",width:"140"}}),t("el-table-column",{attrs:{prop:"role",label:"角色",width:"140"}}),t("el-table-column",{attrs:{prop:"nickname",label:"昵称",width:"120"}}),t("el-table-column",{attrs:{prop:"email",label:"邮箱"}}),t("el-table-column",{attrs:{prop:"phone",label:"电话"}}),t("el-table-column",{attrs:{prop:"address",label:"地址"}}),t("el-table-column",{staticStyle:{display:"inline-block"},attrs:{label:"操作","min-width":"83px"},scopedSlots:e._u([{key:"default",fn:function(l){return[t("el-button",{attrs:{type:"success"},on:{click:function(t){return e.handleEdit(l.row)}}},[e._v("编辑"),t("i",{staticClass:"el-icon-edit",staticStyle:{"margin-left":"3px"}})]),t("el-button",{staticStyle:{"margin-left":"0"},attrs:{type:"danger"},on:{click:function(t){return e.handleDelete(l.row.id)}}},[e._v("删除"),t("i",{staticClass:"el-icon-remove",staticStyle:{"margin-left":"3px"}})])]}}])})],1),t("div",{staticStyle:{padding:"10px 0"}},[t("el-pagination",{attrs:{"current-page":e.pageNum,"page-sizes":[2,5,10,20],"page-size":e.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1),t("el-dialog",{attrs:{title:"用户信息",visible:e.dialogFormVisible,width:"30%"},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[t("el-form",{attrs:{model:e.form,"label-width":"80px",size:"small"}},[t("el-form-item",{attrs:{label:"用户名"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.username,callback:function(t){e.$set(e.form,"username",t)},expression:"form.username"}})],1),t("el-form-item",{attrs:{label:"角色"}},[t("el-select",{staticStyle:{width:"100%"},attrs:{clearable:"",placeholder:"请选择角色"},model:{value:e.form.role,callback:function(t){e.$set(e.form,"role",t)},expression:"form.role"}},e._l(e.roles,(function(e){return t("el-option",{key:e.name,attrs:{label:e.name,value:e.flag}})})),1)],1),t("el-form-item",{attrs:{label:"昵称"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.nickname,callback:function(t){e.$set(e.form,"nickname",t)},expression:"form.nickname"}})],1),t("el-form-item",{attrs:{label:"邮箱"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.email,callback:function(t){e.$set(e.form,"email",t)},expression:"form.email"}})],1),t("el-form-item",{attrs:{label:"电话"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.phone,callback:function(t){e.$set(e.form,"phone",t)},expression:"form.phone"}})],1),t("el-form-item",{attrs:{label:"地址"}},[t("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.address,callback:function(t){e.$set(e.form,"address",t)},expression:"form.address"}})],1)],1),t("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(t){return e.handleCancel()}}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.save()}}},[e._v("确 定")])],1)],1)],1)},s=[],i=l("863e"),o={name:"User",data(){return{serverIp:i["a"],tableData:[],total:0,pageNum:1,pageSize:10,username:"",email:"",address:"",nickname:"",dialogFormVisible:!1,form:{},multipleSelection:[],headerBg:"headerBg",roles:[]}},created(){this.load()},methods:{load(){this.request.get("/user/page",{params:{pageNum:this.pageNum,pageSize:this.pageSize,username:this.username,address:this.address,email:this.email}}).then(e=>{console.log(e),this.tableData=e.records,this.total=e.total}),this.request.get("/role").then(e=>{this.roles=e})},handleSizeChange(e){console.log(e),this.pageSize=e,this.load()},handleCurrentChange(e){console.log(e),this.pageNum=e,this.load()},reset(){this.username="",this.address="",this.email="",this.load()},handleAdd(){this.dialogFormVisible=!0,this.form={}},save(){this.request.post("/user",this.form).then(e=>{e?(this.$message.success("保存成功"),this.dialogFormVisible=!1,this.load()):this.$message.success("保存失败")})},handleEdit(e){this.form=e,console.log("this.form==>"+this.form),this.dialogFormVisible=!0},handleCancel(){this.dialogFormVisible=!1,this.load()},handleDelete(e){this.$confirm("此操作将永久删除该用户信息, 是否继续?","警告",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(()=>{this.request.delete("/user/"+e).then(e=>{console.log("delRes==>"+e),this.load()}),this.$message({type:"success",message:"删除成功!"})}).catch(()=>{this.$message({type:"info",message:"已取消删除"})})},handleSelectionChange(e){console.log(e),this.multipleSelection=e},delbath(){this.$confirm("此操作将永久删除该用户信息, 是否继续?","警告",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(()=>{let e=this.multipleSelection.map(e=>e.id);this.request.post("/user/del/batch",e).then(e=>{console.log("delRes==>"+e),1==e?(this.$message({type:"success",message:"删除成功!"}),this.load()):this.$message({type:"info",message:"请至少选择一个用户进行删除"})})}).catch(()=>{this.$message({type:"info",message:"已取消删除"})})},handleExcelImportSuccess(){this.$message.success("用户信息上传成功!"),this.load()},exp(){window.open(`http://${i["a"]}:9090/user/export`)}}},n=o,r=(l("8ec8"),l("2877")),c=Object(r["a"])(n,a,s,!1,null,null,null);t["default"]=c.exports},4615:function(e,t,l){},"8ec8":function(e,t,l){"use strict";l("4615")}}]);
//# sourceMappingURL=about11.9a60682b.js.map