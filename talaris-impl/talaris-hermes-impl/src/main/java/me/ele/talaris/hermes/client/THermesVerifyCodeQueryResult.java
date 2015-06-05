package me.ele.talaris.hermes.client;

/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.thrift.EncodingUtils;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;
import org.apache.thrift.scheme.TupleScheme;

@SuppressWarnings({ "cast", "rawtypes", "serial", "unchecked" })
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2014-12-22")
public class THermesVerifyCodeQueryResult
		implements
		org.apache.thrift.TBase<THermesVerifyCodeQueryResult, THermesVerifyCodeQueryResult._Fields>,
		java.io.Serializable, Cloneable,
		Comparable<THermesVerifyCodeQueryResult> {
	private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct(
			"THermesVerifyCodeQueryResult");

	private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"id", org.apache.thrift.protocol.TType.I64, (short) 1);
	private static final org.apache.thrift.protocol.TField RECEIVER_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"receiver", org.apache.thrift.protocol.TType.STRING, (short) 2);
	private static final org.apache.thrift.protocol.TField SENDER_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"sender", org.apache.thrift.protocol.TType.STRING, (short) 3);
	private static final org.apache.thrift.protocol.TField CODE_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"code", org.apache.thrift.protocol.TType.STRING, (short) 4);
	private static final org.apache.thrift.protocol.TField IS_VALIDATED_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"is_validated", org.apache.thrift.protocol.TType.BOOL, (short) 5);
	private static final org.apache.thrift.protocol.TField CREATE_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"create_time", org.apache.thrift.protocol.TType.I64, (short) 6);
	private static final org.apache.thrift.protocol.TField VIA_AUDIO_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"via_audio", org.apache.thrift.protocol.TType.BOOL, (short) 7);

	private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
	static {
		schemes.put(StandardScheme.class,
				new THermesVerifyCodeQueryResultStandardSchemeFactory());
		schemes.put(TupleScheme.class,
				new THermesVerifyCodeQueryResultTupleSchemeFactory());
	}

	public long id; // required
	public String receiver; // required
	public String sender; // required
	public String code; // required
	public boolean is_validated; // required
	public long create_time; // required
	public boolean via_audio; // required

	/**
	 * The set of fields this struct contains, along with convenience methods
	 * for finding and manipulating them.
	 */
	public enum _Fields implements org.apache.thrift.TFieldIdEnum {
		ID((short) 1, "id"), RECEIVER((short) 2, "receiver"), SENDER((short) 3,
				"sender"), CODE((short) 4, "code"), IS_VALIDATED((short) 5,
				"is_validated"), CREATE_TIME((short) 6, "create_time"), VIA_AUDIO(
				(short) 7, "via_audio");

		private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

		static {
			for (_Fields field : EnumSet.allOf(_Fields.class)) {
				byName.put(field.getFieldName(), field);
			}
		}

		/**
		 * Find the _Fields constant that matches fieldId, or null if its not
		 * found.
		 */
		public static _Fields findByThriftId(int fieldId) {
			switch (fieldId) {
			case 1: // ID
				return ID;
			case 2: // RECEIVER
				return RECEIVER;
			case 3: // SENDER
				return SENDER;
			case 4: // CODE
				return CODE;
			case 5: // IS_VALIDATED
				return IS_VALIDATED;
			case 6: // CREATE_TIME
				return CREATE_TIME;
			case 7: // VIA_AUDIO
				return VIA_AUDIO;
			default:
				return null;
			}
		}

		/**
		 * Find the _Fields constant that matches fieldId, throwing an exception
		 * if it is not found.
		 */
		public static _Fields findByThriftIdOrThrow(int fieldId) {
			_Fields fields = findByThriftId(fieldId);
			if (fields == null)
				throw new IllegalArgumentException("Field " + fieldId
						+ " doesn't exist!");
			return fields;
		}

		/**
		 * Find the _Fields constant that matches name, or null if its not
		 * found.
		 */
		public static _Fields findByName(String name) {
			return byName.get(name);
		}

		private final short _thriftId;
		private final String _fieldName;

		_Fields(short thriftId, String fieldName) {
			_thriftId = thriftId;
			_fieldName = fieldName;
		}

		public short getThriftFieldId() {
			return _thriftId;
		}

		public String getFieldName() {
			return _fieldName;
		}
	}

	// isset id assignments
	private static final int __ID_ISSET_ID = 0;
	private static final int __IS_VALIDATED_ISSET_ID = 1;
	private static final int __CREATE_TIME_ISSET_ID = 2;
	private static final int __VIA_AUDIO_ISSET_ID = 3;
	private byte __isset_bitfield = 0;
	public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
	static {
		Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(
				_Fields.class);
		tmpMap.put(_Fields.ID, new org.apache.thrift.meta_data.FieldMetaData(
				"id", org.apache.thrift.TFieldRequirementType.REQUIRED,
				new org.apache.thrift.meta_data.FieldValueMetaData(
						org.apache.thrift.protocol.TType.I64)));
		tmpMap.put(_Fields.RECEIVER,
				new org.apache.thrift.meta_data.FieldMetaData("receiver",
						org.apache.thrift.TFieldRequirementType.REQUIRED,
						new org.apache.thrift.meta_data.FieldValueMetaData(
								org.apache.thrift.protocol.TType.STRING)));
		tmpMap.put(_Fields.SENDER,
				new org.apache.thrift.meta_data.FieldMetaData("sender",
						org.apache.thrift.TFieldRequirementType.REQUIRED,
						new org.apache.thrift.meta_data.FieldValueMetaData(
								org.apache.thrift.protocol.TType.STRING)));
		tmpMap.put(_Fields.CODE, new org.apache.thrift.meta_data.FieldMetaData(
				"code", org.apache.thrift.TFieldRequirementType.REQUIRED,
				new org.apache.thrift.meta_data.FieldValueMetaData(
						org.apache.thrift.protocol.TType.STRING)));
		tmpMap.put(_Fields.IS_VALIDATED,
				new org.apache.thrift.meta_data.FieldMetaData("is_validated",
						org.apache.thrift.TFieldRequirementType.REQUIRED,
						new org.apache.thrift.meta_data.FieldValueMetaData(
								org.apache.thrift.protocol.TType.BOOL)));
		tmpMap.put(_Fields.CREATE_TIME,
				new org.apache.thrift.meta_data.FieldMetaData("create_time",
						org.apache.thrift.TFieldRequirementType.REQUIRED,
						new org.apache.thrift.meta_data.FieldValueMetaData(
								org.apache.thrift.protocol.TType.I64,
								"Timestamp")));
		tmpMap.put(_Fields.VIA_AUDIO,
				new org.apache.thrift.meta_data.FieldMetaData("via_audio",
						org.apache.thrift.TFieldRequirementType.REQUIRED,
						new org.apache.thrift.meta_data.FieldValueMetaData(
								org.apache.thrift.protocol.TType.BOOL)));
		metaDataMap = Collections.unmodifiableMap(tmpMap);
		org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(
				THermesVerifyCodeQueryResult.class, metaDataMap);
	}

	public THermesVerifyCodeQueryResult() {
	}

	public THermesVerifyCodeQueryResult(long id, String receiver,
			String sender, String code, boolean is_validated, long create_time,
			boolean via_audio) {
		this();
		this.id = id;
		setIdIsSet(true);
		this.receiver = receiver;
		this.sender = sender;
		this.code = code;
		this.is_validated = is_validated;
		setIs_validatedIsSet(true);
		this.create_time = create_time;
		setCreate_timeIsSet(true);
		this.via_audio = via_audio;
		setVia_audioIsSet(true);
	}

	/**
	 * Performs a deep copy on <i>other</i>.
	 */
	public THermesVerifyCodeQueryResult(THermesVerifyCodeQueryResult other) {
		__isset_bitfield = other.__isset_bitfield;
		this.id = other.id;
		if (other.isSetReceiver()) {
			this.receiver = other.receiver;
		}
		if (other.isSetSender()) {
			this.sender = other.sender;
		}
		if (other.isSetCode()) {
			this.code = other.code;
		}
		this.is_validated = other.is_validated;
		this.create_time = other.create_time;
		this.via_audio = other.via_audio;
	}

	public THermesVerifyCodeQueryResult deepCopy() {
		return new THermesVerifyCodeQueryResult(this);
	}

	public void clear() {
		setIdIsSet(false);
		this.id = 0;
		this.receiver = null;
		this.sender = null;
		this.code = null;
		setIs_validatedIsSet(false);
		this.is_validated = false;
		setCreate_timeIsSet(false);
		this.create_time = 0;
		setVia_audioIsSet(false);
		this.via_audio = false;
	}

	public long getId() {
		return this.id;
	}

	public THermesVerifyCodeQueryResult setId(long id) {
		this.id = id;
		setIdIsSet(true);
		return this;
	}

	public void unsetId() {
		__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield,
				__ID_ISSET_ID);
	}

	/**
	 * Returns true if field id is set (has been assigned a value) and false
	 * otherwise
	 */
	public boolean isSetId() {
		return EncodingUtils.testBit(__isset_bitfield, __ID_ISSET_ID);
	}

	public void setIdIsSet(boolean value) {
		__isset_bitfield = EncodingUtils.setBit(__isset_bitfield,
				__ID_ISSET_ID, value);
	}

	public String getReceiver() {
		return this.receiver;
	}

	public THermesVerifyCodeQueryResult setReceiver(String receiver) {
		this.receiver = receiver;
		return this;
	}

	public void unsetReceiver() {
		this.receiver = null;
	}

	/**
	 * Returns true if field receiver is set (has been assigned a value) and
	 * false otherwise
	 */
	public boolean isSetReceiver() {
		return this.receiver != null;
	}

	public void setReceiverIsSet(boolean value) {
		if (!value) {
			this.receiver = null;
		}
	}

	public String getSender() {
		return this.sender;
	}

	public THermesVerifyCodeQueryResult setSender(String sender) {
		this.sender = sender;
		return this;
	}

	public void unsetSender() {
		this.sender = null;
	}

	/**
	 * Returns true if field sender is set (has been assigned a value) and false
	 * otherwise
	 */
	public boolean isSetSender() {
		return this.sender != null;
	}

	public void setSenderIsSet(boolean value) {
		if (!value) {
			this.sender = null;
		}
	}

	public String getCode() {
		return this.code;
	}

	public THermesVerifyCodeQueryResult setCode(String code) {
		this.code = code;
		return this;
	}

	public void unsetCode() {
		this.code = null;
	}

	/**
	 * Returns true if field code is set (has been assigned a value) and false
	 * otherwise
	 */
	public boolean isSetCode() {
		return this.code != null;
	}

	public void setCodeIsSet(boolean value) {
		if (!value) {
			this.code = null;
		}
	}

	public boolean isIs_validated() {
		return this.is_validated;
	}

	public THermesVerifyCodeQueryResult setIs_validated(boolean is_validated) {
		this.is_validated = is_validated;
		setIs_validatedIsSet(true);
		return this;
	}

	public void unsetIs_validated() {
		__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield,
				__IS_VALIDATED_ISSET_ID);
	}

	/**
	 * Returns true if field is_validated is set (has been assigned a value) and
	 * false otherwise
	 */
	public boolean isSetIs_validated() {
		return EncodingUtils.testBit(__isset_bitfield, __IS_VALIDATED_ISSET_ID);
	}

	public void setIs_validatedIsSet(boolean value) {
		__isset_bitfield = EncodingUtils.setBit(__isset_bitfield,
				__IS_VALIDATED_ISSET_ID, value);
	}

	public long getCreate_time() {
		return this.create_time;
	}

	public THermesVerifyCodeQueryResult setCreate_time(long create_time) {
		this.create_time = create_time;
		setCreate_timeIsSet(true);
		return this;
	}

	public void unsetCreate_time() {
		__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield,
				__CREATE_TIME_ISSET_ID);
	}

	/**
	 * Returns true if field create_time is set (has been assigned a value) and
	 * false otherwise
	 */
	public boolean isSetCreate_time() {
		return EncodingUtils.testBit(__isset_bitfield, __CREATE_TIME_ISSET_ID);
	}

	public void setCreate_timeIsSet(boolean value) {
		__isset_bitfield = EncodingUtils.setBit(__isset_bitfield,
				__CREATE_TIME_ISSET_ID, value);
	}

	public boolean isVia_audio() {
		return this.via_audio;
	}

	public THermesVerifyCodeQueryResult setVia_audio(boolean via_audio) {
		this.via_audio = via_audio;
		setVia_audioIsSet(true);
		return this;
	}

	public void unsetVia_audio() {
		__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield,
				__VIA_AUDIO_ISSET_ID);
	}

	/**
	 * Returns true if field via_audio is set (has been assigned a value) and
	 * false otherwise
	 */
	public boolean isSetVia_audio() {
		return EncodingUtils.testBit(__isset_bitfield, __VIA_AUDIO_ISSET_ID);
	}

	public void setVia_audioIsSet(boolean value) {
		__isset_bitfield = EncodingUtils.setBit(__isset_bitfield,
				__VIA_AUDIO_ISSET_ID, value);
	}

	public void setFieldValue(_Fields field, Object value) {
		switch (field) {
		case ID:
			if (value == null) {
				unsetId();
			} else {
				setId((Long) value);
			}
			break;

		case RECEIVER:
			if (value == null) {
				unsetReceiver();
			} else {
				setReceiver((String) value);
			}
			break;

		case SENDER:
			if (value == null) {
				unsetSender();
			} else {
				setSender((String) value);
			}
			break;

		case CODE:
			if (value == null) {
				unsetCode();
			} else {
				setCode((String) value);
			}
			break;

		case IS_VALIDATED:
			if (value == null) {
				unsetIs_validated();
			} else {
				setIs_validated((Boolean) value);
			}
			break;

		case CREATE_TIME:
			if (value == null) {
				unsetCreate_time();
			} else {
				setCreate_time((Long) value);
			}
			break;

		case VIA_AUDIO:
			if (value == null) {
				unsetVia_audio();
			} else {
				setVia_audio((Boolean) value);
			}
			break;

		}
	}

	public Object getFieldValue(_Fields field) {
		switch (field) {
		case ID:
			return Long.valueOf(getId());

		case RECEIVER:
			return getReceiver();

		case SENDER:
			return getSender();

		case CODE:
			return getCode();

		case IS_VALIDATED:
			return Boolean.valueOf(isIs_validated());

		case CREATE_TIME:
			return Long.valueOf(getCreate_time());

		case VIA_AUDIO:
			return Boolean.valueOf(isVia_audio());

		}
		throw new IllegalStateException();
	}

	/**
	 * Returns true if field corresponding to fieldID is set (has been assigned
	 * a value) and false otherwise
	 */
	public boolean isSet(_Fields field) {
		if (field == null) {
			throw new IllegalArgumentException();
		}

		switch (field) {
		case ID:
			return isSetId();
		case RECEIVER:
			return isSetReceiver();
		case SENDER:
			return isSetSender();
		case CODE:
			return isSetCode();
		case IS_VALIDATED:
			return isSetIs_validated();
		case CREATE_TIME:
			return isSetCreate_time();
		case VIA_AUDIO:
			return isSetVia_audio();
		}
		throw new IllegalStateException();
	}

	public boolean equals(Object that) {
		if (that == null)
			return false;
		if (that instanceof THermesVerifyCodeQueryResult)
			return this.equals((THermesVerifyCodeQueryResult) that);
		return false;
	}

	public boolean equals(THermesVerifyCodeQueryResult that) {
		if (that == null)
			return false;

		boolean this_present_id = true;
		boolean that_present_id = true;
		if (this_present_id || that_present_id) {
			if (!(this_present_id && that_present_id))
				return false;
			if (this.id != that.id)
				return false;
		}

		boolean this_present_receiver = true && this.isSetReceiver();
		boolean that_present_receiver = true && that.isSetReceiver();
		if (this_present_receiver || that_present_receiver) {
			if (!(this_present_receiver && that_present_receiver))
				return false;
			if (!this.receiver.equals(that.receiver))
				return false;
		}

		boolean this_present_sender = true && this.isSetSender();
		boolean that_present_sender = true && that.isSetSender();
		if (this_present_sender || that_present_sender) {
			if (!(this_present_sender && that_present_sender))
				return false;
			if (!this.sender.equals(that.sender))
				return false;
		}

		boolean this_present_code = true && this.isSetCode();
		boolean that_present_code = true && that.isSetCode();
		if (this_present_code || that_present_code) {
			if (!(this_present_code && that_present_code))
				return false;
			if (!this.code.equals(that.code))
				return false;
		}

		boolean this_present_is_validated = true;
		boolean that_present_is_validated = true;
		if (this_present_is_validated || that_present_is_validated) {
			if (!(this_present_is_validated && that_present_is_validated))
				return false;
			if (this.is_validated != that.is_validated)
				return false;
		}

		boolean this_present_create_time = true;
		boolean that_present_create_time = true;
		if (this_present_create_time || that_present_create_time) {
			if (!(this_present_create_time && that_present_create_time))
				return false;
			if (this.create_time != that.create_time)
				return false;
		}

		boolean this_present_via_audio = true;
		boolean that_present_via_audio = true;
		if (this_present_via_audio || that_present_via_audio) {
			if (!(this_present_via_audio && that_present_via_audio))
				return false;
			if (this.via_audio != that.via_audio)
				return false;
		}

		return true;
	}

	public int hashCode() {
		List<Object> list = new ArrayList<Object>();

		boolean present_id = true;
		list.add(present_id);
		if (present_id)
			list.add(id);

		boolean present_receiver = true && (isSetReceiver());
		list.add(present_receiver);
		if (present_receiver)
			list.add(receiver);

		boolean present_sender = true && (isSetSender());
		list.add(present_sender);
		if (present_sender)
			list.add(sender);

		boolean present_code = true && (isSetCode());
		list.add(present_code);
		if (present_code)
			list.add(code);

		boolean present_is_validated = true;
		list.add(present_is_validated);
		if (present_is_validated)
			list.add(is_validated);

		boolean present_create_time = true;
		list.add(present_create_time);
		if (present_create_time)
			list.add(create_time);

		boolean present_via_audio = true;
		list.add(present_via_audio);
		if (present_via_audio)
			list.add(via_audio);

		return list.hashCode();
	}

	public int compareTo(THermesVerifyCodeQueryResult other) {
		if (!getClass().equals(other.getClass())) {
			return getClass().getName().compareTo(other.getClass().getName());
		}

		int lastComparison = 0;

		lastComparison = Boolean.valueOf(isSetId()).compareTo(other.isSetId());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetId()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.id,
					other.id);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetReceiver()).compareTo(
				other.isSetReceiver());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetReceiver()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(
					this.receiver, other.receiver);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetSender()).compareTo(
				other.isSetSender());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetSender()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(
					this.sender, other.sender);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetCode()).compareTo(
				other.isSetCode());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetCode()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.code,
					other.code);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetIs_validated()).compareTo(
				other.isSetIs_validated());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetIs_validated()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(
					this.is_validated, other.is_validated);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetCreate_time()).compareTo(
				other.isSetCreate_time());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetCreate_time()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(
					this.create_time, other.create_time);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetVia_audio()).compareTo(
				other.isSetVia_audio());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetVia_audio()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(
					this.via_audio, other.via_audio);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		return 0;
	}

	public _Fields fieldForId(int fieldId) {
		return _Fields.findByThriftId(fieldId);
	}

	public void read(org.apache.thrift.protocol.TProtocol iprot)
			throws org.apache.thrift.TException {
		schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
	}

	public void write(org.apache.thrift.protocol.TProtocol oprot)
			throws org.apache.thrift.TException {
		schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("THermesVerifyCodeQueryResult(");
		boolean first = true;

		sb.append("id:");
		sb.append(this.id);
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("receiver:");
		if (this.receiver == null) {
			sb.append("null");
		} else {
			sb.append(this.receiver);
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("sender:");
		if (this.sender == null) {
			sb.append("null");
		} else {
			sb.append(this.sender);
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("code:");
		if (this.code == null) {
			sb.append("null");
		} else {
			sb.append(this.code);
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("is_validated:");
		sb.append(this.is_validated);
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("create_time:");
		sb.append(this.create_time);
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("via_audio:");
		sb.append(this.via_audio);
		first = false;
		sb.append(")");
		return sb.toString();
	}

	public void validate() throws org.apache.thrift.TException {
		// check for required fields
		// alas, we cannot check 'id' because it's a primitive and you chose the
		// non-beans generator.
		if (receiver == null) {
			throw new org.apache.thrift.protocol.TProtocolException(
					"Required field 'receiver' was not present! Struct: "
							+ toString());
		}
		if (sender == null) {
			throw new org.apache.thrift.protocol.TProtocolException(
					"Required field 'sender' was not present! Struct: "
							+ toString());
		}
		if (code == null) {
			throw new org.apache.thrift.protocol.TProtocolException(
					"Required field 'code' was not present! Struct: "
							+ toString());
		}
		// alas, we cannot check 'is_validated' because it's a primitive and you
		// chose the non-beans generator.
		// alas, we cannot check 'create_time' because it's a primitive and you
		// chose the non-beans generator.
		// alas, we cannot check 'via_audio' because it's a primitive and you
		// chose the non-beans generator.
		// check for sub-struct validity
	}

	private void writeObject(java.io.ObjectOutputStream out)
			throws java.io.IOException {
		try {
			write(new org.apache.thrift.protocol.TCompactProtocol(
					new org.apache.thrift.transport.TIOStreamTransport(out)));
		} catch (org.apache.thrift.TException te) {
			throw new java.io.IOException(te);
		}
	}

	private void readObject(java.io.ObjectInputStream in)
			throws java.io.IOException, ClassNotFoundException {
		try {
			// it doesn't seem like you should have to do this, but java
			// serialization is wacky, and doesn't call the default constructor.
			__isset_bitfield = 0;
			read(new org.apache.thrift.protocol.TCompactProtocol(
					new org.apache.thrift.transport.TIOStreamTransport(in)));
		} catch (org.apache.thrift.TException te) {
			throw new java.io.IOException(te);
		}
	}

	private static class THermesVerifyCodeQueryResultStandardSchemeFactory
			implements SchemeFactory {
		public THermesVerifyCodeQueryResultStandardScheme getScheme() {
			return new THermesVerifyCodeQueryResultStandardScheme();
		}
	}

	private static class THermesVerifyCodeQueryResultStandardScheme extends
			StandardScheme<THermesVerifyCodeQueryResult> {

		public void read(org.apache.thrift.protocol.TProtocol iprot,
				THermesVerifyCodeQueryResult struct)
				throws org.apache.thrift.TException {
			org.apache.thrift.protocol.TField schemeField;
			iprot.readStructBegin();
			while (true) {
				schemeField = iprot.readFieldBegin();
				if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
					break;
				}
				switch (schemeField.id) {
				case 1: // ID
					if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
						struct.id = iprot.readI64();
						struct.setIdIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 2: // RECEIVER
					if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
						struct.receiver = iprot.readString();
						struct.setReceiverIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 3: // SENDER
					if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
						struct.sender = iprot.readString();
						struct.setSenderIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 4: // CODE
					if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
						struct.code = iprot.readString();
						struct.setCodeIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 5: // IS_VALIDATED
					if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
						struct.is_validated = iprot.readBool();
						struct.setIs_validatedIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 6: // CREATE_TIME
					if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
						struct.create_time = iprot.readI64();
						struct.setCreate_timeIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 7: // VIA_AUDIO
					if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
						struct.via_audio = iprot.readBool();
						struct.setVia_audioIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				default:
					org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
							schemeField.type);
				}
				iprot.readFieldEnd();
			}
			iprot.readStructEnd();

			// check for required fields of primitive type, which can't be
			// checked in the validate method
			if (!struct.isSetId()) {
				throw new org.apache.thrift.protocol.TProtocolException(
						"Required field 'id' was not found in serialized data! Struct: "
								+ toString());
			}
			if (!struct.isSetIs_validated()) {
				throw new org.apache.thrift.protocol.TProtocolException(
						"Required field 'is_validated' was not found in serialized data! Struct: "
								+ toString());
			}
			if (!struct.isSetCreate_time()) {
				throw new org.apache.thrift.protocol.TProtocolException(
						"Required field 'create_time' was not found in serialized data! Struct: "
								+ toString());
			}
			if (!struct.isSetVia_audio()) {
				throw new org.apache.thrift.protocol.TProtocolException(
						"Required field 'via_audio' was not found in serialized data! Struct: "
								+ toString());
			}
			struct.validate();
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot,
				THermesVerifyCodeQueryResult struct)
				throws org.apache.thrift.TException {
			struct.validate();

			oprot.writeStructBegin(STRUCT_DESC);
			oprot.writeFieldBegin(ID_FIELD_DESC);
			oprot.writeI64(struct.id);
			oprot.writeFieldEnd();
			if (struct.receiver != null) {
				oprot.writeFieldBegin(RECEIVER_FIELD_DESC);
				oprot.writeString(struct.receiver);
				oprot.writeFieldEnd();
			}
			if (struct.sender != null) {
				oprot.writeFieldBegin(SENDER_FIELD_DESC);
				oprot.writeString(struct.sender);
				oprot.writeFieldEnd();
			}
			if (struct.code != null) {
				oprot.writeFieldBegin(CODE_FIELD_DESC);
				oprot.writeString(struct.code);
				oprot.writeFieldEnd();
			}
			oprot.writeFieldBegin(IS_VALIDATED_FIELD_DESC);
			oprot.writeBool(struct.is_validated);
			oprot.writeFieldEnd();
			oprot.writeFieldBegin(CREATE_TIME_FIELD_DESC);
			oprot.writeI64(struct.create_time);
			oprot.writeFieldEnd();
			oprot.writeFieldBegin(VIA_AUDIO_FIELD_DESC);
			oprot.writeBool(struct.via_audio);
			oprot.writeFieldEnd();
			oprot.writeFieldStop();
			oprot.writeStructEnd();
		}

	}

	private static class THermesVerifyCodeQueryResultTupleSchemeFactory
			implements SchemeFactory {
		public THermesVerifyCodeQueryResultTupleScheme getScheme() {
			return new THermesVerifyCodeQueryResultTupleScheme();
		}
	}

	private static class THermesVerifyCodeQueryResultTupleScheme extends
			TupleScheme<THermesVerifyCodeQueryResult> {

		public void write(org.apache.thrift.protocol.TProtocol prot,
				THermesVerifyCodeQueryResult struct)
				throws org.apache.thrift.TException {
			TTupleProtocol oprot = (TTupleProtocol) prot;
			oprot.writeI64(struct.id);
			oprot.writeString(struct.receiver);
			oprot.writeString(struct.sender);
			oprot.writeString(struct.code);
			oprot.writeBool(struct.is_validated);
			oprot.writeI64(struct.create_time);
			oprot.writeBool(struct.via_audio);
		}

		public void read(org.apache.thrift.protocol.TProtocol prot,
				THermesVerifyCodeQueryResult struct)
				throws org.apache.thrift.TException {
			TTupleProtocol iprot = (TTupleProtocol) prot;
			struct.id = iprot.readI64();
			struct.setIdIsSet(true);
			struct.receiver = iprot.readString();
			struct.setReceiverIsSet(true);
			struct.sender = iprot.readString();
			struct.setSenderIsSet(true);
			struct.code = iprot.readString();
			struct.setCodeIsSet(true);
			struct.is_validated = iprot.readBool();
			struct.setIs_validatedIsSet(true);
			struct.create_time = iprot.readI64();
			struct.setCreate_timeIsSet(true);
			struct.via_audio = iprot.readBool();
			struct.setVia_audioIsSet(true);
		}
	}

}
