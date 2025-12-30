//
//  MainSvcMgr.m
//  Yilebang
//
//  Created by KimOC on 12/18/13.
//  Copyright (c) 2013 KimOC. All rights reserved.
//

#import <sys/socket.h>
#import <netinet/in.h>
#import <SystemConfiguration/SystemConfiguration.h>
#import "CommManager.h"
#import "ServiceMethod.h"
#import "AccountSvcMgr.h"
//#import "SBJson.h"
#import "AFHTTPClient.h"
#import "AFJSONRequestOperation.h"

#import "Config.h"


//////////////////////////////////////////////////////////
#pragma mark - User Info Manager Interface

@implementation AccountSvcMgr

@synthesize delegate;

/**   用户注册网络方法
 * Call GetChildAppList Service
 * @param : reginfo [in], user info to be registered
 */
#pragma mark 用户注册网络请求方法
- (void) RegisterUser : (STRegUserInfo *)reginfo
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdRegisterUser]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
//    httpClient.parameterEncoding = NSUTF8StringEncoding;
	MyLog(@"%@",reginfo);
    NSString *pushnotif_token =[NSString stringWithFormat:@"%@,%@",[Config baiduPushUserID],[Config baiduPushChannelID]];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							reginfo.username, @"username",
                            reginfo.mobile, @"mobile",
                            reginfo.nickname, @"nickname",
                            reginfo.password, @"password",
                            reginfo.invitecode, @"invitecode",
                            [NSString stringWithFormat:@"%d", reginfo.sex], @"sex",
                            reginfo.city, @"city",
                            [Common getDeviceMacAddress], @"devtoken",
                            CUR_PLATFORM, @"platform",
                            pushnotif_token,@"pushnotif_token",
                            CHANNEL_FLAG,@"channel_flag",
                           nil];
    
  // @"registerUser"

    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];

		 [self parseRegisterUserRes:responseStr];
		
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate registerUserResult:SVCERR_MSG_ERROR userinfo:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

/**校验码网络方法
 * Call GetChildAppList Service
 * @param : reginfo [in], user info to be registered
 */
#pragma mark 校验码网络方法
- (void) GetVerifKey : (NSString *)mobile andRegistered:(NSInteger )registered
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetVerifKey]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
    
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE,@"source",
							mobile, @"mobile",
                            mobile, @"username",
                            [NSString stringWithFormat:@"%d",registered], @"registered",
                            nil];
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseVerifKeyRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getVerifKeyResult:SVCERR_MSG_ERROR keydata:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}


/**
 * Call LoginUser Service
 * @param : username [in], login username
 * @param : passowrd [in], login password
 * @param : city [in], current city
 */
- (void) LoginUser : (NSString *)username password:(NSString *)password city:(NSString *)city devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdLoginUser]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	 NSString *pushnotif_token =[NSString stringWithFormat:@"%@,%@",[Config baiduPushUserID],[Config baiduPushChannelID]];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							username, @"username",
                            password, @"password",
                            CUR_PLATFORM, @"platform",
                            pushnotif_token,@"pushnotif_token",
                            devtoken, @"devtoken",
                            city, @"city",
                            nil];
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseLoginUserRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         if ([delegate respondsToSelector:@selector(loginUserResult:userinfo:exeinfo:)]) {
             [delegate loginUserResult:SVCERR_MSG_ERROR userinfo:nil exeinfo:nil];
         }
		 
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) GetLoginInfoFromDevToken : (NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetLoginInfoFromDevToken]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	NSString *pushnotif_token =[NSString stringWithFormat:@"%@,%@",[Config baiduPushUserID],[Config baiduPushChannelID]];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							devtoken, @"devtoken",
                            pushnotif_token,@"pushnotif_token",
                            nil];
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseLoginInfoFromDevTokenRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate loginInfoFromDevTokenResult:SVCERR_MSG_ERROR userinfo:nil exeinfo:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

/**
 * Call ForgetPassword Service
 * @param : username [in], login username
 * @param : mobile [in], user mobile number
 * @param : newpwd [in], new password
 */
- (void) ForgetPassword : (NSString *)username mobile:(NSString *)mobile newpwd:(NSString *)newpwd
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdForgetPassword]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							username, @"username",
                            mobile, @"mobile",
                            newpwd, @"password",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseForgetPasswordRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate forgetPasswordResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}


