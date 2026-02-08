//
//  AppMgrViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//  乘客首页界面

#import "Cus_MainMgrViewController.h"
#import "Config.h"
#import "Cus_NewsMgrViewController.h"
//#import "Cus_LoginViewController.h"
#import "Cus_grabLongWayOrderViewController.h"
#import "Cus_VerifyUserViewController.h"
#import "LoginVC.h"
#import "NewsMgrVC.h"
#import "Cus_PubShortOrderVC.h"
#import "NearDriver.h"

@interface Annotation : NSObject <BMKAnnotation>
@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *subtitle;
@property (nonatomic, assign) NSInteger orderIndex;
@property (nonatomic, copy) NSString *orderText;

- (id)initWithLocation:(CLLocationCoordinate2D)coord;
- (void)setCoordinate:(CLLocationCoordinate2D)newCoordinate;
@end


@implementation Annotation
@synthesize coordinate;
@synthesize title;
@synthesize subtitle;
@synthesize orderIndex;
@synthesize orderText;

- (id)initWithLocation:(CLLocationCoordinate2D)coord {
	self = [super init];
	if (self) {
		coordinate = coord;
        
	}
	return self;
}

- (void)setCoordinate:(CLLocationCoordinate2D)newCoordinate {
	coordinate = newCoordinate;
}
@end


@interface Cus_MainMgrViewController ()

@property(nonatomic,strong) BMKGeoCodeSearch *geo_search; //    物理地址
@property(nonatomic,strong) BMKReverseGeoCodeOption *rever;
@property(nonatomic,strong)NSTimer *localServiceTiemer;
@property(nonatomic,strong)NSTimer *lSTiemer_long;
@property(nonatomic,assign)BOOL startornot;
@property(nonatomic,strong) BMKUserLocation *userLocation;

@end


#define  MMLastLongitude @"MMLastLongitude"
#define  MMLastLatitude  @"MMLastLatitude"
#define  MMLastCity      @"MMLastCity"
#define  MMLastAddress   @"MMLastAddress"


@implementation Cus_MainMgrViewController

//@synthesize mapView;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)loadView
{
    [self checkAutoLogin];
    [super loadView];
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


	// self.hidesBottomBarWhenPushed =YES;
}
/**
 *  storyBoard 模式连线下隐藏底部tab
 *
 *  @param segue  segue description
 *  @param sender sender description
 */
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    [segue.destinationViewController setHidesBottomBarWhenPushed:YES];
  //  [segue.destinationViewController setTranslucent:YES];
    UIViewController *vc =segue.destinationViewController;
    
    [vc.navigationController.navigationBar setTranslucent:YES];
    
}

