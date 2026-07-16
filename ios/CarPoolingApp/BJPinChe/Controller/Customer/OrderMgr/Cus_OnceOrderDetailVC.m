//
//  Cus_OrderDetViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/1/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//  单次拼车  乘客 详情界面

/**
 *  state : 订单的状态(int),
 (
 0 : 车主接单, 还没有同意
 1 : 发布
 2 : 成交/待执行
 3 : 开始执行
 4 : 车主到达
 5 : 乘客上车
 6 : 执行结束
 7 : 已结算
 8 : 已评价
 9 : 已关闭
 10 : 已退票
 )
 */

#import "Cus_OnceOrderDetailVC.h"
#import "InsuranceDetailModal.h"
#import "UIViewController+CWPopup.h"
#import "Cus_OrderDrvPosViewController.h"
#import "Cus_PayOrderViewController.h"
#import "UIViewController+CWPopup.h"
#import "Cus_EvalDriverViewController.h"
#import "Cus_driverInfoViewController.h"
#import "Cus_EvalDriverLatestViewController.h"
@interface Cus_OnceOrderDetailVC ()<insuranceDetailDelegate,OrderSvcDelegate,EvalDrvDelegate,OrderExecuteSvcDelegate>
@property(nonatomic,strong)STDetailedCusOrderInfo *mDetailOrderInfo;

@end

@implementation Cus_OnceOrderDetailVC

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
    self.hidesBottomBarWhenPushed =YES;
//    UIButton *btn2 =[[UIButton alloc]initWithFrame:CGRectMake(0, 0, 33, 33)];
//    btn2.backgroundColor =[UIColor clearColor];
//    [btn2 addTarget:self action:@selector(onClickedRefresh) forControlEvents:UIControlEventTouchUpInside];
//    UIBarButtonItem *rightItem =[[UIBarButtonItem alloc]initWithCustomView:btn2];
//    self.navigationItem.rightBarButtonItem =rightItem;

	_driverView.userInteractionEnabled =YES;
	UITapGestureRecognizer *tapRecongnazier =[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(onClickedDriver)];
	tapRecongnazier.cancelsTouchesInView=NO;
	[_driverView addGestureRecognizer:tapRecongnazier];
}



- (void)viewDidLoad
{
	[super viewDidLoad];
	// Do any additional setup after loading the view.

	//  [self initControls];
    
    UIButton *btn =[UIButton buttonWithType:UIButtonTypeCustom];
    [btn setFrame:CGRectMake(0, 0, 40, 30)];
    [btn addTarget:self action:@selector(shuaxinClick:) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *barItem =[[UIBarButtonItem alloc]initWithCustomView:btn];
    self.navigationItem.rightBarButtonItem =barItem;
    
}
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self initControls];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
/**
 *  xib 文件采用的autolayout  必须在didappear之后进行设置
 *
 *  @param animated <#animated description#>
 */
- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    CGRect rect = [[UIScreen mainScreen] bounds];
    CGSize size = rect.size;
    CGFloat width = size.width;
    CGFloat height = size.height;
    scrollView.scrollEnabled =YES;
    scrollView.contentSize =CGSizeMake(width, 668);
}



///////////////////////////////////////////////////////////////////////////
#pragma mark - Basic Function

/**
 * Initilaize all ui controls & member variable
 */
- (void) initControls
{
    /******************** set main order info ******************/
    // set user image
    //_imgUser
    
    // set sex image & change age color according to sex
    //_imgSex
    //if ([mOrderInfo.sex isEqualToString:SEX_MALE]) {
    if (mOrderInfo.sex == SEX_MALE)
    {
        _lblAge.textColor = MYCOLOR_GREEN;
    }
    [_lblAge setText:[NSString stringWithFormat:@"%d", mOrderInfo.age]];
    [_lblName setText:mOrderInfo.name];
//    [_lblOrderState sizeToFit];
    /************** show order detail view by order state ***************/
    if(self.mOrderInfo.state < ORDER_STATE_FINISHED)
    {
        [self showWaitOrderDetail];
    }
    else
    {
        [self showAfterOPOrderDetail];
    }

	TEST_NETWORK_RETURN;
	[SVProgressHUD show];
	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
	[[[CommManager getCommMgr] orderSvcMgr] GetDetailedCustomerOrderInfo:[Common getUserID] OrderId:self.mOrderInfo.uid OrderType:self.mOrderInfo.type DevToken:[Common getDeviceMacAddress]];

}