/**
 * Call ChangeUserInfo Service
 * @param : userid [in], index of user
 * @param : mobile [in], user mobile number
 * @param : nickname [in], user ninkname (realname)
 * @param : birthday [in], user birthday
 * @param : sex [in], user sex
 * @param : photo [in], user photo (encocded by base64)
 * @param : photo_changed [in], user photo changed state
 */
- (void) ChangeUserInfo : (long)userid mobile:(NSString *)mobile nickname:(NSString *)nickname birthday:(NSString *)birthday sex:(NSString *)sex photo:(NSString *)photo photo_changed:(int)photo_changed carimg:(NSString *)carimg carimg_changed:(int)carimg_changed devtoken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
	[method appendString:[ServiceMethod cmdChangeUserInfo]];

	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
	//httpClient.parameterEncoding = AFJSONParameterEncoding;
    
    if (photo.length ==0) {
        photo =@"";
    }
    NSString *photo_ch = [NSString stringWithFormat:@"%d",photo_changed];
    if (photo_ch.length == 0) {
        photo_ch = @"";
    }
    if (carimg.length == 0) {
        carimg = @"";
    }
    

	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            mobile, @"mobile",
                            nickname, @"nickname",
                            birthday, @"birthday",
                            sex, @"sex",
                            devtoken, @"devtoken",
                            photo, @"photo",
                            [NSString stringWithFormat:@"%d", photo_changed], @"photo_changed",
							carimg, @"carimg",
                            [NSString stringWithFormat:@"%d", carimg_changed], @"carimg_changed",
                            
                            nil];
    
    [httpClient postPath:@"changeUserInfo.action"/*method*/ parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseChangeUserInfoRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate changeUserInfoResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

/**
 * Call ChangePassword Service
 * @param : userid [in], index of user
 * @param : oldpwd [in], old password
 * @param : newpwd [in], new password
 */
#pragma mark 用户修改密码 网络请求
- (void) ChangePassword : (long)userid oldpwd:(NSString *)oldpwd newpwd:(NSString *)newpwd devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdChangePassword]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            oldpwd, @"oldpwd",
                            newpwd, @"newpwd",
                            devtoken,@"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseChangePasswordRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate changePasswordResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

/**
 * Call VerifyPersonInfo Service
 * @param : userid [in], index of user
 * @param : idforeimg [in], id card front image (encoded by base64)
 * @param : idbackimg [in], id card back image (encoded by base64)
 */
- (void) VerifyPersonInfo : (long)userid idforeimg:(NSString *)idforeimg idbackimg:(NSString *)idbackimg devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdVerifyPersonInfo]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            idforeimg, @"idforeimage",
                            idbackimg, @"idbackimage",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseVerifyPersonRes:responseStr];
		 
		 MyLog(@"VerifyPersonInfo Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate verifyPersonInfoResult:SVCERR_MSG_ERROR];
		 MyLog(@"VerifyPersonInfo [HTTPClient Error]: %@", error.localizedDescription);
     }];
}

/**
 * Call GetUserInfo Service
 * @param : userid [in], index of user
 */
- (void) GetUserInfo : (long)userid devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetUserInfo]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseUserInfoRes:responseStr];
		 
		 MyLog(@"GetUserInfo Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getUserInfoResult:SVCERR_MSG_ERROR userinfo:nil];
		 MyLog(@"GetUserInfo  [HTTPClient Error]: %@", error.localizedDescription);
     }];
}

-(void)GetLatestAppVersion:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetLatestAppVersion]];
    
    NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            CUR_SOURCE, @"source",
                            @"company", @"packname",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
         
         [self parseLatestAppVersion:responseStr];
         
         MyLog(@"GetUserInfo Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getLatestAppVersion:SVCERR_MSG_ERROR dataList:nil];
         MyLog(@"GetUserInfo  [HTTPClient Error]: %@", error.localizedDescription);
     }];
}

/**
 * Call GetDefShareContents Service
 * @param : userid [in], index of user
 */
