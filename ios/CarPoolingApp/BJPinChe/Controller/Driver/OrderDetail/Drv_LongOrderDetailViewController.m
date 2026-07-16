//
//  Drv_LongOrderDetailViewController.m
//  BJPinChe
//
//  Created by CKK on 14-9-2.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "Drv_LongOrderDetailViewController.h"
#import "UIViewController+CWPopup.h"
#import "Drv_LongOrderDetPassCell.h"
#import "Drv_EvalCustomerViewController.h"
#import "Drv_longOrderPreformViewController.h"
#import "InsuranceDetailModal.h"
#import "Cus_EvalDriverLatestViewController.h"
#import "Drv_PassengerInfoViewController.h"

@interface Drv_LongOrderDetailViewController ()<insuranceDetailDelegate>

@end


#define CELL_HEIGHT                                 96


@implementation Drv_LongOrderDetailViewController

@synthesize mOrderInfo;

@synthesize _lblReserveTime;
@synthesize _lblStartTime;
@synthesize _lblSysDesc;
@synthesize _vwWaitState;
@synthesize _vwExtraState;
@synthesize _vwPerformState;
@synthesize _lblPrice;
@synthesize _lblSeatCnt;
@synthesize _lblState;

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
    [_vwWaitState setHidden:YES];
    [_vwPerformState setHidden:YES];
    [_vwExtraState setHidden:YES];
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

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self callGetDetailedDriverOrderInfo];
}


