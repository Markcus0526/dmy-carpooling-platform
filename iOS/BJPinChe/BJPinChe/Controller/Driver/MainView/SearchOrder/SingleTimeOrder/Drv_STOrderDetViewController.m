//
//  Drv_SearchOrderDetViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/28/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_STOrderDetViewController.h"
#import "Drv_SuccessOrderViewController.h"
#import "ImageHelper.h"
#import "Drv_PassengerInfoViewController.h"
#import "OOAnnotation.h"
#define start 0
#define desti 1



@interface Drv_STOrderDetViewController ()<BMKMapViewDelegate>

@end

@implementation Drv_STOrderDetViewController

@synthesize mPassID;

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
    if ([self.status isEqualToString:@"1"]) {
        [_publishBtn setHidden:NO];
    }else
    {
        [_publishBtn setHidden:YES];
    }
    
    [self initControls];
    
    
}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    if (IPHONE4) {
        myScrollView.contentSize = CGSizeMake(myScrollView.frame.size.width, 600);
    }
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
    
}


-(void)viewWillAppear:(BOOL)animated {
    
    [super viewWillAppear:animated];
    
    
    [_mapView viewWillAppear];
    _mapView.delegate = self; // 此处记得不用的时候需要置nil，否则影响内存的释放
   
}

-(void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    [_mapView viewWillDisappear];
    _mapView.delegate = nil; // 不用时，置nil
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
    // map position
    _mapView.delegate = self;

    if ([_status isEqualToString:@"2"] || [_status isEqualToString:@"3"]) {
        falseOrderDic = [[NSDictionary alloc]init];
        
        NSMutableArray *resultData = [[NSMutableArray alloc]init];
        NSArray *paths=NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
        NSString *path=[paths objectAtIndex:0];
        NSString *filename=[path stringByAppendingPathComponent:@"orderdes.plist"];
        
        //读文件
        NSDictionary* dic2 = [NSDictionary dictionaryWithContentsOfFile:filename];
        resultData = [dic2 objectForKey:@"order"];
        for (int i= 0; i<resultData.count; i++) {
            NSDictionary *dic =[resultData objectAtIndex:i];
            if ([_startTimer isEqualToString:[dic objectForKey:@"start_time"]]) {
                falseOrderDic = dic;
            }
        }
        if ([_status isEqualToString:@"2"]) {
            [_publishBtn setHidden:NO];
        }else if ([_status isEqualToString:@"3"])
        {
            [_publishBtn setHidden:YES];
        }
        [self updataUI];
        return;
    }
        
	TEST_NETWORK_RETURN
    [SVProgressHUD show];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] getAcceptableInCityOrderDetailInfo:[Common getUserID] OrderID:self.mOrderID OrderType:1 DevToken:[Common getDeviceMacAddress]];
 
	mWaitCount = 0;
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(applicationDidEnterBackground:) name:UIApplicationDidEnterBackgroundNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(applicationDidBecomeActive:) name:UIApplicationDidBecomeActiveNotification object:nil];
}
-(void)updataUI
{
    
    [_lblEvalPro setText:[falseOrderDic objectForKey:@"EvalPro"]];
    [_lblServeCnt setText:[falseOrderDic objectForKey:@"ServeCnt"]];
    
    [_lblAge setText:[NSString stringWithFormat:@"%@", [falseOrderDic objectForKey:@"pass_age"]]];
    [_lblName setText:[NSString stringWithFormat:@"%@", [falseOrderDic objectForKey:@"pass_name"]]];
//    [_lblStartPos setText:[falseOrderDic objectForKey:@""]];
//    [_lblEndPos setText:[falseOrderDic objectForKey:@""]];
    [_lblStartToEnd setText:[NSString stringWithFormat:@"%@---%@",[falseOrderDic objectForKey:@"start_addr"],[falseOrderDic objectForKey:@"end_addr"]]];
    [_lblTime setText:[NSString stringWithFormat:@"预约时间：%@",[falseOrderDic objectForKey:@"start_time"]]];
    [_lblPrice setText:[NSString stringWithFormat:@"%@点", [falseOrderDic objectForKey:@"price"]]];
    [_lblSysInfoPrice setText:[NSString stringWithFormat:@"平台信息费%0.f",[[falseOrderDic objectForKey:@"sysinfo_fee"] doubleValue]]];
    
//    NSString *strMidPoints = @"";
//    for (STMidPoint *mid_point in order_info.mid_points)
//    {
//        if ([strMidPoints length] > 0)
//        {
//            strMidPoints = [NSString stringWithFormat:@", %@", mid_point.address];
//        }
//        else
//        {
//            strMidPoints = mid_point.address;
//        }
//    }
    
    OOAnnotation  *annotation_start = [[OOAnnotation alloc] init];
    annotation_start.coordinate = CLLocationCoordinate2DMake([[falseOrderDic objectForKey:@"startlat"] doubleValue], [[falseOrderDic objectForKey:@"startlog"] doubleValue]);
    annotation_start.orderIndex = start;
    [_mapView addAnnotation:annotation_start];
    
    OOAnnotation *annotation_Desti = [[OOAnnotation alloc] init];
    annotation_Desti.coordinate = CLLocationCoordinate2DMake([[falseOrderDic objectForKey:@"endlat"] doubleValue], [[falseOrderDic objectForKey:@"endlog"] doubleValue]);
    annotation_Desti.orderIndex = desti;
    [_mapView addAnnotation:annotation_Desti];
    
    // 设置地图显示区域
    BMKCoordinateRegion region;
    region.center.latitude  = ([[falseOrderDic objectForKey:@"startlat"] doubleValue] +[[falseOrderDic objectForKey:@"endlat"] doubleValue])/2 ;
    region.center.longitude = ([[falseOrderDic objectForKey:@"startlog"] doubleValue] + [[falseOrderDic objectForKey:@"endlog"] doubleValue])/2;
    region.span.latitudeDelta  = 0.2;
    region.span.longitudeDelta = 0.2;
    
    if (_mapView)
    {
        _mapView.region = region;
    }
}