- (void) GetDefShareContents : (long)userid devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetDefShareContents]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseDefShareContentsRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getDefShareContentsResult:SVCERR_MSG_ERROR contents1:nil contents2:nil contents3:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) GetUsualRoutes : (long)userid type:(int)type devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetUsualRoutes]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%d", type], @"type",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseUsualRoutesRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getUsualRoutesResult:SVCERR_MSG_ERROR dataList:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) DelUsualRoute : (long)userid route_id:(long)route_id devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdDelUsualRoute]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", route_id], @"route_id",
                            devtoken, @"devtoken",
                            nil];
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseDelRoutesRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate delUsualRouteResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) ChangeUsualRoute : (long)userid route_id:(long)route_id type:(int)type daytype:(int)daytype startcity:(NSString *)startcity endcity:(NSString *)endcity startaddr:(NSString *)startaddr endaddr:(NSString *)endaddr startlat:(double)startlat startlng:(double)startlng endlat:(double)endlat endlng:(double)endlng city:(NSString *)city start_time:(NSString *)start_time devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdChangeUsualRoute]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
							[NSString stringWithFormat:@"%ld", route_id], @"route_id",
                            [NSString stringWithFormat:@"%d", type], @"type",
                            [NSString stringWithFormat:@"%d", daytype], @"daytype",
							startcity, @"startcity",
							endcity, @"endcity",
                            startaddr, @"startaddr",
                            endaddr, @"endaddr",
                            [NSString stringWithFormat:@"%f", startlat], @"startlat",
                            [NSString stringWithFormat:@"%f", startlng], @"startlng",
                            [NSString stringWithFormat:@"%f", endlat], @"endlat",
                            [NSString stringWithFormat:@"%f", endlng], @"endlng",
                            city, @"city",
                            start_time, @"start_time",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseChangeRouteRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getUsualRoutesResult:SVCERR_MSG_ERROR dataList:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) AddRoute : (long)userid type:(int)type daytype:(int)daytype startcity:(NSString*)startcity endcity:(NSString*)endcity startaddr:(NSString *)startaddr endaddr:(NSString *)endaddr startlat:(double)startlat startlng:(double)startlng endlat:(double)endlat endlng:(double)endlng city:(NSString *)city start_time:(NSString *)start_time devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdAddRoute]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%d", type], @"type",
                            [NSString stringWithFormat:@"%d", daytype], @"daytype",
							startcity, @"startcity",
							endcity, @"endcity",
                            startaddr, @"startaddr",
                            endaddr, @"endaddr",
                            [NSString stringWithFormat:@"%f", startlat], @"startlat",
                            [NSString stringWithFormat:@"%f", startlng], @"startlng",
                            [NSString stringWithFormat:@"%f", endlat], @"endlat",
                            [NSString stringWithFormat:@"%f", endlng], @"endlng",
                            city, @"city",
                            start_time, @"start_time",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseAddRouteRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getUsualRoutesResult:SVCERR_MSG_ERROR dataList:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) VerifyDriver
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdVerifyDriver]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
	
    STCarVerifyingInfo *carVerifyingInfo = [Common getCarVerifyingInfo];
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%d", carVerifyingInfo.userid], @"userid",
                            carVerifyingInfo.driver_license_fore, @"driver_licence_fore",
                            carVerifyingInfo.driver_license_back, @"driver_licence_back",
                            carVerifyingInfo.brand, @"brand",
                            carVerifyingInfo.type, @"type",
                            carVerifyingInfo.color, @"color",
                            carVerifyingInfo.carimg, @"carimg",
                            carVerifyingInfo.driving_license_fore, @"driving_licence_fore",
                            carVerifyingInfo.driving_license_back, @"driving_licence_back",
                            [Common getDeviceMacAddress], @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseVerifyDriverRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate verifyDriverResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) ReportDriverPos:(long)userid latitude:(double)lat longitude:(double)lng
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdReportDriverPos]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%f", lat], @"lat",
                            [NSString stringWithFormat:@"%f", lng], @"lng",
                            [Common getDeviceMacAddress], @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseReportDriverPos:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate reportDriverPosResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}
