//
//  Cus_STOrderConfirmViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/1/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//  单次拼车 乘客确认界面
// 未完成  中途点显示 地图显示起始位置  

#import "Cus_STOrderConfirmViewController.h"
#import "Cus_SuccessShortOrderViewController.h"
#import "Cus_PubShortNotEnoughVC1.h"
#import "UIViewController+CWPopup.h"
#import "Config.h"
#import "Cus_ChargeViewController.h"
#import "Cus_driverInfoViewController.h"
#import "Cus_MainTabViewController.h"
#import "Cus_PubOrderAnnotation.h"

@interface Cus_STOrderConfirmViewController ()<Cus_PubShortDelegate>

@end

@implementation Cus_STOrderConfirmViewController

@synthesize mDriverInfo;
@synthesize mOrderInfo;
@synthesize mWaitTime;


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
	
    if ([_soure isEqualToString:@"2"]) {
        [[[CommManager getCommMgr] orderSvcMgr] CheckOnceOrderAcceptance:[Common getUserID] OrderId:self.mOrderId Latitude:[Common getCurrentLatitude] Longitude:[Common getCurrentLongitude] DevToken:[Common getDeviceMacAddress]];
    }else
    {
        [self initControls];
    }
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(shuju:) name:@"checkOnceOrderAcceptanceResult" object:nil];
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:@"Cus_STorder" forKey:@"Cus_STorder"];
    [defaults synchronize];
    
    _mapView.delegate = self;
    _mapView.showsUserLocation = YES;
}
-(void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"checkOnceOrderAcceptanceResult" object:nil];
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
	[_lblCountDown setText:[NSString stringWithFormat:@"%d", mWaitTime]];

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

	// set count down timer
    if (countTimer !=nil) {
        [countTimer invalidate];
        countTimer = nil;
    }
	countTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(countDownFuncClick) userInfo:nil repeats:YES];
