//
//  Cus_PubOrderWaitingViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/1/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//   乘客发布订单 单次拼车发布 等待车主接单界面

#import "Cus_PubOrderWaitingViewController.h"
#import "Cus_STOrderChangePriceViewController.h"
#import "Cus_STOrderConfirmViewController.h"
#import "HZActivityIndicatorView.h"
#import "NearDriver.h"
@interface Cus_PubOrderWaitingViewController ()
@property (nonatomic, assign)double angle;
@end


@implementation Cus_PubOrderWaitingViewController

@synthesize mOrderId;
@synthesize mPrice;
@synthesize mWaitTime;
@synthesize mDriverLockTime;
@synthesize mRepubTimes;
@synthesize mOrderPubData;

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
    [super loadView];
    
    _lblDriverCnt.text = [NSString stringWithFormat:@"%d",self.drvCount+20];
    //显示用户位置
    _mapView.showsUserLocation = YES;
    //设置显示区域
    BMKCoordinateRegion region;
    region.center.latitude  = self.userLocation.location.coordinate.latitude;
    region.center.longitude = self.userLocation.location.coordinate.longitude;
    region.span.latitudeDelta  = 0.02;
    region.span.longitudeDelta = 0.02; 
    if (_mapView)
    {
        _mapView.region = region;
        // NSLog(@"当前的坐标是: %f,%f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude);
    }
    //地图更新显示
    [_mapView updateLocationData:self.userLocation];
   // 右侧Item 初始化
//    UIView *customView =[[UIView alloc]initWithFrame:CGRectMake(0, 0, 74, 27)];
//    UIButton *cancleBtn =[[UIButton alloc]initWithFrame:CGRectMake(20, 2, 64, 27)];
//    [cancleBtn setBackgroundImage:[UIImage imageNamed:@"btnCancelOrder"] forState:UIControlStateNormal];
//    [cancleBtn addTarget:self action:@selector(onClickedBack) forControlEvents:UIControlEventTouchUpInside];
//    [customView addSubview:cancleBtn];
//    UIBarButtonItem *rightItem =[[UIBarButtonItem alloc]initWithCustomView:customView];
//    self.navigationItem.rightBarButtonItem =rightItem;

}
- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
//    [self initControls];
	TEST_NETWORK_RETURN;
    [self startAnimation];
    
    UIScreen *currentScreen = [UIScreen mainScreen];
    
    if (currentScreen.applicationFrame.size.height<500) {
        jishiView.frame =CGRectMake(jishiView.frame.origin.x, jishiView.frame.origin.y-80, jishiView.frame.size.width, jishiView.frame.size.height);
    }
    [_lblCountDown setTextColor:[UIColor colorWithRed:77/255.0 green:180/255.0 blue:125/255.0 alpha:1]];
    
    UIButton *btn =[UIButton buttonWithType:UIButtonTypeCustom];
    btn.frame = CGRectMake(0, 0, 44, 44);
    [btn setBackgroundColor:[UIColor clearColor]];
    UIBarButtonItem *item = [[UIBarButtonItem alloc]initWithCustomView:btn];
    self.navigationItem.leftBarButtonItem = item;
}
-(void) startAnimation
{
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:0.03];
    [UIView setAnimationDelegate:self];
    [UIView setAnimationDidStopSelector:@selector(endAnimation)];
    loadingImage.transform = CGAffineTransformMakeRotation(_angle * (M_PI / 180.0f));
    [UIView commitAnimations];
}

-(void)endAnimation
{
    _angle += 10;
    [self startAnimation];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self initControls];
    
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] getOnceOrderDriverPos:[Common getUserID] orderid:self.mOrderId devtoken:[Common getDeviceIDForVendor]];
}

- (void)viewDidDisappear:(BOOL)animated
{
	if(mCountDownTimer != nil)
	{
		[mCountDownTimer invalidate];
		mCountDownTimer = nil;
	}
}

