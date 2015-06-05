package me.ele.talaris.service.user.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import me.ele.talaris.dao.UserBankBindAbnormalDao;
import me.ele.talaris.service.user.IBankAuthService;
import me.ele.talaris.utils.HttpRequest;
import me.ele.talaris.utils.HttpResponseEntity;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BankAuthService implements IBankAuthService{

	@Autowired
	UserBankBindAbnormalDao userBankBindAbnormalDao;

	@Autowired
	HttpRequest httpRequestProxy;

	private final String partnerId = "3";
	private final String tradeAmount = "0.01";
	private final String outAccountId = "2";
	private final String comment = "绑定验证";
	private final String remark = "验证";

	@Value("${bank.card.auth.payment.key}")
	private String key;

	@Value("${bank.card.auth.payment.url}")
	private String HttpUrlHead;

	private String paymentSuccess = "30";
	private String paymentFailure = "-10";
	private String paymentWarning = "-20";

	private String generatePostSign(String tradeNo, String inAccountName, String inAccountNumber, 
			String inAccountBankId) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		
		String sign = "comment="+comment+"&in_account_bank_id="+inAccountBankId+
				"&in_account_name="+inAccountName+"&in_account_number="+inAccountNumber+
				"&out_account_id="+outAccountId+"&partner_id="+partnerId+"&remark="+remark+
				"&trade_amount="+tradeAmount+"&trade_no="+tradeNo+key;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(sign.getBytes("UTF-8"));

			StringBuilder sb = new StringBuilder();

			for(byte b:md.digest()) {
				String a = Integer.toHexString(b&0xff);
				if(a.length() == 1) {
					a="0"+a;	//补0
				}
				sb.append(a);
			}
			return sb.toString();
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private String generateGetSign(String tradeNo) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		
		String sign = "partner_id="+partnerId+"&trade_no="+tradeNo+key;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(sign.getBytes("UTF-8"));

			StringBuilder sb = new StringBuilder();

			for(byte b:md.digest()) {
				String a = Integer.toHexString(b&0xff);
				if(a.length() == 1) {
					a="0"+a;	//补0
				}
				sb.append(a);
			}
			return sb.toString();
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
//	public int getBankAuthResult(String tradeNo) throws ClientProtocolException, IOException, NoSuchAlgorithmException, JSONException {
//		
//		String sign = generateGetSign(tradeNo);
//		
//		String url = HttpUrlHead + "?partner_id=" + partnerId + "&trade_no=" + tradeNo + "&sign=" + sign;
//		
//		HttpClient httpclient = new DefaultHttpClient();
//		try {
//			HttpGet httpget = new HttpGet(url);
//			HttpResponse response = httpclient.execute(httpget);
//			try {
//				//EntityUtils.consume(response.getEntity());
//				HttpEntity entity = response.getEntity();
//				if (entity != null) {
//					String content = EntityUtils.toString(entity);
//					JSONObject jsonObject = new JSONObject(content);
//					String requestStatus = jsonObject.getString("status");
//					if(requestStatus != null && requestStatus.equals("ok")) {
//						//查看打款状态
//						String data = jsonObject.getString("data");
//						if(data != null) {
//							JSONObject jsonData = new JSONObject(data);
//							String payStatus = jsonData.getString("status");
//							if(payStatus.equals(paymentWarning)) {
//								userBankBindAbnormalDao.addAbnormalLog(tradeNo, partnerId, 0);
//								return 2; //此结果记录日志并通知打款团队，打款结果记为失败
//							} else if(payStatus.equals(paymentSuccess)) {
//								return 1; //打款成功
//							} else if(payStatus.equals(paymentFailure)) {
//								return 2; //打款失败
//							} else {
//								return 0; //其余状态均认为打款进行中
//							}
//						}
//					}
//				}
//			} finally {
//				httpget.releaseConnection();
//			}
//		} finally {
//			httpclient.getConnectionManager().shutdown();
//		}
//		return 0;
//	}
	
//	public int sendBankAuthRequest(String tradeNo, String inAccountName, String inAccountNumber, 
//			String inAccountBankId) throws ClientProtocolException, IOException, NoSuchAlgorithmException, JSONException {
//
//		HttpClient httpclient = new DefaultHttpClient();
//		try {
//			HttpPost httppost = new HttpPost(HttpUrlHead);
//
//			String signParam = generatePostSign(tradeNo, inAccountName, inAccountNumber, inAccountBankId);
//			
//			List<NameValuePair> paramMap = new ArrayList<NameValuePair>();
//			paramMap.add(new BasicNameValuePair("partner_id", partnerId));
//			paramMap.add(new BasicNameValuePair("trade_no", tradeNo));
//			paramMap.add(new BasicNameValuePair("trade_amount", tradeAmount));
//			paramMap.add(new BasicNameValuePair("out_account_id", outAccountId));
//			paramMap.add(new BasicNameValuePair("in_account_name", inAccountName));
//			paramMap.add(new BasicNameValuePair("in_account_number", inAccountNumber));
//			paramMap.add(new BasicNameValuePair("in_account_bank_id", inAccountBankId));
//			paramMap.add(new BasicNameValuePair("comment", comment));
//			paramMap.add(new BasicNameValuePair("remark", remark));
//			paramMap.add(new BasicNameValuePair("sign", signParam));
//
//			httppost.setEntity(new UrlEncodedFormEntity(paramMap, "UTF-8"));
//
//			HttpResponse response = httpclient.execute(httppost);
//			try {
//				//EntityUtils.consume(response.getEntity());
//				HttpEntity entity = response.getEntity();
//				if (entity != null) {
//					String content = EntityUtils.toString(entity);
//					JSONObject jsonObject = new JSONObject(content);
//					String requestStatus = jsonObject.getString("status");
//					if(requestStatus != null && requestStatus.equals("ok")) {
//						return 1;
//					}
//				}
//			} finally {
//				httppost.releaseConnection();
//			}
//		} finally {
//			httpclient.getConnectionManager().shutdown();
//		}
//		return 0;
//	}
	
	public int getBankAuthResult(String tradeNo) throws ClientProtocolException, IOException, NoSuchAlgorithmException, JSONException {
		
		String sign = generateGetSign(tradeNo);
		
		String url = HttpUrlHead + "?partner_id=" + partnerId + "&trade_no=" + tradeNo + "&sign=" + sign;
		
		try {
			HttpResponseEntity httpResponse = httpRequestProxy.doRequest(url, null, null, "UTF-8");
			if(httpResponse != null) {
				String content = httpResponse.getResponseContent();
				JSONObject jsonObject = new JSONObject(content);
				String requestStatus = jsonObject.getString("status");
				if(requestStatus != null && requestStatus.equals("ok")) {
					//查看打款状态
					String data = jsonObject.getString("data");
					if(data != null) {
						JSONObject jsonData = new JSONObject(data);
						String payStatus = jsonData.getString("status");
						if(payStatus.equals(paymentWarning)) {
							userBankBindAbnormalDao.addAbnormalLog(tradeNo, partnerId, 0);
							return 2; //此结果记录日志并通知打款团队，打款结果记为失败
						} else if(payStatus.equals(paymentSuccess)) {
							return 1; //打款成功
						} else if(payStatus.equals(paymentFailure)) {
							return 2; //打款失败
						} else {
							return 0; //其余状态均认为打款进行中
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	
	public int sendBankAuthRequest(String tradeNo, String inAccountName, String inAccountNumber, 
			String inAccountBankId) throws ClientProtocolException, IOException, NoSuchAlgorithmException, JSONException {

		String signParam = generatePostSign(tradeNo, inAccountName, inAccountNumber, inAccountBankId);
		
		String url = HttpUrlHead;
		
		HashMap<String, Object> paramMap = new HashMap<>();
		
		paramMap.put("partner_id", partnerId);
		paramMap.put("trade_no", tradeNo);
		paramMap.put("trade_amount", tradeAmount);
		paramMap.put("out_account_id", outAccountId);
		paramMap.put("in_account_name", inAccountName);
		paramMap.put("in_account_number", inAccountNumber);
		paramMap.put("in_account_bank_id", inAccountBankId);
		paramMap.put("comment", comment);
		paramMap.put("remark", remark);
		paramMap.put("sign", signParam);
		
		HttpResponseEntity httpResponse;
		try {
			httpResponse = httpRequestProxy.doRequest(url, paramMap, null, "UTF-8");
			if(httpResponse != null) {
				String content = httpResponse.getResponseContent();
				JSONObject jsonObject = new JSONObject(content);
				String requestStatus = jsonObject.getString("status");
				if(requestStatus != null && requestStatus.equals("ok")) {
					return 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}
	
}
