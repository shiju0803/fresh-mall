<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item prop="adType">
        <el-select v-model="queryParams.adType" clearable placeholder="请选择广告类型">
          <el-option
            v-for="dict in dict.type.carousel_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择广告状态">
          <el-option
            v-for="dict in dict.type.carousel_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button> -->
        <el-button
          v-hasPermi="['carousel:carousel:add']"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >添加</el-button>
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['carousel:carousel:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['carousel:carousel:edit']"
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['carousel:carousel:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['carousel:carousel:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row> -->

    <el-table v-loading="loading" :data="carouselList">
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="序号" width="80" align="center">
        <template slot-scope="{$index}">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column v-if="true" label="广告ID" align="center" prop="id" />
      <el-table-column label="广告标题" align="left" header-align="center" prop="title" />
      <el-table-column label="广告类型" align="center" prop="adType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.carousel_type" :value="scope.row.adType" />
        </template>
      </el-table-column>
      <el-table-column label="广告状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.carousel_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <!-- <el-table-column label="广告请求路径" align="center" prop="url" /> -->
      <el-table-column label="广告图片" align="center" prop="imgUrl">
        <template slot-scope="{row}">
          <el-image :src="JSON.parse(row.imgUrl)[0].url" />
        </template>
      </el-table-column>
      <el-table-column label="活动链接" align="left" header-align="center" prop="url" />
      <el-table-column label="操作" align="center" class-name="small-padding" width="150">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['carousel:carousel:edit']"
            size="mini"
            type="text"
            style="color: #409EFF;"
            @click="handleUpdate(scope.row)"
          >编辑</el-button>
          <el-button
            v-hasPermi="['carousel:carousel:remove']"
            size="mini"
            type="text"
            style="color: #F56C6C;"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改商铺广告对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="广告标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入广告标题" clearable />
        </el-form-item>
        <el-form-item label="广告图片" prop="imgUrl">
          <image-upload v-model="form.imgUrl" :limit="1" value-type="json" />
        </el-form-item>
        <el-form-item label="广告类型" prop="adType">
          <el-select v-model="form.adType" clearable placeholder="请选择广告类型">
            <el-option
              v-for="dict in dict.type.carousel_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="status" label="广告状态">
          <el-select v-model="form.status" clearable placeholder="请选择广告状态">
            <el-option
              v-for="dict in dict.type.carousel_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="活动链接" prop="url">
          <el-input v-model="form.url" placeholder="请输入活动链接" clearable />
        </el-form-item>
        <el-form-item label="站外链接" prop="outUrl">
          <el-input v-model="form.outUrl" clearable />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCarousel, getCarousel, delCarousel, addCarousel, updateCarousel } from '@/api/carousel/carousel'
import { getToken } from '@/utils/auth'

export default {
  name: 'Carousel',
  dicts: ['carousel_status', 'carousel_type'],
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // // 选中数组
      // ids: [],
      // // 非单个禁用
      // single: true,
      // // 非多个禁用
      // multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 商铺广告表格数据
      carouselList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        adType: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        // id: [
        //   { required: true, message: '不能为空', trigger: 'blur' }
        // ],
        adType: [
          { required: true, message: '广告类型不能为空', trigger: 'change' }
        ],
        title: [
          { required: true, message: '广告标题不能为空', trigger: 'blur' }
        ],
        // url: [
        //   { required: true, message: '广告请求路径不能为空', trigger: 'blur' }
        // ],
        imgUrl: [
          { required: true, message: '广告图片不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '广告状态不能为空', trigger: 'change' }
        ]
        // outUrl: [
        //   { required: true, message: '站外链接不能为空', trigger: 'blur' }
        // ]
      },
      headers: {
        accessToken: getToken()
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询商铺广告列表 */
    getList() {
      this.loading = true
      listCarousel(this.queryParams).then(response => {
        this.carouselList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        adType: undefined,
        title: undefined,
        url: undefined,
        imgUrl: undefined,
        status: undefined,
        outUrl: undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    // /** 重置按钮操作 */
    // resetQuery() {
    //   this.resetForm('queryForm')
    //   this.handleQuery()
    // },
    // // 多选框选中数据
    // handleSelectionChange(selection) {
    //   this.ids = selection.map(item => item.id)
    //   this.single = selection.length !== 1
    //   this.multiple = !selection.length
    // },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '创建'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true
      this.reset()
      const id = row.id || this.ids
      getCarousel(id).then(response => {
        this.loading = false
        this.form = response.data
        this.open = true
        this.title = '编辑'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.buttonLoading = true
          if (this.form.id != null) {
            updateCarousel(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          } else {
            addCarousel(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除？').then(() => {
        this.loading = true
        return delCarousel(ids)
      }).then(() => {
        this.loading = false
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      }).finally(() => {
        this.loading = false
      })
    }
    // /** 导出按钮操作 */
    // handleExport() {
    //   this.download('carousel/carousel/export', {
    //     ...this.queryParams
    //   }, `carousel_${new Date().getTime()}.xlsx`)
    // }
  }
}
</script>
