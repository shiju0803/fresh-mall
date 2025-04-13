package com.zzq.common.constant;

/**
 * 商城统一常量
 */
public interface ShopConstants {

	/**
	 * 订单自动取消时间（分钟）
	 */
	long ORDER_OUTTIME_UNPAY = 30;
	/**
	 * 订单自动收货时间（天）
	 */
	long ORDER_OUTTIME_UNCONFIRM = 7;
	/**
	 * redis订单未付款key
	 */
	String REDIS_ORDER_OUTTIME_UNPAY = "order:unpay:";
	/**
	 * redis订单收货key
	 */
	String REDIS_ORDER_OUTTIME_UNCONFIRM = "order:unconfirm:";

	/**
	 * redis拼团key
	 */
	String REDIS_PINK_CANCEL_KEY = "pink:cancel:";

	/**
	 * 微信支付service
	 */
	String FRESH_MALLS_WEIXIN_PAY_SERVICE = "fresh_mall_weixin_pay_service";

	/**
	 * 微信支付小程序service
	 */
	String FRESH_MALLS_WEIXIN_MINI_PAY_SERVICE = "fresh_mall_weixin_mini_pay_service";

	/**
	 * 微信支付app service
	 */
	String FRESH_MALLS_WEIXIN_APP_PAY_SERVICE = "fresh_mall_weixin_app_pay_service";

	/**
	 * 微信公众号service
	 */
	String FRESH_MALLS_WEIXIN_MP_SERVICE = "fresh_mall_weixin_mp_service";
	/**
	 * 微信小程序service
	 */
	String FRESH_MALLS_WEIXIN_MA_SERVICE = "fresh_mall_weixin_ma_service";

	/**
	 * 商城默认密码
	 */
	String FRESH_MALLS_DEFAULT_PWD = "123456";

	/**
	 * 商城默认注册图片
	 */
	String FRESH_MALLS_DEFAULT_AVATAR = "https://image.dayouqiantu.cn/5e79f6cfd33b6.png";

	/**
	 * 腾讯地图地址解析
	 */
	String QQ_MAP_URL = "https://apis.map.qq.com/ws/geocoder/v1/";

	/**
	 * redis首页键
	 */
	String WMHOP_REDIS_INDEX_KEY = "fresh_mall:index_data";

	/**
	 * 配置列表缓存
	 */
	String FRESH_MALLS_REDIS_CONFIG_DATAS = "fresh_mall:config_datas";

	/**
	 * 充值方案
	 */
	String FRESH_MALLS_RECHARGE_PRICE_WAYS = "fresh_mall_recharge_price_ways";
	/**
	 * 首页banner
	 */
	String FRESH_MALLS_HOME_BANNER = "fresh_mall_home_banner";
	/**
	 * 首页菜单
	 */
	String FRESH_MALLS_HOME_MENUS = "fresh_mall_home_menus";
	/**
	 * 首页滚动新闻
	 */
	String FRESH_MALLS_HOME_ROLL_NEWS = "fresh_mall_home_roll_news";
	/**
	 * 热门搜索
	 */
	String FRESH_MALLS_HOT_SEARCH = "fresh_mall_hot_search";
	/**
	 * 个人中心菜单
	 */
	String FRESH_MALLS_MY_MENUES = "fresh_mall_my_menus";
	/**
	 * 秒杀时间段
	 */
	String FRESH_MALLS_SECKILL_TIME = "fresh_mall_seckill_time";
	/**
	 * 签到天数
	 */
	String FRESH_MALLS_SIGN_DAY_NUM = "fresh_mall_sign_day_num";

	/**
	 * 打印机配置
	 */
	String FRESH_MALLS_ORDER_PRINT_COUNT = "order_print_count";
	/**
	 * 飞蛾用户信息
	 */
	String FRESH_MALLS_FEI_E_USER = "fei_e_user";
	/**
	 * 飞蛾用户密钥
	 */
	String FRESH_MALLS_FEI_E_UKEY= "fei_e_ukey";

	/**
	 * 打印机配置
	 */
	String FRESH_MALLS_ORDER_PRINT_COUNT_DETAIL = "order_print_count_detail";

	/**
	 * 短信验证码长度
	 */
	int FRESH_MALLS_SMS_SIZE = 6;

	/**
	 * 短信缓存时间
	 */
	long FRESH_MALLS_SMS_REDIS_TIME = 600L;

	//零标识
	String FRESH_MALLS_ZERO =  "0";

	//业务标识标识
	String FRESH_MALLS_ONE =  "1";

	//目前完成任务数量是3
	int TASK_FINISH_COUNT = 3;

	int FRESH_MALLS_ONE_NUM = 1;

	String FRESH_MALLS_ORDER_CACHE_KEY = "fresh_mall:order";

	long FRESH_MALLS_ORDER_CACHE_TIME = 600L;

	String WECHAT_MENUS =  "wechat_menus";

	String FRESH_MALLS_EXPRESS_SERVICE = "fresh_mall_express_service";

	String WMSHOP_REDIS_SYS_CITY_KEY = "fresh_mall:city_list";

	String FRESH_MALLS_REDIS_CITY_KEY = "fresh_mall:city";

	String FRESH_MALLS_APP_LOGIN_USER = "app-online-token:";

	String FRESH_MALLS_WECHAT_PUSH_REMARK = "fresh_mall为您服务！";

	String DEFAULT_UNI_H5_URL = "https://h5.fresh_mall.vip";

	String FRESH_MALLS_MINI_SESSION_KET = "fresh_mall:session_key:";

	/**公众号二维码*/
	String WECHAT_FOLLOW_IMG="wechat_follow_img";
	/**后台api地址*/
	String ADMIN_API_URL="admin_api_url";
}
