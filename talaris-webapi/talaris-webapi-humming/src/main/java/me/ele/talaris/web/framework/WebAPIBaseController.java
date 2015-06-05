package me.ele.talaris.web.framework;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.utils.SerializeUtil;
import me.ele.talaris.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public abstract class WebAPIBaseController {

    private final static Logger baseLogger = LoggerFactory.getLogger(WebAPIBaseController.class);

    // @ExceptionHandler(ExceptionEx.class)
    // public @ResponseBody ResponseEntity<Map<String, Object>> handleExceptionEx(
    // HttpServletRequest req, ExceptionEx exception) {
    // baseLogger.error("Request: " + req.getRequestURL() + " raised "
    // + exception);
    //
    // ResponseEntity<Map<String, Object>> rsp = new ResponseEntity<Map<String, Object>>(
    // Utils.killNull(exception.getErrorCode()),
    // Utils.killNull(exception.getMessage()),
    // new HashMap<String, Object>());
    //
    // Metadata metadata = ExceptionContext.metadata(Utils.killNull(exception.getErrorCode()));
    // if (metadata == null) {
    // rsp.getData().put("message", exception.getMessage());
    // } else {
    // rsp.getData().put("code", metadata.getCode());
    // rsp.getData().put("name", metadata.getName());
    // rsp.getData().put("message", metadata.getDescription());
    // }
    //
    // return rsp;
    // }

    public static class ValidationErrorSimple {
        private String objectName;
        private String field;
        private String code;
        private String message;

        public String getObjectName() {
            return objectName;
        }

        public void setObjectName(String objectName) {
            this.objectName = objectName;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ValidationErrorSimple(String objectName, String field, String code, String message) {
            super();
            this.objectName = objectName;
            this.field = field;
            this.code = code;
            this.message = message;
        }

        public ValidationErrorSimple(ObjectError e) {
            this.objectName = e.getObjectName();
            this.message = MessageFormat.format(Utils.killNull(e.getDefaultMessage()), e.getArguments());
            this.code = e.getCode();
            if (e instanceof FieldError) {
                this.field = ((FieldError) e).getField();
            }
        }
    }

    @ExceptionHandler(BindException.class)
    public @ResponseBody ResponseEntity<List<ValidationErrorSimple>> handleBindException(HttpServletRequest req,
            BindException exception) {
        baseLogger.debug("Request: " + req.getRequestURL() + " validation error " + exception);

        List<ValidationErrorSimple> errors = new ArrayList<ValidationErrorSimple>();

        for (ObjectError e : exception.getAllErrors()) {
            errors.add(new ValidationErrorSimple(e));
        }

        return new ResponseEntity<List<ValidationErrorSimple>>(null, "校验错误", errors);
    }

    @ExceptionHandler(SystemException.class)
    public @ResponseBody ResponseEntity<String>
            handleSystemException(HttpServletRequest req, SystemException exception) {
        baseLogger.debug("Request: " + req.getRequestURL() + " validation error ", exception);
        if ("TTL_ERROR_100".equals(exception.getErrorCode())) {
            return new ResponseEntity<String>(exception.getErrorCode(), exception.getMessage(),
                    SerializeUtil.beanToJson(exception.getIds()));
        }
        return new ResponseEntity<String>(exception.getErrorCode(), exception.getMessage(), "");

    }

    // @ExceptionHandler(TTLException.class)
    // public @ResponseBody ResponseEntity<List<String>>
    // handleTTLException(HttpServletRequest req, TTLException exception) {
    // baseLogger.debug("Request: " + req.getRequestURL() + " validation error ", exception);
    // return new ResponseEntity<List<String>>(exception.getError_code(), exception.getMessage(), exception.getIds());
    //
    // }

    @ExceptionHandler(Throwable.class)
    public @ResponseBody ResponseEntity<String> handleThrowable(HttpServletRequest req, Throwable exception) {
        baseLogger.error("Request: " + req.getRequestURL() + " raised ", exception);
        return new ResponseEntity<String>("SYSTEM_ERROR_500", "系统异常", "");
    }

    @ExceptionHandler(UserException.class)
    public @ResponseBody ResponseEntity<String> handleUserException(HttpServletRequest req, UserException exception) {
        baseLogger.error("Request: " + req.getRequestURL() + " raised ", exception);
        if ("TTL_ERROR_100".equals(exception.getErrorCode())) {
            return new ResponseEntity<String>(exception.getErrorCode(), exception.getMessage(),
                    SerializeUtil.beanToJson(exception.getIds()));
        }
        return new ResponseEntity<String>(exception.getErrorCode(), exception.getMessage(), "");
    }
    // @InitBinder
    // protected void binder(WebDataBinder binder) {
    // binder.registerCustomEditor(
    // Collections.<String> emptyList().getClass(),
    // new PropertyEditorSupport() {
    // @Override
    // public void setAsText(String value) {
    // try {
    // @SuppressWarnings("unchecked")
    // List<String> list = (List<String>) Beans
    // .jsonToContainer(value);
    // for (String a : list) {
    // a.length();
    // }
    // this.setValue(list);
    // } catch (Throwable e) {
    // throw new DataBindingException(e);
    // }
    // }
    //
    // @Override
    // public String getAsText() {
    // if (getValue() == null)
    // return null;
    // try {
    // return Beans.toJSONString(getValue());
    // } catch (Throwable e) {
    // throw new DataBindingException(e);
    // }
    // }
    // });
    //
    //
    // }

}
