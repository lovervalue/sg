package com.sofn.core.config;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 加载配置
 * 
 * @author sofn
 * @version 2016年5月20日 下午3:19:19
 */
@PropertySource(value = {
		"classpath:config/sftp.properties", "classpath:config/email.properties",
		"classpath:config/thirdParty.properties", "classpath:i18n/messages*.properties" })
public final class Resources {
//	/** SSH服务器配置 */
//	public static final ResourceBundle SFTP = ResourceBundle.getBundle("config/sftp");
//	/** 邮箱服务器配置 */
//	public static final ResourceBundle EMAIL = ResourceBundle.getBundle("config/email");
	/** 第三方登录配置 */
	public static final ResourceBundle THIRDPARTY = ResourceBundle.getBundle("config/thirdParty");
	/** 国际化信息 */
	private static final Map<String, ResourceBundle> MESSAGES = new HashMap<String, ResourceBundle>();

	/** 国际化信息 */
	public static String getMessage(String key, Object... params) {
		Locale locale = LocaleContextHolder.getLocale();
		ResourceBundle message = MESSAGES.get(locale.getLanguage());
		if (message == null) {
			synchronized (MESSAGES) {
				message = MESSAGES.get(locale.getLanguage());
				if (message == null) {
					message = ResourceBundle.getBundle("i18n/messages", locale);
					MESSAGES.put(locale.getLanguage(), message);
				}
			}
		}
		if (params != null && params.length > 0) {
			return String.format(message.getString(key), params);
		}
		return message.getString(key);
	}

	/** 清除国际化信息 */
	public static void flushMessage() {
		MESSAGES.clear();
	}
}
