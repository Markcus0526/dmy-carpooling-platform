//
//  ShareViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/25/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Cus_ShareViewController.h"
#import "UMSocial.h"
#import "AppDelegate.h"
#import "UMSocial.h"
#import "UMSocialWechatHandler.h"


#import "UMSocial.h"
#import "UMSocialYixinHandler.h"
#import "UMSocialWechatHandler.h"
#import "UMSocialQQHandler.h"
#import "UMSocialSinaHandler.h"
#import "UMSocialTencentWeiboHandler.h"
#import "UMSocialRenrenHandler.h"




@interface Cus_ShareViewController ()<UIWebViewDelegate>

@property(nonatomic,copy)NSString *ShareUrl;
@property(nonatomic,assign)BOOL WebViewDidLoad;
@end

@implementation Cus_ShareViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
-(void)loadView
{
    [super loadView];
    UIButton *btn2 =[[UIButton alloc]initWithFrame:CGRectMake(0, 0, 35, 35)];
    //[btn2 setBackgroundImage:[UIImage imageNamed:@"tabIcon4_normal"] forState:UIControlStateNormal];
    [btn2 setImage:[UIImage imageNamed:@"bk_share_normal"] forState:UIControlStateNormal];
    [btn2 setImage:[UIImage imageNamed:@"bk_share_sel"] forState:UIControlStateHighlighted];
    [btn2 setTitle:@"" forState:UIControlStateNormal];
    [btn2 setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [btn2 addTarget:self action:@selector(onClickedShare) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *rightItem =[[UIBarButtonItem alloc]initWithCustomView:btn2];
    
    self.navigationItem.rightBarButtonItem =rightItem;
   // webView.delegate =self;
    self.WebViewDidLoad =NO;
    
    UIScreen *currentScreen = [UIScreen mainScreen];
    
    if (currentScreen.applicationFrame.size.height<500)
    {
        webView.frame = CGRectMake(webView.frame.origin.x, webView.frame.origin.y, webView.frame.size.width, webView.frame.size.height-80);
    }
    
    webView.delegate = self;
    // 伸缩内容至适应屏幕尺寸
    webView.scalesPageToFit = YES;
    
 
}
 - (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
     if(self.WebViewDidLoad ==NO)
     {
         // 访问地址
         // NSURL *url = [NSURL URLWithString:@"http://192.168.1.97:8080/bk/index/activity_friendVisitUrl.do?activityDto.faqiId=2"];
       //  return @"http://182.92.237.144:8082/PincheService/webservice/";
//         http://182.92.237.144:9082/bk/index/activity_appFx.do?activityDto.userId=
         
         //测试
         NSString *shareurl =[NSString stringWithFormat:@"http://182.92.237.144:8082/bk/index/activity_appFx.do?activityDto.userId=%ld",[Common getUserID]];
//         发布
//         NSString *shareurl =[NSString stringWithFormat:@"http://182.92.237.144:9082/bk/index/activity_appFx.do?activityDto.userId=%ld",[Common getUserID]];
         NSURL *url = [NSURL URLWithString:shareurl];
         self.ShareUrl =shareurl;
         NSURLRequest *request = [NSURLRequest requestWithURL:url];
         
         //设置微信AppId，设置分享url，默认使用友盟的网址
         [UMSocialWechatHandler setWXAppId:@"wxd930ea5d5a258f4f" appSecret:@"db426a9829e4b49a0dcac7b4162da6b6" url:mContents1];
         
         // 3.加载请求
         [webView loadRequest:request];
     }

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


- (void) initControls
{
    //[_lblInviteCode setText:[Common getUserInfo].invitecode];
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
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}


///////////////////////////////////////////////////////////////////////////
#pragma mark - Fenxiang Function




///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation ( Share Button )


- (void)onClickedShare
{
    NSString *str = [NSString stringWithFormat:@"%ld",[Common getUserID]];
    mContents1 = [mContents1 stringByReplacingOccurrencesOfString:@"${activityDto.faqiId}" withString:str];
    
    [self initializePlat:mContents1];

    
    
    // SSO授权: 利用官方客户端进行简单地授权
    NSString *text = @"OO拼车分享"; // 初始化文字
    NSString *shre  =[NSString stringWithFormat:@"%@%@",text,mContents1];
    UIImage *image = [UIImage imageNamed:@"btn_longOrder_rob_press"]; // 图片
    NSArray *names = @[UMShareToWechatSession,UMShareToWechatTimeline,UMShareToQzone,UMShareToSina,UMShareToQQ,UMShareToSms]; // 社交平台(分享到哪些平台)
    
    // 弹出分享界面
    [UMSocialSnsService presentSnsIconSheetView:self appKey:UMAppKey shareText:shre shareImage:image shareToSnsNames:names delegate:nil];
}

/** 
 * Sina Weibo clicked event implementation
 */
- (IBAction)onClickSina:(id)sender
{
    
    // SSO授权: 利用官方客户端进行简单地授权
    NSString *text = @"OO拼车分享"; // 初始化文字
   
    UIImage *image = [UIImage imageNamed:@"btn_longOrder_rob_press"]; // 图片
    NSArray *names = @[UMShareToSina, UMShareToTencent,UMShareToQQ, UMShareToQzone,UMShareToWechatSession,UMShareToWechatTimeline,UMShareToWechatFavorite,UMShareToRenren, UMShareToEmail, UMShareToDouban, UMShareToSms,UMShareToYXTimeline]; // 社交平台(分享到哪些平台)
    
    // 弹出分享界面
    [UMSocialSnsService presentSnsIconSheetView:self appKey:UMAppKey shareText:text shareImage:image shareToSnsNames:names delegate:self];
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
    self.WebViewDidLoad =YES;
    
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


- (void)initializePlat :(NSString *)strURL;
{
    
    //打开调试log的开关
    [UMSocialData openLog:YES];
    
    //如果你要支持不同的屏幕方向，需要这样设置，否则在iPhone只支持一个竖屏方向
    //[UMSocialConfig setSupportedInterfaceOrientations:UIInterfaceOrientationMaskAll];
    
    //设置友盟社会化组件appkey
    [UMSocialData setAppKey:UMAppKey];
    
    
    //打开新浪微博的SSO开关  回调地址
    [UMSocialSinaHandler openSSOWithRedirectURL:strURL];
    
    //打开腾讯微博SSO开关，设置回调地址
    [UMSocialTencentWeiboHandler openSSOWithRedirectUrl:strURL];
    
    //打开人人网SSO开关
    //  [UMSocialRenrenHandler openSSO];
    
    //    //设置分享到QQ空间的应用Id，和分享url 链接
    [UMSocialQQHandler setQQWithAppId:@"1103298924" appKey:@"zjJYnJSBi614wYFZ" url:strURL];
    //    //设置支持没有客户端情况下使用SSO授权
    [UMSocialQQHandler setSupportWebView:YES];
    //
    //    //设置易信Appkey和分享url地址
    //  [UMSocialYixinHandler setYixinAppKey:@"yx35664bdff4db42c2b7be1e29390c1a06" url:@"http://www.umeng.com/social"];
    //
    
}

@end
