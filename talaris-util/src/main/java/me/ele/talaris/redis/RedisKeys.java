package me.ele.talaris.redis;

public enum RedisKeys {
	CALL_TASK_INFO_KEY("_CALL_TASK_INFO_KEY_"),
	SMS_ALARM_KEY("_SMS_ALARM_KEY_");

	private String key;

	private RedisKeys(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return this.name();
	}

}