-(void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    [_mapView viewWillDisappear];
    _mapView.delegate = nil; // 不用时，置nil
   
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


///////////////////////////////////////////////////////////////////////////
#pragma mark - Basic Function

/**
 * Initilaize all ui controls & member variable
 */
- (void) initControls
{
//    HZActivityIndicatorView *activityIndicator = [[HZActivityIndicatorView alloc] initWithFrame:CGRectMake(16, 22, 0, 0)];
////    activityIndicator = [[HZActivityIndicatorView alloc] initWithFrame:CGRectMake(50, 150, 0, 0)];
//    activityIndicator.backgroundColor = self.view.backgroundColor;
//    activityIndicator.opaque = YES;
//    activityIndicator.steps = 12; //花瓣条数
//    activityIndicator.finSize = CGSizeMake(4, 10); // 花瓣（宽度 长度）
//    activityIndicator.indicatorRadius = 10; // 圆心大小
//    activityIndicator.stepDuration = 0.100; // 转速
//    activityIndicator.color = [UIColor grayColor];
//    activityIndicator.roundedCoreners = UIRectCornerTopRight;
//    activityIndicator.cornerRadii = CGSizeMake(10, 10);
//    [activityIndicator setBackgroundColor:[UIColor clearColor]];
//    [activityIndicator startAnimating];
//    [jishiView addSubview:activityIndicator];
    
    
    _mapView.delegate = self;
    
    piceLable.text =[NSString stringWithFormat:@"%d",_price_add];
    
	if(self.mWaitTime <= 0)
		self.mWaitTime = DEF_WAITING_DELAY;
	//等待倒计时
    [_lblCountDown setText:[NSString stringWithFormat:@"%d", self.mWaitTime]];
    [_lblCountDown setTextColor:[UIColor colorWithRed:77/255.0 green:180/255.0 blue:125/255.0 alpha:1]];
    
    // initialize count down progress
    MDRadialProgressTheme *newTheme = [[MDRadialProgressTheme alloc] init];
	newTheme.completedColor = [UIColor colorWithRed:90/255.0 green:212/255.0 blue:39/255.0 alpha:1.0];
	newTheme.incompletedColor = [UIColor colorWithRed:164/255.0 green:231/255.0 blue:134/255.0 alpha:1.0];
	newTheme.centerColor = [UIColor clearColor];
	newTheme.centerColor = [UIColor colorWithRed:224/255.0 green:248/255.0 blue:216/255.0 alpha:1.0];
	newTheme.sliceDividerHidden = YES;
	newTheme.labelColor = [UIColor blackColor];
	newTheme.labelShadowColor = [UIColor whiteColor];
    newTheme.sliceDividerHidden = YES;

	_pvCountDown.progressTotal = COUNTDOWN_PROGRESS_MAX;
	_pvCountDown.label.hidden = YES;
    [_pvCountDown setTheme:newTheme];

	_pvCountDown.progressCounter = 0;
    
   // _mapView
    // set count down timer
    if (mCountDownTimer !=nil) {
        [mCountDownTimer invalidate];
        mCountDownTimer = nil;
    }
    
    mCountDownTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(countDownFunc) userInfo:nil repeats:YES];
//    [[NSRunLoop currentRunLoop] addTimer:mCountDownTimer forMode:NSDefaultRunLoopMode];
	//网络请求 检查是否有人接单

	TEST_NETWORK_RETURN;

	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
	[[[CommManager getCommMgr] orderSvcMgr] CheckOnceOrderAcceptance:[Common getUserID] OrderId:self.mOrderId Latitude:[Common getCurrentLatitude] Longitude:[Common getCurrentLongitude] DevToken:[Common getDeviceMacAddress]];
}


