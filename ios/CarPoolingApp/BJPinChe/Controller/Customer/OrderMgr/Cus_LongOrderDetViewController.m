//
//  Cus_LongOrderSureViewController.m
//  BJPinChe
//
//  Created by CKK on 14-8-28.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//  乘客 长途订单 详情页面

#import "Cus_LongOrderDetViewController.h"
#import "LongWayOrderSvcMgr.h"
#import "UIViewController+CWPopup.h"
#import "FindDriverViewController.h"
#import "Cus_PayOrderViewController.h"
#import "Cus_LongOrderCancelViewController.h"
#import "InsuranceDetailModal.h"
#import "Cus_driverInfoViewController.h"

@interface Cus_LongOrderDetViewController ()

@end

@implementation Cus_LongOrderDetViewController



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
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
//    [self createDataSource];
   // self.hidesBottomBarWhenPushed =YES;
	
    UITapGestureRecognizer *tap=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tapClick)];
    tap.cancelsTouchesInView=NO;
    [_imageUesrView addGestureRecognizer:tap];
    
	TEST_NETWORK_RETURN;
    [SVProgressHUD show];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetDetailedCustomerOrderInfo:[Common getUserID] OrderId:self.mOrderInfo.uid OrderType:self.mOrderInfo.type DevToken:[Common getDeviceMacAddress]];
}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    UIScreen *screen  = [UIScreen mainScreen];
    if (screen.bounds.size.height<500) {
        OrederScrollView.contentSize = CGSizeMake(302, 406);
    }
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - UITapGestureRecognizer


- (void)tapClick {
    Cus_driverInfoViewController * driverInfoViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"DriverInfo"];

	driverInfoViewController.driverid = mDetailedInfo.driver_info.uid;

	[self.navigationController pushViewController:driverInfoViewController animated:YES];
}



///////////////////////////////////////////////////////////////////////////
#pragma mark - Basic Function

/**
 * Initilaize all ui controls & member variable
 */
