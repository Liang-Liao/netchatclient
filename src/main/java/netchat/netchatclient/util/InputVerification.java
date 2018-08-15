package netchat.netchatclient.util;

public class InputVerification {
	// 账号验证
	public static boolean accountVerfication(String account) {
		if (null == account || "".equals(account)) {
			return false;
		}
		return true;
	}

	// 密码验证
	public static boolean passwordVerfication(String password) {
		if (null == password || "".equals(password)) {
			return false;
		}
		return true;
	}

	// 昵称验证
	public static boolean usernameVerfication(String username) {
		if (null == username || "".equals(username)) {
			return false;
		}
		return true;
	}
}
