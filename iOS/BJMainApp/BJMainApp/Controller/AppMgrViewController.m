//
//  AppMgrViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "AppMgrViewController.h"
#import "Config.h"
#import "MMLocationManager.h"
@interface AppMgrViewController ()

@end

#define SCROLLER_HEIGHT             110
#define APPITEM_WIDTH               80
#define APPITEM_HEIGHT              80
#define APPITEM_OFFSET_TOP          10
#define APPITEM_OFFSET_LEFT         15
#define APPITEM_LABEL_HEIGHT        20

@implementation AppMgrViewController

@synthesize ctrlScroller1;
@synthesize ctrlScroller2;

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
    
    // Get current city  城市定位，
	[[MMLocationManager shareLocation] getCity:^(NSString *cityString) {
        [Common setCurrentCity:cityString];
    }];
    
    if([Common getCurrentCity]!=nil)
    {
    
        [[MMLocationManager shareLocation]stopLocation];
    }
    [self initAppDataList];

	[[CommManager getCommMgr] accountSvcMgr].delegate = self;
	[[[CommManager getCommMgr] accountSvcMgr] GetLatestAppVersion:[Common getDeviceMacAddress]];
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


///////////////////////////////// Initialize ////////////////////////////////
#pragma mark - Initialize App Array
- (void) initAppDataList
{
    mAllAppArray = [[NSMutableArray alloc] init];
    mAppArray1 = [[NSMutableArray alloc] init];
    mAppArray2 = [[NSMutableArray alloc] init];
    
    [self callGetChildAppList];
}

/**  跳转判断是否能执行 分成两类
 * Splite all app list into 2 kinds (installed, uninstalled)
 */
- (void) splitAppList
{
    [mAppArray1 removeAllObjects];
    [mAppArray2 removeAllObjects];
    
    for (int i = 0; i < mAllAppArray.count; i++) {
        STAppInfo * oneInfo = (STAppInfo *)[mAllAppArray objectAtIndex:i];
        if ([[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString:oneInfo.appScheme]])
        {
            [mAppArray1 addObject:oneInfo];
        }
        else
        {
            [mAppArray2 addObject:oneInfo];
        }
    }
}


//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/**
 * call service to get child app list
 */
- (void) callGetChildAppList
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] mainSvcMgr].delegate = self;
    [[[CommManager getCommMgr] mainSvcMgr] GetChlidAppList:[Common getDeviceMacAddress]];
}

- (void) getChildAppListResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];
        
        mAllAppArray = dataList;
        
//        // make one real
//        STAppInfo * real = [[STAppInfo alloc] init];
//        real.appName = @"Yilebang";
//        real.appIcon = @"";
//        real.appScheme = @"Yilebang://";
//        real.appUrl = @"";
//        [mAllAppArray addObject:real];
//        
//        STAppInfo * hunting = [[STAppInfo alloc] init];
//        hunting.appName = @"全民夺宝";
//        hunting.appIcon = @"OOhutingicon";
//        hunting.appScheme = @"oohuntinglogin://xuandy:123456";
//        hunting.appUrl = @"";
//        [mAllAppArray addObject:hunting];
//        
//        STAppInfo *pinche =[[STAppInfo alloc]init];
//        pinche.appName =@"OO快拼";
//        pinche.appIcon =@"ofoafa";
//        pinche.appScheme =@"ooPinche://";
//        pinche.appUrl = @"";
//        [mAllAppArray addObject:pinche];
		
        [self splitAppList];
        [self drawInstalledApps];
        [self drawUninstalledApps];
		
		
		[self callLoginFromDevToken];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
    
}


//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - UI Relation

/**    已经安装的应用
 * show installed app list
 */