- (void) countDownFunc
{
    // get remain time
    int curVal = [[_lblCountDown text] intValue];
    
    //
    if (_pvCountDown.progressCounter == COUNTDOWN_PROGRESS_MAX)
    {
        _pvCountDown.progressCounter = 0;
    }
    else
    {
        _pvCountDown.progressCounter = _pvCountDown.progressCounter + 1;
    }
    if (curVal == 180 || curVal == 179) {
        [UIView beginAnimations:nil context:nil];
        [UIView setAnimationDuration:0.2];
        [fabuView setFrame:CGRectMake(0, -150, fabuView.frame.size.width, fabuView.frame.size.height)];
        [UIView commitAnimations];
        [_lblCountDown setTextColor:[UIColor colorWithRed:77/255.0 green:180/255.0 blue:125/255.0 alpha:1]];
    }
    curVal -= 1;
    
    if (curVal == 120 ) {
        if (![jiajiaStr1 isEqualToString:@"3"]) {
            [UIView beginAnimations:nil context:nil];
            [UIView setAnimationDuration:0.2];
            [fabuView setFrame:CGRectMake(0, 64, fabuView.frame.size.width, fabuView.frame.size.height)];
            [UIView commitAnimations];
            
            [tishilabel1 setText:[NSString stringWithFormat:@"%@",_tishi1]];
            tishilabel1.adjustsFontSizeToFitWidth =YES;
            tishilabel1.numberOfLines = 0;
            [tishilabel1 sizeToFit];
            [tishilabel1 setTextColor:[UIColor colorWithRed:113/255.0 green:113/255.0 blue:113/255.0 alpha:1]];
        }
        [_lblCountDown setTextColor:[UIColor colorWithRed:255/255.0 green:165/255.0 blue:0/255.0 alpha:1]];
    }
    if (curVal == 60 ) {
        if (![jiajiaStr1 isEqualToString:@"3"]) {
            [UIView beginAnimations:nil context:nil];
            [UIView setAnimationDuration:0.2];
            [fabuView setFrame:CGRectMake(0, 64, fabuView.frame.size.width, fabuView.frame.size.height)];
            [UIView commitAnimations];
            
            [tishilabel1 setText:[NSString stringWithFormat:@"%@",_tishi2]];
            tishilabel1.adjustsFontSizeToFitWidth =YES;
            
        }
        [_lblCountDown setTextColor:[UIColor colorWithRed:255/255.0 green:0 blue:23/255.0 alpha:1]];
//        [_lblCountDown setTextColor:[UIColor colorWithRed:0 green:128/255.0 blue:35/255.0 alpha:1]];
    }
    [_lblCountDown setText:[NSString stringWithFormat:@"%d", curVal]];
	MyLog(@"count down : %d", curVal);
    
    // stop timer
    if (curVal < 1) {
        [mCountDownTimer invalidate];

		mCountDownTimer = nil;
//        [NSRunLoop currentRunLoop]
        //[self gotoSTConfirm];
		[self gotoChangePrice:TYPE_SINGLE_ORDER];
    }
    //查看车主是否接单
    

	TEST_NETWORK_RETURN;

	[[[CommManager getCommMgr] orderSvcMgr] CheckOnceOrderAcceptance:[Common getUserID] OrderId:self.mOrderId Latitude:[Common getCurrentLatitude] Longitude:[Common getCurrentLongitude] DevToken:[Common getDeviceMacAddress]];
    
}

///////////////////////////////////////////////////////////////////////////////
/**
 * go to confirm view controller
 */
