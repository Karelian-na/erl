package cn.karelian.erl;

import java.io.Serializable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import cn.karelian.erl.entity.TableFieldsInfo;
import cn.karelian.erl.utils.VerifyType;
import lombok.Getter;
import lombok.Setter;

public class DataTransferObject {
	public static class LoginParam {
		public String account;
		public String pwd;
		public boolean remember;
	}

	public static class VerifyCodeSendParam {
		public String pageTraceId;
		public String serial;
		public VerifyType type;
	}

	public static class VerifyCodeParam extends VerifyCodeSendParam {
		public String code;
	}

	@Getter
	@Setter
	public static class IndexParam {
		public Long initPageSize;

		public Long pageIdx;
		public Long pageSize;
		public String searchKey;
		public String searchField;

		public boolean one;
	}

	public static class AuditParam {
		public List<Integer> ids;
		public String comment;
		public Byte audit_status;
	}

	public static class BaseReservationParam {
		public Integer id;
		public String message;
	}

	public static class BookReservationParam extends BaseReservationParam {
		public Integer amount;
	}

	public static class LaboratoryReservationParam extends BaseReservationParam {
		public LocalDate day;
		public LocalTime start_time;
		public LocalTime end_time;
	}

	public static class InternshipManageParam {
		public Byte status;

		public String reason;
		public Byte appStatus;
		public List<Integer> ids;
	}

	public static class DeclareParam implements Serializable {
		private static final long serialVersionUID = 1L;

		public String jsonData;
		public String message;
		public String awardType;
		public String enclosures;
	}

	public static class ViewEditParam {
		public String viewName;
		public String comment;
		public Map<String, TableFieldsInfo> fields;
	}

	public static class AuthorizeParam {
		public Long id;
		public Map<Integer, Byte> auths;
	}

	public static class AsignRoleParam {
		public List<Long> ids;
		public Map<Byte, Byte> auths;
	}

	public static class RevisePasswordParam {
		public String account;
		public String old;
		public String pwd;
	}
}
