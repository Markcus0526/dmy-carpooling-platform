//
//  Drv_OrderPerformViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/26/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//   车主 单次拼车 订单执行界面

#import "Drv_OrderPerformViewController.h"
#import "UIViewController+CWPopup.h"
#import "MMLocationManager.h"

@interface Drv_OrderPerformViewController ()<UIAlertViewDelegate>

@property(nonatomic,assign)NSInteger failureTimes;

@end


#define PROGRESS_START                  0.05
#define PROGRESS_SETCUSTOM              0.30
#define PROGRESS_CHKCUSTOM              0.65
#define PROGRESS_FINISH                 1.00

#define TIMER_DELAY                     10

#define DIST_LIMIT_MIN                  0.01                // 1s / run 1m
#define DIST_LIMIT_MAX                  0.3                 // 1h / run 120km

#define ORDER_NOT_CANCELLED             0
#define ORDER_CANCELLED                 1

@implementation Drv_OrderPerformViewController

@synthesize mOrderId;
@synthesize mOrderType;
@synthesize mTotalTime;
@synthesize mCurRunDistance;

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
    self.failureTimes=0;
    [self initControls];
    _myScroll.contentSize = CGSizeMake(320, 600);
}

- (void)viewDidDisappear:(BOOL)animated
{
	if(mCountDownTimer != nil)
	{
		[mCountDownTimer invalidate];
		mCountDownTimer = nil;
	}
    self.automaticallyAdjustsScrollViewInsets =NO;
   
    
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
    _progressBar.color = [UIColor colorWithRed:0.00f green:0.64f blue:0.00f alpha:1.00f];
    _progressBar.showText = @NO;
    _progressBar.flat = @YES;
    _progressBar.progress = PROGRESS_START;
    _progressBar.animate = @YES;
    
    // make tap recognizer to hide keyboard && popup
    UITapGestureRecognizer * tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapBackGround:)];
    
    [self.view addGestureRecognizer:tapRecognizer];
	
    // get current location coordinate
    [[MMLocationManager shareLocation] getLocationCoordinate:^(CLLocationCoordinate2D locationCorrrdinate) {
        mOldCoord = locationCorrrdinate;
    }];
    
    // set calculate running distance timer
    mCountDownTimer = [NSTimer scheduledTimerWithTimeInterval:TIMER_DELAY target:self selector:@selector(calcRunningDistance:) userInfo:nil repeats:YES];
    [[NSRunLoop currentRunLoop] addTimer:mCountDownTimer forMode:NSDefaultRunLoopMode];
    
	TEST_NETWORK_RETURN
	
	[SVProgressHUD show];
	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
	[[[CommManager getCommMgr] orderSvcMgr] GetDetailedDriverOrderInfo:[Common getUserID] OrderId:self.mOrderId OrderType:self.mOrderType DevToken:[Common getDeviceMacAddress]];
}

- (void) handleTapBackGround:(id)sender
{
    [self.view endEditing:YES];
    
    // dismiss popup
    if (self.popupViewController != nil) {
        [self dismissPopupViewControllerAnimated:YES completion:^{
            NSLog(@"popup view dismissed");
        }];
    }
}

- (void) calcRunningDistance:(NSTimer *)timer
{
	TEST_NETWORK_RETURN
	
    [[CommManager getCommMgr] orderExecuteSvcMgr].delegate = self;
    
//    [[MMLocationManager shareLocation] getLocationCoordinate:^(CLLocationCoordinate2D locationCorrrdinate) {
//        // calc distance between two coordinate
//        double offsetDist = [Common calcDistOfCoords:mOldCoord coord2:locationCorrrdinate];
//        // check offset distance with limit value
//        if ((offsetDist > DIST_LIMIT_MIN) && (offsetDist < DIST_LIMIT_MAX))
//        {
//            mOldCoord = locationCorrrdinate;
//            mCurRunDistance += offsetDist;
//            
//            // set running distance
//            [_lblUsedDistance setText:[NSString stringWithFormat:@"总距离：%.02f %@", mCurRunDistance, TEXT_GONGLI]];
//        }
    
        // calc total time & set
        mTotalTime += TIMER_DELAY;
        long time = mTotalTime / 60;
        [_lblUsedTime setText:[NSString stringWithFormat:@"总用时：%ld %@", time, TEXT_FENZHONG]];
        
        // call check cancelled order state service
        [[[CommManager getCommMgr] orderExecuteSvcMgr] CheckOrderCancelledState:[Common getUserID] OrderId:self.mOrderId OrderType:self.mOrderType Distance:mTotalTime DevToken:[Common getDeviceMacAddress]];
//    }];
    
}