- (void) gotoSTConfirm
{
    UIViewController * curCtrl = (UIViewController *)self.presentingViewController;
    //[self.navigationController pop]
//    [self dismissViewControllerAnimated:NO completion:^{
//        
//        // go to single time order confirm view controller
//        Cus_STOrderConfirmViewController *viewController = (Cus_STOrderConfirmViewController *)[curCtrl.storyboard instantiateViewControllerWithIdentifier:@"PubSTOrderConfirm"];
//		viewController.mDriverInfo = mAcceptedDriver;
//        viewController.mOrderInfo = mAcceptedOnceOrder;
//		
//        CATransition *animation = [CATransition animation]; \
//        [animation setDuration:0.3]; \
//        [animation setType:kCATransitionPush]; \
//        [animation setSubtype:kCATransitionFromRight]; \
//        [animation setTimingFunction:[CAMediaTimingFunction functionWithName: kCAMediaTimingFunctionEaseInEaseOut]]; \
//        [[curCtrl.view.superview layer] addAnimation:animation forKey:@"SwitchToView"];
//        
//        [curCtrl presentViewController:viewController animated:NO completion:nil];
//    }];
      UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
    Cus_STOrderConfirmViewController *viewController = (Cus_STOrderConfirmViewController *)[customerStroyboard instantiateViewControllerWithIdentifier:@"PubSTOrderConfirm"];
    viewController.mDriverInfo = mAcceptedDriver;
    viewController.mOrderInfo = mAcceptedOnceOrder;
	viewController.mWaitTime = mDriverLockTime;
    viewController.mOrderId = self.mOrderId;
    [self presentViewController:viewController animated:YES completion:nil];
}
/*
- (void) gotoWTConfirm
{
    UIViewController * curCtrl = (UIViewController *)self.presentingViewController;
    
    [self dismissViewControllerAnimated:NO completion:^{
        
        // go to single time order confirm view controller
        UIViewController *viewController = (UIViewController *)[curCtrl.storyboard instantiateViewControllerWithIdentifier:@"PubWTOrderConfirm"];
        
        CATransition *animation = [CATransition animation]; \
        [animation setDuration:0.3]; \
        [animation setType:kCATransitionPush]; \
        [animation setSubtype:kCATransitionFromRight]; \
        [animation setTimingFunction:[CAMediaTimingFunction functionWithName: kCAMediaTimingFunctionEaseInEaseOut]]; \
        [[curCtrl.view.superview layer] addAnimation:animation forKey:@"SwitchToView"];
        
        [curCtrl presentViewController:viewController animated:NO completion:nil];
    }];
}
*/
#pragma mark  - 同城发布订单时间到了 没有人抢单 跳转
- (void) gotoChangePrice:(int)order_type
{
    UIViewController * curCtrl = (UIViewController *)self.presentingViewController;
    // getting the customer storyboard
    UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
    
    
    Cus_STOrderChangePriceViewController *viewController = [[Cus_STOrderChangePriceViewController alloc]initWithNibName:@"Cus_STOrderChangePrice" bundle:nil];
    
    viewController.mOrderType = order_type;
    viewController.mPrice = self.mPrice;
    viewController.mRepubTimes = self.mRepubTimes + 1;
    viewController.mOrderPubData = self.mOrderPubData;
    viewController.pubWaitingVC =self;
    viewController.orderId =self.mOrderId;
    viewController.driverCount = self.drvCount;
//    CATransition *animation = [CATransition animation]; \
//    [animation setDuration:0.3]; \
//    [animation setType:kCATransitionPush]; \
//    [animation setSubtype:kCATransitionFromBottom]; \
//    [animation setTimingFunction:[CAMediaTimingFunction functionWithName: kCAMediaTimingFunctionEaseInEaseOut]]; \
//    [[self.view.superview layer] addAnimation:animation forKey:@"SwitchToView"];
    
    [self presentViewController:viewController animated:YES completion:^{
        [mCountDownTimer invalidate];
		mCountDownTimer = nil;
    }];
   // [self.navigationController presentPopupViewController:<#(UIViewController *)#> animated:<#(BOOL)#> completion:<#^(void)completion#>]
    //[self.view]

//    
//    [self dismissViewControllerAnimated:NO completion:^{
//        
//        // go to single time order confirm view controller
//           }];
}

///////////////////////////////////////////////////////////////////////////////
#pragma mark - UI User Event  同城 取消订单
/**   取消订单按钮
 * Back to parent view controller
 */
