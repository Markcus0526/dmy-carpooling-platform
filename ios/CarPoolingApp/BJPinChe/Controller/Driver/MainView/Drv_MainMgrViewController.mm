//
//  AppMgrViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_MainMgrViewController.h"
#import "Config.h"
#import "Drv_NewsMgrViewController.h"
#import "Drv_SearchOrderViewController.h"
#import "Drv_VerifyCarFstViewController.h"
#import "Drv_OrderPerformViewController.h"
#import "Drv_publishLongOrderViewController.h"
#import "LoginVC.h"
#import "NewsMgrVC.h"
#import "MMLocationManager.h"
#import "RDActionSheet.h"
#import "NonRotateImagePickerController.h"
#import "ResultDlgView.h"
#import "SoundTools.h"
#import "Cus_VerifyUserViewController.h"
#import "NearOrder.h"

@interface Drv_MainMgrViewController () <BMKMapViewDelegate> {
	UIImage* user_photo;
}


@property(nonatomic,strong) BMKGeoCodeSearch *geo_search; //    物理地址

@end

@implementation Drv_MainMgrViewController

@synthesize mNowPos;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Drvtom initialization
    }
    return self;
}

- (void)loadView
{
    [super loadView];

	[self checkAutoLogin];
    //顶部两个按钮Item
    UIButton *btn1 =[[UIButton alloc]initWithFrame:CGRectMake(0, 0, 35, 35)];
    [btn1 setBackgroundImage:[UIImage imageNamed:@"btnSwitch_Type"] forState:UIControlStateNormal];
    [btn1 setBackgroundImage:[UIImage imageNamed:@"btnSwitch_Type_highlted"] forState:UIControlStateHighlighted];
    [btn1 addTarget:self action:@selector(onClickedSwitch) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *leftItem =[[UIBarButtonItem alloc]initWithCustomView:btn1];
    self.navigationItem.leftBarButtonItem =leftItem;


	UIView *navView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 35, 35)];
    [navView setBackgroundColor:[UIColor clearColor]];
    
    UIButton *btn2 =[[UIButton alloc]initWithFrame:CGRectMake(0, 0, 35, 35)];
    [btn2 setBackgroundImage:[UIImage imageNamed:@"btnGoNews"] forState:UIControlStateNormal];
    [btn2 addTarget:self action:@selector(onClickedNews) forControlEvents:UIControlEventTouchUpInside];
    [navView addSubview:btn2];
    
    _imgBadge = [[UIImageView alloc] initWithFrame:CGRectMake(20, 0, 15, 15)];
    [_imgBadge setImage:[UIImage imageNamed:@"new_badge.png"]];
    [navView addSubview:_imgBadge];

	UIBarButtonItem *rightItem =[[UIBarButtonItem alloc]initWithCustomView:navView];
	self.navigationItem.rightBarButtonItem = rightItem;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.

	_imgBadge.hidden = YES;
	

    flageOrder = 0;
    
	// Initialize person verify status
	[_vwDriverVerify setHidden:YES];
	[_vwPersonVerify setHidden:YES];
	[_imgUser setImage:[UIImage imageNamed:@"stOrderImage.png"]];


	NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
	[defaults setObject:@"YES" forKey:@"SingleOrder"];
	[defaults synchronize];

	if (iOS8) {
		//由于IOS8中定位的授权机制改变 需要进行手动授权
		locationManager = [[CLLocationManager alloc] init];

		//获取授权认证
		[locationManager requestAlwaysAuthorization];
		[locationManager requestWhenInUseAuthorization];
	}

     CGRect rect = [[UIScreen mainScreen] bounds];
    if (rect.size.height>500) {
        [tongchengBtn setFrame:CGRectMake(tongchengBtn.frame.origin.x, tongchengBtn.frame.origin.y+88, tongchengBtn.frame.size.width, tongchengBtn.frame.size.height)];
        [chengtuBtn setFrame:CGRectMake(chengtuBtn.frame.origin.x, chengtuBtn.frame.origin.y+88, chengtuBtn.frame.size.width, chengtuBtn.frame.size.height)];
    }
    mSingleLastOrderID = 0;
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self initControls];
    
    [mMapView viewWillAppear];
    mMapView.delegate = self; // 此处记得不用的时候需要置nil，否则影响内存的释放
    locService.delegate = self;

	STUserInfo* userInfo = [Common getUserInfo];

	if (userInfo != nil)
	{
		TEST_NETWORK_RETURN

		CommManager* commMgr = [CommManager getCommMgr];
		MainSvcMgr* mainSvcMgr = commMgr.mainSvcMgr;
		mainSvcMgr.delegate = self;
		[mainSvcMgr hasNewsWithUserID:[Common getUserID] city:[Common getCurrentCity] driververif:userInfo.driver_verified lastannounceid:0 lastordernotifid:0 lastpersonnotifid:0 devtoken:[Common getDeviceMacAddress]];
	}
	else
	{
		_imgBadge.hidden = YES;
    	[_vwPersonVerify setHidden:YES];
		[_vwDriverVerify setHidden:YES];
	}

	[self checkVerifiedState];
    
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] GetLatestAppVersion:[Common getDeviceMacAddress]];
    
}