-(void)applicationDidEnterBackground:(NSNotificationCenter *)notication{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:@"NO" forKey:@"SingleOrder"];
    
    [defaults setObject:[self timerNow] forKey:@"timerNow"];
    
    [defaults synchronize];
    if (mWaitAgreeTimer !=nil) {
        [mWaitAgreeTimer invalidate];
        mWaitAgreeTimer = nil;
    }
}
-(void)applicationDidBecomeActive:(NSNotificationCenter *)notication{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    //    [defaults setObject:@"YES" forKey:@"SingleOrder"];
    if ([[defaults objectForKey:@"SingleOrder"] isEqualToString:@"NO"]) {
        NSString *str = [self timerNow];
        NSString *strOld = [defaults objectForKey:@"timerNow"];
        NSString *timerStr = [self shijian:str oldTimer:strOld];
        if (![timerStr isEqualToString:@"NO"]) {
            if (mWaitCount - [timerStr intValue] >0) {
                mWaitCount -=[timerStr intValue];
				TEST_NETWORK_RETURN
                [[[CommManager getCommMgr] orderSvcMgr] CheckOnceOrderAgree:[Common getUserID] OrderId:self.mOrderID DevToken:[Common getDeviceMacAddress]];
                
                NSString *timeTag = [NSString stringWithFormat:@"%d",mWaitCount];
                //    [SVProgressHUD showWithStatus:timeTag];
                [SVProgressHUD showWithStatus:timeTag maskType:SVProgressHUDMaskTypeClear];
                if (mWaitAgreeTimer !=nil) {
                    [mWaitAgreeTimer invalidate];
                    mWaitAgreeTimer = nil;
                }
                mWaitAgreeTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(countDownFunc:) userInfo:nil repeats:YES];
            }
        }
    }
}
-(NSString *)shijian:(NSString *)newTimer oldTimer:(NSString *)oldTimer
{
    NSArray *array=[oldTimer componentsSeparatedByString:@":"];
    NSArray *array1=[newTimer componentsSeparatedByString:@":"];
    int time1=[[array objectAtIndex:0] integerValue];
    int time2=[[array objectAtIndex:1] integerValue];
    int time3=[[array objectAtIndex:2] integerValue];
    
    int time4=[[array1 objectAtIndex:0] integerValue];
    int time5=[[array1 objectAtIndex:1] integerValue];
    int time6=[[array1 objectAtIndex:2] integerValue];
    
    int time;
    if ((time6 - time3) <0) {
        time = time6 - time3 +60;
        time5--;
    }else
        time = time6 -time3;
    if (time >=60) {
        time -=60;
        time5 ++;
    }
    if ((time5 - time2) <0) {
        time +=(time5 - time2 +60)*60;
        time4 --;
    }else
        time +=(time5 - time2)*60;
    
    if ((time4 - time1) != 0 && (time5 - time2 +60) >=60) {
        return @"NO";
    }
    NSString *str =[NSString stringWithFormat:@"%d",time];
    return str;
}
-(NSString *)timerNow
{
    NSDateFormatter *datePickerFormat = [[NSDateFormatter alloc] init];
    [datePickerFormat setDateFormat:@"HH:mm:ss"];
    NSString *str=[datePickerFormat stringFromDate:[[NSDate date] dateByAddingTimeInterval:(0 * 60)]];
    return str;
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

// main control event
/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
//    BACK_VIEW;
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)onClickedUserImg:(id)sender
{
    // go to passenger detail view controller
    UIStoryboard *story = [UIStoryboard storyboardWithName:@"DriverStoryboard" bundle:nil];
    
    Drv_PassengerInfoViewController *viewController = (Drv_PassengerInfoViewController *)[story instantiateViewControllerWithIdentifier:@"passenger_info"];
    
    if ([_status isEqualToString:@"2"] || [_status isEqualToString:@"3"]) {
        viewController.startTimer = [falseOrderDic objectForKey:@"start_time"];
    }else
    {
        viewController.mPassID = mPassID;
    }
    
    [self presentViewController:viewController animated:YES completion:nil];
}