- (void) drawInstalledApps
{
    for (int i = 0; i < mAppArray1.count; i++) {
        STAppInfo * oneInfo = (STAppInfo *)[mAppArray1 objectAtIndex:i];
        // make item container
        int posx = i * (APPITEM_OFFSET_LEFT + APPITEM_WIDTH);
        int posy = 0;
        UIView * itemContainer = [[UIView alloc] initWithFrame:CGRectMake(posx, posy, APPITEM_WIDTH, SCROLLER_HEIGHT)];
        
        // make app icon
        UIImageView * appicon = [[UIImageView alloc] initWithFrame:CGRectMake(APPITEM_OFFSET_LEFT, APPITEM_OFFSET_TOP, APPITEM_WIDTH, APPITEM_HEIGHT)];
        // draw item image by url
        [appicon setImageWithURL:[NSURL URLWithUnicodeString:oneInfo.appIcon] placeholderImage:[UIImage imageNamed:@"demoicon"]];
        
        // make app label
        UILabel * appname = [[UILabel alloc] initWithFrame:CGRectMake(APPITEM_OFFSET_LEFT, APPITEM_OFFSET_TOP + APPITEM_HEIGHT, APPITEM_WIDTH, APPITEM_LABEL_HEIGHT)];
        [appname setText:oneInfo.appName];
        [appname setFont:[UIFont fontWithName:@"Arial" size:13]];
        [appname setTextAlignment:NSTextAlignmentCenter];
        
        // make link button
        UIButton * applink = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, APPITEM_WIDTH, APPITEM_HEIGHT)];
        applink.tag = i;
        [applink addTarget:self action:@selector(onClickedInstalledAppIcon:) forControlEvents:UIControlEventTouchUpInside];
        
        
        [itemContainer addSubview:appicon];
        [itemContainer addSubview:appname];
        [itemContainer addSubview:applink];
        [ctrlScroller1 addSubview:itemContainer];
    }
    
    long totalWidth = mAppArray1.count * (APPITEM_OFFSET_LEFT + APPITEM_WIDTH) + APPITEM_OFFSET_LEFT;
    [ctrlScroller1 setContentSize:CGSizeMake(totalWidth, SCROLLER_HEIGHT)];
}

/**
 * show uninstalled app list
 */
- (void) drawUninstalledApps
{
    for (int i = 0; i < mAppArray2.count; i++) {
        STAppInfo * oneInfo = (STAppInfo *)[mAppArray2 objectAtIndex:i];
        // make item container
        int posx = i * (APPITEM_OFFSET_LEFT + APPITEM_WIDTH);
        int posy = 0;
        UIView * itemContainer = [[UIView alloc] initWithFrame:CGRectMake(posx, posy, APPITEM_WIDTH, SCROLLER_HEIGHT)];
        // make app icon
        UIImageView *appicon = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"demoicon"]];
        [appicon setFrame:CGRectMake(APPITEM_OFFSET_LEFT, APPITEM_OFFSET_TOP, APPITEM_WIDTH, APPITEM_HEIGHT)];
        // make app label
        UILabel * appname = [[UILabel alloc] initWithFrame:CGRectMake(APPITEM_OFFSET_LEFT, APPITEM_OFFSET_TOP + APPITEM_HEIGHT, APPITEM_WIDTH, APPITEM_LABEL_HEIGHT)];
        [appname setText:oneInfo.appName];
        [appname setFont:[UIFont fontWithName:@"Arial" size:13]];
        [appname setTextAlignment:NSTextAlignmentCenter];
        
        // make link button
        UIButton * applink = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, APPITEM_WIDTH, APPITEM_HEIGHT)];
        applink.tag = i;
        [applink addTarget:self action:@selector(onClickedUninstalledAppIcon:) forControlEvents:UIControlEventTouchUpInside];
        
        [itemContainer addSubview:appicon];
        [itemContainer addSubview:appname];
        [itemContainer addSubview:applink];
        [ctrlScroller2 addSubview:itemContainer];
    }
    
    long totalWidth = mAppArray2.count * (APPITEM_OFFSET_LEFT + APPITEM_WIDTH) + APPITEM_OFFSET_LEFT;
    [ctrlScroller2 setContentSize:CGSizeMake(totalWidth, SCROLLER_HEIGHT)];
}