-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
//    [mMapView viewWillDisappear];
    mMapView.delegate = nil; // 不用时，置nil
    locService.delegate = nil;
    
    [locService stopUserLocationService];
    locService.delegate = nil;
    locService =nil;
    
    mMapView.delegate = nil;
    mMapView = nil;

//	_imgBadge.hidden = YES;

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    [segue.destinationViewController setHidesBottomBarWhenPushed:YES];
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
#pragma mark - Initialize data info
- (void) initControls
{
    locService = [[BMKLocationService alloc]init];
    [locService startUserLocationService];
    mMapView.showsUserLocation = NO;
    mMapView.userTrackingMode = BMKUserTrackingModeFollow;
    mMapView.showsUserLocation = YES;

	self.mNowPos = CLLocationCoordinate2DMake(0.0, 0.0);

    if (mReportTimer != nil) {
        [mReportTimer invalidate];
        mReportTimer = nil;
    }
//	mReportTimer = [NSTimer timerWithTimeInterval:(float)20.0 target:self selector:@selector(reportPosTimer:) userInfo:nil repeats:YES];
    mReportTimer = [NSTimer scheduledTimerWithTimeInterval:20 target:self selector:@selector(reportPosTimer:) userInfo:nil repeats:YES];

	STUserInfo * userInfo = [Common getUserInfo];
	if (userInfo.driver_verified == 0)
    {
		if (userInfo.person_verified == 0) {
            if (mSwitchVerifyTimer !=nil) {
                [mSwitchVerifyTimer invalidate];
                mSwitchVerifyTimer = nil;
            }
			mSwitchVerifyTimer = [NSTimer timerWithTimeInterval:(float)5.0 target:self selector:@selector(switchVerifyTimer:) userInfo:nil repeats:YES];
			[[NSRunLoop mainRunLoop] addTimer:mSwitchVerifyTimer forMode:NSRunLoopCommonModes];
			_vwPersonVerify.frame = CGRectMake(-320, 0, 320, 45);
		} else {
			_vwDriverVerify.frame = CGRectMake(0, 0, 320, 45);
		}
    }
	else
	{
		if (userInfo.person_verified == 0) {
			_vwPersonVerify.frame = CGRectMake(0, 0, 320, 45);
		} else {
		}
	}

	if (userInfo.driver_verified == 1)
		[_vwDriverVerify setHidden:YES];

	if (userInfo.person_verified == 1)
		[_vwPersonVerify setHidden:YES];

	//	物理地址
	self.geo_search =[[BMKGeoCodeSearch alloc]init];
	self.geo_search.delegate=self;
}