/////////////////////////////////////////////////////////////////////////////
#pragma mark - Password check modal dialog delegate

- (void)tappedDone:(NSString *)result
{
    // check input result
    
    // dismiss popup
    if (self.popupViewController != nil) {
		[SVProgressHUD show];
		[[CommManager getCommMgr] orderExecuteSvcMgr].delegate = self;
		STPassengerInfo *passenger = [mOrderInfo.pass_list objectAtIndex:0];

		TEST_NETWORK_RETURN
		
		if(self.mOrderType == TYPE_SINGLE_ORDER)
		{
			[[[CommManager getCommMgr] orderExecuteSvcMgr] SignOnceOrderPassengerUpload:[Common getUserID] OrderId:self.mOrderId PassId:passenger.uid Password:result DevToken:[Common getDeviceMacAddress]];
		}
		else if(self.mOrderType == TYPE_WORK_ORDER)
		{
			[[[CommManager getCommMgr] orderExecuteSvcMgr] SignOnOffOrderPassengerUpload:[Common getUserID] OrderId:self.mOrderId PassId:passenger.uid Password:result DevToken:[Common getDeviceMacAddress]];
		}
		
//        [self dismissPopupViewControllerAnimated:YES completion:^{
//        }];
    }
    
}


/////////////////////////////////////////////////////////////////////////////
#pragma mark - User Interaction

/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)onClickedCallCustomer:(id)sender
{
    // init with phone number
    NSString * sPhoneNum = [[mOrderInfo.pass_list objectAtIndex:0] phone];
    // call this phone number
   // [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@", sPhoneNum]]];
    
    NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",sPhoneNum]];
    UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
    [self.view addSubview:phoneCallWebView];

    
}

/**
 * 车主标记到达
 */
- (IBAction)onClickedSetCustomer:(id)sender
{
    //_progressBar.progress = PROGRESS_SETCUSTOM;
    UIAlertView *setCusAlert =[[UIAlertView alloc]initWithTitle:@"" message:@"是否确认已到达乘客上车位置" delegate:self cancelButtonTitle:@"确定" otherButtonTitles:@"取消", nil];
    setCusAlert.tag=0;
    [setCusAlert show];
    
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
	TEST_NETWORK_RETURN

	if (alertView.tag == 0)
	{
		//标记到达
		if (buttonIndex == 0)
		{
			[SVProgressHUD show];
			[[CommManager getCommMgr] orderExecuteSvcMgr].delegate = self;

			if(self.mOrderType == TYPE_SINGLE_ORDER)
			{
				[[[CommManager getCommMgr] orderExecuteSvcMgr]  SignOnceOrderDriverArrival:[Common getUserID] OrderId:mOrderId DevToken:[Common getDeviceMacAddress]];
			}
		}
	} else if (alertView.tag ==1) { //结束服务
		if (buttonIndex == 0)
		{
			[SVProgressHUD show];
			[[CommManager getCommMgr] orderExecuteSvcMgr].delegate = self;

			if (self.mOrderType == TYPE_SINGLE_ORDER)
			{
				[[[CommManager getCommMgr] orderExecuteSvcMgr] EndOnceOrder:[Common getUserID] OrderId:self.mOrderId DevToken:[Common getDeviceMacAddress]];
			}
		}
	}
}


- (IBAction)onClickedChkCustomer:(id)sender
{
	// show check password modal
	PasswordCheckModal *popup = [[PasswordCheckModal alloc] initWithNibName:@"PasswordCheckModal" bundle:nil];
	popup.tips =@"乘客上车后，要求乘客输入他设定的4位电子车标密码，请勿自行尝试";
	popup.delegate = self;

	[self presentPopupViewController:popup animated:YES completion:^(void) {
		MyLog(@"popup view presented");
	}];
}


- (IBAction)onClickedFinish:(id)sender
{
	UIAlertView *finishalert =[[UIAlertView alloc]initWithTitle:@"" message:@"是否已将乘客安全送达目的地?" delegate:self cancelButtonTitle:@"确定" otherButtonTitles:@"取消", nil];
	finishalert.tag =1;
	[finishalert show];	
}

