package me.ele.talaris.web.framework;

import java.util.Locale;

import javax.validation.MessageInterpolator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

public class SpringMessageSourceMessageInterpolator implements MessageInterpolator,
    MessageSourceAware, InitializingBean {

    private MessageSource messageSource;
    
    public String interpolate(String messageTemplate, Context context) {
        return messageSource.getMessage(messageTemplate, new Object[]{}, Locale.getDefault());
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
    	if (messageTemplate == null)
    		return null;
    	if(messageTemplate.startsWith("{") && messageTemplate.endsWith("}")) {
    		messageTemplate = messageTemplate.substring(1, messageTemplate.length()-1);
    	}
        return messageSource.getMessage(messageTemplate, new Object[]{}, locale);
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (messageSource == null) {
            throw new IllegalStateException("MessageSource was not injected, could not initialize " 
                    + this.getClass().getSimpleName());
        }
    }

}