- (void)checkVerifiedState
{
    STUserInfo * userInfo = [Common getUserInfo];
	if (userInfo == nil || userInfo.userid <= 0) {		// Not registered yet.
		[_vwDriverVerify setHidden:YES];
		[_vwPersonVerify setHidden:YES];
		[_imgUser setImage:[UIImage imageNamed:@"stOrderImage.png"]];
		return;
	}

	if(userInfo.driver_verified == 1 ||
       userInfo.person_verified == 1)
    {
        if(mSwitchVerifyTimer != nil) {
            [mSwitchVerifyTimer invalidate];
            mSwitchVerifyTimer = nil;
        }
    }
// 0 未认证  1 通过 2 审核中
	if (userInfo.driver_verified == 1)
        [_vwDriverVerify setHidden:YES];
	else if (userInfo.driver_verified == 0)
		[_vwDriverVerify setHidden:NO];

	if (userInfo.person_verified == 1)
        [_vwPersonVerify setHidden:YES];
	else
		[_vwPersonVerify setHidden:NO];

	[_imgUser setImageWithURL:[NSURL URLWithUnicodeString:userInfo.photo] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
}
///////////////////////////////////////////////////////////////////////////////
#pragma mark - UI User Event
- (void)onClickedSwitch
{
    [locService stopUserLocationService];
    locService.delegate = nil;
    locService =nil;
    
    mMapView.delegate = nil;
    mMapView = nil;
    
    // change login option
    [Config setLoginOption:LOGIN_OPTION_CUSTOMER];
    

    // getting the customer storyboard
    UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
    
    // initializing view controller
    UIViewController * controller = [customerStroyboard instantiateInitialViewController];
    controller.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
//    [self presentModalViewController:controller animated:YES];

    [self presentViewController:controller animated:YES completion:nil];
    
    if(mReportTimer != nil)
    {
        [mReportTimer invalidate];
        mReportTimer = nil;
    }
    
    if(mSwitchVerifyTimer != nil)
    {
        [mSwitchVerifyTimer invalidate];
        mSwitchVerifyTimer = nil;
    }
}

- (void)onClickedNews
{
    // check login state
    if ([Common getUserInfo] == nil)
    {
        // go to login view controller
       // Drv_LoginViewController *viewController = (Drv_LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
        
        
        LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
        [self presentViewController:loginVC animated:YES completion:nil];
    }
    else
    {
        // go to news manage view controller
//        Drv_NewsMgrViewController *viewController = (Drv_NewsMgrViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"newsmgr"];
//        
//        [self presentViewController:viewController animated:YES completion:nil];
		NewsMgrVC *news =[[NewsMgrVC alloc]initWithNibName:@"NewsMgrView" bundle:nil];
		news.announcement_count = anc_count;
		news.ordernotify_count = order_count;
		news.personnotify_count = person_count;
		[self presentViewController:news animated:YES completion:nil];
    }
}

- (IBAction)onClickedDriverVerify:(id)sender
{
    if ([Common getUserInfo] == nil)
    {
        // go to login view controller
       // Drv_LoginViewController *viewController = (Drv_LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
        
        LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
        [self presentViewController:loginVC animated:YES completion:nil];
    }else
    {
     Drv_VerifyCarFstViewController *controller = (Drv_VerifyCarFstViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"driver_verify_1"];
        controller.hidesBottomBarWhenPushed =YES;
        [self.navigationController pushViewController:controller animated:YES] ;
        //presentModalViewController:controller animated:YES];
    }
    
   
}

- (IBAction)onClickedPersonVerify:(id)sender
{
    if ([Common getUserInfo] == nil)
    {
        // go to login view controller
      //  Drv_LoginViewController *viewController = (Drv_LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
        
        LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
        [self presentViewController:loginVC animated:YES completion:nil];
    }else
    {
        UIStoryboard *story =[UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
        Cus_VerifyUserViewController *cus = [story instantiateViewControllerWithIdentifier:@"person_verify"];
        cus.hidesBottomBarWhenPushed =YES;
        [self.navigationController pushViewController:cus animated:YES];
    }
}
/**
 *  点击发布长途
 *
 *  @param sender <#sender description#>
 */
- (IBAction)onClickedLongOrder:(id)sender
{
    // check login state
    STUserInfo *userInfo =[Common getUserInfo];
    if (userInfo == nil)
    {
        // go to login view controller
        //Drv_LoginViewController *viewController = (Drv_LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
        
        LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
        [self presentViewController:loginVC animated:YES completion:nil];
    }
    else
    {
        MyLog(@"%d",userInfo.driver_verified);

		if(userInfo.driver_verified == 1)
        {
			// go to search order view controller
			Drv_publishLongOrderViewController *viewController = [self.storyboard instantiateViewControllerWithIdentifier:@"PublishLongOrder"];
			viewController.hidesBottomBarWhenPushed =YES;
			[self.navigationController pushViewController:viewController animated:YES];
            //[self presentViewController:viewController animated:YES completion:nil];
        }
		else
        {
            UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"对不起" message:@"未验证车主身份不能抢单" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
            alert.tag = 101;
            [alert show];
        }
    }

}

/**
 *
 person_verified : 是否个人认证通过(int)
 (0 : 未通过, 1 : 已通过, 2 : 审核中)
 driver_verified : 是否车主信息认证通过(int)
 (0 : 未通过, 1 : 已通过, 2 : 审核中)
 
 *
 *  @param sender
 */

- (IBAction)onClickedShortOrder:(id)sender
{
    // check login state
    STUserInfo *userInfo =[Common getUserInfo];
    if (userInfo == nil)
    {
        // go to login view controller
    //Drv_LoginViewController *viewController = (Drv_LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
        
        LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
        [self presentViewController:loginVC animated:YES completion:nil];
    }
    else
    {
     
        
     //   MyLog(@"%d",userInfo.driver_verified);
        if(userInfo.driver_verified == 1)
        {
            // go to search order view controller
            Drv_SearchOrderViewController *viewController = [self.storyboard instantiateViewControllerWithIdentifier:@"searchorder"];
            
            
            viewController.hidesBottomBarWhenPushed =YES;
            [self.navigationController pushViewController:viewController animated:YES];
            //[self presentViewController:viewController animated:YES completion:nil];
        }else
        {
           UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"对不起" message:@"未验证车主身份不能抢单" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
            alert.tag = 101;
            [alert show];
          }
    }
}

- (void) reportPosTimer:(NSTimer *)timer
{
    if(mNowPos.longitude <= 0.0)
    {
//        mNowPos = mMapView.userLocation.coordinate;
        mNowPos = mMapView.region.center;
    }
    if([Common getUserInfo] != nil &&
       mNowPos.longitude > 0.0)
    {
		TEST_NETWORK_RETURN
		
        [[CommManager getCommMgr] accountSvcMgr].delegate = self;
        [[[CommManager getCommMgr] accountSvcMgr] ReportDriverPos:[Common getUserInfo].userid latitude:self.mNowPos.latitude longitude:self.mNowPos.longitude];
        
        [[CommManager getCommMgr] orderSvcMgr].delegate = self;
        [[[CommManager getCommMgr] orderSvcMgr] GetLatestAcceptableOnceOrders:[Common getUserID] LimitId:mSingleLastOrderID OrderType:3 DevToken:[Common getDeviceMacAddress]];
    }
    if (orderDic ==nil) {
        orderDic = [[NSMutableDictionary alloc]init];
    }
}

#pragma mark - NearPoint ///////////////////
-(void)nearCustomer:(CLLocationCoordinate2D)pos
{
    int rlat = arc4random()%10;
    int rlon = arc4random()%10;
    int i = arc4random()%10;
    NSString *strLat=nil;
    NSString *strLon=nil;
    if (startAddress.length ==0) {
        if(i%2 ==0 )
        {
            strLat = [NSString stringWithFormat:@"0.%.3d",rlat];
            strLon = [NSString stringWithFormat:@"0.%.3d",rlon];
        }else if(i%3 ==0 )
        {
            strLat = [NSString stringWithFormat:@"0.%.3d",rlat];
            strLon = [NSString stringWithFormat:@"-0.%.3d",rlon];
        }else
        {
            strLat = [NSString stringWithFormat:@"-0.%.3d",rlat];
            strLon = [NSString stringWithFormat:@"-0.%.3d",rlon];
        }
    }else
    {
        if(i%2 ==0 )
        {
            strLat = [NSString stringWithFormat:@"0.%.2d",rlat];
            strLon = [NSString stringWithFormat:@"0.%.2d",rlon];
        }else if(i%3 ==0 )
        {
            strLat = [NSString stringWithFormat:@"0.%.2d",rlat];
            strLon = [NSString stringWithFormat:@"-0.%.2d",rlon];
        }else
        {
            strLat = [NSString stringWithFormat:@"-0.%.2d",rlat];
            strLon = [NSString stringWithFormat:@"-0.%.2d",rlon];
        }
    }
    
    double newlat =pos.latitude- [strLat doubleValue];
    double newlon =pos.longitude- [strLon doubleValue];
    
    CLLocationCoordinate2D cllocation;
    cllocation.longitude = newlon;
    cllocation.latitude = newlat;
    
    BMKReverseGeoCodeOption *rever = [[BMKReverseGeoCodeOption alloc]init];
    rever.reverseGeoPoint = cllocation;
    
    NSNumber *startLat = [NSNumber numberWithDouble:cllocation.latitude];
    NSNumber *startLog = [NSNumber numberWithDouble:cllocation.longitude];
    
    if (startAddress.length ==0) {
        [orderDic setValue:startLat forKey:@"startlat"];
        [orderDic setValue:startLog forKey:@"startlog"];
    }else
    {
        [orderDic setValue:startLat forKey:@"endlat"];
        [orderDic setValue:startLog forKey:@"endlog"];
    }
    
    BOOL flage = [_geo_search reverseGeoCode:rever];
    NSLog(@"===%@",flage?@"YES":@"NO");
}

- (void)switchVerifyTimer:(NSTimer *)timer
{
    
    if(_vwDriverVerify.frame.origin.x == 0)
    {
        _vwPersonVerify.frame = CGRectMake(-320, 0, 320, 360);
        [UIView animateWithDuration:0.5 animations:^{
            _vwDriverVerify.frame = CGRectMake(320, 0, 320, 36);
            _vwPersonVerify.frame = CGRectMake(0, 0, 320, 36);
        }];
    }
    else
    {
        _vwDriverVerify.frame = CGRectMake(-320, 0, 320, 360);
        [UIView animateWithDuration:0.5 animations:^{
            _vwDriverVerify.frame = CGRectMake(0, 0, 320, 36);
            _vwPersonVerify.frame = CGRectMake(320, 0, 320, 36);
        }];
    }
    
}

//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Auto Login Relation


- (void)checkAutoLogin
{
    // check login state
//    if ([Common getUserInfo] != nil) {
//        return;
//    }
    
    [self callLoginByDevToken];
}

/**
 * call login user info by devtoken service
 */
- (void) callLoginByDevToken
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];

	TEST_NETWORK_RETURN;

	// Call get login user info by devtoken service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] GetLoginInfoFromDevToken:[Common getDeviceMacAddress]];
}