- (IBAction)onClickedComplain:(id)sender
{
//    NSString * urlString = [NSString stringWithFormat:@"telprompt://%@",COMPLAIN_TEL_NUM];
//    [[UIApplication sharedApplication]openURL:[NSURL URLWithString:urlString]];
    
    NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",COMPLAIN_TEL_NUM]];
    UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
    [self.view addSubview:phoneCallWebView];

}

/////////////////////////////////////////////////////////////////////////////
#pragma mark - OrderSvcMgr Delegate Methods
- (void) getDetailedDriverOrderInfoResult:(NSString *)result OrderInfo:(STDetailedDrvOrderInfo *)order_info
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        NSString *startToDesti =[NSString stringWithFormat:@"%@---%@",order_info.start_addr,order_info.end_addr];
		[_lblStartAddr setText:startToDesti];
        NSString *timeStr =[NSString stringWithFormat:@"预定出发时间：%@",order_info.start_time];
        [_lblUsedDistance setText:[NSString stringWithFormat:@"总距离：%@ %@", order_info.total_distance, TEXT_GONGLI]];
        
		[_lblStartTime setText:timeStr];
//        ORDER_STATE_STARTED,//开始执行
//        ORDER_STATE_DRIVER_ARRIVED,//车主到达
//        ORDER_STATE_PASSENGER_GETON,// 乘客上车
//        ORDER_STATE_FINISHED,//执行结束
        switch (order_info.state) {
            case ORDER_STATE_STARTED:
                _progressBar.progress = PROGRESS_START;
                _btnSetCus.enabled=YES;
                _btnCheckCus.enabled =NO;
                _btnFinish.enabled =NO;
                break;
            case ORDER_STATE_DRIVER_ARRIVED:
                _progressBar.progress = PROGRESS_SETCUSTOM;
                _btnSetCus.enabled=NO;
                _btnCheckCus.enabled =YES;
                _btnFinish.enabled =NO;
                break;
             case ORDER_STATE_PASSENGER_GETON:
                _progressBar.progress = PROGRESS_CHKCUSTOM;
                _btnSetCus.enabled=NO;
                _btnCheckCus.enabled =NO;
                _btnFinish.enabled =YES;
                break;
                case ORDER_STATE_FINISHED:
                _progressBar.progress = PROGRESS_FINISH;
            default:
                break;
        }
        
       
		mOrderInfo = order_info;
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void) executeOnceOrderResult:(NSString *)result
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
		
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void) executeOnOffOrderResult:(NSString *)result
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
		
		
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void) signOnceOrderDriverArrivalResult:(NSString *)result
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
		_progressBar.progress = PROGRESS_SETCUSTOM;
        _btnSetCus.enabled=NO;
        _btnCheckCus.enabled =YES;
        _btnFinish.enabled =NO;
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void) signOnOffOrderDriverArrivalResult:(NSString *)result
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
		_progressBar.progress = PROGRESS_SETCUSTOM;
		
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void) signOnceOrderPassengerUploadResult:(NSString *)result
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
		[self dismissPopupViewControllerAnimated:YES completion:^{
		}];
		_progressBar.progress = PROGRESS_CHKCUSTOM;
        _btnSetCus.enabled=NO;
        _btnCheckCus.enabled =NO;
        _btnFinish.enabled =YES;
        [SVProgressHUD dismiss];
    }
    else
    {
        self.failureTimes +=1;
        if(self.failureTimes ==5)
        {
        [SVProgressHUD dismissWithError:@"密码验证错误，司机请勿自行尝试，诚信拼车！" afterDelay:DEF_DELAY];
        }
        else
        {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
        }
    }
}

- (void) signOnOffOrderPassengerUploadResult:(NSString *)result
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
		[self dismissPopupViewControllerAnimated:YES completion:^{
		}];
		_progressBar.progress = PROGRESS_CHKCUSTOM;
		
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}
/**
 *  结束服务 代理接收结果
 *
 *  @param result <#result description#>
 */
- (void) endOnceOrderResult:(NSString *)result
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
		_progressBar.progress = PROGRESS_FINISH;
		
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    [self dismissViewControllerAnimated:YES completion:nil];
    
}

- (void) endOnOffOrderResult:(NSString *)result
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
		_progressBar.progress = PROGRESS_FINISH;
		
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void) checkOrderCancelledStateResult:(NSString *)result status:(int)status
{
    if (status == ORDER_CANCELLED) {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
        // back to parent
        [self performSelector:@selector(onClickedBack:) withObject:nil afterDelay:DEF_DELAY];
    }
}

@end