//	[[NSRunLoop currentRunLoop] addTimer:mCountDownTimer forMode:NSDefaultRunLoopMode];
	[_imgUser setImageWithURL:[NSURL URLWithString:self.mDriverInfo.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
	[_imgCar setImageWithURL:[NSURL URLWithString:self.mDriverInfo.carimg] placeholderImage:[UIImage imageNamed:@"demo_carimg.png"]];

	if(self.mDriverInfo.sex == 0) {
		[_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
	} else {
		[_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
		_lblAge.textColor = MYCOLOR_GREEN;
	}

	[_lblAge setText:[NSString stringWithFormat:@"%d", self.mDriverInfo.age]];
	[_lblName setText:[NSString stringWithFormat:@"%@", self.mDriverInfo.name]];
	[_lblStartPos setText:self.mOrderInfo.startPos];
	[_lblEndPos setText:self.mOrderInfo.endPos];
	[_lblEvalPro setText:self.mDriverInfo.goodeval_rate_desc];
	[_lblServeCnt setText:self.mDriverInfo.carpool_count_desc];
	[_lblDistance setText:self.mOrderInfo.distance_desc];
    

    [_mapView setDelegate:self];
    [_mapView setShowsUserLocation:YES];
    BMKCoordinateRegion region;
    region.center.latitude  = mOrderInfo.start_lat;
    region.center.longitude = mOrderInfo.start_lng;
    region.span.latitudeDelta  = 0.05;
    region.span.longitudeDelta = 0.05;
    if (_mapView)
    {
        [_mapView setRegion:region animated:TRUE];
    }
    
    Cus_PubOrderAnnotation *annotation = [[Cus_PubOrderAnnotation alloc] init];
    annotation.coordinate = CLLocationCoordinate2DMake(mOrderInfo.start_lat, mOrderInfo.start_lng);
    annotation.title = [NSString stringWithFormat:@"%ld", mDriverInfo.uid];
    annotation.orderText = @" ";
    [_mapView addAnnotation:annotation];
}
-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    if (countTimer !=nil) {
        [countTimer invalidate];
        countTimer = nil;
    }
    [_lblCountDown setText:@"0"];
}
-(void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:@"1111" forKey:@"Cus_STorder"];
    [defaults synchronize];
    
    if (countTimer !=nil) {
        [countTimer invalidate];
        countTimer = nil;
    }
    if (countTimer !=nil) {
        [countTimer invalidate];
        countTimer = nil;
    }
    [_lblCountDown setText:@"0"];
    
    _mapView.delegate = nil;
}

- (void) countDownFuncClick
{
	// get remain time
    
	int curVal = [[_lblCountDown text] intValue];
    if (curVal <=0 || [_lblCountDown.text isEqualToString:@"0"]) {
        if (countTimer !=nil) {
            [countTimer invalidate];
            countTimer = nil;
        }
        return;
    }

	curVal -= 1;

	[_lblCountDown setText:[NSString stringWithFormat:@"%d", curVal]];

	if (_pvCountDown.progressCounter == COUNTDOWN_PROGRESS_MAX)
	{
		_pvCountDown.progressCounter = 0;
	}
	else
	{
		_pvCountDown.progressCounter = _pvCountDown.progressCounter + 1;
	}

	NSLog(@"count down : %d", curVal);

	// stop timer
	if (curVal == 0) {
		[countTimer invalidate];
		countTimer = nil;
        
        if (self.view.window == nil) {
            return;
        }
        UIAlertView *alert = (UIAlertView *)[self.view viewWithTag:10];
        if (alert !=nil) {
            [alert dismissWithClickedButtonIndex:0 animated:NO];
        }
        alert = [[UIAlertView alloc]initWithTitle:@"" message:@"您未确认订单，本次订单取消”，同时取消本次订单" delegate:self cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
        alert.tag =10;
        [alert show];
	}
}


#pragma OrderExecuteSvcMgr Delegate Methods
- (void) duplicateUser:(NSString *)result
{
	NSLog(@"Detect duplicate user");

	[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	//[mParent duplicateLogout];
}


///////////////////////////////////////////////////////////////////////////////



#pragma mark - UI User Event
/**
 * Back to parent view controller
 */
- (void)onClickedBack:(id)sender
{
//    Cus_MainMgrViewController *shortOrderVC =[[Cus_MainMgrViewController alloc]initWithNibName:@"Cus_MainMgrViewController" bundle:nil];
//    shortOrderVC.hidesBottomBarWhenPushed =YES;
//    [self.navigationController pushViewController:shortOrderVC animated:YES];
    
//    UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
//    Cus_MainMgrViewController *viewController = (Cus_MainMgrViewController *)[customerStroyboard instantiateViewControllerWithIdentifier:@"appmgr"];
//    [self presentViewController:viewController animated:YES completion:nil];
    
    if (countTimer !=nil) {
        [countTimer invalidate];
        countTimer = nil;
    }
    Cus_MainTabViewController *controller = (Cus_MainTabViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];
    
    // set target tab
    controller.mCurTab = TAB_APPS;
    
    SHOW_VIEW(controller);
//	if (self.navigationController == nil) {
//		BACK_VIEW;
//	} else {
//		[self popoverPresentationController];
//	}
}



///
- (IBAction)onClickedConfirm:(id)sender
{
	TEST_NETWORK_RETURN
    if (countTimer != nil) {
        [countTimer invalidate];
        countTimer = nil;
    }
   
	[SVProgressHUD show];
	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
	[[[CommManager getCommMgr] orderSvcMgr] ConfirmOnceOrder:[Common getUserID] OrderId:self.mOrderId DevToken:[Common getDeviceMacAddress]];
}



/**
 *  确认订单 结果
 *
 *  @param result
 */
- (void) confirmOnceOrderResult:(NSString *)result
{
	[SVProgressHUD dismiss];
    //[mCountDownTimer invalidate];
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
        if (countTimer !=nil) {
            [countTimer invalidate];
            countTimer = nil;
        }
        _lblCountDown.text =@"0";
        
		PasswordCheckModal *popup = [[PasswordCheckModal alloc] initWithNibName:@"PasswordCheckModal" bundle:nil];
		popup.delegate = self;

		[self presentPopupViewController:popup animated:YES completion:^(void) {
#warning   设置密码网络请求
		}];
	} else if ([result isEqualToString:@"绿点不足"]) {
		//绿点不足
		Cus_PubShortNotEnoughVC1 *notenough =[[Cus_PubShortNotEnoughVC1 alloc]initWithNibName:@"Cus_PubShortNotEnoughView1" bundle:nil];

		notenough.leftPrice = [Config lvdianAccount];
		notenough.needPrice =[NSString stringWithFormat:@"%1.f",self.mOrderInfo.price];
		notenough.delegate = self;

		[self presentPopupViewController:notenough animated:YES completion:^(void) {
			MyLog(@"Cus_NotEnoughViewController's view presented") ;
		}];
	}
}


#pragma mark   代理返回结果 
- (void)Cus_OnceOrdernotEnoughViewCharge:(BOOL)chargeOrNot
{
	if(chargeOrNot)  //充值
	{
		if (self.popupViewController != nil)
		{
			TEST_NETWORK_RETURN

			[[CommManager getCommMgr] orderSvcMgr].delegate = self;
			[[[CommManager getCommMgr]orderSvcMgr]clickchargingbtn:[Common getUserID] OrderID:self.mOrderId DevToken:[Common getDeviceMacAddress]];

			[self dismissPopupViewControllerAnimated:YES completion:^{
				MyLog(@"popup view dismissed");
				//跳转到充值页面
				Cus_ChargeViewController *charge =[[Cus_ChargeViewController alloc]initWithNibName:@"ChargeView" bundle:nil];

				[self presentViewController:charge animated:NO completion:nil];
			}];
		}
	} else {
		// dismiss popup
		if (self.popupViewController != nil)
		{
			[self dismissPopupViewControllerAnimated:YES completion:^{
			}];
            UIAlertView* alertView = [[UIAlertView alloc] initWithTitle:@"" message:@"是否确定取消发布？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
            alertView.tag = 11;
            [alertView show];
		}
	}
}



#pragma PasswordModal Delegate Methods
- (void)tappedDone:(NSString *)result
{
	NSString * password = @"0000";
	if([result length] == 4)
		password = result;

	TEST_NETWORK_RETURN

	[SVProgressHUD show];
	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
	[[[CommManager getCommMgr]orderSvcMgr]SetOnceOrderPassword:[Common getUserID] OrderId:self.mOrderId Password:password DevToken:[Common getDeviceMacAddress]];
}

-(void)shuju:(NSNotification *)user
{
    if (self.view.window ==nil) {
        return;
    }
    if (![_soure isEqualToString:@"2"]) {
        return;
    }
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    STDriverInfo * driverInfo = [[STDriverInfo alloc] init];
    STSingleTimeOrderInfo * orderInfo = [[STSingleTimeOrderInfo alloc] init];
    NSDictionary *tmp = user.userInfo;
    
    NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
    
    // get service result
    jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
    // check result
    if (jsonRet != SVCERR_SUCCESS)
    {
        retMsg = [tmp1 objectForKey:SVCC_RETMSG];
        
        // check duplicate login state
        if (jsonRet == SVCERR_DUPLICATE_USER) {
            [self.view makeToast:@"获取订单信息失败" duration:2 position:@"center"];
            return;
        }
    }
    else
    {
        NSDictionary * tmp2 = [tmp1 objectForKey:SVCC_DATA];
        
        driverInfo.image = [tmp2 objectForKey:@"img"];
        driverInfo.name = [tmp2 objectForKey:@"name"];
        driverInfo.sex = [[tmp2 objectForKey:@"gender"] intValue];
        driverInfo.age = [[tmp2 objectForKey:@"age"] intValue];
        driverInfo.carimg = [tmp2 objectForKey:@"carimg"];
        driverInfo.drv_career = [[tmp2 objectForKey:@"drv_career"] intValue];
        driverInfo.drv_career_desc = [tmp2 objectForKey:@"drv_career"];
        driverInfo.goodeval_rate = [[tmp2 objectForKey:@"evgood_rate"] intValue];
        driverInfo.goodeval_rate_desc = [tmp2 objectForKey:@"evgood_rate_desc"];
        driverInfo.carpool_count = [[tmp2 objectForKey:@"carpool_count"] intValue];
        driverInfo.carpool_count_desc = [tmp2 objectForKey:@"carpool_count_desc"];
        driverInfo.carno = [tmp2 objectForKey:@"carno"];
        driverInfo.car_brand = [tmp2 objectForKey:@"brand"];
        driverInfo.car_style = [tmp2 objectForKey:@"style"];
        driverInfo.car_type = [[tmp2 objectForKey:@"type"] intValue];
        driverInfo.uid = [[tmp2 objectForKey:@"drvid"] longValue];
        
        orderInfo.distance = [[tmp2 objectForKey:@"distance"] doubleValue];
        orderInfo.distance_desc = [tmp2 objectForKey:@"distance_desc"];
        orderInfo.startPos = [tmp2 objectForKey:@"start_addr"];
        orderInfo.start_lat = [[tmp2 objectForKey:@"start_lat"] doubleValue];
        orderInfo.start_lng = [[tmp2 objectForKey:@"start_lng"] doubleValue];
        orderInfo.endPos = [tmp2 objectForKey:@"end_addr"];
        orderInfo.end_lat = [[tmp2 objectForKey:@"end_lat"] doubleValue];
        orderInfo.end_lng = [[tmp2 objectForKey:@"end_lng"] doubleValue];
        orderInfo.price =[[tmp2 objectForKey:@"price"]doubleValue];
        
        NSArray * tmp4 = [tmp2 objectForKey:@"midpoints"];
        NSMutableArray * arrMidPoint = [[NSMutableArray alloc] init];
        for(NSDictionary * midInfo in tmp4)
        {
            STMidPoint *midPoint = [[STMidPoint alloc] init];
            midPoint.index = [[midInfo objectForKey:@"index"] intValue];
            midPoint.latitude = [[midInfo objectForKey:@"lat"] doubleValue];
            midPoint.longitude = [[midInfo objectForKey:@"lng"] doubleValue];
            midPoint.address = [midInfo objectForKey:@"addr"];
            [arrMidPoint addObject:midPoint];
        }
        orderInfo.mid_points = arrMidPoint;
        
    }
    if ([retMsg isEqualToString:SVCERR_MSG_SUCCESS])
    {
        mDriverInfo = driverInfo;
        mOrderInfo = orderInfo;
        mWaitTime = 40;
        if ([_soure isEqualToString:@"2"]) {
            
        }
        [self initControls];
    }else
    {
        [self.view makeToast:@"获取订单信息失败" duration:2 position:@"center"];
    }
}

#pragma mark 充值时候调用下接口
-(void)getClickchargingbtn:(NSString *)result RetData:(NSDictionary *)RetData
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
	}
	else
	{
	}
}



#pragma OrderSvcMgr Delegate Methods
- (void) checkOnceOrderAcceptanceResult:(NSString *)result DriverInfo:(STDriverInfo *)driverInfo OrderInfo:(STSingleTimeOrderInfo *)orderInfo
{
//    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
//    {
//        mDriverInfo = driverInfo;
//        mOrderInfo = orderInfo;
//        mWaitTime = 40;
//        
//        [self initControls];
//    }
//    else
//    {
//        [SVProgressHUD showErrorWithStatus:@"获取订单信息失败" duration:DEF_DELAY];
//    }
}


/**
 *  设定车票密码 结果
 *
 *  @param result
 *  @param driverInfo
 *  @param orderInfo
 */

- (void) setOnceOrderPasswordResult:(NSString *)result DriverInfo:(STDriverInfo *)driverInfo OrderInfo:(STSingleTimeOrderInfo *)orderInfo
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		[SVProgressHUD dismiss];

//		UIViewController * curCtrl = (UIViewController *)self.presentingViewController;
//
//		[self dismissViewControllerAnimated:NO completion:^{
//			// go to single time order confirm view controller
//		}];
        
        UIStoryboard *story = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
        Cus_SuccessShortOrderViewController *viewController = [story instantiateViewControllerWithIdentifier:@"PubSTOrderSuccess"];
        viewController.mDriverInfo = driverInfo;
        viewController.mOrderInfo = orderInfo;
        
        [self presentViewController:viewController animated:YES completion:nil];
        
//        CATransition *animation = [CATransition animation]; \
//        [animation setDuration:0.3]; \
//        [animation setType:kCATransitionPush]; \
//        [animation setSubtype:kCATransitionFromRight]; \
//        [animation setTimingFunction:[CAMediaTimingFunction functionWithName: kCAMediaTimingFunctionEaseInEaseOut]]; \
//        [[curCtrl.view.superview layer] addAnimation:animation forKey:@"SwitchToView"];
//        
//        [curCtrl presentViewController:viewController animated:NO completion:nil];
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
}


