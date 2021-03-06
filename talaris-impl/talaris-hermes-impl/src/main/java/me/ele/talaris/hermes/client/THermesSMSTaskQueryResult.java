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
public class THermesSMSTaskQueryResult
		implements
		org.apache.thrift.TBase<THermesSMSTaskQueryResult, THermesSMSTaskQueryResult._Fields>,
		java.io.Serializable, Cloneable, Comparable<THermesSMSTaskQueryResult> {
	private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct(
			"THermesSMSTaskQueryResult");

	private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"id", org.apache.thrift.protocol.TType.I64, (short) 1);
	private static final org.apache.thrift.protocol.TField RECEIVER_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"receiver", org.apache.thrift.protocol.TType.STRING, (short) 2);
	private static final org.apache.thrift.protocol.TField SENDER_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"sender", org.apache.thrift.protocol.TType.STRING, (short) 3);
	private static final org.apache.thrift.protocol.TField MESSAGE_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"message", org.apache.thrift.protocol.TType.STRING, (short) 4);
	private static final org.apache.thrift.protocol.TField STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"status", org.apache.thrift.protocol.TType.I32, (short) 5);
	private static final org.apache.thrift.protocol.TField CREATE_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField(
			"create_time", org.apache.thrift.protocol.TType.I64, (short) 6);

	private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
	static {
		schemes.put(StandardScheme.class,
				new THermesSMSTaskQueryResultStandardSchemeFactory());
		schemes.put(TupleScheme.class,
				new THermesSMSTaskQueryResultTupleSchemeFactory());
	}

	public long id; // required
	public String receiver; // required
	public String sender; // required
	public String message; // required
	/**
	 *
	 * @see THermesTaskStatus
	 */
	public THermesTaskStatus status; // required
	public long create_time; // required

	/**
	 * The set of fields this struct contains, along with convenience methods
	 * for finding and manipulating them.
	 */
	public enum _Fields implements org.apache.thrift.TFieldIdEnum {
		ID((short) 1, "id"), RECEIVER((short) 2, "receiver"), SENDER((short) 3,
				"sender"), MESSAGE((short) 4, "message"),
		/**
		 *
		 * @see THermesTaskStatus
		 */
		STATUS((short) 5, "status"), CREATE_TIME((short) 6, "create_time");

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
			case 4: // MESSAGE
				return MESSAGE;
			case 5: // STATUS
				return STATUS;
			case 6: // CREATE_TIME
				return CREATE_TIME;
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
	private static final int __CREATE_TIME_ISSET_ID = 1;
	private byte __isset_bitfield = 0;
	public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
	static {
		Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(
				_Fields.class);
		tmpMap.put(_Fields.ID, new org.apache.thrift.meta_data.FieldMetaData(
				"id", org.apache.thrift.TFieldRequirementType.REQUIRED,
				new org.apache.thrift.meta_data.FieldValueMetaData(
						org.apache.thrift.protocol.TType.I64, "TaskId")));
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
		tmpMap.put(_Fields.MESSAGE,
				new org.apache.thrift.meta_data.FieldMetaData("message",
						org.apache.thrift.TFieldRequirementType.REQUIRED,
						new org.apache.thrift.meta_data.FieldValueMetaData(
								org.apache.thrift.protocol.TType.STRING)));
		tmpMap.put(_Fields.STATUS,
				new org.apache.thrift.meta_data.FieldMetaData("status",
						org.apache.thrift.TFieldRequirementType.REQUIRED,
						new org.apache.thrift.meta_data.EnumMetaData(
								org.apache.thrift.protocol.TType.ENUM,
								THermesTaskStatus.class)));
		tmpMap.put(_Fields.CREATE_TIME,
				new org.apache.thrift.meta_data.FieldMetaData("create_time",
						org.apache.thrift.TFieldRequirementType.REQUIRED,
						new org.apache.thrift.meta_data.FieldValueMetaData(
								org.apache.thrift.protocol.TType.I64,
								"Timestamp")));
		metaDataMap = Collections.unmodifiableMap(tmpMap);
		org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(
				THermesSMSTaskQueryResult.class, metaDataMap);
	}

	public THermesSMSTaskQueryResult() {
	}

	public THermesSMSTaskQueryResult(long id, String receiver, String sender,
			String message, THermesTaskStatus status, long create_time) {
		this();
		this.id = id;
		setIdIsSet(true);
		this.receiver = receiver;
		this.sender = sender;
		this.message = message;
		this.status = status;
		this.create_time = create_time;
		setCreate_timeIsSet(true);
	}

	/**
	 * Performs a deep copy on <i>other</i>.
	 */
	public THermesSMSTaskQueryResult(THermesSMSTaskQueryResult other) {
		__isset_bitfield = other.__isset_bitfield;
		this.id = other.id;
		if (other.isSetReceiver()) {
			this.receiver = other.receiver;
		}
		if (other.isSetSender()) {
			this.sender = other.sender;
		}
		if (other.isSetMessage()) {
			this.message = other.message;
		}
		if (other.isSetStatus()) {
			this.status = other.status;
		}
		this.create_time = other.create_time;
	}

	public THermesSMSTaskQueryResult deepCopy() {
		return new THermesSMSTaskQueryResult(this);
	}

	public void clear() {
		setIdIsSet(false);
		this.id = 0;
		this.receiver = null;
		this.sender = null;
		this.message = null;
		this.status = null;
		setCreate_timeIsSet(false);
		this.create_time = 0;
	}

	public long getId() {
		return this.id;
	}

	public THermesSMSTaskQueryResult setId(long id) {
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

	public THermesSMSTaskQueryResult setReceiver(String receiver) {
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

	public THermesSMSTaskQueryResult setSender(String sender) {
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

	public String getMessage() {
		return this.message;
	}

	public THermesSMSTaskQueryResult setMessage(String message) {
		this.message = message;
		return this;
	}

	public void unsetMessage() {
		this.message = null;
	}

	/**
	 * Returns true if field message is set (has been assigned a value) and
	 * false otherwise
	 */
	public boolean isSetMessage() {
		return this.message != null;
	}

	public void setMessageIsSet(boolean value) {
		if (!value) {
			this.message = null;
		}
	}

	/**
	 *
	 * @see THermesTaskStatus
	 */
	public THermesTaskStatus getStatus() {
		return this.status;
	}

	/**
	 *
	 * @see THermesTaskStatus
	 */
	public THermesSMSTaskQueryResult setStatus(THermesTaskStatus status) {
		this.status = status;
		return this;
	}

	public void unsetStatus() {
		this.status = null;
	}

	/**
	 * Returns true if field status is set (has been assigned a value) and false
	 * otherwise
	 */
	public boolean isSetStatus() {
		return this.status != null;
	}

	public void setStatusIsSet(boolean value) {
		if (!value) {
			this.status = null;
		}
	}

	public long getCreate_time() {
		return this.create_time;
	}

	public THermesSMSTaskQueryResult setCreate_time(long create_time) {
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

		case MESSAGE:
			if (value == null) {
				unsetMessage();
			} else {
				setMessage((String) value);
			}
			break;

		case STATUS:
			if (value == null) {
				unsetStatus();
			} else {
				setStatus((THermesTaskStatus) value);
			}
			break;

		case CREATE_TIME:
			if (value == null) {
				unsetCreate_time();
			} else {
				setCreate_time((Long) value);
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

		case MESSAGE:
			return getMessage();

		case STATUS:
			return getStatus();

		case CREATE_TIME:
			return Long.valueOf(getCreate_time());

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
		case MESSAGE:
			return isSetMessage();
		case STATUS:
			return isSetStatus();
		case CREATE_TIME:
			return isSetCreate_time();
		}
		throw new IllegalStateException();
	}

	public boolean equals(Object that) {
		if (that == null)
			return false;
		if (that instanceof THermesSMSTaskQueryResult)
			return this.equals((THermesSMSTaskQueryResult) that);
		return false;
	}

	public boolean equals(THermesSMSTaskQueryResult that) {
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

		boolean this_present_message = true && this.isSetMessage();
		boolean that_present_message = true && that.isSetMessage();
		if (this_present_message || that_present_message) {
			if (!(this_present_message && that_present_message))
				return false;
			if (!this.message.equals(that.message))
				return false;
		}

		boolean this_present_status = true && this.isSetStatus();
		boolean that_present_status = true && that.isSetStatus();
		if (this_present_status || that_present_status) {
			if (!(this_present_status && that_present_status))
				return false;
			if (!this.status.equals(that.status))
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

		boolean present_message = true && (isSetMessage());
		list.add(present_message);
		if (present_message)
			list.add(message);

		boolean present_status = true && (isSetStatus());
		list.add(present_status);
		if (present_status)
			list.add(status.getValue());

		boolean present_create_time = true;
		list.add(present_create_time);
		if (present_create_time)
			list.add(create_time);

		return list.hashCode();
	}

	public int compareTo(THermesSMSTaskQueryResult other) {
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
		lastComparison = Boolean.valueOf(isSetMessage()).compareTo(
				other.isSetMessage());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetMessage()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(
					this.message, other.message);
			if (lastComparison != 0) {
				return lastComparison;
			}
		}
		lastComparison = Boolean.valueOf(isSetStatus()).compareTo(
				other.isSetStatus());
		if (lastComparison != 0) {
			return lastComparison;
		}
		if (isSetStatus()) {
			lastComparison = org.apache.thrift.TBaseHelper.compareTo(
					this.status, other.status);
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
		StringBuilder sb = new StringBuilder("THermesSMSTaskQueryResult(");
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
		sb.append("message:");
		if (this.message == null) {
			sb.append("null");
		} else {
			sb.append(this.message);
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("status:");
		if (this.status == null) {
			sb.append("null");
		} else {
			sb.append(this.status);
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("create_time:");
		sb.append(this.create_time);
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
		if (message == null) {
			throw new org.apache.thrift.protocol.TProtocolException(
					"Required field 'message' was not present! Struct: "
							+ toString());
		}
		if (status == null) {
			throw new org.apache.thrift.protocol.TProtocolException(
					"Required field 'status' was not present! Struct: "
							+ toString());
		}
		// alas, we cannot check 'create_time' because it's a primitive and you
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

	private static class THermesSMSTaskQueryResultStandardSchemeFactory
			implements SchemeFactory {
		public THermesSMSTaskQueryResultStandardScheme getScheme() {
			return new THermesSMSTaskQueryResultStandardScheme();
		}
	}

	private static class THermesSMSTaskQueryResultStandardScheme extends
			StandardScheme<THermesSMSTaskQueryResult> {

		public void read(org.apache.thrift.protocol.TProtocol iprot,
				THermesSMSTaskQueryResult struct)
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
				case 4: // MESSAGE
					if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
						struct.message = iprot.readString();
						struct.setMessageIsSet(true);
					} else {
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot,
								schemeField.type);
					}
					break;
				case 5: // STATUS
					if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
						struct.status = THermesTaskStatus.findByValue(iprot
								.readI32());
						struct.setStatusIsSet(true);
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
			if (!struct.isSetCreate_time()) {
				throw new org.apache.thrift.protocol.TProtocolException(
						"Required field 'create_time' was not found in serialized data! Struct: "
								+ toString());
			}
			struct.validate();
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot,
				THermesSMSTaskQueryResult struct)
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
			if (struct.message != null) {
				oprot.writeFieldBegin(MESSAGE_FIELD_DESC);
				oprot.writeString(struct.message);
				oprot.writeFieldEnd();
			}
			if (struct.status != null) {
				oprot.writeFieldBegin(STATUS_FIELD_DESC);
				oprot.writeI32(struct.status.getValue());
				oprot.writeFieldEnd();
			}
			oprot.writeFieldBegin(CREATE_TIME_FIELD_DESC);
			oprot.writeI64(struct.create_time);
			oprot.writeFieldEnd();
			oprot.writeFieldStop();
			oprot.writeStructEnd();
		}

	}

	private static class THermesSMSTaskQueryResultTupleSchemeFactory implements
			SchemeFactory {
		public THermesSMSTaskQueryResultTupleScheme getScheme() {
			return new THermesSMSTaskQueryResultTupleScheme();
		}
	}

	private static class THermesSMSTaskQueryResultTupleScheme extends
			TupleScheme<THermesSMSTaskQueryResult> {

		public void write(org.apache.thrift.protocol.TProtocol prot,
				THermesSMSTaskQueryResult struct)
				throws org.apache.thrift.TException {
			TTupleProtocol oprot = (TTupleProtocol) prot;
			oprot.writeI64(struct.id);
			oprot.writeString(struct.receiver);
			oprot.writeString(struct.sender);
			oprot.writeString(struct.message);
			oprot.writeI32(struct.status.getValue());
			oprot.writeI64(struct.create_time);
		}

		public void read(org.apache.thrift.protocol.TProtocol prot,
				THermesSMSTaskQueryResult struct)
				throws org.apache.thrift.TException {
			TTupleProtocol iprot = (TTupleProtocol) prot;
			struct.id = iprot.readI64();
			struct.setIdIsSet(true);
			struct.receiver = iprot.readString();
			struct.setReceiverIsSet(true);
			struct.sender = iprot.readString();
			struct.setSenderIsSet(true);
			struct.message = iprot.readString();
			struct.setMessageIsSet(true);
			struct.status = THermesTaskStatus.findByValue(iprot.readI32());
			struct.setStatusIsSet(true);
			struct.create_time = iprot.readI64();
			struct.setCreate_timeIsSet(true);
		}
	}

}