#pragma  mark 获取附近的车主 网络请求
- (void) GetNearbyDrivers:(long)userid latitude:(double)lat longitude:(double)lng
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetNearbyDrivers]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%f", lat], @"lat",
                            [NSString stringWithFormat:@"%f", lng], @"lng",
                            [Common getDeviceMacAddress], @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetNearbyDrivers:responseStr];
		 
		 MyLog(@"GetNearbyDrivers Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getNearbyDriversResult:SVCERR_MSG_ERROR dataList:nil];
		 MyLog(@"GetNearbyDrivers [HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) getOnceOrderDriverPos:(long)userid	orderid:(long)orderid devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetOnceOrderDriverPos]];
    
    NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            CUR_SOURCE, @"source",
                            [NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            [Common getDeviceMacAddress], @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
         
         [self parseGetOnceOrderDriverPos:responseStr];
         
         MyLog(@"GetNearbyDrivers Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getGetOnceOrderDriverPos:SVCERR_MSG_ERROR dataList:nil];
         MyLog(@"GetNearbyDrivers [HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) GetBrandsAndColors
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetBrandsAndColors]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetBrandsAndColorsRes:responseStr];
		 
		// MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getBrandsAndColorsResult:SVCERR_MSG_ERROR brandList:nil colorList:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) LogoutUser : (long)userid devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdLogoutUser]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseLogoutRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate logoutUserResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}


///////////////////////////////// event implementation //////////////////////
#pragma mark - Service Event Implementation
/**
 *  注册返回信息处理
 *
 *  @param  <# description#>
 *
 *  @return <#return value description#>
 */
- (void)parseRegisterUserRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    STUserInfo * stInfo = [[STUserInfo alloc] init];
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result  返回状态码
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result  //根据返回状态码判断操作结果
        if (jsonRet != SVCERR_SUCCESS)
        {   //如果失败
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
        }
        else
        {
            NSDictionary * jsonData = [tmp1 objectForKey:SVCC_DATA];
            
            // make child app data list 分深V深Vv
            stInfo.userid = [[jsonData objectForKey:@"userid"] longValue];
            stInfo.photo = [jsonData objectForKey:@"photo"];
            stInfo.mobile = [jsonData objectForKey:@"mobile"];
            stInfo.sex = [[jsonData objectForKey:@"sex"] intValue];
            stInfo.birthday = [jsonData objectForKey:@"birthday"];
            stInfo.person_verified = [[jsonData objectForKey:@"person_verified"] intValue];
            stInfo.driver_verified = [[jsonData objectForKey:@"driver_verified"] intValue];
            stInfo.carimg = [jsonData objectForKey:@"carimg"];
            stInfo.invitecode = [jsonData objectForKey:@"invitecode"];
            
        }
    }
    if ([delegate respondsToSelector:@selector(registerUserResult:userinfo:)]) {
        //调用代理方法  显示结果
        [delegate registerUserResult:retMsg userinfo:stInfo];
    }
    
}

- (void)parseVerifKeyRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSString * keydata = nil;

	if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];

		NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];

		// get service result
		jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];

		// check result
		if (jsonRet != SVCERR_SUCCESS)
		{
			retMsg = [tmp1 objectForKey:SVCC_RETMSG];
        }
        else
        {
			keydata = [tmp1 objectForKey:SVCC_DATA];
        }
    }

	if ([delegate respondsToSelector:@selector(getVerifKeyResult:keydata:)]) {
        [delegate getVerifKeyResult:retMsg keydata:keydata];
    }
    
}

/**
 *  应该是用户登录数据解析
 *
 *  @param responseStr 网络请求返回值
 */
- (void)parseLoginUserRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	STUserInfo * stInfo = [[STUserInfo alloc] init];
    STExeOrderInfo * stExeInfo = [[STExeOrderInfo alloc] init];
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * jsonData = [tmp1 objectForKey:SVCC_DATA];
            
            // make child app data list
            stInfo.userid = [[jsonData objectForKey:@"userid"] longValue];
            stInfo.photo = [jsonData objectForKey:@"photo"];
            stInfo.mobile = [jsonData objectForKey:@"mobile"];
            stInfo.sex = [[jsonData objectForKey:@"sex"] intValue];
            stInfo.birthday = [jsonData objectForKey:@"birthday"];
            stInfo.person_verified = [[jsonData objectForKey:@"person_verified"] intValue];
            stInfo.driver_verified = [[jsonData objectForKey:@"driver_verified"] intValue];
            stInfo.carimg = [jsonData objectForKey:@"carimg"];
            stInfo.invitecode = [jsonData objectForKey:@"invitecode"];
            // get executing order info
            NSDictionary * jsonExeInfo = [jsonData objectForKey:@"exec_info"];
            stExeInfo.orderId = [[jsonExeInfo objectForKey:@"orderid"] longValue];
            stExeInfo.orderType = [[jsonExeInfo objectForKey:@"ordertype"] intValue];
            stExeInfo.runTime = [[jsonExeInfo objectForKey:@"time"] intValue];
            stExeInfo.runDistance = [[jsonExeInfo objectForKey:@"distance"] doubleValue];
        }
    }
    if ([delegate respondsToSelector:@selector(loginUserResult:userinfo:exeinfo:)]) {
        [delegate loginUserResult:retMsg userinfo:stInfo exeinfo:stExeInfo];
    }
    
}

- (void)parseLoginInfoFromDevTokenRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	STUserInfo * stInfo = [[STUserInfo alloc] init];
    STExeOrderInfo * stExeInfo = [[STExeOrderInfo alloc] init];
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * jsonData = [tmp1 objectForKey:SVCC_DATA];
            
            // make child app data list
            stInfo.userid = [[jsonData objectForKey:@"userid"] longValue];
            stInfo.photo = [jsonData objectForKey:@"photo"];
            stInfo.mobile = [jsonData objectForKey:@"mobile"];
            stInfo.sex = [[jsonData objectForKey:@"sex"] intValue];
            stInfo.birthday = [jsonData objectForKey:@"birthday"];
            stInfo.person_verified = [[jsonData objectForKey:@"person_verified"] intValue];
            stInfo.driver_verified = [[jsonData objectForKey:@"driver_verified"] intValue];
            stInfo.carimg = [jsonData objectForKey:@"carimg"];
            stInfo.invitecode = [jsonData objectForKey:@"invitecode"];
            // get executing order info
            NSDictionary * jsonExeInfo = [jsonData objectForKey:@"exec_info"];
            stExeInfo.orderId = [[jsonExeInfo objectForKey:@"orderid"] longValue];
            stExeInfo.orderType = [[jsonExeInfo objectForKey:@"ordertype"] intValue];
            stExeInfo.runTime = [[jsonExeInfo objectForKey:@"time"] intValue];
            stExeInfo.runDistance = [[jsonExeInfo objectForKey:@"distance"] doubleValue];
        }
    }
    if ([delegate respondsToSelector:@selector(loginInfoFromDevTokenResult:userinfo:exeinfo:)]) {
        [delegate loginInfoFromDevTokenResult:retMsg userinfo:stInfo exeinfo:stExeInfo];
    }
    
}


- (void)parseForgetPasswordRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    if ([delegate respondsToSelector:@selector(forgetPasswordResult:)]) {
        [delegate forgetPasswordResult:retMsg];
    }
    
}

- (void)parseChangeUserInfoRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    if ([delegate respondsToSelector:@selector(changeUserInfoResult:)]) {
        [delegate changeUserInfoResult:retMsg];
    }
    
}

- (void)parseChangePasswordRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    if ([delegate respondsToSelector:@selector(changePasswordResult:)]) {
        [delegate changePasswordResult:retMsg];
    }
    
}


- (void)parseVerifyPersonRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    if ([delegate respondsToSelector:@selector(verifyPersonInfoResult:)]) {
        [delegate verifyPersonInfoResult:retMsg];
    }
}
-(void)parseLatestAppVersion:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSMutableArray *array =nil;
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
        }
        else
        {
            array = [[NSMutableArray alloc]init];
            NSDictionary * jsonData = [tmp1 objectForKey:SVCC_DATA];
            [array addObject:jsonData];
            
        }
    }
    if ([delegate respondsToSelector:@selector(getLatestAppVersion:dataList:)]) {
        [delegate getLatestAppVersion:retMsg dataList:array];
    }
}
- (void)parseUserInfoRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	STUserInfo * stInfo = [[STUserInfo alloc] init];
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * jsonData = [tmp1 objectForKey:SVCC_DATA];
            
            // make child app data list
            stInfo.photo = [jsonData objectForKey:@"photo"];
            stInfo.mobile = [jsonData objectForKey:@"mobile"];
            stInfo.nickname = [jsonData objectForKey:@"nickname"];
            stInfo.sex = [[jsonData objectForKey:@"sex"] intValue];
            stInfo.birthday = [jsonData objectForKey:@"birthday"];
            stInfo.person_verified = [[jsonData objectForKey:@"person_verified"] intValue];
            stInfo.driver_verified = [[jsonData objectForKey:@"driver_verified"] intValue];
            stInfo.carimg = [jsonData objectForKey:@"carimg"];
            stInfo.invitecode = [jsonData objectForKey:@"invitecode"];
        }
    }
    if ([delegate respondsToSelector:@selector(getUserInfoResult:userinfo:)]) {
        [delegate getUserInfoResult:retMsg userinfo:stInfo];
    }
    
}


