package com.nmdy.common;

import net.sf.json.JSONObject;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;

public class PushUtil {

	public static void pushToMobile(final String userid,
			final String channelid, final STPushNotificationData data,
			final boolean isAndroid) {
		try {
			// Constant variables
			String apiKey = "RzKyPYoXGQwEq9BSWv3CaHPf";
			String secretKey = "2e6dPQgF1PSFAFLDFhPG5oZ7kTaO18X4";

			if (!isAndroid) {
				apiKey = "1u2r8ICcyTEOasdKGojEwA7K";
				secretKey = "ZnWyUb60tcbyPV9cbB3ayrgTn02yCWw1";
			}

			ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

			BaiduChannelClient channelClient = new BaiduChannelClient(pair);

			// 3. 若要了解交互细节，请注册YunLogHandler类
/*			channelClient.setChannelLogHandler(new YunLogHandler() {
				@Override
				public void onHandle(YunLogEvent event) {
					System.out.println(event.getMessage());
				}
			});*/

			PushUnicastMessageRequest request = new PushUnicastMessageRequest();

			if (isAndroid)
				request.setDeviceType(3);
			else
				request.setDeviceType(4);
			// device_type =>
			// 1:Web 2:PC
			// 3:android
			// 4:iOS 5:WinPhone
			request.setDeployStatus(2); // DeployStatus => 1: Developer 2:
										// Production

			// request.setChannelId(Long.parseLong("4869882339753703240"));
			// request.setUserId("671885805291844334");
			request.setChannelId(Long.parseLong(channelid));
			request.setUserId(userid);

			request.setMessageType(1);
			request.setMessage(JSONObject.fromObject(data).toString());
			PushUnicastMessageResponse response = channelClient
					.pushUnicastMessage(request);

			System.out.println("push amount : " + response.getSuccessAmount());
		} catch (ChannelClientException e) {
			// 处理客户端错误异常
			e.printStackTrace();
		} catch (ChannelServerException e) {
			// 处理服务端错误异常
			System.out.println(String.format(
					"request_id: %d, error_code: %d, error_message: %s",
					e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
		} catch(Exception e){
			System.out.println("推送异常");
			e.printStackTrace();
		} finally {
		}
	}
}
