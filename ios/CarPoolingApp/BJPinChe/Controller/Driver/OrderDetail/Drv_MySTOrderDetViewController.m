//
//  Drv_STOrderDetViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/3/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//   车主 单次拼车详情页面
// 未完成


#import "Drv_MySTOrderDetViewController.h"
#import "UIViewController+CWPopup.h"
#import "InsuranceDetailModal.h"
#import "UIView+Extension.h"
#import "UIViewController+CWPopup.h"
#import "Drv_OrderPerformViewController.h"
#import "Drv_EvalCustomerViewController.h"
#import "Drv_PassengerInfoViewController.h"
#import "Cus_EvalDriverLatestViewController.h"


@interface Drv_MySTOrderDetViewController ()<EvalCusDelegate,insuranceDetailDelegate,OrderExecuteSvcDelegate,UIAlertViewDelegate>
@property(nonatomic,strong)STDetailedDrvOrderInfo *mDetailorderInfo;
@property(nonatomic,strong)STPassengerInfo *passengerInfo;
@end

@implementation Drv_MySTOrderDetViewController

@synthesize mOrderInfo;


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
	UIButton *btn2 =[[UIButton alloc]initWithFrame:CGRectMake(0, 0, 33, 33)];
	[btn2 setBackgroundImage:[UIImage imageNamed:@"btnRefresh.png"] forState:UIControlStateNormal];
	[btn2 addTarget:self action:@selector(onClickedRefresh:) forControlEvents:UIControlEventTouchUpInside];
	UIBarButtonItem *rightItem =[[UIBarButtonItem alloc]initWithCustomView:btn2];
	self.navigationItem.rightBarButtonItem =rightItem;

	_cusView.userInteractionEnabled =YES;

//	UITapGestureRecognizer *tapRecongnazier =[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(onClickedPassenger)];
//	tapRecongnazier.cancelsTouchesInView=NO;
//	[_cusView addGestureRecognizer:tapRecongnazier];
}


- (void)viewDidLoad
{
	[super viewDidLoad];
}


- (void)viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
    CGRect rect = [[UIScreen mainScreen] bounds];
    if (rect.size.height<500)
    {
        myScrollView.contentSize = CGSizeMake(320, 550);
    }
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


///////////////////////////////// Initialize ////////////////////////////////
#pragma mark - Initialize data info
- (void) initControls
{
	TEST_NETWORK_RETURN

	[SVProgressHUD show];
	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
	[[[CommManager getCommMgr] orderSvcMgr] GetDetailedDriverOrderInfo:[Common getUserID] OrderId:self.mOrderInfo.uid OrderType:self.mOrderInfo.type DevToken:[Common getDeviceMacAddress]];
}


///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

- (IBAction)onClickedBack:(id)sender
{
	BACK_VIEW;
}


- (IBAction)onClickedPassenger:(id)sender
{
	// go to search passenger view controller
	Drv_PassengerInfoViewController *viewController = (Drv_PassengerInfoViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"passenger_info"];

	viewController.mPassID = mOrderInfo.pass_id;

	[self presentViewController:viewController animated:YES completion:nil];
}


/**
 * Passenger image click event implementation
 */

/**
 * Refresh button clicke event
 */
- (void)onClickedRefresh:(id)sender
{
	TEST_NETWORK_RETURN

	[SVProgressHUD show];
	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
	[[[CommManager getCommMgr] orderSvcMgr] GetDetailedDriverOrderInfo:[Common getUserID] OrderId:self.mOrderInfo.uid OrderType:self.mOrderInfo.type DevToken:[Common getDeviceMacAddress]];
}


/**  左侧按钮点击事件
 * First operation button click event (first circle button)
 */