- (void)onClickedBack
{
//#warning 这里应该有取消订单的网络请求吧
    if (mCountDownTimer !=nil) {
        [mCountDownTimer invalidate];
        mCountDownTimer = nil;
    }
    if (mCountDownTimer !=nil) {
        [mCountDownTimer invalidate];
        mCountDownTimer = nil;
    }
    
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] CancelOnceOrder:[Common getUserID]  OrderId:self.mOrderId DevToken:[Common getDeviceMacAddress]];
    
   // BACK_VIEW;
}
#pragma mark 取消订单回调
-(void)cancelOnceOrderResult:(NSString *)result
{
    NSString *str = [NSString stringWithFormat:@"您周围有%@位待拼车主",_lblDriverCnt.text];
    [SVProgressHUD showSuccessWithStatus:str duration:2];
    
    [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(fanhui) userInfo:nil repeats:NO];
}
//取消订单失败
-(void)duplicateUser:(NSString *)result
{
    [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
}
- (IBAction)upMapView:(id)sender {
    BMKCoordinateRegion region;
    region.center.latitude  = self.userLocation.location.coordinate.latitude;
    region.center.longitude = self.userLocation.location.coordinate.longitude;
    region.span.latitudeDelta  = 0.02;
    region.span.longitudeDelta = 0.02;
    if (_mapView)
    {
        _mapView.region = region;
        // NSLog(@"当前的坐标是: %f,%f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude);
    }
    //地图更新显示
    [_mapView updateLocationData:self.userLocation];
}
#pragma mark ++++ 加价后重新发送
- (IBAction)jiajiaClick:(id)sender {
    UIButton *btn =sender;
    switch (btn.tag) {
        case 0:
        {
            int price = [piceLable.text intValue];
            if (price -_cut_price_range <_add_price_min) {
                piceLable.text = [NSString stringWithFormat:@"%d",_add_price_min];
            }else
            {
                piceLable.text = [NSString stringWithFormat:@"%d",price -_cut_price_range];
            }
            
            self.mOrderPubData.price -= [piceLable.text doubleValue];
        }
            break;
        case 1:
        {
            int price = [piceLable.text intValue];
            if (price +_price_addrange >9999) {
                piceLable.text = @"9999";
            }else
            {
                piceLable.text = [NSString stringWithFormat:@"%d",price +_price_addrange];
            }
            
            self.mOrderPubData.price += [piceLable.text doubleValue];
        }
            break;
        case 2:
        {
            [SVProgressHUD show];
            
            self.mOrderPubData.price +=[piceLable.text doubleValue];
            
            [[CommManager getCommMgr] orderSvcMgr].delegate = self;
            [[[CommManager getCommMgr] orderSvcMgr] publishOnceOrderAddPrice:[Common getUserID] OrderID:self.mOrderId DevToken:[Common getDeviceMacAddress] Price:self.mOrderPubData.price];
//            [[[CommManager getCommMgr] orderSvcMgr] publishOnceOrder:[Common getUserID] StartAddr:self.mOrderPubData.start_addr StartLat:self.mOrderPubData.start_lat StartLng:self.mOrderPubData.start_lng EndAddr:self.mOrderPubData.end_addr EndLat:self.mOrderPubData.end_lat EndLng:self.mOrderPubData.end_lng StartTime:self.mOrderPubData.start_time MidPoints:self.mOrderPubData.mid_points Remark:self.mOrderPubData.remark ReqStyle:self.mOrderPubData.req_style Price:self.mOrderPubData.price City:self.mOrderPubData.city WaitTime:self.mOrderPubData.wait_time DevToken:[Common getDeviceMacAddress]];
        }
            break;
            
        default:
            break;
    }
    
}
-(void)publishOnceOrderAddPrice:(NSString *)result retData:(NSDictionary *)dic
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        [SVProgressHUD dismiss];
        
        if (mCountDownTimer !=nil) {
            [mCountDownTimer invalidate];
            mCountDownTimer = nil;
        }
        if (jiajiaStr1.length !=0) {
            jiajiaStr1 =@"3";
        }
        if (jiajiaStr1.length ==0) {
            jiajiaStr1 =@"1";
        }
        [_lblCountDown setTextColor:[UIColor colorWithRed:77/255.0 green:180/255.0 blue:125/255.0 alpha:1]];
        [_lblCountDown setText:[NSString stringWithFormat:@"%d", self.mWaitTime]];
        
        mCountDownTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(countDownFunc) userInfo:nil repeats:YES];
//        [[NSRunLoop currentRunLoop] addTimer:mCountDownTimer forMode:NSDefaultRunLoopMode];
        //网络请求 检查是否有人接单
        
        TEST_NETWORK_RETURN;
        
        [[CommManager getCommMgr] orderSvcMgr].delegate = self;
        [[[CommManager getCommMgr] orderSvcMgr] CheckOnceOrderAcceptance:[Common getUserID] OrderId:self.mOrderId Latitude:[Common getCurrentLatitude] Longitude:[Common getCurrentLongitude] DevToken:[Common getDeviceMacAddress]];
    }else
    {
        [SVProgressHUD showErrorWithStatus:result duration:DEF_DELAY];
    }
}
#pragma OrderSvcDelegate Methods
- (void) publishOnceOrderResult:(NSString *)result RetData:(STPubOnceOrderRet *)retData
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        _price_add = retData.add_price_default;
        mWaitTime = retData.wait_time;
        _add_price_min = retData.add_price_min;
        _cut_price_range = retData.cut_price_range;
        self.mOrderId = retData.order_id;
        [_lblCountDown setText:[NSString stringWithFormat:@"%d", self.mWaitTime]];
        
        if (mCountDownTimer !=nil) {
            [mCountDownTimer invalidate];
            mCountDownTimer = nil;
        }
        mCountDownTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(countDownFunc) userInfo:nil repeats:YES];