/**
 * Publish button click event implementation
 */
- (IBAction)onClickedPublish:(id)sender
{
//    [SVProgressHUD showWithStatus:];
    [SVProgressHUD showWithStatus:@"乘客正在确认，请稍候" maskType:SVProgressHUDMaskTypeClear];
    
    if ([_status isEqualToString:@"2"]) {
        NSMutableArray *resultData = [[NSMutableArray alloc]init];
        NSArray *paths=NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
        NSString *path=[paths objectAtIndex:0];
        NSString *filename=[path stringByAppendingPathComponent:@"orderdes.plist"];
        
        //读文件
        NSDictionary* dic2 = [NSDictionary dictionaryWithContentsOfFile:filename];
        resultData = [dic2 objectForKey:@"order"];
        for (int i= 0; i<resultData.count; i++) {
            NSDictionary *dic =[resultData objectAtIndex:i];
            if ([_startTimer isEqualToString:[dic objectForKey:@"start_time"]]) {
                [dic setValue:@"3" forKey:@"status"];
            }
        }
        NSDictionary *dic =[NSDictionary dictionaryWithObject:resultData forKey:@"order"];
        [dic writeToFile:filename atomically:YES];
        
        [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(cancelOrder) userInfo:nil repeats:NO];
        
        return;
    }
    
    
	TEST_NETWORK_RETURN
    
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] acceptOnceOrder:[Common getUserID] OrderID:self.mOrderID Latitude:[Common getCurrentLatitude] Longitude:[Common getCurrentLongitude] DevToken:[Common getDeviceMacAddress]];

}
-(void)cancelOrder
{
    [SVProgressHUD showErrorWithStatus:@"手太慢了！" duration:DEF_DELAY];
    
    [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(fanhui) userInfo:nil repeats:NO];
}
-(void)duplicateUser:(NSString *)result
{
    if (mWaitAgreeTimer !=nil) {
        [mWaitAgreeTimer invalidate];
        mWaitAgreeTimer = nil;
    }
    [SVProgressHUD showErrorWithStatus:result duration:DEF_DELAY];
    
    [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(fanhui) userInfo:nil repeats:NO];
}
-(void)fanhui
{
    BACK_VIEW;
}
- (void) countDownFunc:(NSTimer *)timer
{
    mWaitCount -= 1;
    NSString *timeTag = [NSString stringWithFormat:@"%d",mWaitCount];
//    [SVProgressHUD showWithStatus:timeTag];
//    [SVProgressHUD showWithStatus:timeTag maskType:SVProgressHUDMaskTypeClear];
    [SVProgressHUD showWithStatusGra:timeTag maskType:SVProgressHUDMaskTypeClear tishi:tishiString];
    // stop timer
	TEST_NETWORK_RETURN
    if (mWaitCount < 1) {
        //时间到了判断是否充值
        [[CommManager getCommMgr] orderSvcMgr].delegate = self;
        [[[CommManager getCommMgr] orderSvcMgr] has_clickedchargingbtn:[Common getUserID] OrderID:self.mOrderID DevToken:[Common getDeviceMacAddress]];
        
        [mWaitAgreeTimer invalidate];
        mWaitAgreeTimer = nil;
    }
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] CheckOnceOrderAgree:[Common getUserID] OrderId:self.mOrderID DevToken:[Common getDeviceMacAddress]];
}