///////////////////////////////// Initialize ////////////////////////////////
#pragma mark - Initialize data info
- (void) initControls
{
    // get order detail info
    
    // make tap recognizer to hide keyboard && popup
    UITapGestureRecognizer * tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapBackGround:)];
    
    [self.view addGestureRecognizer:tapRecognizer];
    
    UIButton *rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [rightBtn setBackgroundImage:[UIImage imageNamed:@"btn_refresh.png"] forState:UIControlStateNormal];
    [rightBtn setFrame:CGRectMake(0, 0, 30, 30)];
    [rightBtn addTarget:self action:@selector(upDateOrder) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *backBar = [[UIBarButtonItem alloc]initWithCustomView:rightBtn];
    self.navigationItem.rightBarButtonItem = backBar;
    
    _tblPassenger.separatorStyle = NO;
}
-(void)upDateOrder{
    [self callGetDetailedDriverOrderInfo];
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

- (void)refreshOrderInfo
{
    // get order detail info
    [self callGetDetailedDriverOrderInfo];
    
}

- (void)onClickedBtnPassOperate:(STPassengerInfo *)passInfo
{
    // check order state
//    switch (mOrderInfo.state) {
//        case ORDER_STATE_DRIVER_ACCEPTED:
//        case ORDER_STATE_PUBLISHED:
//        case ORDER_STATE_GRABBED:
//        case ORDER_STATE_STARTED:
//        case ORDER_STATE_DRIVER_ARRIVED:
//        case ORDER_STATE_PASSENGER_GETON:
//        {
//            // call customer
//            // init with phone number
//            NSString * sPhoneNum = passInfo.phone;
//            // call this phone number
//            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@", sPhoneNum]]];
//            
//            break;
//        }
//        case ORDER_STATE_FINISHED:
//        case ORDER_STATE_PAYED:
//        case ORDER_STATE_EVALUATED:
//        {
//            // set evaluate image by state
//            if (passInfo.evaluated == EVAL_NOT) {
//                // show evaluation dialog
//                Drv_EvalCustomerViewController *popup = [[Drv_EvalCustomerViewController alloc] initWithNibName:@"Drv_EvalCustomerViewController" bundle:nil];
//                
//                popup.delegate = self;
//                [popup initData:self dataInfo:passInfo];
//                
//                [self presentPopupViewController:popup animated:YES completion:^(void) {
//                    NSLog(@"popup view presented");
//                }];
//            } else {
//                // show eval history data
//                [[[CommManager getCommMgr] orderSvcMgr] GetPassengerLatestEvalInfo:[Common getUserID] PassId:passInfo.uid LimitId:mDetailedOrderInfo.uid DevToken:[Common getDeviceMacAddress]];
//            }
//            
//            break;
//        }
//        case ORDER_STATE_CLOSED:
//        {
//            break;
//        }
//            
//        default:
//            break;
//    }
    
    
    switch (passInfo.state) {
            //打电话
        case PASS_STATE_UPLOAD:
        case PASS_STATE_NOT_CHECKED:
        case PASS_STATE_WEIJIESUAN:
        {
            NSString * sPhoneNum = passInfo.phone;
            // call this phone number
           // [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@", sPhoneNum]]];
            
            NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",sPhoneNum]];
            UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
            [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
            [self.view addSubview:phoneCallWebView];
            
        }
            break;
            //评价
        case PASS_STATE_PAYED:
        case PASS_STATE_GIVEUP:
        {
            Drv_EvalCustomerViewController *popup = [[Drv_EvalCustomerViewController alloc] initWithNibName:@"Drv_EvalCustomerViewController" bundle:nil];
            
            popup.delegate = self;
            [popup initData:self dataInfo:passInfo];
            
            [self presentPopupViewController:popup animated:YES completion:^(void) {
                NSLog(@"popup view presented");
            }];
        }
            break;
            //已评价
        case PASS_STATE_WEIPINGJIA:
        {
            [[[CommManager getCommMgr] orderSvcMgr] GetPassengerLatestEvalInfo:[Common getUserID] PassId:passInfo.uid LimitId:mDetailedOrderInfo.uid DevToken:[Common getDeviceMacAddress]];
        }
            break;
        default:
            break;
    }
    
}

- (void)onClickedBtnPassHeader:(STPassengerInfo *)passInfo
{
    Drv_PassengerInfoViewController *viewController = (Drv_PassengerInfoViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"passenger_info"];
    
    viewController.mPassID = passInfo.uid;
    
    [self presentViewController:viewController animated:YES completion:nil];

}

///////////////////////////////////////////////////////////////////////////////
/**
 * update ui using detailed order info
 * change controls' state
 */
-(NSString *)dateFormatter:(NSString *)dateString
{
    NSString * year = [dateString componentsSeparatedByString:@"-"][0];
    NSString * month = [dateString componentsSeparatedByString:@"-"][1];
    NSString * day = [([dateString componentsSeparatedByString:@"-"][2]) componentsSeparatedByString:@" "][0];
    NSString * hour = [([dateString componentsSeparatedByString:@" "][1]) componentsSeparatedByString:@":"][0];
    NSString * minute = [([dateString componentsSeparatedByString:@" "][1]) componentsSeparatedByString:@":"][1];
    if (year == nil || month == nil || day == nil || hour == nil || minute == nil) {
        return @"";
    }
    return [NSString stringWithFormat:@"%@年%@月%@日 %@:%@",year,month,day,hour,minute];
}

- (void) updateUI
{
    if (mDetailedOrderInfo.start_addr.length >=9 || mDetailedOrderInfo.end_addr.length >8) {
        _startCityLabel.numberOfLines = 0;
        _endCityLabel.numberOfLines = 0;
        [_startCityLabel sizeToFit];
        [_endCityLabel sizeToFit];
        if (_startCityLabel.frame.size.height <30) {
            _startCityLabel.frame = CGRectMake(_startCityLabel.frame.origin.x, _startCityLabel.frame.origin.y, 150, _startCityLabel.frame.size.height+20);
            _endCityLabel.frame = CGRectMake(_endCityLabel.frame.origin.x, _endCityLabel.frame.origin.y, 130, _endCityLabel.frame.size.height+20);
            _lblReserveTime.frame = CGRectMake(_lblReserveTime.frame.origin.x, _lblReserveTime.frame.origin.y+15, _lblReserveTime.frame.size.width, _lblReserveTime.frame.size.height);
            _lblPrice.frame = CGRectMake(_lblPrice.frame.origin.x, _lblPrice.frame.origin.y+13, _lblPrice.frame.size.width, _lblPrice.frame.size.height);
            _lblSeatCnt.frame = CGRectMake(_lblSeatCnt.frame.origin.x, _lblSeatCnt.frame.origin.y+13, _lblSeatCnt.frame.size.width, _lblSeatCnt.frame.size.height);
            _lblState.frame = CGRectMake(_lblState.frame.origin.x, _lblState.frame.origin.y+13, _lblState.frame.size.width, _lblState.frame.size.height);
            _lblSysDesc.frame = CGRectMake(_lblSysDesc.frame.origin.x, _lblSysDesc.frame.origin.y+13, _lblSysDesc.frame.size.width, _lblSysDesc.frame.size.height);
            _label1.frame = CGRectMake(_label1.frame.origin.x, _label1.frame.origin.y+10, _label1.frame.size.width, _label1.frame.size.height);
            _label2.frame = CGRectMake(_label2.frame.origin.x, _label2.frame.origin.y+15, _label2.frame.size.width, _label2.frame.size.height);
        }
    }

	_startCityLabel.textAlignment = NSTextAlignmentRight;
    _endCityLabel.textAlignment = NSTextAlignmentLeft;
    [_startCityLabel setText:mDetailedOrderInfo.start_addr];
    [_endCityLabel setText:mDetailedOrderInfo.end_addr];
    [_orderNumLabel setText:mDetailedOrderInfo.order_num];
    _orderNumLabel.adjustsFontSizeToFitWidth = YES;

	[_lblReserveTime setText:[self dateFormatter:mDetailedOrderInfo.start_time]];

	[_lblPrice setText:[NSString stringWithFormat:@"%.2f点/座", mDetailedOrderInfo.price]];
    [_lblSeatCnt setText:[NSString stringWithFormat:@"共%d座", mDetailedOrderInfo.total_seat]];
    [_lblState setText:mDetailedOrderInfo.state_desc];
    [_lblSysDesc setText:mDetailedOrderInfo.sysinfo_fee_desc];
    
    // reload passenger table view
    [_tblPassenger reloadData];
    [_tblPassenger setHidden:NO];
    
    UILabel *label = (UILabel *)[self.view viewWithTag:100];
    [label setHidden:YES];
    
    // check order state
    switch (mDetailedOrderInfo.state) {
        case ORDER_STATE_DRIVER_ACCEPTED:
        case ORDER_STATE_GRABBED:
        {
            [_vwWaitState setHidden:NO];
            [_vwPerformState setHidden:YES];
            [_vwExtraState setHidden:YES];
            
            break;
        }
        case ORDER_STATE_PUBLISHED:
        {
            [_vwWaitState setHidden:NO];
            [_vwPerformState setHidden:YES];
            [_vwExtraState setHidden:YES];
            if (mDetailedOrderInfo.pass_list.count == 0) {
                UILabel *label = (UILabel *)[self.view viewWithTag:100];
                if (label ==nil) {
                    label = [[UILabel alloc]initWithFrame:CGRectMake(_tblPassenger.frame.origin.x, _tblPassenger.frame.origin.y+20, 320, 20)];
                    label.text = @"暂时还没有乘客抢座，耐心等等吧";
                    label.textAlignment = NSTextAlignmentCenter;
                    [label setBackgroundColor:[UIColor clearColor]];
                    label.font  = [UIFont boldSystemFontOfSize:20];
                    label.tag = 100;
                    [self.view addSubview:label];

                }
                [label setHidden:NO];
                [_tblPassenger setHidden:YES];
            }
            break;
        }
        case ORDER_STATE_STARTED:
        case ORDER_STATE_DRIVER_ARRIVED:
        case ORDER_STATE_PASSENGER_GETON:
        {
            [_vwWaitState setHidden:YES];
            [_vwPerformState setHidden:NO];
            [_vwExtraState setHidden:YES];
            
            break;
        }
        case ORDER_STATE_FINISHED:
        {
            [_vwWaitState setHidden:YES];
            [_vwPerformState setHidden:YES];
            [_vwExtraState setHidden:NO];
        }
        case ORDER_STATE_PAYED:
        case ORDER_STATE_EVALUATED:
        case ORDER_STATE_CLOSED:
        {
            [_vwWaitState setHidden:YES];
            [_vwPerformState setHidden:YES];
            [_vwExtraState setHidden:NO];
            
            break;
        }
            
        default:
            break;
    }
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - Evaluate Delegate Methods
- (void) submitComment:(NSString *)message level:(int)level passInfo:(STPassengerInfo *)passInfo
{
    // call submit comment service
    [self callEvaluateLongOrderPass:passInfo level:level msg:message];
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - OrderSvcMgr Delegate Methods

/**
 * get detailed driver order info ( calling service )
 */
- (void) callGetDetailedDriverOrderInfo
{
	TEST_NETWORK_RETURN
	
    [SVProgressHUD show];
    
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetDetailedDriverOrderInfo:[Common getUserID] OrderId:self.mOrderInfo.uid OrderType:self.mOrderInfo.type DevToken:[Common getDeviceMacAddress]];
}

- (void) getDetailedDriverOrderInfoResult:(NSString *)result OrderInfo:(STDetailedDrvOrderInfo *)order_info
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        mDetailedOrderInfo = order_info;

		// update ui using detail information
        [self updateUI];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}

-(void)getDriverCancelResult:(NSString *)result RetData:(NSDictionary *)RetData
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        [SVProgressHUD showSuccessWithStatus:@"订单已取消" duration:2];
        [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(fuihui) userInfo:nil repeats:NO];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}
-(void)getPassengerLatestEvalInfoResult:(NSString *)result dataList:(NSMutableArray *)dataList
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
-(void)fuihui
{
    [self.navigationController popViewControllerAnimated:YES];
}
/**
 * call evaluate long order pass service
 */
- (void) callEvaluateLongOrderPass : (STPassengerInfo *)passInfo level:(enum EVAL_STATE)level msg:(NSString *)msg
{
	TEST_NETWORK_RETURN
	
    [SVProgressHUD show];
    
    [[CommManager getCommMgr] longWayOrderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] longWayOrderSvcMgr] EvaluateLongOrderPass:[Common getUserID] passid:passInfo.uid orderid:mOrderInfo.uid level:level msg:msg devToken:[Common getDeviceMacAddress]];
}