- (void) loginInfoFromDevTokenResult:(NSString *)result userinfo:(STUserInfo *)userinfo exeinfo:(STExeOrderInfo *)exeinfo
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];
        
        // set user info
        [Common setUserInfo:userinfo];
        [self checkVerifiedState];
        // check executing order info
        if (exeinfo.orderId > 0) {
            // go to perform view
            Drv_OrderPerformViewController *controller = (Drv_OrderPerformViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"orderperform"];
            controller.mOrderId = exeinfo.orderId;
            controller.mOrderType = exeinfo.orderType;
            controller.mCurRunDistance = exeinfo.runDistance;
            controller.mTotalTime = exeinfo.runTime;

			SHOW_VIEW(controller);
        }

		TEST_NETWORK_RETURN
		
		CommManager* commMgr = [CommManager getCommMgr];
		MainSvcMgr* mainSvcMgr = commMgr.mainSvcMgr;
		mainSvcMgr.delegate = self;
		[mainSvcMgr hasNewsWithUserID:[Common getUserID] city:[Common getCurrentCity] driververif:userinfo.driver_verified lastannounceid:0 lastordernotifid:0 lastpersonnotifid:0 devtoken:[Common getDeviceMacAddress]];

    }
    else
    {
        [SVProgressHUD dismiss];
    }
}

