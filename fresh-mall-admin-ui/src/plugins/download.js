import axios from 'axios'
import {Loading, Message} from 'element-ui'
import { saveAs } from 'file-saver'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { blobValidate } from "@/utils/fresh-mall";

const baseURL = process.env.VUE_APP_BASE_API
let downloadLoadingInstance;

export default {
  oss(ossId) {
    var url = baseURL + '/system/oss/download/' + ossId
    downloadLoadingInstance = Loading.service({ text: "正在下载数据，请稍候", spinner: "el-icon-loading", background: "rgba(0, 0, 0, 0.7)", })
    axios({
      method: 'get',
      url: url,
      responseType: 'blob',
      headers: { 'Authorization': 'Bearer ' + getToken() }
    }).then(async (res) => {
      const isLogin = await blobValidate(res.data);
      if (isLogin) {
        const blob = new Blob([res.data], { type: 'application/octet-stream' })
        this.saveAs(blob, decodeURIComponent(res.headers['download-filename']))
      } else {
        this.printErrMsg(res.data);
      }
      downloadLoadingInstance.close();
    }).catch((r) => {
      console.error(r)
      Message.error('下载文件出现错误，请联系管理员！')
      downloadLoadingInstance.close();
    })
  },
  zip(url, name) {
    var url = baseURL + url
    axios({
      method: 'get',
      url: url,
      responseType: 'blob',
      headers: {
        'Authorization': 'Bearer ' + getToken(),
        'datasource': localStorage.getItem("dataName")
      }
    }).then(async (res) => {
      const isLogin = await blobValidate(res.data);
      if (isLogin) {
        const blob = new Blob([res.data], { type: 'application/zip' })
        this.saveAs(blob, name)
      } else {
        this.printErrMsg(res.data);
      }
    })
  },
  saveAs(text, name, opts) {
    saveAs(text, name, opts);
  },
  async printErrMsg(data) {
    const resText = await data.text();
    const rspObj = JSON.parse(resText);
    const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
    Message.error(errMsg);
  }
}

