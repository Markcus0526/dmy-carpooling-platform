//
//  ShareViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/25/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_ShareViewController.h"

#import "AppDelegate.h"

@interface Drv_ShareViewController ()

@end

@implementation Drv_ShareViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Drvtom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initControls];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/


- (void) initControls
{
    [_lblInviteCode setText:[Common getUserInfo].invitecode];
    [self callGetDefShareContents];
}



//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/**
 * call get default share contents
 */
- (void) callGetDefShareContents
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] GetDefShareContents:[Common getUserID] devtoken:[Common getDeviceMacAddress]];
}

- (void) getDefShareContentsResult:(NSString *)result contents1:(NSString *)contents1 contents2:(NSString *)contents2 contents3:(NSString *)contents3
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        mContents1 = contents1;
        mContents2 = contents2;
        mContents3 = contents3;
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}


///////////////////////////////////////////////////////////////////////////
#pragma mark - Fenxiang Function


/**
 *	@brief	分享到新浪微博
 *
 *	@param 	sender 	事件对象
 */
- (void)shareToSinaWeiboClickHandler:(UIButton *)sender
{
    
}

/**
 *	@brief	分享到腾讯微博
 *
 *	@param 	sender 	事件对象
 */
- (void)shareToTencentWeiboClickHandler:(UIButton *)sender
{
   
}

/**
 *	@brief	分享给QQ好友
 *
 *	@param 	sender 	事件对象
 */
- (void)shareToQQFriendClickHandler:(UIButton *)sender
{
    
}

/**
 *	@brief	分享到QQ空间
 *
 *	@param 	sender 	事件对象
 */
- (void)shareToQQSpaceClickHandler:(UIButton *)sender
{
    //创建分享内容
  
}

/**
 *	@brief	分享给微信好友
 *
 *	@param 	sender 	事件对象
 */
- (void)shareToWeixinSessionClickHandler:(UIButton *)sender
{
   
}

/**
 *	@brief	分享给微信朋友圈
 *
 *	@param 	sender 	事件对象
 */
- (void)shareToWeixinTimelineClickHandler:(UIButton *)sender
{
   }


/**
 *	@brief	短信分享
 *
 *	@param 	sender 	事件对象
 */
- (void)shareBySMSClickHandler:(UIButton *)sender
{
    
}

/**
 *	@brief	邮件分享
 *
 *	@param 	sender 	事件对象
 */
- (void)shareByMailClickHandler:(UIButton *)sender
{
    }


/**
 *	@brief	分享给易信好友
 *
 *	@param 	sender 	事件对象
 */
- (void)shareToYiXinSessionClickHandler:(UIButton *)sender
{
   }

///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation ( Share Button )

/** 
 * Sina Weibo clicked event implementation
 */
- (IBAction)onClickSina:(id)sender
{
    [self shareToSinaWeiboClickHandler:sender];
}

/**
 * Tongxun Weibo clicked event implementation
 */
- (IBAction)onClickTengxun:(id)sender
{
    [self shareToTencentWeiboClickHandler:sender];
}

/**
 * QQ friend clicked event implementation
 */
- (IBAction)onClickQQFriend:(id)sender
{
    [self shareToQQFriendClickHandler:sender];
}

/**
 * QQ Space clicked event implementation
 */
- (IBAction)onClickQQSpace:(id)sender
{
    [self shareToQQSpaceClickHandler:sender];
}

/**
 * Weixin clicked event implementation
 */
- (IBAction)onClickWeixin:(id)sender
{
    [self shareToWeixinSessionClickHandler:sender];
}

/**
 * Friend space clicked event implementation
 */
- (IBAction)onClickFriendSpace:(id)sender
{
    [self shareToWeixinTimelineClickHandler:sender];
}

/**
 * Sms clicked event implementation
 */
- (IBAction)onClickSms:(id)sender
{
    [self shareBySMSClickHandler:sender];
}

/**
 * Email clicked event implementation
 */
- (IBAction)onClickEmail:(id)sender
{
    [self shareByMailClickHandler:sender];
}

/**
 * Yixin clicked event implementation
 */
- (IBAction)onclickYixin:(id)sender
{
    [self shareToYiXinSessionClickHandler:sender];
}

@end