- (IBAction)onClickedCancel:(id)sender {
	UIAlertView* alertView = [[UIAlertView alloc] initWithTitle:@"" message:@"是否确定取消发布？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
	[alertView show];
}


- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
    if (alertView.tag ==10) {
        if (countTimer !=nil) {
            [countTimer invalidate];
            countTimer = nil;
        }
        Cus_MainTabViewController *controller = (Cus_MainTabViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];
        
        // set target tab
        controller.mCurTab = TAB_APPS;
        
        SHOW_VIEW(controller);
    }else if (alertView.tag == 11)
    {
        if (buttonIndex == 0) {
            Cus_PubShortNotEnoughVC1 *notenough =[[Cus_PubShortNotEnoughVC1 alloc]initWithNibName:@"Cus_PubShortNotEnoughView1" bundle:nil];
            
            notenough.leftPrice = [Config lvdianAccount];
            notenough.needPrice =[NSString stringWithFormat:@"%1.f",self.mOrderInfo.price];
            notenough.delegate = self;
            
            [self presentPopupViewController:notenough animated:YES completion:^(void) {
                MyLog(@"Cus_NotEnoughViewController's view presented") ;
            }];
        } else {
            [[CommManager getCommMgr] orderSvcMgr].delegate = self;
            [[[CommManager getCommMgr] orderSvcMgr] CancelOnceOrder:[Common getUserID]  OrderId:self.mOrderId DevToken:[Common getDeviceMacAddress]];
            [self onClickedBack:nil];
        }
    }
    else{
        if (buttonIndex == 0) {
            // Do nothing
        } else {
            [[CommManager getCommMgr] orderSvcMgr].delegate = self;
            [[[CommManager getCommMgr] orderSvcMgr] CancelOnceOrder:[Common getUserID]  OrderId:self.mOrderId DevToken:[Common getDeviceMacAddress]];
            [self onClickedBack:nil];
        }
    }
}


- (IBAction)onClickedDriverInfo:(id)sender {
    UIStoryboard *story =[UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
	Cus_driverInfoViewController * driverInfoViewController = [story instantiateViewControllerWithIdentifier:@"DriverInfo"];

	driverInfoViewController.driverid = mDriverInfo.uid;

    [self presentViewController:driverInfoViewController animated:YES completion:nil];
//	[self.navigationController pushViewController:driverInfoViewController animated:YES];
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
            
//            UIButton *selectButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
//            selectButton.tag = 1000 + ((Cus_PubOrderAnnotation*)annotation).orderIndex;
//            
//            annotationView.rightCalloutAccessoryView = selectButton;
        }
        return annotationView;
    }
    return nil;
}
@end