- (IBAction)onClickedOPButton1:(id)sender
{
	switch (_mDetailorderInfo.state) {
		case 2:
        {//[[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",_passengerInfo.phone]]];
            
            NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",_passengerInfo.phone]];
            UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
            [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
            [self.view addSubview:phoneCallWebView];
			break;
        }
		case 6:
		//	[[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",_passengerInfo.phone]]];
        {
            NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",_passengerInfo.phone]];
            UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
            [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
            [self.view addSubview:phoneCallWebView];
            
			break;
        }
		default:
			break;
	}
}



/** 右侧按钮 点击事件
 * Second operation button click event (second circle button)
 */
- (IBAction)onClickedOPButton2:(id)sender
{
	switch (_mDetailorderInfo.state)
	{
		case 2: //执行订单
		{
			UIAlertView *alert=[[UIAlertView alloc]initWithTitle:@"" message:@"市内订单开始执行后不能暂停或退出，必须执行完毕，是否开始执行？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
			[alert show];
			break;
		}
		case 7: // 评价乘客
		case 9:
		{
			Drv_EvalCustomerViewController *popup = [[Drv_EvalCustomerViewController alloc] initWithNibName:@"Drv_EvalCustomerViewController" bundle:nil];

			popup.delegate = self;

			STPassengerInfo *passInfo = [[STPassengerInfo alloc] init];
			passInfo.uid = mOrderInfo.pass_id;
			[popup initData:self dataInfo:passInfo];

			[self presentPopupViewController:popup animated:YES completion:^(void) {
				MyLog(@"popup view presented");
			}];
			break;
		}
		case 8:			// 已评价
		{
			Cus_EvalDriverLatestViewController* controller = [[Cus_EvalDriverLatestViewController alloc] initWithNibName:@"Cus_EvalDriverLatestViewController" bundle:nil];
            
            [self presentPopupViewController:controller animated:YES completion:^(void) {
                MyLog(@"popup view presented");
            }];
			[controller setParent:self message:self.mOrderInfo.eval_content level:self.mOrderInfo.evaluated];
//			[self presentPopupViewController:controller animated:YES completion:nil];
			break;
		}
		default:
		{
			break;
		}
	}
}



#pragma mark  alertView 代理返回
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
	if (buttonIndex == 1)
	{
		if ([CLLocationManager locationServicesEnabled] &&
			([CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorized || [CLLocationManager authorizationStatus] == kCLAuthorizationStatusNotDetermined))
		{
			//定位功能可用，开始定位
			TEST_NETWORK_RETURN

			[SVProgressHUD show];
			[[CommManager getCommMgr] orderExecuteSvcMgr].delegate = self;
			[[[CommManager getCommMgr] orderExecuteSvcMgr] ExecuteOnceOrder:[Common getUserID] OrderId:self.mOrderInfo.uid DevToken:[Common getDeviceMacAddress]];
		} else if ([CLLocationManager authorizationStatus] == kCLAuthorizationStatusDenied) {
			UIAlertView *alertNavi =[[UIAlertView alloc]initWithTitle:@"" message:@"执行订单需开启GPS，请开启GPS后再次执行订单" delegate:self cancelButtonTitle:@"" otherButtonTitles:nil, nil];
			[alertNavi show];
		}
	}
	else
	{
	}
}


/**
 *  Description
 *
 *  @param result result description
 */
- (void) executeOnceOrderResult:(NSString *)result
{
	Drv_OrderPerformViewController *controller = (Drv_OrderPerformViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"orderperform"];
	controller.mOrderId = self.mOrderInfo.uid;
	controller.mOrderType = TYPE_SINGLE_ORDER ;

	[self presentViewController:controller animated:NO completion:nil];

	//SHOW_VIEW(controller);
}


#pragma EvalPassengerDelegate Methods
- (void) submitComment : (NSString *)message level:(int)level passInfo:(STPassengerInfo *)passInfo
{
	TEST_NETWORK_RETURN

	[SVProgressHUD show];
	[[CommManager getCommMgr] orderExecuteSvcMgr].delegate = self;

	[[[CommManager getCommMgr] orderExecuteSvcMgr] EvaluateOnceOrderPass:[Common getUserID] PassId:_passengerInfo.uid OrderId:_mDetailorderInfo.uid Level:level Msg:message DevToken:[Common getDeviceMacAddress]];
}


- (void)evaluateOnceOrderPassResult:(NSString *)result Level:(int)level
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		if(self.popupViewController !=nil)
		{
			[self dismissPopupViewControllerAnimated:YES completion:nil];
		}

		mOrderInfo.state = ORDER_STATE_EVALUATED;
		switch (level)
		{
			case EVAL_NOT:
				[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment.png"] forState:UIControlStateNormal];
				break;
			case EVAL_HIGH:
				[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_good.png"] forState:UIControlStateNormal];
				break;
			case EVAL_MEDIUM:
				[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_medium.png"] forState:UIControlStateNormal];
				break;
			case EVAL_LOW:
				[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_low.png"] forState:UIControlStateNormal];
				break;
			default:
				break;
		}
		[SVProgressHUD dismiss];
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
}



/**
 *  投诉
 *
 *  @param sender
 */
- (IBAction)onClickedComplain:(id)sender
{
	//[[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",ComplainNummber]]];
    NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",ComplainNummber]];
    UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
    [self.view addSubview:phoneCallWebView];
}


- (IBAction)onClickedInsuranceDetail:(id)sender {
	InsuranceDetailModal *insuranceDetail =[[InsuranceDetailModal alloc]initWithNibName:@"InsuranceDetailModal" bundle:nil];
	insuranceDetail.mOrderInfo =_mDetailorderInfo;
	insuranceDetail.delegate =self;
	[self presentPopupViewController:insuranceDetail animated:YES completion:nil];
}


- (void)tappedDone
{
	[self dismissPopupViewControllerAnimated:YES completion:nil];
}



#pragma OrderSvcMgr Delegate Methods
-(void) getDetailedDriverOrderInfoResult:(NSString *)result OrderInfo:(STDetailedDrvOrderInfo *)order_info
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		_mDetailorderInfo =order_info;
		if([order_info.pass_list count] > 0)
		{
			STPassengerInfo *passenger = [order_info.pass_list objectAtIndex:0];
			_passengerInfo = passenger;

			[_imgUser setImageWithURL:[NSURL URLWithString:passenger.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];

			if (passenger.sex == 0) {
				[_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
			} else {
				[_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
				_lblAge.textColor = [UIColor orangeColor];
			}

			[_lblAge setText:[NSString stringWithFormat:@"%d", passenger.age]];
			[_lblName setText:[NSString stringWithFormat:@"%@", passenger.name]];

			if (passenger.pverified ==1)
			{
				[_verifiedImgeView setImage:[UIImage imageNamed:@"icon_verified"]];
				[verifiedLabel setText:passenger.pverified_desc];
			} else {
				[_verifiedImgeView setImage:[UIImage imageNamed:@"icon_unverified"]];
				[verifiedLabel setText:passenger.pverified_desc];
			}

            [_lblEvalPro setText:passenger.goodeval_rate_desc];
            [_lblServeCnt setText:passenger.carpool_count_desc];
		}

		// 订单编号
		[_orderIDLabel setText:[NSString stringWithFormat:@"订单编号：%@", order_info.order_num]];


		// 起点 目的地
		NSString *startToEndAddr =[NSString stringWithFormat:@"%@---%@",order_info.start_addr,order_info.end_addr];
		[_lblStartToEndPos setText:startToEndAddr];
		[_lblAcceptTime  setText:order_info.start_time];
		[_lblPubTime setText:order_info.create_time];
		[_lblPrice setText:[NSString stringWithFormat:@"%1.f点", order_info.price]];
		[_lblState setText:order_info.state_desc];
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

		if ([strMidPoints length] > 0)
		{
			_lblMidPoint.hidden =NO;
			[_lblMidPoint setText:[NSString stringWithFormat:@"中途点：%@",strMidPoints]];
		}
		else
		{
			_lblMidPoint.hidden =YES;

			_lblPrice.y =CGRectGetMaxY(_lblAcceptTime.frame) +10;
			_lblState.y =CGRectGetMaxY(_lblState.frame) +5;
		}

		switch (order_info.state) {
			case 2:			// 成交待执行		拨打乘客电话
				[_btnOperate1 setBackgroundImage:[UIImage imageNamed:@"btn_order_call"] forState:UIControlStateNormal];

				//  右边 执行订单
				[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_execute"] forState:UIControlStateNormal];
				[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_execute_highlighted"] forState:UIControlStateHighlighted];
				[_btnOperate2 setTitle:@"执行订单" forState:UIControlStateNormal];
				[_btnOperate2 setTitleColor:MyColor(65, 168, 106) forState:UIControlStateNormal];
				_btnOperate2.titleEdgeInsets =UIEdgeInsetsMake(33,3 , 0, 0);
				break;
			case 6:			// 执行结束		拨打乘客电话
				[_btnOperate1 setBackgroundImage:[UIImage imageNamed:@"btn_order_call"] forState:UIControlStateNormal];

				// 右边 评价乘客 灰
				[_btnOperate2 setTitle:nil forState:UIControlStateNormal];
				[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_disabled"] forState:UIControlStateNormal];
				break;
			case 7:			// 已结算			订单完成
				[_btnOperate1 setBackgroundImage:[UIImage imageNamed:@"btn_order_finish"] forState:UIControlStateNormal];

				// 右边 评价乘客 激活
				[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment"] forState:UIControlStateNormal];
				break;
			case 8:			// 已评价			订单完成
			{
				[_btnOperate1 setBackgroundImage:[UIImage imageNamed:@"btn_order_finish"] forState:UIControlStateNormal];
				STPassengerInfo  *passenger = order_info.pass_list[0];
				[self evaluateOnceOrderPassResult:SVCERR_MSG_SUCCESS Level:passenger.evaluated];

				if (self.mOrderInfo.evaluated == 1)					// Good evaluation
				{
					[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_good"] forState:UIControlStateNormal];
				}
				else if (self.mOrderInfo.evaluated == 2)
				{
					[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_medium"] forState:UIControlStateNormal];
				}
				else
				{
					[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_low"] forState:UIControlStateNormal];
				}

				break;
			}
			case 9:			// 已关闭
				[_btnOperate1 setBackgroundImage:[UIImage imageNamed:@"btn_order_exit"] forState:UIControlStateNormal];
				[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment"] forState:UIControlStateNormal];
				break;
			default:
				break;
		}

		[SVProgressHUD dismiss];
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
}



@end
