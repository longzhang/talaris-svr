package me.ele.talaris.service.auth.impl;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.hermes.model.SendVerifyCodeResult;
import me.ele.talaris.hermes.service.IHermesService;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.User;
import me.ele.talaris.service.auth.IVerificationCodeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class VerificationCodeService implements IVerificationCodeService {
	private final static Logger logger = LoggerFactory.getLogger(VerificationCodeService.class);

	private IHermesService coffeeHermesService;

	@Override
	public boolean sendUserVerifyCode(Context context) throws UserException, SystemException {
		User user = context.getUser();
		SendVerifyCodeResult result = coffeeHermesService.requestSendVerifyCode(Long.toString(user.getMobile()));
		logger.info("请求短信验证码:" + result.getCode());
		return true;

	}

	@Override
	public boolean sendUserVoiceVerifyCode(Long mobile) throws UserException, SystemException {
		SendVerifyCodeResult result = coffeeHermesService.requestSendVerifyCodeUseVoice(mobile.toString());
		logger.info("请求语音验证码:" + result.getCode());
		return true;
	}

	@Override
	public boolean validateCodeWithReceiver(Context context, String verification_code) throws UserException, SystemException {
		User user = context.getUser();
		logger.info("验证短信验证码:" + verification_code);
		return coffeeHermesService.validateCodeWithReceiver(verification_code, Long.toString(user.getMobile()));

	}

	@Override
	public boolean validateCodeWithMobileReceiver(Context context, String verification_code, String mobile) throws UserException,
			SystemException {
		String msg = "mobile : " + mobile;
		logger.info(msg + ",验证短信验证码:" + verification_code);
		return coffeeHermesService.validateCodeWithReceiver(verification_code, mobile);
	}

	@Override
	public boolean sendVerifyCodeForMobile(Context context, String mobile) throws UserException, SystemException {
		String msg = "mobile : " + mobile;
		SendVerifyCodeResult result = coffeeHermesService.requestSendVerifyCode(mobile);
		logger.info(msg + ",请求短信验证码:" + result.getCode());
		return true;

	}

	@Override
	public boolean sendVoiceVerifyCodeForMobile(Context context, String mobile) throws UserException, SystemException {
		String msg = "mobile : " + mobile;
		SendVerifyCodeResult result = coffeeHermesService.requestSendVerifyCodeUseVoice(mobile);
		logger.info(msg + ",请求语音验证码:" + result.getCode());
		return true;

	}

	public void setCoffeeHermesService(IHermesService coffeeHermesService) {
		this.coffeeHermesService = coffeeHermesService;
	}
}