#pragma OrderSvcMgr Delegate Methods
- (void)getAcceptableInCityOrderDetailInfoResult:(NSString *)result OrderInfo:(STDetailedDrvInCityOrderInfo *)order_info
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		STPassengerInfo *passenger = order_info.pass_info;

		[_imgUser setImageWithURL:[NSURL URLWithString:passenger.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];

		if (passenger.sex == 0) {
            [_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
		} else {
			[_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
			_lblAge.textColor = MYCOLOR_GREEN;
		}

		if(passenger.pverified ==1)
		{
			[_verifiedImgeView setImage:[UIImage imageNamed:@"icon_verified"]];
			[verifiedLabel setText:passenger.pverified_desc];
		} else {
			[_verifiedImgeView setImage:[UIImage imageNamed:@"icon_unverified"]];
			[verifiedLabel setText:passenger.pverified_desc];
		}

		[_lblEvalPro setText:passenger.goodeval_rate_desc];
		[_lblServeCnt setText:passenger.carpool_count_desc];

		[_lblAge setText:[NSString stringWithFormat:@"%d", passenger.age]];
		[_lblName setText:[NSString stringWithFormat:@"%@", passenger.name]];
		[_lblStartPos setText:order_info.start_addr];
		[_lblEndPos setText:order_info.end_addr];
		[_lblStartToEnd setText:[NSString stringWithFormat:@"%@---%@",order_info.start_addr,order_info.end_addr]];
		[_lblTime setText:[NSString stringWithFormat:@"预约时间：%@",order_info.start_time]];
		[_lblPrice setText:[NSString stringWithFormat:@"%.1f点", order_info.price]];
		[_lblSysInfoPrice setText:order_info.sysinfo_fee_desc];

		NSString *strMidPoints = @"";
		for (STMidPoint *mid_point in order_info.mid_points)
		{
			if ([strMidPoints length] > 0)
			{
				strMidPoints = [NSString stringWithFormat:@", %@", mid_point.address];
			}
			else
			{
				strMidPoints = mid_point.address;
			}
		}

		OOAnnotation  *annotation_start = [[OOAnnotation alloc] init];
		annotation_start.coordinate = CLLocationCoordinate2DMake(order_info.start_lat, order_info.start_lng);
		annotation_start.orderIndex = start;
		[_mapView addAnnotation:annotation_start];

		OOAnnotation *annotation_Desti = [[OOAnnotation alloc] init];
		annotation_Desti.coordinate = CLLocationCoordinate2DMake(order_info.end_lat, order_info.end_lng);
		annotation_Desti.orderIndex = desti;
		[_mapView addAnnotation:annotation_Desti];

		// 设置地图显示区域
		BMKCoordinateRegion region;
		region.center.latitude  = (order_info.start_lat +order_info.end_lat)/2 ;
		region.center.longitude = (order_info.start_lng + order_info.end_lng)/2;
		region.span.latitudeDelta  = 0.2;
		region.span.longitudeDelta = 0.2;

		if (_mapView)
		{
			_mapView.region = region;
		}

		[SVProgressHUD dismiss];
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
}


- (void)acceptOnceOrderResult:(int)result retmsg:(NSString *)retmsg wait_time:(int)wait_time
{
//    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
//    {
//        [SVProgressHUD dismiss];
//        
//		TEST_NETWORK_RETURN
//		[[[CommManager getCommMgr] orderSvcMgr] CheckOnceOrderAgree:[Common getUserID] OrderId:self.mOrderID DevToken:[Common getDeviceMacAddress]];
//
//		mWaitCount = 40;
//        NSString *timeTag = [NSString stringWithFormat:@"%d",mWaitCount];
////        [SVProgressHUD showErrorWithStatus:];
//        [SVProgressHUD showWithStatus:timeTag maskType:SVProgressHUDMaskTypeClear];
//        
//		mWaitAgreeTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(countDownFunc:) userInfo:nil repeats:YES];
//    }
//    else
//    {
//        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
//    }
//    TEST_NETWORK_RETURN
    
    if (result == SVCERR_SUCCESS)
    {
        if(mWaitAgreeTimer != nil)
        {
            [mWaitAgreeTimer invalidate];
            mWaitAgreeTimer = nil;
            mWaitCount = 0;
        }
        
        [[[CommManager getCommMgr] orderSvcMgr] CheckOnceOrderAgree:[Common getUserID] OrderId:self.mOrderID DevToken:[Common getDeviceMacAddress]];

        mWaitCount = wait_time;
        
        NSString *timeTag = [NSString stringWithFormat:@"%d",mWaitCount];
        
        //    [SVProgressHUD showWithStatus:timeTag];
//        [SVProgressHUD showWithStatus:timeTag maskType:SVProgressHUDMaskTypeClear];
        tishiString = @"拼友确认中，请耐心等待";
        [SVProgressHUD showWithStatusGra:timeTag maskType:SVProgressHUDMaskTypeClear tishi:tishiString];

		mWaitAgreeTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(countDownFunc:) userInfo:nil repeats:YES];
    }
//    else if (result == SVCERR_ALREADY_ACCEPTED)
//    {
//        [SVProgressHUD dismissWithError:retmsg afterDelay:DEF_DELAY];
//    }
    else
    {
        if(mWaitAgreeTimer != nil)
        {
            [mWaitAgreeTimer invalidate];
            mWaitAgreeTimer = nil;
            mWaitCount = 0;
        }
        if (retmsg.length ==0) {
            [SVProgressHUD dismissWithError:@"乘客没有确认订单，抢其他订单吧" afterDelay:DEF_DELAY];
        }else
        {
            [SVProgressHUD dismissWithError:retmsg afterDelay:DEF_DELAY];
        }
        
    }
}



- (void) checkOnceOrderAgreeResult:(NSString *)result PassImg:(NSString *)pass_img PassName:(NSString *)pass_name PassGender:(int)pass_gender PassAge:(int)pass_age StartTime:(NSString *)start_time StartAddr:(NSString *)start_addr EndAddr:(NSString *)end_addr
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
		if([pass_name length] > 0)
		{
			if(mWaitAgreeTimer != nil)
			{
				[mWaitAgreeTimer invalidate];
				mWaitAgreeTimer = nil;
				mWaitCount = 0;
			}
			[SVProgressHUD dismiss];
			
			UIViewController * curCtrl = (UIViewController *)self.presentingViewController;
			
			[self dismissViewControllerAnimated:NO completion:^{
				
				// show order success view controller
				Drv_SuccessOrderViewController *viewController = (Drv_SuccessOrderViewController *)[curCtrl.storyboard instantiateViewControllerWithIdentifier:@"success_order"];
				viewController.orderid = _mOrderID;

				CATransition *animation = [CATransition animation]; \
				[animation setDuration:0.3]; \
				[animation setType:kCATransitionPush]; \
				[animation setSubtype:kCATransitionFromRight]; \
				[animation setTimingFunction:[CAMediaTimingFunction functionWithName: kCAMediaTimingFunctionEaseInEaseOut]]; \
				[[curCtrl.view.superview layer] addAnimation:animation forKey:@"SwitchToView"];
				
				[curCtrl presentViewController:viewController animated:NO completion:nil];
			}];
			
		}
		else
		{
//			if(mWaitAgreeTimer != nil)
//			{
//				[[[CommManager getCommMgr] orderSvcMgr] CheckOnceOrderAgree:[Common getUserID] OrderId:self.mOrderID DevToken:[Common getDeviceMacAddress]];
//			}
		}
    }
    else
    {
		//[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}
- (BMKAnnotationView *) mapView:(BMKMapView *)mapView viewForAnnotation:(id <BMKAnnotation>) annotation{
        
    if ([annotation isKindOfClass:[OOAnnotation class]])
    {
        static NSString *AnnotationViewID = @"annotationViewID";
        
        BMKAnnotationView *annotationView = [mapView dequeueReusableAnnotationViewWithIdentifier:AnnotationViewID];
        if (annotationView == nil) {
            annotationView = [[BMKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:AnnotationViewID];
            ((BMKPinAnnotationView*)annotationView).animatesDrop = YES;
            
            if(((OOAnnotation*)annotation).orderIndex ==start)
            {
                annotationView.image =[UIImage imageNamed:@"AnnoStartPoint"];

            }else
            {
                annotationView.image =[UIImage imageNamed:@"AnnoEndPoint"];
            }
        }
        
        return annotationView;
    }
    return nil;
}
-(void)getHas_clickedchargingbtn:(NSString *)result RetData:(NSDictionary *)RetData
{
    if(mWaitAgreeTimer != nil)
    {
        [mWaitAgreeTimer invalidate];
        mWaitAgreeTimer = nil;
        mWaitCount = 0;
    }
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        int timer = [[RetData objectForKey:@"waittime_when_charging"] integerValue];
        if (timer == 0) {
            [SVProgressHUD dismissWithError:@"拼友未确认，本订单取消" afterDelay:DEF_DELAY];
        }else
        {
            mWaitCount = timer;
            NSString *timeTag = [NSString stringWithFormat:@"%d",mWaitCount];
            //    [SVProgressHUD showWithStatus:timeTag];
//            [SVProgressHUD showWithStatus:timeTag maskType:SVProgressHUDMaskTypeClear];
            tishiString = @"拼友正在充值，请耐心等待";
            [SVProgressHUD showWithStatusGra:timeTag maskType:SVProgressHUDMaskTypeClear tishi:tishiString];
            
            mWaitAgreeTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(countDownFunc:) userInfo:nil repeats:YES];
        }
    }else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}
@end