///////////////////////////////////////////////////////////////////////////
#pragma mark - Setting Control by order state  执行前
- (void) showWaitOrderDetail
{
    // show & hide detain view
    [_vwOrderInfo setHidden:NO];
    [_vwOrderInfo1 setHidden:YES];
    
    // 出发  目的地
    NSString *startToend =[NSString stringWithFormat:@"%@---%@",mOrderInfo.startPos,mOrderInfo.endPos];
    [_labelStartToEndPos_before setText:startToend];
    
    _lblOrderStartTime1.text =[NSString stringWithFormat:@"预约时间：%@",mOrderInfo.start_time];
    _lblOrderPubTime.text =[NSString stringWithFormat:@"预约时间：%@",mOrderInfo.start_time];
}
#pragma mark - Setting Control by order state  执行后
- (void) showAfterOPOrderDetail
{
    // show & hide detain view
    [_vwOrderInfo setHidden:YES];
    [_vwOrderInfo1 setHidden:NO];
    
    // set control value
    NSString *startToend =[NSString stringWithFormat:@"%@---%@",mOrderInfo.startPos,mOrderInfo.endPos];
    [_labelStartToEndPos_after setText:startToend];
    
    _lblOrderStartTime1.text =[NSString stringWithFormat:@"预约时间：%@",mOrderInfo.start_time];
    _lblOrderPubTime.text =[NSString stringWithFormat:@"预约时间：%@",mOrderInfo.start_time];
    
    // change button image by state
    
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

// main control event
/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    // BACK_VIEW;
}

-(void)onClickedDriver
{
    UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
    
    Cus_driverInfoViewController * driverInfoViewController = [customerStroyboard instantiateViewControllerWithIdentifier:@"DriverInfo"];
    
    driverInfoViewController.driverid = mOrderInfo.driver_id  ;
    
    [self.navigationController pushViewController:driverInfoViewController animated:YES];
}

/**
 * refresh button click event implementation
 */
- (IBAction)onClickedRefresh:(id)sender
{
    [self initControls];
}

//////////////////////////////// wait operate view
/**
 * call driver button clicked event
 */
- (IBAction)onClickedCallDriver:(id)sender
{
    if (_mDetailOrderInfo.driver_info.phone.length == 0) {
        [SVProgressHUD showErrorWithStatus:@"还没有车主抢单" duration:2];
        return;
    }

	if (_mDetailOrderInfo.state == 7)
		return;

    NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",_mDetailOrderInfo.driver_info.phone]];
    UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
    [self.view addSubview:phoneCallWebView];
    
    //[[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",_mDetailOrderInfo.driver_info.phone]]];
}
/**
 *  查看车主位置  只需要传递车主id
 *
 *  @param sender <#sender description#>
 */
- (IBAction)onClickedDriverPos:(id)sender
{
     UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
    Cus_OrderDrvPosViewController *drvPosViewController =[customerStroyboard instantiateViewControllerWithIdentifier:@"order_driver_pos"];
    drvPosViewController.mOrderInfo = self.mOrderInfo;
    [self .navigationController pushViewController:drvPosViewController animated:YES];
}

/**
 * cancel order button clicked event
 */
- (IBAction)onClickedCancelOrder:(id)sender
{
#define CONFIRM_CANCEL_TAG			999
	UIAlertView* alertView = [[UIAlertView alloc] initWithTitle:@"" message:@"终止服务确认" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确认", nil];
	alertView.tag = CONFIRM_CANCEL_TAG;
	[alertView show];
}


- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
	if (alertView.tag == CONFIRM_CANCEL_TAG) {
		if (buttonIndex == 1) {
			TEST_NETWORK_RETURN;
			[SVProgressHUD show];
			[[CommManager getCommMgr] orderSvcMgr].delegate = self;
			[[[CommManager getCommMgr] orderSvcMgr] CancelOnceOrder:[Common getUserID]  OrderId:self.mOrderInfo.uid DevToken:[Common getDeviceMacAddress]];
		}
	}
}


/**
 * complain button clicked event
 */
- (IBAction)onClickedComplain:(id)sender
{
   // [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",ComplainNummber]]];
    
    NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",ComplainNummber]];
    UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
    [self.view addSubview:phoneCallWebView];

}

///////////////////////////////// after operate view
/**
 * complain button clicked event (started operate state)
 */
- (IBAction)onClickedComplainAfter:(id)sender
{
   // [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",ComplainNummber]]];
    
    NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",ComplainNummber]];
    UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
    [self.view addSubview:phoneCallWebView];

}

