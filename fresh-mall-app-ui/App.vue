<script>
	/**
	 * vuex管理登陆状态，具体可以参考官方登陆模板示例
	 */
	import {
		mapMutations
	} from 'vuex';
	export default {
		methods: {
			async init() {
				var [err, res1] = await uni.getLocation({
					type: 'wgs84'
				})

				if (res1.errMsg === 'getLocation:ok') {
					uni.showLoading({
						title: "加载中..."
					})
					this.$api.request('get', 'storage/position/getRecentlyStorage', {
						lng: res1.longitude,
						lat: res1.latitude
					}, failres => {
						uni.hideLoading()
					}).then(res => {
						uni.hideLoading()
						this.$store.commit('setStorage', res.data.id)
						this.$store.commit('setStorageObj', {
							businessStartTime: "9:30",
							businessState: false,
							businessStopTime: "22:30",
							deliveryStartTime: "9:30",
							deliveryStopTime: "23:00",
							haveStorage: true,
							id: res.data.id
						})
					})
				}
				if (this.$store.state.userInfo.accessToken && this.$store.state.storageId != 0) {
					this.$api.request('get', 'cart/app/countCart', {
						storageId: this.$store.state.storageId
					}).then(res => {
						if (res.data > 0) {
							uni.setTabBarBadge({
								index: 2,
								text: res.data + ''
							})
						}
						this.$store.commit('addCart', res.data)
					}).catch(err => {
						this.$api.msg('请求失败，请稍后再试')
					})
				}
			},
			...mapMutations(['login'])
		},
		onLaunch: function() {
			let userInfo = uni.getStorageSync('userInfo') || '';
			if (userInfo.accessToken) {
				//更新登陆状态
				uni.getStorage({
					key: 'userInfo',
					success: (res) => {
						this.login(res.data);
					}
				});
			}
			//全局获取仓库信息
			this.init()
		},
		onShow: function() {
			//全局获取仓库信息
			this.init()
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		},
	}
</script>

