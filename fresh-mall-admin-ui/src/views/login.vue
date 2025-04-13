<template>
  <div class="login-form-layout">
    <div class="login-form-left" />
    <div class="login-form-right">
      <div class="login-form-container">
        <el-form
          ref="loginForm"
          class="login-form"
          auto-complete="on"
          :model="loginForm"
          :rules="loginRules"
          label-position="left"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              name="username"
              type="text"
              auto-complete="on"
              placeholder="请输入用户名"
            >
              <span slot="prefix">
                <svg-icon icon-class="user" class="color-main" />
              </span>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              name="password"
              :type="pwdType"
              auto-complete="on"
              placeholder="请输入密码"
              @keyup.enter.native="handleLogin"
            >
              <span slot="prefix">
                <svg-icon icon-class="password" class="color-main" />
              </span>
              <span slot="suffix" @click="showPwd">
                <svg-icon icon-class="eye" class="color-main" />
              </span>
            </el-input>
          </el-form-item>
          <el-form-item prop="code">
            <el-col :span="13">
              <el-input
                v-model="loginForm.code"
                placeholder="输入验证码"
                auto-complete="off"
                clearable
                @keyup.enter.native="handleLogin"
              >
                <span slot="prefix">
                  <svg-icon
                    slot="prefix"
                    icon-class="email"
                    style="margin-top: 6px"
                  />
                </span>
              </el-input>
            </el-col>
            <el-col :span="6" style="margin-left: 10px; margin-top: 2px">
              <img :src="codeUrl" width="130" height="35" @click="getCode">
            </el-col>
          </el-form-item>
          <el-form-item style="margin-bottom: 20px">
            <el-button
              style="width: 100%"
              type="primary"
              :loading="loading"
              @click.native.prevent="handleLogin"
            >
              登录
            </el-button>
          </el-form-item>

          <!--          <el-form-item style="margin-bottom: 10px">-->
          <!--            <el-button style="width: 100%" type="primary"  @click.native.prevent="handleStore">-->
          <!--              商家入驻-->
          <!--            </el-button>-->
          <!--          </el-form-item>-->

        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from '@/api/login';
import Cookies from 'js-cookie';
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: 'Login',
  data() {
    return {
      codeUrl: '',
      pwdType: 'password',
      loginForm: {
        username: 'admin',
        password: 'es110212',
        rememberMe: false,
        code: '',
        uuid: ''
      },
      loginRules: {
        username: [
          { required: true, trigger: 'blur', message: '请输入您的账号' }
        ],
        password: [
          { required: true, trigger: 'blur', message: '请输入您的密码' }
        ],
        code: [{ required: true, trigger: 'change', message: '请输入验证码' }]
      },
      loading: false,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关
      register: false,
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    this.getCookie();
    this.getCode();
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.data.captchaEnabled === undefined ? true : res.data.captchaEnabled;
        if (this.captchaEnabled) {
          this.codeUrl = 'data:image/gif;base64,' + res.data.img;
          this.loginForm.uuid = res.data.uuid;
        }
      });
    },
    getCookie() {
      const username = Cookies.get('username');
      const password = Cookies.get('password');
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      };
    },
    showPwd () {
      if (this.pwdType === 'password') {
        this.pwdType = ''
      } else {
        this.pwdType = 'password'
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set('username', this.loginForm.username, { expires: 30 });
            Cookies.set('password', encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove('username');
            Cookies.remove('password');
            Cookies.remove('rememberMe');
          }
          this.$store.dispatch('Login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' }).catch(() => {});
          }).catch(() => {
            this.loading = false;
            if (this.captchaEnabled) {
              this.getCode();
            }
          });
        }
      });
    }
  }
};
</script>

<style scoped lang="scss">
.login-form-layout {
  display: flex;
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;

  .login-form-left {
    width: 60%;
    height: 100vh;
    float: left;
    background-image: url(../assets/images/login/loginLogoLeft.png);
    background-size: cover;
  }

  .login-form-right {
    height: 100vh;
    width: 40%;
    float: left;

    .logo-right {
      width: 40%;
      margin-top: 10vh;
    }

    .login-form-container {
      width: 50%;
      margin: auto;
      height: 30vh;
      margin-top: 35vh;

      .login-form {
        width: 100%;
        height: 20vh;
      }
    }
  }
}
</style>