- (void) evaluateLongOrderPass:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        // get detailed order information 
        [self callGetDetailedDriverOrderInfo];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - TableView Delegate

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (mDetailedOrderInfo == nil || mDetailedOrderInfo.pass_list == nil) {
        return 0;
    }
    
    return mDetailedOrderInfo.pass_list.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	//return height1;
	return CELL_HEIGHT;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSString * cellIdentifier = @"";
    
    if([mDetailedOrderInfo.pass_list count] <= indexPath.row)
        return nil;
    
    STPassengerInfo * dataInfo = [mDetailedOrderInfo.pass_list objectAtIndex:indexPath.row];
    
    switch (dataInfo.state) {
            //打电话
        case PASS_STATE_UPLOAD:
        case PASS_STATE_NOT_CHECKED:
        case PASS_STATE_WEIJIESUAN:
        {
            cellIdentifier = @"long_orderdet_pass_cell_normal";
        }
            break;
            //已关闭
        case PASS_STATE_GIVEUP:
        {//long_orderdet_pass_cell_cancel
            cellIdentifier = @"long_orderdet_pass_cell_cancel";
        }
            break;
        case PASS_STATE_PAYED: //评价
        case PASS_STATE_WEIPINGJIA://已评价
        {
            cellIdentifier = @"long_orderdet_pass_cell_eval";
        }
            break;
        default:
            break;
    }
    
            
    
    Drv_LongOrderDetPassCell * cell = (Drv_LongOrderDetPassCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    [cell initData:dataInfo orderInfo:mOrderInfo parent:self];
    
//    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
	return cell;
}