<style lang='scss'>
	@import "./static/common.css";

	@font-face {
		font-family: "yticon";
		src: url('data:application/x-font-woff2;charset=utf-8;base64,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')
	}

	.status-bar {
		height: var(--status-bar-height);
		width: 750rpx;
		display: flex;
		position: relative;
	}

	.yticon {
		font-family: "yticon" !important;
		font-size: 16px;
		font-style: normal;
		-webkit-font-smoothing: antialiased;
		-moz-osx-font-smoothing: grayscale;
	}

	.icon-iconfontshanchu1:before {
		content: "\e619";
	}

	.icon-iconfontweixin:before {
		content: "\e611";
	}

	.icon-alipay:before {
		content: "\e636";
	}

	.icon-shang:before {
		content: "\e624";
	}

	.icon-shouye:before {
		content: "\e626";
	}

	.icon-shanchu4:before {
		content: "\e622";
	}

	.icon-xiaoxi:before {
		content: "\e618";
	}

	.icon-jiantour-copy:before {
		content: "\e600";
	}

	.icon-fenxiang2:before {
		content: "\e61e";
	}

	.icon-pingjia:before {
		content: "\e67b";
	}

	.icon-daifukuan:before {
		content: "\e68f";
	}

	.icon-pinglun-copy:before {
		content: "\e612";
	}

	.icon-dianhua-copy:before {
		content: "\e621";
	}

	.icon-shoucang:before {
		content: "\e645";
	}

	.icon-xuanzhong2:before {
		content: "\e62f";
	}

	.icon-gouwuche_:before {
		content: "\e630";
	}

	.icon-icon-test:before {
		content: "\e60c";
	}

	.icon-icon-test1:before {
		content: "\e632";
	}

	.icon-bianji:before {
		content: "\e646";
	}

	.icon-jiazailoading-A:before {
		content: "\e8fc";
	}

	.icon-zuoshang:before {
		content: "\e613";
	}

	.icon-jia2:before {
		content: "\e60a";
	}

	.icon-huifu:before {
		content: "\e68b";
	}

	.icon-sousuo:before {
		content: "\e7ce";
	}

	.icon-arrow-fine-up:before {
		content: "\e601";
	}

	.icon-hot:before {
		content: "\e60e";
	}

	.icon-lishijilu:before {
		content: "\e6b9";
	}

	.icon-zhengxinchaxun-zhifubaoceping-:before {
		content: "\e616";
	}

	.icon-naozhong:before {
		content: "\e64a";
	}

	.icon-xiatubiao--copy:before {
		content: "\e608";
	}

	.icon-shoucang_xuanzhongzhuangtai:before {
		content: "\e6a9";
	}

	.icon-jia1:before {
		content: "\e61c";
	}

	.icon-bangzhu1:before {
		content: "\e63d";
	}

	.icon-arrow-left-bottom:before {
		content: "\e602";
	}

	.icon-arrow-right-bottom:before {
		content: "\e603";
	}

	.icon-arrow-left-top:before {
		content: "\e604";
	}

	.icon-icon--:before {
		content: "\e744";
	}

	.icon-zuojiantou-up:before {
		content: "\e605";
	}

	.icon-xia:before {
		content: "\e62d";
	}

	.icon--jianhao:before {
		content: "\e60b";
	}

	.icon-weixinzhifu:before {
		content: "\e61a";
	}

	.icon-comment:before {
		content: "\e64f";
	}

	.icon-weixin:before {
		content: "\e61f";
	}

	.icon-fenlei1:before {
		content: "\e620";
	}

	.icon-erjiye-yucunkuan:before {
		content: "\e623";
	}

	.icon-Group-:before {
		content: "\e688";
	}

	.icon-you:before {
		content: "\e606";
	}

	.icon-forward:before {
		content: "\e607";
	}

	.icon-tuijian:before {
		content: "\e610";
	}

	.icon-bangzhu:before {
		content: "\e679";
	}

	.icon-share:before {
		content: "\e656";
	}

	.icon-yiguoqi:before {
		content: "\e997";
	}

	.icon-shezhi1:before {
		content: "\e61d";
	}

	.icon-fork:before {
		content: "\e61b";
	}

	.icon-kafei:before {
		content: "\e66a";
	}

	.icon-iLinkapp-:before {
		content: "\e654";
	}

	.icon-saomiao:before {
		content: "\e60d";
	}

	.icon-shezhi:before {
		content: "\e60f";
	}

	.icon-shouhoutuikuan:before {
		content: "\e631";
	}

	.icon-gouwuche:before {
		content: "\e609";
	}

	.icon-dizhi:before {
		content: "\e614";
	}

	.icon-fenlei:before {
		content: "\e706";
	}

	.icon-xingxing:before {
		content: "\e70b";
	}

	.icon-tuandui:before {
		content: "\e633";
	}

	.icon-zuanshi:before {
		content: "\e615";
	}

	.icon-zuo:before {
		content: "\e63c";
	}

	.icon-yiguoqi1:before {
		content: "\e700";
	}

	.icon-shoucang2:before {
		content: "\e62e";
	}

	.icon-shouhuodizhi:before {
		content: "\e712";
	}

	.icon-yishouhuo:before {
		content: "\e71a";
	}

	.icon-dianzan-ash:before {
		content: "\e617";
	}






	view,
	scroll-view,
	swiper,
	swiper-item,
	cover-view,
	cover-image,
	icon,
	text,
	rich-text,
	progress,
	button,
	checkbox,
	form,
	input,
	label,
	radio,
	slider,
	switch,
	textarea,
	navigator,
	audio,
	camera,
	image,
	video {
		box-sizing: border-box;
	}

	/* 骨架屏替代方案 */
	.Skeleton {
		background: #f3f3f3;
		padding: 20upx 0;
		border-radius: 8upx;
	}

	/* 图片载入替代方案 */
	.image-wrapper {
		font-size: 0;
		background: #f3f3f3;
		border-radius: 4px;

		image {
			width: 100%;
			height: 100%;
			transition: .6s;
			opacity: 0;

			&.loaded {
				opacity: 1;
			}
		}
	}

	.clamp {
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		display: block;
	}

	.common-hover {
		background: #f5f5f5;
	}

	/*边框*/
	.b-b:after,
	.b-t:after {
		position: absolute;
		z-index: 3;
		left: 0;
		right: 0;
		height: 0;
		content: '';
		transform: scaleY(.5);
		border-bottom: 1px solid $border-color-base;
	}

	.b-b:after {
		bottom: 0;
	}

	.b-t:after {
		top: 0;
	}

	/* button样式改写 */
	uni-button,
	button {
		height: 80upx;
		line-height: 80upx;
		font-size: $font-lg + 2upx;
		font-weight: normal;

		&.no-border:before,
		&.no-border:after {
			border: 0;
		}
	}

	uni-button[type=default],
	button[type=default] {
		color: $font-color-dark;
	}

	/* input 样式 */
	.input-placeholder {
		color: #999999;
	}

	.placeholder {
		color: #999999;
	}

	/* 张洁 02.15 */
	/*  -- flex弹性布局 -- */

	.flex {
		display: flex;
	}

	.basis-xs {
		flex-basis: 20%;
	}

	.basis-sm {
		flex-basis: 40%;
	}

	.basis-df {
		flex-basis: 50%;
	}

	.basis-lg {
		flex-basis: 60%;
	}

	.basis-xl {
		flex-basis: 80%;
	}

	.flex-sub {
		flex: 1;
	}

	.flex-twice {
		flex: 2;
	}

	.flex-treble {
		flex: 3;
	}

	.flex-direction {
		flex-direction: column;
	}

	.flex-wrap {
		flex-wrap: wrap;
	}

	.align-start {
		align-items: flex-start;
	}

	.align-end {
		align-items: flex-end;
	}

	.align-center {
		align-items: center;
	}

	.align-stretch {
		align-items: stretch;
	}

	.self-start {
		align-self: flex-start;
	}

	.self-center {
		align-self: flex-center;
	}

	.self-end {
		align-self: flex-end;
	}

	.self-stretch {
		align-self: stretch;
	}

	.align-stretch {
		align-items: stretch;
	}

	.justify-start {
		justify-content: flex-start;
	}

	.justify-end {
		justify-content: flex-end;
	}

	.justify-center {
		justify-content: center;
	}

	.justify-between {
		justify-content: space-between;
	}

	.justify-around {
		justify-content: space-around;
	}

	/*  -- 内外边距 -- */

	.margin-0 {
		margin: 0;
	}

	.margin-xs {
		margin: 10rpx;
	}

	.margin-sm {
		margin: 20rpx;
	}

	.margin {
		margin: 30rpx;
	}

	.margin-lg {
		margin: 40rpx;
	}

	.margin-xl {
		margin: 50rpx;
	}

	.margin-top-xs {
		margin-top: 10rpx;
	}

	.margin-top-sm {
		margin-top: 20rpx;
	}

	.margin-top {
		margin-top: 30rpx;
	}

	.margin-top-lg {
		margin-top: 40rpx;
	}

	.margin-top-xl {
		margin-top: 50rpx;
	}

	.margin-right-xs {
		margin-right: 10rpx;
	}

	.margin-right-sm {
		margin-right: 20rpx;
	}

	.margin-right {
		margin-right: 30rpx;
	}

	.margin-right-lg {
		margin-right: 40rpx;
	}

	.margin-right-xl {
		margin-right: 50rpx;
	}

	.margin-bottom-xs {
		margin-bottom: 10rpx;
	}

	.margin-bottom-sm {
		margin-bottom: 20rpx;
	}

	.margin-bottom {
		margin-bottom: 30rpx;
	}

	.margin-bottom-lg {
		margin-bottom: 40rpx;
	}

	.margin-bottom-xl {
		margin-bottom: 50rpx;
	}

	.margin-left-xs {
		margin-left: 10rpx;
	}

	.margin-left-sm {
		margin-left: 20rpx;
	}

	.margin-left {
		margin-left: 30rpx;
	}

	.margin-left-lg {
		margin-left: 40rpx;
	}

	.margin-left-xl {
		margin-left: 50rpx;
	}

	.margin-lr-xs {
		margin-left: 10rpx;
		margin-right: 10rpx;
	}

	.margin-lr-sm {
		margin-left: 20rpx;
		margin-right: 20rpx;
	}

	.margin-lr {
		margin-left: 30rpx;
		margin-right: 30rpx;
	}

	.margin-lr-lg {
		margin-left: 40rpx;
		margin-right: 40rpx;
	}

	.margin-lr-xl {
		margin-left: 50rpx;
		margin-right: 50rpx;
	}

	.margin-tb-xs {
		margin-top: 10rpx;
		margin-bottom: 10rpx;
	}

	.margin-tb-sm {
		margin-top: 20rpx;
		margin-bottom: 20rpx;
	}

	.margin-tb {
		margin-top: 30rpx;
		margin-bottom: 30rpx;
	}

	.margin-tb-lg {
		margin-top: 40rpx;
		margin-bottom: 40rpx;
	}

	.margin-tb-xl {
		margin-top: 50rpx;
		margin-bottom: 50rpx;
	}

	.padding-0 {
		padding: 0;
	}

	.padding-xs {
		padding: 10rpx;
	}

	.padding-sm {
		padding: 20rpx;
	}

	.padding {
		padding: 30rpx;
	}

	.padding-lg {
		padding: 40rpx;
	}

	.padding-xl {
		padding: 50rpx;
	}

	.padding-top-xs {
		padding-top: 10rpx;
	}

	.padding-top-sm {
		padding-top: 20rpx;
	}

	.padding-top {
		padding-top: 30rpx;
	}

	.padding-top-lg {
		padding-top: 40rpx;
	}

	.padding-top-xl {
		padding-top: 50rpx;
	}

	.padding-right-xs {
		padding-right: 10rpx;
	}

	.padding-right-sm {
		padding-right: 20rpx;
	}

	.padding-right {
		padding-right: 30rpx;
	}

	.padding-right-lg {
		padding-right: 40rpx;
	}

	.padding-right-xl {
		padding-right: 50rpx;
	}

	.padding-bottom-xs {
		padding-bottom: 10rpx;
	}

	.padding-bottom-sm {
		padding-bottom: 20rpx;
	}

	.padding-bottom {
		padding-bottom: 30rpx;
	}

	.padding-bottom-lg {
		padding-bottom: 40rpx;
	}

	.padding-bottom-xl {
		padding-bottom: 50rpx;
	}

	.padding-left-xs {
		padding-left: 10rpx;
	}

	.padding-left-sm {
		padding-left: 20rpx;
	}

	.padding-left {
		padding-left: 30rpx;
	}

	.padding-left-lg {
		padding-left: 40rpx;
	}

	.padding-left-xl {
		padding-left: 50rpx;
	}

	.padding-lr-xs {
		padding-left: 10rpx;
		padding-right: 10rpx;
	}

	.padding-lr-sm {
		padding-left: 20rpx;
		padding-right: 20rpx;
	}

	.padding-lr {
		padding-left: 30rpx;
		padding-right: 30rpx;
	}

	.padding-lr-lg {
		padding-left: 40rpx;
		padding-right: 40rpx;
	}

	.padding-lr-xl {
		padding-left: 50rpx;
		padding-right: 50rpx;
	}

	.padding-tb-xs {
		padding-top: 10rpx;
		padding-bottom: 10rpx;
	}

	.padding-tb-sm {
		padding-top: 20rpx;
		padding-bottom: 20rpx;
	}

	.padding-tb {
		padding-top: 30rpx;
		padding-bottom: 30rpx;
	}

	.padding-tb-lg {
		padding-top: 40rpx;
		padding-bottom: 40rpx;
	}

	.padding-tb-xl {
		padding-top: 50rpx;
		padding-bottom: 50rpx;
	}

	/* 文字颜色 */
	.text-red,
	.line-red,
	.lines-red {
		color: #e54d42;
	}

	.text-orange,
	.line-orange,
	.lines-orange {
		color: #fdaa00;
	}

	.text-yellow,
	.line-yellow,
	.lines-yellow {
		color: #ffe300;
	}

	.text-olive,
	.line-olive,
	.lines-olive {
		color: #8dc63f;
	}

	.text-green,
	.line-green,
	.lines-green {
		color: #39b54a;
	}

	.text-cyan,
	.line-cyan,
	.lines-cyan {
		color: #1cbbb4;
	}

	.text-blue,
	.line-blue,
	.lines-blue {
		color: #0081ff;
	}

	.text-purple,
	.line-purple,
	.lines-purple {
		color: #6739b6;
	}

	.text-mauve,
	.line-mauve,
	.lines-mauve {
		color: #9c26b0;
	}

	.text-pink,
	.line-pink,
	.lines-pink {
		color: #e03997;
	}

	.text-brown,
	.line-brown,
	.lines-brown {
		color: #a5673f;
	}

	.text-grey,
	.line-grey,
	.lines-grey {
		color: #8799a3;
	}

	.text-gray,
	.line-gray,
	.lines-gray {
		color: #aaa;
	}

	.text-black,
	.line-black,
	.lines-black {
		color: #333;
	}

	.text-white,
	.line-white,
	.lines-white {
		color: #fff;
	}

	/* 文字大小 */

	.text-xs {
		font-size: 20rpx;
	}

	.text-sm {
		font-size: 24rpx;
	}

	.text-df {
		font-size: 28rpx;
	}

	.text-lg {
		font-size: 32rpx;
	}

	.text-xl {
		font-size: 36rpx;
	}

	.text-xxl {
		font-size: 44rpx;
	}

	.text-sl {
		font-size: 80rpx;
	}

	.text-xsl {
		font-size: 120rpx;
	}

	.round {
		border-radius: 5000rpx;
	}

	.radius {
		border-radius: 15rpx;
	}

	/* 文字截断 */
	.text-cut {
		text-overflow: ellipsis;
		white-space: nowrap;
		overflow: hidden;
	}

	.bg-red {
		background-color: #e54d42;
		color: #fff;
	}

	.bg-orange {
		background-color: #fdb012;
		color: #fff;
	}

	.bg-yellow {
		background-color: #ffe300;
		color: #333;
	}

	.bg-olive {
		background-color: #8dc63f;
		color: #fff;
	}

	.bg-green {
		background-color: #39b54a;
		color: #fff;
	}

	.bg-cyan {
		background-color: #1cbbb4;
		color: #fff;
	}

	.bg-blue {
		background-color: #0081ff;
		color: #fff;
	}

	.bg-purple {
		background-color: #6739b6;
		color: #fff;
	}

	.bg-mauve {
		background-color: #9c26b0;
		color: #fff;
	}

	.bg-pink {
		background-color: #e03997;
		color: #fff;
	}

	.bg-brown {
		background-color: #a5673f;
		color: #fff;
	}

	.bg-grey {
		background-color: #8799a3;
		color: #fff;
	}

	.bg-gray {
		background-color: #f0f0f0;
		color: #666;
	}

	.bg-black {
		background-color: #333;
		color: #fff;
	}

	.bg-white {
		background-color: #fff;
		/* color: #666; */
	}

	.bg-gradual-green {
		background-image: linear-gradient(45deg, #39b54a, #8dc63f);
		color: #fff;
	}

	.text-2-cut {
		display: -webkit-box;

		-webkit-box-orient: vertical;

		-webkit-line-clamp: 2;

		overflow: hidden;
	}

	.solid-bottom {
		border-bottom: 1rpx solid rgba(0, 0, 0, 0.1);
	}
</style>