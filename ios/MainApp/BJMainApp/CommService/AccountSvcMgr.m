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


//////////////////////////////////////////////////////////
#pragma mark - User Info Manager Interface

@implementation AccountSvcMgr

@synthesize delegate;

/**
 * Call GetChildAppList Service
 * @param : reginfo [in], user info to be registered
 */
- (void) RegisterUser : (STRegUserInfo *)reginfo
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdRegisterUser]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							reginfo.username, @"username",
                            reginfo.mobile, @"mobile",
                            reginfo.nickname, @"nickname",
                            reginfo.password, @"password",
                            reginfo.invitecode, @"invitecode",
                            reginfo.sex, @"sex",
                            reginfo.city, @"city",
                            CUR_PLATFORM, @"platform",
                            [Common getDeviceMacAddress], @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseRegisterUserRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate registerUserResult:SVCERR_MSG_ERROR userinfo:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

/**
 * Call GetChildAppList Service
 * @param : reginfo [in], user info to be registered
 */
- (void) GetVerifKey : (NSString *)mobile
{

    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetVerifKey]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
    
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							mobile, @"mobile",
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
 * Call LoginUser Service  登录网络请求
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
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							username, @"username",
                            password, @"password",
                            city, @"city",
                            devtoken, @"devtoken",
                            CUR_PLATFORM, @"platform",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseLoginUserRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate loginUserResult:SVCERR_MSG_ERROR userinfo:nil];
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
- (void) ChangeUserInfo : (long)userid mobile:(NSString *)mobile nickname:(NSString *)nickname birthday:(NSString *)birthday sex:(NSString *)sex devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdChangeUserInfo]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	   
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            mobile, @"mobile",
                            nickname, @"nickname",
                            birthday, @"birthday",
                            sex, @"sex",
                            devtoken, @"devtoken",
                            @"0", @"photo_changed",
                            @"0", @"carimg_changed",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
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
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate verifyPersonInfoResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
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
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getUserInfoResult:SVCERR_MSG_ERROR userinfo:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
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

- (void) GetLoginFromDevToken : (NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetLoginFromDevToken]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
         if ((self.delegate != nil) && ([delegate respondsToSelector:@selector(getLoginFromDevTokenFromResult:userinfo:)])) {
             [self parseLoginInfoFromDevToken:responseStr];
         }
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         if ((self.delegate != nil) && ([delegate respondsToSelector:@selector(getLoginFromDevTokenFromResult:userinfo:)])) {
             [delegate getLoginFromDevTokenFromResult:SVCERR_MSG_ERROR userinfo:nil];
         }
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) UpdateUserPhoto : (long)userid userphoto:(NSString *)userphoto devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdUpdateUserPhoto]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
                            [NSString stringWithFormat:@"%ld", userid], @"userid",
                            userphoto, @"userphoto",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
         if ((self.delegate != nil) && ([delegate respondsToSelector:@selector(updateUserPhotoResult:photourl:)])) {
             [self parseUpdateUserPhoto:responseStr];
         }
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         if ((self.delegate != nil) && ([delegate respondsToSelector:@selector(getLoginFromDevTokenFromResult:userinfo:)])) {
             [delegate updateUserPhotoResult:SVCERR_MSG_ERROR photourl:nil];
         }
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
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseLogoutRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
//		 [delegate logoutUserResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

///////////////////////////////// event implementation //////////////////////
#pragma mark - Service Event Implementation
- (void)parseRegisterUserRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	NSMutableArray * dataList = nil;
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
        }
        else
        {
            NSDictionary * jsonData = [tmp1 objectForKey:SVCC_DATA];
            
            // make child app data list
            stInfo.userid = [[jsonData objectForKey:@"userid"] longValue];
            stInfo.photo = [jsonData objectForKey:@"photo"];
            stInfo.mobile = [jsonData objectForKey:@"mobile"];

			stInfo.sex = [[jsonData objectForKey:@"sex"]intValue];
            stInfo.birthday = [jsonData objectForKey:@"birthday"];
            stInfo.person_verified = [[jsonData objectForKey:@"person_verified"] intValue];
            stInfo.driver_verified = [[jsonData objectForKey:@"driver_verified"] intValue];
            stInfo.carimg = [jsonData objectForKey:@"carimg"];
            stInfo.invitecode = [jsonData objectForKey:@"invitecode"];
        }
    }

	[delegate registerUserResult:retMsg userinfo:stInfo];
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
			keydata = (NSString*)[tmp1 objectForKey:SVCC_DATA];
		}
	}

	[delegate getVerifKeyResult:retMsg keydata:keydata];
}


- (void)parseLoginUserRes:(NSString *)responseStr
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
        }
    }
    
    [delegate loginUserResult:retMsg userinfo:stInfo];
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
        }
        else
        {
            
        }
    }
    
    [delegate forgetPasswordResult:retMsg];
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
        }
        else
        {
            
        }
    }
    
    [delegate changeUserInfoResult:retMsg];
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
        }
        else
        {
            
        }
    }
    
    [delegate changePasswordResult:retMsg];
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
        }
        else
        {
            
        }
    }
    
    [delegate verifyPersonInfoResult:retMsg];
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
            if (jsonRet == (int)SVCERR_DUPLICATE_USER) {
                [Common logout];
            }
        }
        else
        {
            NSDictionary * jsonData = [tmp1 objectForKey:SVCC_DATA];
            
            
            // responseStr	__NSCFString *	@"{"result":{"retcode":0,"retmsg":"操作成功","retdata":{"userid":56,"photo":"","mobile":"1234567890","nickname":"è§\u0084å\u0088\u0092å±\u0080","sex":0,"birthday":"","person_verified":0,"driver_verified":0,"carimg":"","invitecode":"00001N"}}}"	0x18b393b0
            stInfo.nickname =[jsonData objectForKey:@"nickname"];
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
    
    [delegate getUserInfoResult:retMsg userinfo:stInfo];
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
        }
        else
        {
            NSDictionary * jsonData = [tmp1 objectForKey:SVCC_DATA];
            
            // make child app data list
            cont1 = [jsonData objectForKey:@"contents1"];
            cont2 = [jsonData objectForKey:@"contents2"];
            cont3 = [jsonData objectForKey:@"contents3"];
        }
    }
    
    [delegate getDefShareContentsResult:retMsg contents1:cont1 contents2:cont2 contents3:cont3];
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
                //               [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    
    [delegate logoutUserResult:retMsg];
}

- (void)parseLoginInfoFromDevToken:(NSString *)responseStr
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
            if (jsonRet == (int)SVCERR_DUPLICATE_USER) {
                [Common logout];
            }
        }
        else
        {
            NSDictionary * jsonData = [tmp1 objectForKey:SVCC_DATA];
            
            stInfo.nickname =[jsonData objectForKey:@"nickname"];
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
    
    [delegate getLoginFromDevTokenFromResult:retMsg userinfo:stInfo];
}

- (void) parseUpdateUserPhoto:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	NSString * photoUrl = @"";
    
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
            if (jsonRet == (int)SVCERR_DUPLICATE_USER) {
                [Common logout];
            }
        }
        else
        {
            NSDictionary * jsonData = [tmp1 objectForKey:SVCC_DATA];
            
            photoUrl = [jsonData objectForKey:@"photo_url"];
        }
    }
    
    [delegate updateUserPhotoResult:retMsg photourl:photoUrl];
}



-(void)GetLatestAppVersion:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
	[method appendString:[[CommManager getCommMgr] bathServiceUrl]];
	[method appendString:@"getLatestAppVersion"];

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



@end