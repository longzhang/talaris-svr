package me.ele.talaris.service.auth;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;

public interface IVerificationCodeService {

	public boolean sendUserVerifyCode(Context context) throws UserException,
			SystemException;

	public boolean sendUserVoiceVerifyCode(Long mobile) throws UserException,
			SystemException;

	public boolean validateCodeWithReceiver(Context context,
			String verification_code) throws UserException, SystemException;

	public boolean validateCodeWithMobileReceiver(Context context,
			String verification_code, String mobile) throws UserException,
			SystemException;

	public boolean sendVerifyCodeForMobile(Context context, String mobile)
			throws UserException, SystemException;

	public boolean sendVoiceVerifyCodeForMobile(Context context, String mobile)
			throws UserException, SystemException;

}