/**   
* @Title: OnLoginResponse.java
* @ProjectName XmppCoreForClientBetaV2_0 
* @Package com.ihengtu.xmpp.core.service 
* @author hepengcheng
* @date 2015年2月6日 上午11:58:46 
* @version V1.0
* Copyright 2013-2015 深圳市点滴互联科技有限公司  版权所有
*/
package com.ihengtu.xmpp.core.service;

/** 
 * @ClassName: OnLoginResponse 
 * @Description: TODO 登录Xmpp Server 响应结果处理
 * @author hepengcheng
 * @date 2015年2月6日 上午11:58:46 
 *  
 */
public interface OnLoginResponse {

	void onloginSuccessful();
	
	void onloginFailure(Exception e);
}