/**
 *  @property(nonatomic,copy)NSString *appl_no;
 @property(nonatomic,copy)NSString *effect_time;
 @property(nonatomic,copy)NSString *insexpr_date;
 @property(nonatomic,assign)double  total_amount;
 @property(nonatomic,copy)NSString *isd_name;
 *
 *  @param sender <#sender description#>
 */

- (IBAction)onClickedInsuranceDetail:(id)sender
{
    InsuranceDetailModal *insuranceDetail =[[InsuranceDetailModal alloc]initWithNibName:@"InsuranceDetailModal" bundle:nil];
    STDetailedDrvOrderInfo *detailDrvInfo =[[STDetailedDrvOrderInfo alloc]init];
    detailDrvInfo.appl_no =_mDetailOrderInfo.appl_no;
    detailDrvInfo.effect_time =_mDetailOrderInfo.effect_time;
    detailDrvInfo.insexpr_date =_mDetailOrderInfo.insexpr_date;
    detailDrvInfo.total_amount =_mDetailOrderInfo.total_amount;
    detailDrvInfo.isd_name =_mDetailOrderInfo.isd_name;
    insuranceDetail.mOrderInfo =detailDrvInfo;
    insuranceDetail.delegate =self;
    [self presentPopupViewController:insuranceDetail animated:YES completion:nil];
}
- (void)tappedDone
{
    [self dismissPopupViewControllerAnimated:YES completion:nil];
}

/**
 * First button clicked event (started operate state)
 * or call driver button clicked event
 */
- (IBAction)onClickedCallDriverAfter:(id)sender
{
    //[[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",_mDetailOrderInfo.driver_info.phone]]];
    
    NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",_mDetailOrderInfo.driver_info.phone]];
    UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
    [self.view addSubview:phoneCallWebView];

}

/**
 * Second button clicked event (started operate state)
 * pay or evalution button
 */
- (IBAction)onClickedPayOrEval:(id)sender
{
    if(_mDetailOrderInfo.state == ORDER_STATE_FINISHED)//执行结束 待结算
    {
        UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
        Cus_PayOrderViewController * viewController = (Cus_PayOrderViewController *)[customerStroyboard instantiateViewControllerWithIdentifier:@"pay_order"];

        viewController.mOrderInfo = self.mOrderInfo;
        [self presentViewController:viewController animated:YES completion:nil];
       // SHOW_VIEW(viewController);
    
    }else if (_mDetailOrderInfo.state == ORDER_STATE_PAYED)//已经结算 待评价
    {
        // show evaluation dialog
        Cus_EvalDriverViewController *popup = [[Cus_EvalDriverViewController alloc] initWithNibName:@"Cus_EvalDriverViewController" bundle:nil];
        
        popup.delegate = self;
        [popup setParent:self];
        
        [self presentPopupViewController:popup animated:YES completion:^(void) {
            MyLog(@"popup view presented");
        }];
    }

    if (_mDetailOrderInfo.state == ORDER_STATE_EVALUATED) {
        TEST_NETWORK_RETURN;
        [SVProgressHUD show];
        [[CommManager getCommMgr] orderSvcMgr].delegate = self;
        [[[CommManager getCommMgr] orderSvcMgr] GetDriverLatestEvalInfo:[Common getUserID] DriverId:_mDetailOrderInfo.driver_info.uid LimitId:_mDetailOrderInfo.uid DevToken:[Common getDeviceMacAddress]];
        
    }

}