/**  打开已经安装的应用
 * Clicked event of uninstalled app icon
 * @author : kimoc
 * @param : [sender], clicked app button
 */
- (void)onClickedInstalledAppIcon:(id)sender
{
    UIButton * button = (UIButton *)sender;
    int pos = button.tag;
    
    // get selected app info
    STAppInfo * appInfo = (STAppInfo *)[mAppArray1 objectAtIndex:pos];
    
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:appInfo.appScheme]];
}


/**  点击未安装应用的图标  跳转下载
 * Clicked event of uninstalled app icon
 * @author : kimoc
 * @param : [sender], clicked app button
 */
- (void)onClickedUninstalledAppIcon:(id)sender
{
    UIButton * button = (UIButton *)sender;
    int pos = button.tag;
    
    // get selected app info
    STAppInfo * appInfo = (STAppInfo *)[mAppArray2 objectAtIndex:pos];
    
//    NSString * URLString = @"http://itunes.apple.com/cn/app/id535715926?mt=8";
    NSString * URLString = appInfo.appUrl;
    
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:URLString]];
}




//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Auto Login Relation

/**
 * call service to get child app list
 */
- (void) callLoginFromDevToken
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the get login info from devtoken service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] GetLoginFromDevToken:[Common getDeviceMacAddress]];
}

- (void) getLoginFromDevTokenFromResult:(NSString *)result userinfo:(STUserInfo *)userinfo
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];
		
        // set user info
        [Common setUserInfo:userinfo];
		
		CommManager* commMgr = [CommManager getCommMgr];
		MainSvcMgr* mainSvcMgr = commMgr.mainSvcMgr;
		mainSvcMgr.delegate = self;
		[mainSvcMgr hasNewsWithUserID:[Common getUserID] city:[Common getCurrentCity] driververif:userinfo.driver_verified lastannounceid:0 lastordernotifid:0 lastpersonnotifid:0 devtoken:[Common getDeviceMacAddress]];
    }
    else
    {
//        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
		[SVProgressHUD dismiss];
    }
}



- (void)hasNewsResultCode:(int)retcode retmsg:(NSString *)retmsg announcement:(int)announcement ordernotif:(int)ordernotif personnotif:(int)personnotif
{
	if (retcode == SVCERR_SUCCESS)
	{
		anc_count = announcement;
		order_count = ordernotif;
		person_count = personnotif;
	}
	else
	{
		anc_count = 0;
		order_count = 0;
		person_count = 0;
	}
	
	[Config setUnreadNewsCount:anc_count news2:order_count news3:person_count];
	
//	if (anc_count > 0 || order_count > 0 || person_count > 0)
//	{
//		_imgBadge.hidden = NO;
//	}
//	else
//	{
//		_imgBadge.hidden = YES;
//	}
	
	[SVProgressHUD dismiss];
}



-(void)getLatestAppVersion:(NSString *)result dataList:(NSMutableArray *)dataList
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
		[SVProgressHUD dismiss];
		NSDictionary *dic = [dataList objectAtIndex:0];
		NSString *version  = [dic objectForKey:@"latestver"];
		versionURL = [dic objectForKey:@"downloadurl"];
		NSString *version1 = [[[NSBundle mainBundle] infoDictionary] objectForKey:(NSString *)kCFBundleVersionKey];
		if ([version longLongValue]-[version1 longLongValue]>0) {
			UIAlertView *alert =[[UIAlertView alloc]initWithTitle:@"提示!" message:[NSString stringWithFormat:@"发现新版本：%@，是否立即更新？", version] delegate:self cancelButtonTitle:@"否" otherButtonTitles:@"是", nil];
			[alert show];
		}
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
}


- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
	if (buttonIndex == 1)
		[[UIApplication sharedApplication] openURL:[NSURL URLWithString:versionURL]];
}




@end