//        [[NSRunLoop currentRunLoop] addTimer:mCountDownTimer forMode:NSDefaultRunLoopMode];
        //网络请求 检查是否有人接单
        
        TEST_NETWORK_RETURN;
        
        [[CommManager getCommMgr] orderSvcMgr].delegate = self;
        [[[CommManager getCommMgr] orderSvcMgr] CheckOnceOrderAcceptance:[Common getUserID] OrderId:self.mOrderId Latitude:[Common getCurrentLatitude] Longitude:[Common getCurrentLongitude] DevToken:[Common getDeviceMacAddress]];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

-(void)fanhui
{
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma OrderSvcMgr Delegate Methods  代理方法回传结果
- (void) checkOnceOrderAcceptanceResult:(NSString *)result DriverInfo:(STDriverInfo *)driverInfo OrderInfo:(STSingleTimeOrderInfo *)orderInfo
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        if (mCountDownTimer !=nil) {
            [mCountDownTimer invalidate];
            mCountDownTimer = nil;
        }
		mAcceptedDriver = driverInfo;
		mAcceptedOnceOrder = orderInfo;
		mAcceptedOnceOrder.uid = self.mOrderId;
        mAcceptedOnceOrder.price =self.mOrderPubData.price;
		[self gotoSTConfirm];  //有人接单，去确认界面
    }
    else
    {
		if(mCountDownTimer != nil)  //判断定时器
		{    // 这样的效果就是反复的网络请求 确实是否有人接单
		}
    }
}
-(void)getGetOnceOrderDriverPos:(NSString *)result dataList:(NSMutableArray *)dataList
{
    NSMutableArray *locs = [[NSMutableArray alloc] init];
    for (id<BMKAnnotation>annot in [_mapView annotations])
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
    [_mapView removeAnnotations:locs];

    int orderIndex = 0;
    for(STNearbyDriverInfo *posInfo in dataList)
    {
        Cus_PubOrderAnnotation *annotation = [[Cus_PubOrderAnnotation alloc] init];
        annotation.coordinate = CLLocationCoordinate2DMake(posInfo.latitude, posInfo.longitude);
        annotation.title = [NSString stringWithFormat:@"%ld", posInfo.driverid];
        annotation.orderIndex = orderIndex;
        annotation.orderText = @"";
        [_mapView addAnnotation:annotation];
        
        orderIndex++;
    }
    NearDriver *driver = [[NearDriver alloc]init];
    for(STNearbyDriverInfo *posInfo in driver.driverArray)
    {
        Cus_PubOrderAnnotation *annotation = [[Cus_PubOrderAnnotation alloc] init];
        annotation.coordinate = CLLocationCoordinate2DMake(posInfo.latitude, posInfo.longitude);
        annotation.title = [NSString stringWithFormat:@"%ld", posInfo.driverid];
        annotation.orderIndex = orderIndex;
        annotation.orderText = @"";
        [_mapView addAnnotation:annotation];
        
        orderIndex++;
    }
}

#pragma  marik BMK - delegate
- (BMKAnnotationView *) mapView:(BMKMapView *)mapView viewForAnnotation:(id <BMKAnnotation>) annotation
{
    
    if ([annotation isKindOfClass:[Cus_PubOrderAnnotation class]])
    {
        NSString *AnnotationViewID = @"annotationViewID";
        
        BMKAnnotationView *annotationView = [mapView dequeueReusableAnnotationViewWithIdentifier:AnnotationViewID];
        if (annotationView == nil) {
            annotationView = [[BMKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:AnnotationViewID];
            ((BMKPinAnnotationView*)annotationView).animatesDrop = YES;
            
            // ((BMKPinAnnotationView*)annotationView).image = [UIImage imageNamed:[NSString stringWithFormat:@"placemark_atm"]];
            annotationView.image =[UIImage imageNamed:@"driverAnnotation"];
            
            UIButton *selectButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
            selectButton.tag = 1000 + ((Cus_PubOrderAnnotation*)annotation).orderIndex;
            
            annotationView.rightCalloutAccessoryView = selectButton;
        }
        return annotationView;
    }
    return nil;
}
@end






