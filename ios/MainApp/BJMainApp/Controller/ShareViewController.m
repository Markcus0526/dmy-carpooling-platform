//
//  ShareViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/25/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "ShareViewController.h"
#import "AppDelegate.h"
@interface ShareViewController ()

@end

@implementation ShareViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
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

- (void) viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    // set share bar button item
    [self showShareBarItem:YES];
}

- (void) viewWillDisappear:(BOOL)animated
{
    [self showShareBarItem:NO];
    
    [super viewWillDisappear:animated];
}

- (void) initControls
{
    [self callGetDefShareContents];
    [self setWebUrl];
}

- (void) setWebUrl
{
    // 访问地址
    // NSURL *url = [NSURL URLWithString:@"http://192.168.1.97:8080/bk/index/activity_friendVisitUrl.do?activityDto.faqiId=2"];
    NSURL *url = [NSURL URLWithString:@"http://www.baidu.com"];
//    self.ShareUrl =@"http://www.2345.com";
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    // 3.加载请求
    [webView loadRequest:request];
}

- (void) showShareBarItem : (BOOL)show
{
    if (show == YES)
    {
        UIBarButtonItem * barShare = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"tabIcon4_active.png"] landscapeImagePhone:[UIImage imageNamed:@"tabIcon4_active.png"] style:UIBarButtonItemStylePlain target:self action:@selector(onTapShare:)];
        
        self.parentViewController.navigationItem.rightBarButtonItem = barShare;
    }
    else
    {
        self.parentViewController.navigationItem.rightBarButtonItem = nil;
    }
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


#pragma mark - UIWebViewDelegate
/**
 *  UIWebView开始加载资源的时候调用(开始发送请求)
 */
- (void)webViewDidStartLoad:(UIWebView *)webView
{
    NSLog(@"webViewDidStartLoad---");
    // [MBProgressHUD showMessage:@"正在加载中..."];
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];//设置进度条开始
}

/**
 *  UIWebView加载完毕的时候调用(请求完毕)
 */
- (void)webViewDidFinishLoad:(UIWebView *)webView
{
    NSLog(@"webViewDidFinishLoad---");
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];//设置进度条结束
}

/**
 *  UIWebView加载失败的时候调用(请求失败)
 */
- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error
{
    NSLog(@"didFailLoadWithError---");
    // [MBProgressHUD hideHUD];
}


#pragma mark - UMSocialUIDelegate
// 实现回调方法（可选）：
-(void)didFinishGetUMSocialDataInViewController:(UMSocialResponseEntity *)response
{
    // 根据`responseCode`得到发送结果,如果分享成功
    if(response.responseCode == UMSResponseCodeSuccess)
    {
        // 得到分享到的微博平台名
        NSLog(@"分享成功, share to sns name is %@", [[response.data allKeys] objectAtIndex:0]);
    }
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation ( Share Button )

- (IBAction)onTapShare:(id)sender
{
    // SSO授权: 利用官方客户端进行简单地授权
    NSString *text = @"OO拼车分享"; // 初始化文字
    NSString *shre  =[NSString stringWithFormat:@"%@%@", text, mContents1];
    UIImage *image = [UIImage imageNamed:@"btn_longOrder_rob_press"]; // 图片
    NSArray *names = @[UMShareToSina, UMShareToTencent,UMShareToQQ, UMShareToQzone,UMShareToWechatSession,UMShareToWechatTimeline,UMShareToWechatFavorite,UMShareToRenren, UMShareToEmail, UMShareToDouban, UMShareToSms,UMShareToYXTimeline]; // 社交平台(分享到哪些平台)
    
    // 弹出分享界面
    [UMSocialSnsService presentSnsIconSheetView:self appKey:UMAppKey shareText:shre shareImage:image shareToSnsNames:names delegate:self];
}

@end