- (void) reportDriverPosResult : (NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        MyLog(@"Driver position reporting success!");
    }
    else
    {
        MyLog(@"Driver position reporting fail!");
    }
}


#pragma mark BMKMapViewDelegate Methods
-(void)didUpdateBMKUserLocation:(BMKUserLocation *)userLocation
{
//    MyLog(@"Driver didUpdateUserLocation lat %f,long %f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude);
    
    self.mNowPos = userLocation.location.coordinate;
    
    if ([Common getCurrentCity].length ==0) {
        BMKReverseGeoCodeOption *rever = [[BMKReverseGeoCodeOption alloc]init];
        rever.reverseGeoPoint = userLocation.location.coordinate;
        BOOL flage = [self.geo_search reverseGeoCode:rever];
        if (flage == NO) {
            [self disMapViewGPS];
        }
    }
}
- (void)didUpdateUserLocation:(BMKUserLocation *)userLocation
{
    
}
- (void)onGetReverseGeoCodeResult:(BMKGeoCodeSearch *)searcher result:(BMKReverseGeoCodeResult *)result errorCode:(BMKSearchErrorCode)error
{
    [Common setCurrentCity:result.addressDetail.city];
    [Common setCurrentAdress:result.address];
    
    if (flageStartOrder ==NO) {
        if (startAddress.length ==0){
            startAddress = result.address;
            [self nearCustomer:self.mNowPos];
            [orderDic setValue:startAddress forKey:@"startAddress"];
        }else if(startAddress.length !=0)
        {
            endAddress = result.address;
            [orderDic setValue:endAddress forKey:@"endAddress"];
            if (orderDic !=nil) {
                NearOrder *nearO = [[NearOrder alloc]init];
                [nearO nearCustomerOrder:orderDic :self.mNowPos];
            }
            endAddress =@"";
            startAddress =@"";
            flageStartOrder = YES;
            [[SoundTools sharedSoundTools]playSoundWithFileName:@"5103.wav"];
        }
    }
    
}