///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

- (IBAction)OnBackButtonClick:(id)sender {
    //BACK_VIEW;
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onClickedComplain:(id)sender {
//    NSString * urlString = [NSString stringWithFormat:@"telprompt://%@",COMPLAIN_TEL_NUM];
//    [[UIApplication sharedApplication]openURL:[NSURL URLWithString:urlString]];
    
    NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",ComplainNummber]];
    UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
    [self.view addSubview:phoneCallWebView];
}
//取消订单
- (IBAction)onClickedCancel:(id)sender {
    //BACK_VIEW;
    
//    [self.navigationController popViewControllerAnimated:YES];
    NSString *str = @"您确定要取消订单吗？";
    if(mDetailedOrderInfo.pass_list.count ==0)
    {
        str = @"还没有乘客抢座呢，不再等等吗？";
    }
    UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"提示" message:str delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
    alert.delegate = self;
    alert.tag = 10;
    [alert show];
}

- (IBAction)onClickedPerform:(id)sender {
    if ((mDetailedOrderInfo.state  = ORDER_STATE_PUBLISHED) && mDetailedOrderInfo.pass_list.count == 0) {
        [SVProgressHUD showSuccessWithStatus:@"抱歉，还没有乘客拼车" duration:2];
        return;
    }
    for (STPassengerInfo * dataInfo in mDetailedOrderInfo.pass_list) {
        if (dataInfo.state == PASS_STATE_UPLOAD || dataInfo.state == PASS_STATE_NOT_CHECKED) {
            UIAlertView *alert =[[UIAlertView alloc]initWithTitle:@"提示" message:@"长途订单开始执行后无法取消订单，是否确认？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
            alert.tag = 11;
            [alert show];
            return;
        }
        else
        {
            [SVProgressHUD showSuccessWithStatus:@"抱歉，还没有乘客拼车" duration:2];
            return;
        }
    }
}
- (IBAction)onClickedGotoPerform:(id)sender {
    
    Drv_longOrderPreformViewController * longOrderPreformViewController = (Drv_longOrderPreformViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"LongOrderPreform"];
    longOrderPreformViewController.mOrderInfo = self.mOrderInfo;
    SHOW_VIEW(longOrderPreformViewController);
    
}

- (IBAction)onClickedInsuranceDetail:(id)sender
{
    InsuranceDetailModal *insuranceDetail =[[InsuranceDetailModal alloc]initWithNibName:@"InsuranceDetailModal" bundle:nil];
    insuranceDetail.mOrderInfo =mDetailedOrderInfo;
    insuranceDetail.delegate =self;
    [self presentPopupViewController:insuranceDetail animated:YES completion:nil];
    
}
- (void)tappedDone
{
    [self dismissPopupViewControllerAnimated:YES completion:nil];
}
#pragma mark - UIAlertView - delegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == 10) {
        if (buttonIndex == 0) {
            
        }else
        {
		TEST_NETWORK_RETURN
            [SVProgressHUD show];
            
            [[CommManager getCommMgr] orderSvcMgr].delegate = self;
            [[[CommManager getCommMgr] orderSvcMgr] GetDetailedDriverCancelLongOrderInfo:1 DriverId:[Common getUserID] OrderID:self.mOrderInfo.uid DevToken:[Common getDeviceMacAddress]];
        }
    }
    if (alertView.tag == 11) {
        if (buttonIndex ==1) {
            Drv_longOrderPreformViewController * longOrderPreformViewController = (Drv_longOrderPreformViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"LongOrderPreform"];
            longOrderPreformViewController.mOrderInfo = self.mOrderInfo;
            SHOW_VIEW(longOrderPreformViewController);
        }
    }
    
}
@end