#pragma OrderSvcMgr Delegate Methods
- (void) getDetailedCustomerOrderInfoResult:(NSString *)result OrderInfo:(STDetailedCusOrderInfo *)order_info
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        _mDetailOrderInfo =order_info;
        
        [_imgUser setImageWithURL:[NSURL URLWithString:order_info.driver_info.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
        if(mOrderInfo.sex == 0) {
            [_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
        }
        else {
            [_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
            _lblAge.textColor = MYCOLOR_GREEN;
        }
        [_imgCar setImageWithURL:[NSURL URLWithString:order_info.driver_info.carimg] placeholderImage:[UIImage imageNamed:@"demo_carimg.png"]];
        [_carBrand setText:order_info.driver_info.car_brand];
        [_carBrand setTextColor:MYCOLOR_GREEN];
        [_carStyle setTitle:order_info.driver_info.car_style forState:UIControlStateNormal];
        [_carStyle setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [_carStyle setBackgroundColor:MYCOLOR_GREEN];
        
        [_lblAge setText:[NSString stringWithFormat:@"%d", order_info.driver_info.age]];
        [_lblName setText:order_info.driver_info.name];
        [_lblEvalPro setText:order_info.driver_info.goodeval_rate_desc];
        [_lblServeCnt setText:order_info.driver_info.carpool_count_desc];


		if (order_info.state < ORDER_STATE_FINISHED) //执行前
        {
            [self showWaitOrderDetail];
            [_btnOpertate1_before setBackgroundImage:[UIImage imageNamed:@"btn_CallDrv.png"] forState:UIControlStateNormal];
            [_lblOrderNum setText: [NSString stringWithFormat:@"订单编号：%@",order_info.order_num]];
            [_lblOrderState setText:order_info.state_desc];

			// 出发  目的地
            NSString *startToend =[NSString stringWithFormat:@"%@---%@",mOrderInfo.startPos,mOrderInfo.endPos];
            [_labelStartToEndPos_before setText:startToend];

			[_lblOrderPubTime1 setText:order_info.start_time];

			NSString *strMidPoints = @"";
            for (STMidPoint *midPoint in order_info.mid_points)
            {
                if([strMidPoints length] <= 0)
                {
                    strMidPoints = [midPoint address];
                }
                else
                {
                    strMidPoints = [NSString stringWithFormat:@"%@, %@", strMidPoints, [midPoint address]];
                }
            }

			[_lblMidPoints setText:[NSString stringWithFormat:@"中途点：%@",strMidPoints]];
			[_lblCarNum setText:[NSString stringWithFormat:@"车牌号：%@", [Common getConcealedCarNo:order_info.driver_info.carno]]];
            [_lblElectricTicket setText:[NSString stringWithFormat:@"电子车票：%@",order_info.password]];
            _btnOperate2before.enabled =NO;

			// [_btnOperate2before setBackgroundImage:[UIImage imageNamed:@"btn_orderCheckDrvPos_disable"] forState:UIControlStateNormal];
		}
		else //执行后
		{
            [self showAfterOPOrderDetail];
			[_lblOrderNum1 setText:[NSString stringWithFormat:@"订单编号：%@",order_info.order_num]];

			// 出发  目的地
			NSString *startToend =[NSString stringWithFormat:@"%@---%@",mOrderInfo.startPos,mOrderInfo.endPos];
			[_labelStartToEndPos_after setText:startToend];
			[_lblOrderState1 setText:order_info.state_desc];

			[_lblOrderPubTime1 setText:order_info.start_time];
            NSString *strMidPoints = @"";
            for(STMidPoint *midPoint in order_info.mid_points)
            {
                if([strMidPoints length] <= 0)
                {
                    strMidPoints = [midPoint address];
                }
                else
                {
                    strMidPoints = [NSString stringWithFormat:@"%@, %@", strMidPoints, [midPoint address]];
                }
            }
            [_lblMidPoints1 setText:[NSString stringWithFormat:@"中途点：%@",strMidPoints]];
		}

        _btnOperate2before.enabled =YES;
        switch (_mDetailOrderInfo.state)
        {
                case ORDER_STATE_FINISHED:
                [_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_pay_ludian.png"] forState:UIControlStateNormal];
                [_btnOperate1 setBackgroundImage:[UIImage imageNamed:@"btn_CallDrv.png"] forState:UIControlStateNormal];
				[_btnOperate2 setEnabled:YES];
                break;
            case ORDER_STATE_PAYED:
				[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_eval_driver.png"] forState:UIControlStateNormal];
				[_btnOperate1 setBackgroundImage:[UIImage imageNamed:@"btn_order_finish"] forState:UIControlStateNormal];
				[_btnOperate2 setEnabled:YES];
				break;
			case ORDER_STATE_EVALUATED:
				[_btnOperate1 setBackgroundImage:[UIImage imageNamed:@"btn_order_finish"] forState:UIControlStateNormal];

				if (_mDetailOrderInfo.evaluated == 1) {
					[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_good.png"] forState:UIControlStateNormal];
				} else if (_mDetailOrderInfo.evaluated == 2) {
					[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_medium.png"] forState:UIControlStateNormal];
				} else {
					[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_low.png"] forState:UIControlStateNormal];
				}

				[_btnOperate2 setEnabled:YES];

				//右侧  评价状态设置
				[self evaluateOnceOrderDriverResult:SVCERR_MSG_SUCCESS Level:self.mOrderInfo.evaluated];
				break;
			case ORDER_STATE_CLOSED:
				[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_exit.png"] forState:UIControlStateNormal];
				[_btnOperate2 setEnabled:NO];
				break;
			case ORDER_STATE_Ticket_REFUND:
				[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_exit.png"]   forState:UIControlStateNormal];
				[_btnOperate2 setEnabled:NO];
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

- (void)cancelOnceOrderResult:(NSString *)result
{
    [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];

	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		[self.navigationController popViewControllerAnimated:YES];
	}
	else
	{
//		[self.view makeToast:result];
	}
}

#pragma EvalDrvDelegate Methods
- (void) submitComment : (NSString *)message level:(int)level
{
	TEST_NETWORK_RETURN;
    [SVProgressHUD show];
    [[CommManager getCommMgr]orderExecuteSvcMgr].delegate =self;
     [[[CommManager getCommMgr] orderExecuteSvcMgr] EvaluateOnceOrderDriver:[Common getUserID] DriverId:_mDetailOrderInfo.driver_info.uid OrderId:self.mOrderInfo.uid  Level:level Msg:message DevToken:[Common getDeviceMacAddress]];
}
- (void)evaluateOnceOrderDriverResult:(NSString *)result Level:(int)level
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        mOrderInfo.state = ORDER_STATE_EVALUATED;
        switch (level) {
            case EVAL_NOT:
                [_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment.png"] forState:UIControlStateNormal];
                break;
            case EVAL_HIGH:
                [_btnOperate2  setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_good.png"] forState:UIControlStateNormal];
                break;
            case EVAL_MEDIUM:
                [_btnOperate2  setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_medium.png"] forState:UIControlStateNormal];
                break;
            case EVAL_LOW:
                [_btnOperate2  setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_low.png"] forState:UIControlStateNormal];
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



- (IBAction)onClickUserPhoto:(id)sender {
    UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
    
    Cus_driverInfoViewController * driverInfoViewController = (Cus_driverInfoViewController *)[customerStroyboard instantiateViewControllerWithIdentifier:@"DriverInfo"];
    
    driverInfoViewController.driverid = _mDetailOrderInfo.driver_info.uid;
    [self.navigationController pushViewController:driverInfoViewController animated:YES];
}


- (IBAction)onClickCarImage:(id)sender {
	UIView* mainView = [[UIView alloc] initWithFrame:[UIScreen mainScreen].bounds];
	mainView.backgroundColor = [UIColor blackColor];


	UIImageView* imgView = [[UIImageView alloc] initWithFrame:[UIScreen mainScreen].bounds];
	[imgView setImageWithURL:[NSURL URLWithString:_mDetailOrderInfo.driver_info.carimg] placeholderImage:[UIImage imageNamed:@"demo_carimg.png"]];
	imgView.frame = CGRectMake(0, mainView.frame.size.height / 2 - mainView.frame.size.width / 2, mainView.frame.size.width, mainView.frame.size.width);

	UIButton* btnItem = [UIButton buttonWithType:UIButtonTypeCustom];
	[btnItem setBackgroundColor:[UIColor clearColor]];
	[btnItem addTarget:self action:@selector(onClickImage:) forControlEvents:UIControlEventTouchUpInside];
	btnItem.frame = mainView.bounds;

	[mainView addSubview:imgView];
	[mainView addSubview:btnItem];

	[self.navigationController.view addSubview:mainView];
}


- (void)onClickImage:(id)sender {
	UIButton* btnItem = (UIButton*)sender;
	UIView* viewMain = btnItem.superview;
	[viewMain performSelector:@selector(removeFromSuperview) withObject:nil afterDelay:0];
}


#pragma mark 获取评价信息
-(void)getDriverLatestEvalInfoResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		[SVProgressHUD dismiss];
		if (dataList.count !=0) {
			Cus_EvalDriverLatestViewController *popup = [[Cus_EvalDriverLatestViewController alloc] initWithNibName:@"Cus_EvalDriverLatestViewController" bundle:nil];

			[self presentPopupViewController:popup animated:YES completion:^(void) {
				MyLog(@"popup view presented");
			}];

			STDriverEvalInfo *dtivet = [dataList objectAtIndex:0];
			[popup setParent:self message:dtivet.contents level:dtivet.eval];
		}
        
	} else {
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
}

- (void)shuaxinClick:(id)sender {
    [self initControls];
}
@end