#pragma OrderSvcMgr Delegate Methods
- (void) getNearbyDriversResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
	
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
	
	if (anc_count > 0 || order_count > 0 || person_count > 0)
	{
		_imgBadge.hidden = NO;
	}
	else
	{
		_imgBadge.hidden = YES;
	}
}
-(void)disMapViewGPS
{
    NSString *url = [NSString stringWithFormat:@"http://api.map.baidu.com/geocoder?location=%f,%f",self.mNowPos.latitude,self.mNowPos.longitude];
    NSURLRequest* request = [NSURLRequest requestWithURL:[NSURL URLWithString:url]];
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    NSString* string = [[NSString alloc] initWithData:response encoding:NSUTF8StringEncoding];
    NSRange range1 = [string rangeOfString:@"<formatted_address>"];
    NSRange range2 = [string rangeOfString:@"</formatted_address>"];
    NSString* string1 = [string substringWithRange:NSMakeRange(range1.location+range1.length, range2.location-range1.location-range1.length) ];
    NSString* address = [NSString stringWithFormat:@"%@",string1];
    
    NSRange range3 = [string rangeOfString:@"<city>"];
    NSRange range4 = [string rangeOfString:@"</city>"];
    NSString *string3=[string substringWithRange:NSMakeRange(range3.location+range3.length, range4.location-range3.location-range3.length) ];
    if (address.length != 0  && ![address isEqualToString:@"(null)"]&&![address isEqualToString:@"<null>"])
    {
        [Common setCurrentCity:string3];
        [Common setCurrentAdress:string1];
    }
}