- (BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender
{
  if([identifier isEqualToString:@"LongOrderCall"])
  {
      if ([Common getUserInfo] == nil)
      {
          LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
          
          [self presentViewController:loginVC animated:YES completion:nil];
          return NO;
          
      }else{
          return YES;
      }

  }
    return YES;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

	_imgBadge.hidden = YES;
    
    if (iOS8) {
        //由于IOS8中定位的授权机制改变 需要进行手动授权
        locationManager = [[CLLocationManager alloc] init];
//        locationManager.delegate = self;
        //获取授权认证
        [locationManager requestAlwaysAuthorization];
        [locationManager requestWhenInUseAuthorization];
    }
    [self initControls];
    // Do any additional setup after loading the view.
//      [NSTimer alloc]initWithFireDate:(NSDate *) interval:2.0 target:self selector:@selector(selector) userInfo:nil repeats:YES
    
    //[NSTimer]
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
//    if ([SDiPhoneVersion deviceVersion] == 8) {
//        [cus_MapView setFrame:CGRectMake(-200,-320,1080/2+240,1920/2)];
//    }
}
-(void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    STUserInfo * userInfo = [Common getUserInfo];
    if (userInfo != nil)
    {
		if(userInfo.person_verified == 1) {
			[_vwValidate setHidden:YES];
            [_vwValidate1 setHidden:YES];
		}else
        {
            [_vwValidate1 setHidden:NO];
            [_vwValidate  setHidden:NO];
            if (vwTimer !=nil) {
                [vwTimer invalidate];
                vwTimer = nil;
            }
            [_vwValidate1 setFrame:CGRectMake(-320, 5, 320, 36)];
            if (vwTimer !=nil) {
                [vwTimer invalidate];
                vwTimer = nil;
            }
            vwTimer = [NSTimer scheduledTimerWithTimeInterval:5 target:self selector:@selector(switchVerifyTimer) userInfo:nil repeats:YES];
        }
	}
    
	[cus_MapView viewWillAppear];
	cus_MapView.delegate = self; // 此处记得不用的时候需要置nil，否则影响内存的释放
    cus_MapView.showsUserLocation = YES;
    
	locService.delegate = self;
	[locService startUserLocationService];

	// [self startGetNearbyDrivers];
	if (userInfo != nil)
	{
		TEST_NETWORK_RETURN;
		CommManager* commMgr = [CommManager getCommMgr];
		MainSvcMgr* mainSvcMgr = commMgr.mainSvcMgr;
		mainSvcMgr.delegate = self;
		[mainSvcMgr hasNewsWithUserID:[Common getUserID] city:[Common getCurrentCity] driververif:userInfo.driver_verified lastannounceid:0 lastordernotifid:0 lastpersonnotifid:0 devtoken:[Common getDeviceMacAddress]];
	}
	else
	{
		_imgBadge.hidden = YES;
	}
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] GetLatestAppVersion:[Common getDeviceMacAddress]];
}


-(void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    [cus_MapView viewWillDisappear];
    cus_MapView.delegate = nil; // 不用时，置nil
    locService.delegate = nil;

//	_imgBadge.hidden = YES;
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
#pragma mark - Initialize data info
- (void) initControls
{
    STUserInfo * userInfo = [Common getUserInfo];
    if(userInfo != nil)
    {
        if(userInfo.person_verified == 1) {
            [_vwValidate setHidden:YES];
            [_vwValidate1 setHidden:YES];
        }else
        {
            [_vwValidate1 setHidden:NO];
            [_vwValidate  setHidden:NO];
        }
    }else
    {
        [_vwValidate setHidden:YES];
        [_vwValidate1 setHidden:YES];
    }
    
    cus_MapView.mapType=BMKMapTypeStandard;
    cus_MapView.zoomLevel = 14.0;
    
    
    locService = [[BMKLocationService alloc]init];
    [locService startUserLocationService];
    cus_MapView.showsUserLocation = NO;
    cus_MapView.userTrackingMode = BMKUserTrackingModeNone;
    cus_MapView.showsUserLocation = YES;
    
    //    物理地址
    self.geo_search =[[BMKGeoCodeSearch alloc]init];
    self.geo_search.delegate=self;
    self.rever = [[BMKReverseGeoCodeOption alloc]init];

    [self updateUI];
    // = [[NSBundle mainBundle] pathForResource: ofType:];

    //NSString *path =[[NSBundle mainBundle].bundlePath stringByAppendingPathComponent:@"Sounds.bundle"];

   

    
   // [plistFile objectAtIndex:0];
/*  奇葩需求测试
   //  NSString *path  =[[NSBundle mainBundle] pathForResource:@"plist" ofType:@"bundle"];
    NSString *plistPath =[path stringByAppendingPathComponent:@"invite.plist"];
        NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
    MyLog(@"%@", data);//直接打印数据。
    NSString  *invite =[data objectForKey:@"InviteCode"];
    UIAlertView *alert =[[UIAlertView alloc]initWithTitle:@"邀请码" message:invite delegate:self cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
    [alert show];
    */
}
/**
 *  定时器触发事件，根据布尔值改变定时器下一次的触发时间
 */

- (void) updateUI
{
    //    BMKPointAnnotation* annotation = [[BMKPointAnnotation alloc]init];
//	CLLocationCoordinate2D coor;
//	coor.latitude = mDataInfo.latitude;
//	coor.longitude = mDataInfo.longitude;
//	annotation.coordinate = coor;
//    
//    //    annotation.title = mProdDetail.name;
//	annotation.subtitle = mDataInfo.name;
//	[mapView addAnnotation:annotation];
//    ``
//    BMKCoordinateRegion region = BMKCoordinateRegionMakeWithDistance(CLLocationCoordinate2DMake(mDataInfo.latitude, mDataInfo.longitude), 1500, 1500);
//    [mapView setRegion:region];
    
    
}

/**
 *  获取附近拼车车主的方法
    显示HUD，然后发起网络请求
 */
-(void) startGetNearbyDrivers
{  
	TEST_NETWORK_RETURN;
    [SVProgressHUD show];
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] GetNearbyDrivers:[Common getUserID] latitude:myCoord.latitude longitude:myCoord.longitude];
}