- (void)parseDefShareContentsRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	NSString * cont1;
    NSString * cont2;
    NSString * cont3;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * jsonData = [tmp1 objectForKey:SVCC_DATA];
            
            // make child app data list
            cont1 = [jsonData objectForKey:@"output_url"];
//            cont2 = [jsonData objectForKey:@"contents2"];
//            cont3 = [jsonData objectForKey:@"contents3"];
        }
    }
    if ([delegate respondsToSelector:@selector(getDefShareContentsResult:contents1:contents2:contents3:)]) {
        [delegate getDefShareContentsResult:retMsg contents1:cont1 contents2:cont2 contents3:cont3];
    }
    
}

- (void) parseUsualRoutesRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSMutableArray * dataList = nil;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSMutableArray * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            
            // make route data list
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STRouteInfo * stInfo = [[STRouteInfo alloc] init];
                stInfo.uid = [[item objectForKey:@"uid"] longValue];
				stInfo.startcity = [item objectForKey:@"startcity"];
				stInfo.endcity = [item objectForKey:@"endcity"];
                stInfo.startaddr = [item objectForKey:@"startaddr"];
                stInfo.endaddr = [item objectForKey:@"endaddr"];
				stInfo.startlat = [[item objectForKey:@"startlat"] doubleValue];
				stInfo.startlng = [[item objectForKey:@"startlng"] doubleValue];
				stInfo.endlat = [[item objectForKey:@"endlat"] doubleValue];
				stInfo.endlng = [[item objectForKey:@"endlng"] doubleValue];
                stInfo.time = [item objectForKey:@"create_time"];
                stInfo.daytype = [[item objectForKey:@"daytype"] integerValue];
                // add one advert data info
                [dataList addObject:stInfo];
            }
        }
    }
    if ([delegate respondsToSelector:@selector(getUsualRoutesResult:dataList:)]) {
        [delegate getUsualRoutesResult:retMsg dataList:dataList];
    }
}

- (void)parseDelRoutesRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    if ([delegate respondsToSelector:@selector(delUsualRouteResult:)]) {
        [delegate delUsualRouteResult:retMsg];
    }
}


- (void)parseChangeRouteRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    if ([delegate respondsToSelector:@selector(changeUsualRouteResult:)]) {
        [delegate changeUsualRouteResult:retMsg];
    }
    
}

- (void)parseAddRouteRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    if ([delegate respondsToSelector:@selector(addRouteResult:)]) {
        [delegate addRouteResult:retMsg];
    }
    
}


- (void)parseVerifyDriverRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    if ([delegate respondsToSelector:@selector(verifyDriverResult:)]) {
        [delegate verifyDriverResult:retMsg];
    }
    
}


- (void)parseReportDriverPos:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    if ([delegate respondsToSelector:@selector(reportDriverPosResult:)]) {
        [delegate reportDriverPosResult:retMsg];
    }
}

- (void)parseGetNearbyDrivers:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSMutableArray * dataList = nil;
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSMutableArray * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            
            // make route data list
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                // add one advert data info
                STNearbyDriverInfo * stInfo = [[STNearbyDriverInfo alloc] init];
                stInfo.driverid = [[item objectForKey:@"driverid"] longValue];
                stInfo.latitude = [[item objectForKey:@"lat"] doubleValue];
                stInfo.longitude = [[item objectForKey:@"lng"] doubleValue];
                [dataList addObject:stInfo];
            }
        }
    }
    if ([delegate respondsToSelector:@selector(getNearbyDriversResult:dataList:)]) {
        [delegate getNearbyDriversResult:retMsg dataList:dataList];
    }
}