- (IBAction)onClickPhoto:(id)sender
{
	STUserInfo* userinfo = [Common getUserInfo];
	if (userinfo == nil || userinfo.userid <= 0) {
    
        LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
       // loginVC.mTargetTab =mCurTab;
        [self presentViewController:loginVC animated:YES completion:nil];

	}else
    {
	  RDActionSheet *actionSheet = [RDActionSheet alloc];
	   if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]) {
		[actionSheet initWithCancelButtonTitle:@"取消"
							primaryButtonTitle:nil
							destroyButtonTitle:nil
							 otherButtonTitles:@"相册", @"拍照", nil];
	  }
	  else
      {
		[actionSheet initWithCancelButtonTitle:@"取消"
							primaryButtonTitle:nil
							destroyButtonTitle:nil
							 otherButtonTitles:@"相册",nil];
	   }


	actionSheet.callbackBlock = ^(RDActionSheetResult result, NSInteger buttonIndex) {
		switch (result) {
			case RDActionSheetButtonResultSelected:
			{
				NSLog(@"Pressed %i", buttonIndex);

				UIImagePickerController *picker = [[NonRotateImagePickerController alloc] init];
				//                picker.delegate = [HuiYuanZhongXinViewController sharedInstance];
				picker.delegate = self;
				picker.allowsEditing = YES;

				if (buttonIndex == 0)
					picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
				else
					picker.sourceType = UIImagePickerControllerSourceTypeCamera;

				if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPad) {
					//picker.modalInPopover = YES;
					UIPopoverController *popover = [[UIPopoverController alloc] initWithContentViewController:picker];
					//                    popover.delegate = [HuiYuanZhongXinViewController sharedInstance];
					popover.delegate = self;
					[popover presentPopoverFromRect:CGRectMake(90, 150, 270, 300)
											 inView:self.view
						   permittedArrowDirections:UIPopoverArrowDirectionLeft
										   animated:YES];
					//[popover setPopoverContentSize:CGSizeMake(500, 500)];
				} else {
					//                    [self presentModalViewController:picker animated:YES];
					[self presentViewController:picker animated:YES completion:nil];
				}
				break;
			}
			case RDActionSheetResultResultCancelled:
				NSLog(@"Sheet cancelled");
				break;
		}
	  };
	  [actionSheet showFrom:self.tabBarController.view];
    }
}



- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
	
	UIImage *srcImage = [info objectForKey:UIImagePickerControllerEditedImage];
	UIImage *orgImage = [info objectForKey:UIImagePickerControllerOriginalImage];
	if (srcImage == nil)
		srcImage = orgImage;

	//UIImage *image = [srcImage imageByScalingAndCroppingForSize:CGSizeMake(128, 128)];
	UIImage * image = srcImage;

	if (image != nil)
	{
		user_photo = image;
        //未确定修改成功地情况下不更换图片
        //[_imgUser setImage:user_photo];

        NSData *imageData=UIImageJPEGRepresentation(user_photo, 0.1f);
		NSString * base64Image = [Common base64forData:imageData];

		[self callUpdateUserPhoto:base64Image];
	}
	else
	{
		[self.view makeToast:STRING_DATAMANAGER_PHOTOSIZE duration:DEF_DELAY position:@"center"];
	}

	//	[picker dismissModalViewControllerAnimated:YES];
	[picker dismissViewControllerAnimated:YES completion:nil];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
	//	[picker dismissModalViewControllerAnimated:YES];
	[picker dismissViewControllerAnimated:YES completion:nil];
}