/////////////////////////////////////////////////////////////////////////////
#pragma mark - BMKMapViewDelegate Methods


#pragma mark 地图长按事件
-(void)mapview:(BMKMapView *)mapView onLongClick:(CLLocationCoordinate2D)coordinate
{
    MyLog(@"长按");
}
#pragma mark 地图双击手势
- (void)mapview:(BMKMapView *)mapView onDoubleClick:(CLLocationCoordinate2D)coordinate
{
    MyLog(@"双击");
}
#pragma mark 在开时定位时调用
- (void)willStartLocatingUser
{
    MyLog(@"开始定位");
}
#pragma mark 在停止定位后，会调用此函数
- (void)didStopLocatingUser
{
    MyLog(@"定位结束");
    
}

#pragma mark 在地图View将要启动定位时，会调用此函数
- (void)mapViewWillStartLocatingUser:(BMKMapView *)mapView
{
	MyLog(@"start locate");
}
#pragma mark 用户方向更新后，会调用此函数
- (void)didUpdateUserHeading:(BMKUserLocation *)userLocation
{
//    [mapView updateLocationData:userLocation];
//    
//    //地理反编码 获取实际地址
    if (userLocation.location.coordinate.latitude != 0) {
        self.rever.reverseGeoPoint = userLocation.location.coordinate;
        [cus_MapView updateLocationData:userLocation];
    }else
    {
        self.rever.reverseGeoPoint = self.userLocation.location.coordinate;
        [cus_MapView updateLocationData:self.userLocation];
    }
   MyLog(@"didUpdateUserHeading: %d",[self.geo_search reverseGeoCode:self.rever]);
    
   //[locService stopUserLocationService];
}
#pragma mark 用户位置更新后，会调用此函数
-(void)didUpdateBMKUserLocation:(BMKUserLocation *)userLocation
{
    self.rever.reverseGeoPoint = userLocation.location.coordinate;
    MyLog(@"callreverseGeoCode: %d",[self.geo_search reverseGeoCode:self.rever]);
    
    MyLog(@"didUpdateUserLocation lat %f,long %f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude);
    
    self.userLocation =userLocation;
    //设置地图显示区域
    BMKCoordinateRegion region;
    region.center.latitude  = userLocation.location.coordinate.latitude;
    region.center.longitude = userLocation.location.coordinate.longitude;
    region.span.latitudeDelta  = 0.02;
    region.span.longitudeDelta = 0.02;
    if (cus_MapView)
    {
        [cus_MapView setRegion:region animated:TRUE];
    }
    //    cus_MapView.zoomLevel = 14.0;
    // set updated user location
    CLLocation * newLocation = userLocation.location;
    myCoord = newLocation.coordinate;
    [Common setCurrentPosLat:myCoord.latitude andLng:myCoord.longitude];
    //获取附近的车主
    [self startGetNearbyDrivers];
    //地图更新显示
    [cus_MapView updateLocationData:userLocation];
    [locService stopUserLocationService];
}
- (void)didUpdateUserLocation:(BMKUserLocation *)userLocation
{
    
}

#pragma mark 在地图View停止定位后，会调用此函数
- (void)mapViewDidStopLocatingUser:(BMKMapView *)mapView
{
    MyLog(@"stop locate");
}
#pragma mark 定位失败后，会调用此函数
- (void)mapView:(BMKMapView *)mapView didFailToLocateUserWithError:(NSError *)error
{
    MyLog(@"location error");
}
#pragma mark 获取到物理地址信息
-(void)onGetReverseGeoCodeResult:(BMKGeoCodeSearch *)searcher result:(BMKReverseGeoCodeResult *)result errorCode:(BMKSearchErrorCode)error
{
    [Common  setCurrentAdress:result.address];
    [Common setCurrentCity:result.addressDetail.city];
    MyLog(@"onGetReverseGeoCodeResult %@",result.address);
}
#pragma mark 地图改变完成
-(void)mapView:(BMKMapView *)mapView regionDidChangeAnimated:(BOOL)animated
{
    BOOL flage = [self.geo_search reverseGeoCode:self.rever];
    if (flage ==NO) {
        [self disMapViewGPS];
    }
    MyLog(@"改变完成");
}


/////-----------

- (BMKAnnotationView *) mapView:(BMKMapView *)mapView viewForAnnotation:(id <BMKAnnotation>) annotation{
//    if ([annotation isKindOfClass:[BMKPointAnnotation class]]) {
//		BMKPinAnnotationView *newAnnotation = [[BMKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"company"];
//		newAnnotation.pinColor = BMKPinAnnotationColorPurple;
//		newAnnotation.animatesDrop = YES;
//        newAnnotation.image =[UIImage imageNamed:@"driverAnnotation"];
//		newAnnotation.draggable = NO;
//		
//		return newAnnotation;
//	}
    
    if ([annotation isKindOfClass:[Annotation class]])
	{
		static NSString *AnnotationViewID = @"annotationViewID";
        
		BMKAnnotationView *annotationView = [mapView dequeueReusableAnnotationViewWithIdentifier:AnnotationViewID];
		if (annotationView == nil) {
			annotationView = [[BMKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:AnnotationViewID];
			((BMKPinAnnotationView*)annotationView).animatesDrop = YES;
			
           // ((BMKPinAnnotationView*)annotationView).image = [UIImage imageNamed:[NSString stringWithFormat:@"placemark_atm"]];
            annotationView.image =[UIImage imageNamed:@"driverAnnotation"];
            
			UIButton *selectButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
			selectButton.tag = 1000 + ((Annotation*)annotation).orderIndex;
            
			annotationView.rightCalloutAccessoryView = selectButton;
		}
		
//		if ([annotation.title isEqualToString:@""]) {
//			((BMKPinAnnotationView *)annotationView).pinColor = BMKPinAnnotationColorGreen;
//			[annotationView setDraggable:YES];
//			[annotationView setSelected:YES animated:YES];
//		}
//		else {
//			((BMKPinAnnotationView *)annotationView).pinColor = BMKPinAnnotationColorRed;
//		}
		
//        annotationView.frame = CGRectMake(0, 0, 24, 32);
//		annotationView.centerOffset = CGPointMake(0, -(annotationView.frame.size.height * 0.5));
//		annotationView.annotation = annotation;
//		annotationView.canShowCallout = TRUE;
        
		return annotationView;
	}
	return nil;
}


//- (void)onGetAddrResult:(BMKAddrInfo*)result errorCode:(int)error
//{
//	if (error == 0) {
//        mapView.userLocation.title = result.strAddr;
//	}
//}

///////////////////////////////////////////////////////////////////////////////
#pragma mark - UI User Event  角色切换按钮事件
- (void)onClickedSwitch
{
    
    [locService stopUserLocationService];
    locService.delegate = nil;
    locService =nil;
    // change login option
    [Config setLoginOption:LOGIN_OPTION_DRIVER];
    
    UIStoryboard * driverStroyboard = [UIStoryboard storyboardWithName:@"DriverStoryboard" bundle:nil];
    
    // initializing view controller
    UIViewController * controller = [driverStroyboard instantiateInitialViewController];
    controller.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
//    [self presentModalViewController:controller animated:YES];
    
    [self presentViewController:controller animated:YES completion:nil];
    
    [Common setCurrentClientSide:DRIVER_SIDE];    
}
#pragma mark 消息公告
- (void)onClickedNews
{
    // check login state
    if ([Common getUserInfo] == nil)
    {
        // go to login view controller
//        Cus_LoginViewController *viewController = (Cus_LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
//        
//        [self presentViewController:viewController animated:YES completion:nil];
        LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
        [self presentViewController:loginVC animated:YES completion:nil];
        
    }
    else
    {
        // go to news manage view controller
       // Cus_NewsMgrViewController *viewController = (Cus_NewsMgrViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"newsmgr"];

        NewsMgrVC *news =[[NewsMgrVC alloc]initWithNibName:@"NewsMgrView" bundle:nil];
		news.announcement_count = anc_count;
		news.ordernotify_count = order_count;
		news.personnotify_count = person_count;
        [self presentViewController:news animated:YES completion:nil];
    }
}

/**
 *  恢复到用户定位位置显示
 *   APP_USER
 *  @param sender <#sender description#>
 */
- (IBAction)onClickedGPS:(id)sender
{
    [locService startUserLocationService];
    BOOL flage = [self.geo_search reverseGeoCode:self.rever];
    if (flage ==NO) {
        [self disMapViewGPS];
    }
    
//    BMKCoordinateRegion region;
//    region.center.latitude  = self.userLocation.location.coordinate.latitude;
//    region.center.longitude = self.userLocation.location.coordinate.longitude;
//    region.span.latitudeDelta  = 0.02;
//    region.span.longitudeDelta = 0.02;
//    if (mapView)
//    {
//        mapView.region = region;
//        // NSLog(@"当前的坐标是: %f,%f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude);
//    }

}

- (IBAction)onClickedValidate:(id)sender
{
    if ([Common getUserInfo] == nil)
    {
        // go to login view controller
      
        
        LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
        [self presentViewController:loginVC animated:YES completion:nil];
        
    }else
    {
        Cus_VerifyUserViewController *controller = (Cus_VerifyUserViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"person_verify"];
//        [self presentModalViewController:controller animated:YES];
        [self presentViewController:controller animated:YES completion:nil];
    }
}

#pragma mark 市内发单跳转
- (IBAction)onClickedShortOrder:(id)sender
{
    if ([Common getUserInfo] == nil)
    {
        LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
        
        [self presentViewController:loginVC animated:YES completion:nil];

    }else{
      //  Cus_PubShortOrderVC *shortOrderVC =[[Cus_PubShortOrderVC alloc]initWithNibName:@"Cus_PubShortOrderView" bundle:nil];
        Cus_PubShortOrderVC *shortOrderVC =[[Cus_PubShortOrderVC alloc]initWithNibName:@"Cus_PubShortOrderView_New" bundle:nil];
        shortOrderVC.hidesBottomBarWhenPushed =YES;
        shortOrderVC.userLocation =self.userLocation;
        [self.navigationController pushViewController:shortOrderVC animated:YES];
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
        
        
        STUserInfo * userInfo = [Common getUserInfo];
        if (userInfo != nil)
        {
            if(userInfo.person_verified == 1) {
                [_vwValidate setHidden:YES];
                [_vwValidate1 setHidden:YES];
            }else
            {
                [_vwValidate1 setHidden:NO];
                [_vwValidate  setHidden:NO];
                if (vwTimer !=nil) {
                    [vwTimer invalidate];
                    vwTimer = nil;
                }
                [_vwValidate1 setFrame:CGRectMake(-320, 5, 320, 36)];
                if (vwTimer !=nil) {
                    [vwTimer invalidate];
                    vwTimer = nil;
                }
                vwTimer = [NSTimer scheduledTimerWithTimeInterval:5 target:self selector:@selector(switchVerifyTimer) userInfo:nil repeats:YES];
            }
        }
        
		TEST_NETWORK_RETURN;

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
/**
 *  <#Description#>
 *
 *  @param result   <#result description#>
 *  @param dataList dataList description
 */
- (void) getNearbyDriversResult : (NSString *)result dataList:(NSMutableArray *)dataList
{
    NSMutableArray *locs = [[NSMutableArray alloc] init];
    for (id<BMKAnnotation>annot in [cus_MapView annotations])
    {
        //用户本人位置的大头针 不做任何处理
        if([annot isKindOfClass:[BMKUserLocation class]]) {
        }
        else
        {   //车主位置大头针 加入可删除的数组，
            [locs addObject:annot];
        }
    }
    //删除上一次获得的车主位置
    [cus_MapView removeAnnotations:locs];
    //待拼车司机数量显示
    _lblDriverNum.text =[NSString stringWithFormat:@"%d",dataList.count+20];
    
    int orderIndex = 0;
    for(STNearbyDriverInfo *posInfo in dataList)
    {
        Annotation *annotation = [[Annotation alloc] init];
        annotation.coordinate = CLLocationCoordinate2DMake(posInfo.latitude, posInfo.longitude);
        annotation.title = [NSString stringWithFormat:@"%ld", posInfo.driverid];
        annotation.orderIndex = orderIndex;
        annotation.orderText = @"";
        [cus_MapView addAnnotation:annotation];
        
        orderIndex++;
    }
    NearDriver *driver = [[NearDriver alloc]init];
    NSMutableArray *arr = [driver nearDriverFalse:myCoord.latitude :myCoord.longitude];
    for(STNearbyDriverInfo *posInfo in arr)
    {
        Annotation *annotation = [[Annotation alloc] init];
        annotation.coordinate = CLLocationCoordinate2DMake(posInfo.latitude, posInfo.longitude);
        annotation.title = [NSString stringWithFormat:@"%ld", posInfo.driverid];
        annotation.orderIndex = orderIndex;
        annotation.orderText = @"";
        [cus_MapView addAnnotation:annotation];
        
        orderIndex++;
    }
    [SVProgressHUD dismiss];
}

- (void) reportDriverPosResult : (NSString *)result
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
    NSString *url = [NSString stringWithFormat:@"http://api.map.baidu.com/geocoder?location=%f,%f",self.rever.reverseGeoPoint.latitude,self.rever.reverseGeoPoint.longitude];
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
    
    //NSString *iTunesString = [NSString stringWithFormat:versionURL ];
    NSURL *iTunesURL = [NSURL URLWithString:versionURL];
    
    UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:iTunesURL]];
    [ self.view addSubview:phoneCallWebView];
  //  [[UIApplication sharedApplication] openURL:[NSURL URLWithString:versionURL]];
    

}

- (void)switchVerifyTimer
{
    
    if(_vwValidate.frame.origin.x == 0)
    {
        _vwValidate.frame = CGRectMake(0, 5, 320, 360);
        _vwValidate1.frame = CGRectMake(-320, 5, 320, 360);
        [UIView animateWithDuration:0.5 animations:^{
            _vwValidate1.frame = CGRectMake(0, 5, 320, 36);
            _vwValidate.frame = CGRectMake(320, 5, 320, 36);
        }];
    }
    else
    {
        _vwValidate.frame = CGRectMake(-320, 5, 320, 360);
        _vwValidate1.frame = CGRectMake(0, 5, 320, 360);
        [UIView animateWithDuration:0.5 animations:^{
            _vwValidate1.frame = CGRectMake(320, 5, 320, 36);
            _vwValidate.frame = CGRectMake(0, 5, 320, 36);
        }];
    }
    
}

@end