- (void) updateUI
{
    /************** show order detail view by order state ***************/
    
    // check order state
    [_btnOperate2 setEnabled:YES];
    switch (mOrderInfo.state) {
        case ORDER_STATE_DRIVER_ACCEPTED:
        case ORDER_STATE_PUBLISHED:
        case ORDER_STATE_GRABBED:
        {
            [_vwCancelOrder setHidden:YES];
            
            [_lblOrderStateWait setHidden:NO];
            [_lblOrderStateNormal setHidden:YES];
            
            [_lblCarNum setHidden:NO];
            [_lblCarNumName setHidden:NO];
            [_lblElectricTicket setHidden:NO];
            [_lblEletricTicketName setHidden:NO];
#warning 有问题
            [_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_checkDriveLocation.png"] forState:UIControlStateNormal];
            [_btnOperate2 setEnabled:YES];
            break;
        }
        case ORDER_STATE_STARTED:
        case ORDER_STATE_DRIVER_ARRIVED:
        case ORDER_STATE_PASSENGER_GETON:
        {
            [_vwCancelOrder setHidden:YES];
            
            [_lblOrderStateWait setHidden:YES];
            [_lblOrderStateNormal setHidden:NO];
            
            [_lblCarNum setHidden:YES];
            [_lblCarNumName setHidden:YES];
            [_lblElectricTicket setHidden:YES];
            [_lblEletricTicketName setHidden:YES];
            [_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_pay_ludian.png"] forState:UIControlStateNormal];
            [_btnOperate2 setEnabled:NO];
            break;
        }
        case ORDER_STATE_FINISHED:
        {
            [_vwCancelOrder setHidden:YES];
            
            [_lblOrderStateWait setHidden:YES];
            [_lblOrderStateNormal setHidden:NO];
            
            [_lblCarNum setHidden:YES];
            [_lblCarNumName setHidden:YES];
            [_lblElectricTicket setHidden:YES];
            [_lblEletricTicketName setHidden:YES];
            
            [_btnCancelOrder setHidden:YES];
            // change operate button image
            [_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_pay_ludian.png"] forState:UIControlStateNormal];
            
            break;
        }
        case ORDER_STATE_PAYED:
        {
            [_vwCancelOrder setHidden:YES];
            
            [_lblOrderStateWait setHidden:YES];
            [_lblOrderStateNormal setHidden:NO];
            
            [_lblCarNum setHidden:YES];
            [_lblCarNumName setHidden:YES];
            [_lblElectricTicket setHidden:YES];
            [_lblEletricTicketName setHidden:YES];
            
            [_btnCancelOrder setHidden:YES];
            
            // change operate button image
            [_btnOperate1 setBackgroundImage:[UIImage imageNamed:@"btn_order_finish.png"] forState:UIControlStateDisabled];
            [_btnOperate1 setEnabled:NO];
            
            [_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_eval_driver.png"] forState:UIControlStateNormal];
            
            break;
        }
        case ORDER_STATE_EVALUATED:
        {
            [_vwCancelOrder setHidden:YES];
            
            [_lblOrderStateWait setHidden:YES];
            [_lblOrderStateNormal setHidden:NO];
            
            [_lblCarNum setHidden:YES];
            [_lblCarNumName setHidden:YES];
            [_lblElectricTicket setHidden:YES];
            [_lblEletricTicketName setHidden:YES];
            
            [_btnCancelOrder setHidden:YES];
            
            // change operate button image
            [_btnOperate1 setBackgroundImage:[UIImage imageNamed:@"btn_order_finish.png"] forState:UIControlStateDisabled];
            [_btnOperate1 setEnabled:NO];
            
            if (mOrderInfo.evaluated ==1) {
                [_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_good.png"] forState:UIControlStateNormal];
            }else if (mOrderInfo.evaluated ==2)
            {
                [_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_medium.png"] forState:UIControlStateNormal];
            }else
                [_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_comment_low.png"] forState:UIControlStateNormal];
            break;
        }
        case ORDER_STATE_CLOSED:
        {
            [_vwCancelOrder setHidden:YES];
            
            [_lblOrderStateWait setHidden:YES];
            [_lblOrderStateNormal setHidden:NO];
            
            [_lblCarNum setHidden:YES];
            [_lblCarNumName setHidden:YES];
            [_lblElectricTicket setHidden:YES];
            [_lblEletricTicketName setHidden:YES];
            [_btnCancelOrder setHidden:YES];
            
            // change operate button image
            [_btnOperate1 setBackgroundImage:[UIImage imageNamed:@"btn_order_exit.png"] forState:UIControlStateDisabled];
            [_btnOperate1 setEnabled:NO];

			[_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_eval_driver.png"] forState:UIControlStateNormal];
			[_btnOperate2 setEnabled:YES];

            break;
        }
        case ORDER_STATE_Ticket_REFUND:
        {
            [_vwCancelOrder setHidden:NO];
            
            [_lblOrderStateWait setHidden:YES];
            [_lblOrderStateNormal setHidden:NO];
            
            [_lblCarNum setHidden:YES];
            [_lblCarNumName setHidden:YES];
            [_lblElectricTicket setHidden:YES];
            [_lblEletricTicketName setHidden:YES];
            [_btnCancelOrder setHidden:YES];
            
            // change operate button image
            [_btnOperate1 setHidden:YES];
            [_btnOperate2 setHidden:YES];
            
//            [_btnOperate1 setBackgroundImage:[UIImage imageNamed:@"btn_order_exit.png"] forState:UIControlStateDisabled];
//            [_btnOperate1 setEnabled:NO];
//            
//            [_btnOperate2 setBackgroundImage:[UIImage imageNamed:@"btn_order_eval_driver.png"] forState:UIControlStateNormal];
        }
            break;
        default:
            break;
    }
    
    [self refreshView];
}

-(void)refreshView
{
    //车主信息
    [_imgUser setImageWithURL:[NSURL URLWithString:mDetailedInfo.driver_info.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
    if(mOrderInfo.sex == 0) {
        [_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
    }
    else {
        [_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
        _lblAge.textColor = MYCOLOR_GREEN;
    }
    [_lblAge setText:[NSString stringWithFormat:@"%d", mDetailedInfo.driver_info.age]];
    [_lblName setText:mDetailedInfo.driver_info.name];
    [_lblEvalPro setText:mDetailedInfo.driver_info.goodeval_rate_desc];
    [_lblServeCnt setText:mDetailedInfo.driver_info.carpool_count_desc];
    [_lblCareer setText:[NSString stringWithFormat:@"驾龄 %d年",mDetailedInfo.driver_info.drv_career]];
    //车辆信息
    [_imgCar setImageWithURL:[NSURL URLWithString:mDetailedInfo.driver_info.carimg] placeholderImage:[UIImage imageNamed:@"demo_carimg.png"]];
    NSString * car_typeImageName = [NSString stringWithFormat:@"cartype%d_active.png",mDetailedInfo.driver_info.car_type];
    [_imgCarType setImage:[UIImage imageNamed:car_typeImageName]];
    _lblCarBrand.text = mDetailedInfo.driver_info.car_brand;
    _lblCarSubType.text = mDetailedInfo.driver_info.car_style;
    _lblCarColor.text = mDetailedInfo.driver_info.color;
    
    //订单信息
    [_lblOrderNum setText:mDetailedInfo.order_num];
    _lblOrderNum.adjustsFontSizeToFitWidth = YES;
    [_lblOrderStateWait setText:mDetailedInfo.state_desc];
    
    [_lblStartPos setText:[NSString stringWithFormat:@"%@ -- %@",mDetailedInfo.start_addr,mDetailedInfo.end_addr]];
    if ((mDetailedInfo.start_addr.length + mDetailedInfo.end_addr.length)*16 >300) {
        _lblStartPos.frame = CGRectMake(_lblStartPos.frame.origin.x,30, _lblStartPos.frame.size.width, _lblStartPos.frame.size.height+20);
        _lblStartPos.numberOfLines  = 0;
        [_lblStartPos sizeToFit];
    }else
        _lblStartPos.frame = CGRectMake(_lblStartPos.frame.origin.x,40, _lblStartPos.frame.size.width, 20);
    _lblStartPos.textAlignment = NSTextAlignmentCenter;
//    [_lblStartPos setText:mDetailedInfo.start_addr];
//    [_lblEndPos setText:mDetailedInfo.end_addr];
    [_lblOrderPubTime setText:mDetailedInfo.start_time];
    [_lblOrderAcceptTime setText:mDetailedInfo.accept_time];
    [_lblCarNum setText:mDetailedInfo.driver_info.carno];//车牌号
    NSString *carNumStr = [NSString stringWithFormat:@"%@",mDetailedInfo.driver_info.carno];
    if (carNumStr.length == 4) {
        NSString *newCarNumStr = [NSString stringWithFormat:@"***%@",[carNumStr substringToIndex:1]];
        [_lblCarNum setText:newCarNumStr];//车牌号
    }
    if (carNumStr.length > 4) {
        NSString *newCarNumStr = [NSString stringWithFormat:@"%@***%@",[carNumStr substringToIndex:1],[carNumStr substringFromIndex:4]];
        [_lblCarNum setText:newCarNumStr];//车牌号
    }
    [_lblElectricTicket setText:mDetailedInfo.password];
    [_lblOrderStateNormal setText:mDetailedInfo.state_desc];
//    [_lblCancelOrder setText:mDetailedInfo.cancelled_balance];
    [_lblCancelOrder setText:mDetailedInfo.cancelled_balance_desc];
}


///////////////////////////////////////////////////////////////////////////
#pragma mark - Evaluate Delegate Methods
- (void) submitComment:(NSString *)message level:(int)level
{
    // call submit comment service
    [self callEvaluateLongOrderDriver:level msg:message];
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

// main control event
/**
 * Back to parent view controller
 */
- (IBAction)OnBackClick:(id)sender {
    BACK_VIEW;
}
#pragma mark 拨打电话
- (IBAction)onClickedOperate1:(id)sender
{
    // init with phone number
    NSString * sPhoneNum = mDetailedInfo.driver_info.phone;
    // call this phone number
    NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",sPhoneNum]];
    UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
    [self.view addSubview:phoneCallWebView];

}
#pragma mark  未完成
- (IBAction)onClickedOperate2:(id)sender
{
    // check order state
    switch (mOrderInfo.state) {
        case ORDER_STATE_DRIVER_ACCEPTED:
        case ORDER_STATE_PUBLISHED:
        case ORDER_STATE_GRABBED:
        {
            // show position
            [self showPosition];
            break;
        }
        case ORDER_STATE_STARTED:
        case ORDER_STATE_DRIVER_ARRIVED:
        case ORDER_STATE_PASSENGER_GETON:
        {
            
            break;
        }
        case ORDER_STATE_FINISHED:
        {
            // pay order
            Cus_PayOrderViewController * viewController = (Cus_PayOrderViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"pay_order"];
            viewController.mOrderInfo = mOrderInfo;
            
            SHOW_VIEW(viewController);
            
            break;
        }
        case ORDER_STATE_PAYED:
        {
            Cus_EvalDriverViewController *popup = [[Cus_EvalDriverViewController alloc] initWithNibName:@"Cus_EvalDriverViewController" bundle:nil];
            
            popup.delegate = self;
            [popup setParent:self];
            
            [self presentPopupViewController:popup animated:YES completion:^(void) {
                MyLog(@"popup view presented");
            }];
            break;
        }
        case ORDER_STATE_EVALUATED:
        {
//            GetDriverLatestEvalInfo
            
			TEST_NETWORK_RETURN;
            [SVProgressHUD show];
            [[CommManager getCommMgr] orderSvcMgr].delegate = self;
            [[[CommManager getCommMgr] orderSvcMgr] GetDriverLatestEvalInfo:[Common getUserID] DriverId:mDetailedInfo.driver_info.uid LimitId:mDetailedInfo.uid DevToken:[Common getDeviceMacAddress]];
            break;
        }
        case ORDER_STATE_CLOSED:
        {
            Cus_EvalDriverViewController *popup = [[Cus_EvalDriverViewController alloc] initWithNibName:@"Cus_EvalDriverViewController" bundle:nil];
            
            popup.delegate = self;
            [popup setParent:self];
            
            [self presentPopupViewController:popup animated:YES completion:^(void) {
                MyLog(@"popup view presented");
            }];
            break;
        }
            
        default:
            break;
    }
}
//投诉电话
- (IBAction)complaintsClick:(id)sender {
    
    NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",COMPLAIN_TEL_NUM]];
    UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
    [self.view addSubview:phoneCallWebView];

//    NSString * urlString = [NSString stringWithFormat:@"telprompt://%@",COMPLAIN_TEL_NUM];
//    [[UIApplication sharedApplication]openURL:[NSURL URLWithString:urlString]];
}
#pragma 确定退票按钮
- (IBAction)onClickedCancel:(id)sender {
    
	Cus_LongOrderCancelViewController *cancel = (Cus_LongOrderCancelViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"LongOrderCancel"];
    cancel.orderDetailedInfo = mDetailedInfo;
    [self.navigationController pushViewController:cancel animated:YES];

//    Cus_LongOrderCancelViewController *cancel =[[Cus_LongOrderCancelViewController alloc]initWithNibName:@"langOrderCancelView" bundle:nil];
//    cancel.orderDetailedInfo = mDetailedInfo;
//    [self.navigationController pushViewController:cancel animated:YES];
}

- (IBAction)onClickedInsuranceDetail:(id)sender {
    
    
    
    InsuranceDetailModal *insuranceDetail =[[InsuranceDetailModal alloc]initWithNibName:@"InsuranceDetailModal" bundle:nil];
    STDetailedDrvOrderInfo *detailDrvInfo =[[STDetailedDrvOrderInfo alloc]init];
    detailDrvInfo.appl_no =mDetailedInfo.appl_no;
    detailDrvInfo.effect_time =mDetailedInfo.effect_time;
    detailDrvInfo.insexpr_date =mDetailedInfo.insexpr_date;
    detailDrvInfo.total_amount =mDetailedInfo.total_amount;
    detailDrvInfo.isd_name =mDetailedInfo.isd_name;
    
    insuranceDetail.mOrderInfo =detailDrvInfo;
        insuranceDetail.delegate =self;
    [self presentPopupViewController:insuranceDetail animated:YES completion:nil];
    
}
- (void)tappedDone
{
    [self dismissPopupViewControllerAnimated:YES completion:nil];
}

-(void)showPosition
{
    //FindDriverViewController * findDriverView = (FindDriverViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"FindDriver"];
    FindDriverViewController *findDriverView =[[FindDriverViewController alloc]initWithNibName:@"FindDriverView" bundle:nil];
    findDriverView.driverId = mDetailedInfo.driver_info.uid;
    SHOW_VIEW(findDriverView);
    
    
    
}





#pragma OrderSvcMgr Delegate Methods
- (void) getDetailedCustomerOrderInfoResult:(NSString *)result OrderInfo:(STDetailedCusOrderInfo *)order_info
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        
        
        mDetailedInfo = order_info;
        [self updateUI];
        
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}


/**
 * call evaluate long order pass service
 */
- (void) callEvaluateLongOrderDriver:(enum EVAL_STATE)level msg:(NSString *)msg
{
	TEST_NETWORK_RETURN;
    [SVProgressHUD show];
    
    [[CommManager getCommMgr] longWayOrderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] longWayOrderSvcMgr] EvaluateLongOrderDriver:[Common getUserID] orderid:mDetailedInfo.uid level:level msg:msg devToken:[Common getDeviceMacAddress] driverId:mDetailedInfo.driver_info.uid];
}
- (void) evaluateLongOrderPass:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        // get detailed order information
		TEST_NETWORK_RETURN;
        [SVProgressHUD show];
        [[CommManager getCommMgr] orderSvcMgr].delegate = self;
        [[[CommManager getCommMgr] orderSvcMgr] GetDetailedCustomerOrderInfo:[Common getUserID] OrderId:self.mOrderInfo.uid OrderType:self.mOrderInfo.type DevToken:[Common getDeviceMacAddress]];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}
-(void)duplicateUser:(NSString *)result
{
    [SVProgressHUD showErrorWithStatus:result];
}
-(void)evaluateLongOrderDriver:(NSString *)result
{
    [SVProgressHUD showSuccessWithStatus:result];
    [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(fanhiu) userInfo:nil repeats:NO];
}
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
        
    }else
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    
    
}
-(void)fanhiu
{
    [self.navigationController popViewControllerAnimated:YES];
}
@end