- (BOOL)popoverControllerShouldDismissPopover:(UIPopoverController *)popoverController {
	return YES;
}


- (void)callUpdateUserPhoto:(NSString*)base64_img {
	STUserInfo* userinfo = [Common getUserInfo];
	if (userinfo == nil) {
		return;
	}

	TEST_NETWORK_RETURN
	
	[SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];

	CommManager* commMgr = [CommManager getCommMgr];
	AccountSvcMgr* svcMgr = commMgr.accountSvcMgr;
	svcMgr.delegate = self;
	[svcMgr changeUserPhotoWithUserID:userinfo.userid photo:base64_img devtoken:[Common getDeviceMacAddress]];
}


- (void)changeUserPhotoResult:(int)retcode retmsg:(NSString *)retmsg photo_url:(NSString *)photo_url {
	[SVProgressHUD dismiss];

	if (retcode == SVCERR_SUCCESS) {
        [_imgUser setImage:user_photo];
        STUserInfo * userInfo = [Common getUserInfo];
        userInfo.photo = photo_url;

		[self.view makeToast:@"修改成功" duration:2 position:@"center"];
	} else {
		[self.view makeToast:retmsg duration:2 position:@"center"];
	}
}

-(void)getLatestAppVersion:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        [SVProgressHUD dismiss];
        NSDictionary *dic = [dataList objectAtIndex:0];
        NSString *version  = [dic objectForKey:@"latestver"];
        versionURL = [dic objectForKey:@"downloadurl"];
        NSString *version1 = [[[NSBundle mainBundle] infoDictionary] objectForKey:(NSString *)kCFBundleVersionKey];
        if ([version doubleValue]-[version1 doubleValue]>0) {
            UIAlertView *alert =[[UIAlertView alloc]initWithTitle:@"提示!" message:@"有新的版本需要更新" delegate:self cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
            alert.tag =100;
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
    if (alertView.tag ==100) {
      //  [[UIApplication sharedApplication] openURL:[NSURL URLWithString:versionURL]];
        NSURL *iTunesURL = [NSURL URLWithString:versionURL];
        
        UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
        [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:iTunesURL]];
        [ self.view addSubview:phoneCallWebView];
    }
    if (alertView.tag == 101) {
        if ([Common getUserInfo] == nil)
        {
            // go to login view controller
            // Drv_LoginViewController *viewController = (Drv_LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
            
            LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
            [self presentViewController:loginVC animated:YES completion:nil];
        }else
        {
            Drv_VerifyCarFstViewController *controller = (Drv_VerifyCarFstViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"driver_verify_1"];
            controller.hidesBottomBarWhenPushed =YES;
            [self.navigationController pushViewController:controller animated:YES] ;
            //presentModalViewController:controller animated:YES];
        }
    }
    
}

-(void)getLatestAcceptableOnceOrdersResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        // [self.mSTOrderArray removeAllObjects];
        
        if([dataList count] > 0)
        {
            STSingleTimeOrderInfo *lastOrder = [dataList objectAtIndex:0];
            mSingleLastOrderID = lastOrder.uid;
            [[SoundTools sharedSoundTools]playSoundWithFileName:@"5103.wav"];
            flageOrder = 0;
        }else
        {
            //制作假订单
//            flageOrder ++;
//            if (flageOrder == 30) {
//                flageOrder = 0;
//                flageStartOrder = NO;
//                [self nearCustomer:self.mNowPos];
//            }
        }

    }

    
}

@end