- (void)parseGetOnceOrderDriverPos:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSMutableArray * dataList = nil;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSMutableArray * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            
            // make route data list
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                // add one advert data info
                STNearbyDriverInfo * stInfo = [[STNearbyDriverInfo alloc] init];
                stInfo.driverid = [[item objectForKey:@"driverid"] longValue];
                stInfo.latitude = [[item objectForKey:@"lat"] doubleValue];
                stInfo.longitude = [[item objectForKey:@"lng"] doubleValue];
                [dataList addObject:stInfo];
            }
        }
    }
    if ([delegate respondsToSelector:@selector(getGetOnceOrderDriverPos:dataList:)]) {
        [delegate getGetOnceOrderDriverPos:retMsg dataList:dataList];
    }
}

- (void)parseGetBrandsAndColorsRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSMutableArray * brandList = [[NSMutableArray alloc] init];
    NSMutableArray * colorList = [[NSMutableArray alloc] init];
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            
            NSArray * tmp3 = [tmp2 objectForKey:@"brands"];
            for(NSDictionary *dicBrand in tmp3)
            {
                STCarBrandInfo *brandInfo = [[STCarBrandInfo alloc] init];
                brandInfo.uid = [[dicBrand objectForKey:@"id"] longValue];
                brandInfo.name = [dicBrand objectForKey:@"name"];
                
                NSMutableArray *arrTypes = [[NSMutableArray alloc] init];
                NSArray * tmp4 = [dicBrand objectForKey:@"types"];
                for(NSDictionary * dicType in tmp4)
                {
                    STCarTypeInfo * typeInfo = [[STCarTypeInfo alloc] init];
                    typeInfo.uid = [[dicType objectForKey:@"id"] longValue];
                    typeInfo.name = [dicType objectForKey:@"name"];
                    typeInfo.style = [[dicType objectForKey:@"style"] intValue];
                    [arrTypes addObject:typeInfo];
                }
                brandInfo.types = arrTypes;
                
                [brandList addObject:brandInfo];
            }
            
            NSArray * tmp5 = [tmp2 objectForKey:@"colors"];
            for(NSDictionary * dicColor in tmp5)
            {
                STCarColorInfo *colorInfo = [[STCarColorInfo alloc] init];
                colorInfo.uid = [[dicColor objectForKey:@"id"] longValue];
                colorInfo.name = [dicColor objectForKey:@"name"];
                colorInfo.code = [dicColor objectForKey:@"code"];
                [colorList addObject:colorInfo];
            }
        }
    }
    if ([delegate respondsToSelector:@selector(getBrandsAndColorsResult:brandList:colorList:)]) {
        [delegate getBrandsAndColorsResult:retMsg brandList:brandList colorList:colorList];
    }
}


- (void)parseLogoutRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    if ([delegate respondsToSelector:@selector(logoutUserResult:)]) {
        [delegate logoutUserResult:retMsg];
    }
    
}


- (void)changeUserPhotoWithUserID:(long)userid photo:(NSString *)userphoto devtoken:(NSString *)devtoken {
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];

	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSNumber numberWithLong:userid], @"userid",
							userphoto, @"userphoto",
							devtoken, @"devtoken",
							nil];
	
	// @"registerUser"
	
	[httpClient postPath:@"changeUserPhoto" parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
	 {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 NSData* resData = [responseStr dataUsingEncoding:NSUTF8StringEncoding];
		 NSDictionary* resDict = [NSJSONSerialization JSONObjectWithData:resData options:NSJSONReadingMutableContainers error:nil];

		 NSDictionary* result = [resDict objectForKey:SVCC_RESULT];

		 int nRetCode = ((NSString*)[result objectForKey:@"retcode"]).intValue;
		 NSString* szRetMsg = [result objectForKey:@"retmsg"];

		 if (nRetCode == SVCERR_SUCCESS) {
			 NSDictionary* retdata = [result objectForKey:@"retdata"];
			 NSString* photo_url = [retdata objectForKey:@"photo_url"];

			 if (delegate != nil && [delegate respondsToSelector:@selector(changeUserPhotoResult:retmsg:photo_url:)])
			 {
				 [delegate changeUserPhotoResult:nRetCode retmsg:szRetMsg photo_url:photo_url];
			 }
		 } else {
			 [delegate changeUserPhotoResult:SVCERR_FAILURE retmsg:SVCERR_MSG_ERROR photo_url:@""];
		 }

	 } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate changeUserPhotoResult:SVCERR_FAILURE retmsg:SVCERR_MSG_ERROR photo_url:@""];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
	 }];
}



@end



