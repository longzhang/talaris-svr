package me.ele.talaris.hermes.client;

/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

/**
 * Exceptions
 */
public enum HermesErrorCode implements org.apache.thrift.TEnum {
	UNKNOWN_ERROR(0), NOT_IMPLEMENTED(1), INVALID_ARGUMENTS(2), TASK_NOT_FOUND(
			3), TASK_CREATION_FAILED(4), INVALID_PARAMS_ON_TASK_CREATION(5), SENDER_CREATION_FAILED(
			6), TEMPLATE_CREATION_FAILED(7), GET_TEMPLATE_VERIFY_STATUS_FAILED(
			8), QUERY_STATUS_FAILED(9), QUERY_USER_REPLY_FAILED(10), VERIFY_CODE_CREATION_FAILED(
			11), VERIFY_CODE_VALIDATE_FAILED(12), INVALID_PHONE_NUMBER(13), SMS_TEMPLATE_NOT_FOUND(
			14), VERIFY_CODE_SEND_LIMIT_REACHED(15), SENDER_NOT_FOUND(16);

	private final int value;

	private HermesErrorCode(int value) {
		this.value = value;
	}

	/**
	 * Get the integer value of this enum value, as defined in the Thrift IDL.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Find a the enum type by its integer value, as defined in the Thrift IDL.
	 * 
	 * @return null if the value is not found.
	 */
	public static HermesErrorCode findByValue(int value) {
		switch (value) {
		case 0:
			return UNKNOWN_ERROR;
		case 1:
			return NOT_IMPLEMENTED;
		case 2:
			return INVALID_ARGUMENTS;
		case 3:
			return TASK_NOT_FOUND;
		case 4:
			return TASK_CREATION_FAILED;
		case 5:
			return INVALID_PARAMS_ON_TASK_CREATION;
		case 6:
			return SENDER_CREATION_FAILED;
		case 7:
			return TEMPLATE_CREATION_FAILED;
		case 8:
			return GET_TEMPLATE_VERIFY_STATUS_FAILED;
		case 9:
			return QUERY_STATUS_FAILED;
		case 10:
			return QUERY_USER_REPLY_FAILED;
		case 11:
			return VERIFY_CODE_CREATION_FAILED;
		case 12:
			return VERIFY_CODE_VALIDATE_FAILED;
		case 13:
			return INVALID_PHONE_NUMBER;
		case 14:
			return SMS_TEMPLATE_NOT_FOUND;
		case 15:
			return VERIFY_CODE_SEND_LIMIT_REACHED;
		case 16:
			return SENDER_NOT_FOUND;
		default:
			return null;
		}
	}
}
