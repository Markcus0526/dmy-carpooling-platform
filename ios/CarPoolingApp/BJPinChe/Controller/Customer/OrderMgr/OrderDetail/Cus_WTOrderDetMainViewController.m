//
//  Cus_WTOrderDetMainViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/2/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Cus_WTOrderDetMainViewController.h"
#import "UIViewController+CWPopup.h"
#import "Cus_OrderDrvPosViewController.h"

@interface Cus_WTOrderDetMainViewController ()

@end

@implementation Cus_WTOrderDetMainViewController

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


///////////////////////////////////////////////////////////////////////////
#pragma mark - Basic Function

/**
 * Initilaize all ui controls & member variable
 */
- (void) initControls
{
    // set normal
    [self setInfoForWaitState];
    
    // get detail order info
    [self callGetDetailOrder];
}

- (void) updateUI
{
    // change view by state
    switch (mDetOrderInfo.state) {
        case ORDER_STATE_DRIVER_ACCEPTED:
        case ORDER_STATE_PUBLISHED:
        case ORDER_STATE_GRABBED:
        {
            [self setInfoForWaitState];
            break;
        }
        case ORDER_STATE_STARTED:
        case ORDER_STATE_DRIVER_ARRIVED:
        case ORDER_STATE_PASSENGER_GETON:
        case ORDER_STATE_FINISHED:
        case ORDER_STATE_PAYED:
        case ORDER_STATE_EVALUATED:
        {
            [self setInfoForStartState];
            break;
        }
        case ORDER_STATE_CLOSED:
        {
            [self setInfoForStopState];
            break;
        }
            
        default:
            break;
    }
    
    // set detail information
    [_imgUser setImageWithURL:[NSURL URLWithString:mDetOrderInfo.driver_info.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
    if(mDetOrderInfo.driver_info.sex == SEX_MALE) {
        [_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
        _lblAge.textColor = MYCOLOR_GREEN;
    }
    else {
        [_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
        _lblAge.textColor = [UIColor orangeColor];
    }
    [_imgCar setImageWithURL:[NSURL URLWithString:mDetOrderInfo.driver_info.carimg] placeholderImage:[UIImage imageNamed:@"demo_carimg.png"]];
    
    [_lblAge setText:[NSString stringWithFormat:@"%d", mDetOrderInfo.driver_info.age]];
    [_lblName setText:mDetOrderInfo.driver_info.name];
    [_lblEvalPro setText:mDetOrderInfo.driver_info.goodeval_rate_desc];
    [_lblServeCnt setText:mDetOrderInfo.driver_info.carpool_count_desc];
    [_lblStartPos setText:mDetOrderInfo.start_addr];
    [_lblEndPos setText:mDetOrderInfo.end_addr];
    
    [_lblOrderPubTime setText:mDetOrderInfo.start_time];
    [_lblCarNum setText:mDetOrderInfo.driver_info.carno];
    
    NSString *strMidPoints = @"";
    for(STMidPoint *midPoint in mDetOrderInfo.mid_points)
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
    [_lblMidPoints setText:strMidPoints];
    
    
    // show valid days
    NSArray * selDays = [mDetOrderInfo.valid_days componentsSeparatedByString:@","];
    if ([selDays count] > 0) {
        for (int i = 0; i < selDays.count; i++) {
            int dayNum = [selDays[i] intValue];
            UIButton * button = (UIButton *)[self.view viewWithTag:(100 + dayNum)];
            [button setEnabled:YES];
            [button setSelected:YES];
        }
    }
    // show selected days
    selDays = [mDetOrderInfo.left_days componentsSeparatedByString:@","];
    if ([selDays count] > 0) {
        for (int i = 0; i < selDays.count; i++) {
            int dayNum = [selDays[i] intValue];
            UIButton * button = (UIButton *)[self.view viewWithTag:(100 + dayNum)];
            [button setEnabled:YES];
        }
    }
    
    
}


///////////////////////////////////////////////////////////////////////////
#pragma mark - Setting Control by order state (show & hide controls)

- (void) setInfoForWaitState
{
    // show & hide controls
    [_lblOrderWait setHidden:NO];
    [_btnStopOneDay setHidden:NO];
    [_btnStopServeSmall setHidden:NO];
    
    [_vwOrderState setHidden:YES];
    [_lblOrderState setHidden:YES];
    [_btnStopServeWide setHidden:YES];
    
    [_vwStopServeInfo setHidden:YES];
}

- (void) setInfoForStartState
{
    // show & hide controls
    [_lblOrderWait setHidden:YES];
    [_btnStopOneDay setHidden:YES];
    [_btnStopServeSmall setHidden:YES];
    
    [_vwOrderState setHidden:NO];
    [_lblOrderState setHidden:NO];
    [_btnStopServeWide setHidden:NO];
    
    [_vwStopServeInfo setHidden:YES];
}

- (void) setInfoForStopState
{
    // show & hide controls
    [_lblOrderWait setHidden:YES];
    [_btnStopOneDay setHidden:YES];
    [_btnStopServeSmall setHidden:YES];
    
    [_vwOrderState setHidden:NO];
    [_lblOrderState setHidden:NO];
    [_btnStopServeWide setHidden:NO];
    
    [_vwStopServeInfo setHidden:NO];
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

// main control event
/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    BACK_VIEW;
}

/**
 * refresh button click event implementation
 */
- (IBAction)onClickedRefresh:(id)sender
{
    [self callGetDetailOrder];
}

// circle operate button
- (IBAction)onClickedBtnOperate1:(id)sender
{
    // change view by state
    switch (mDetOrderInfo.state) {
        case ORDER_STATE_DRIVER_ACCEPTED:
        case ORDER_STATE_PUBLISHED:
        case ORDER_STATE_GRABBED:
        case ORDER_STATE_STARTED:
        case ORDER_STATE_DRIVER_ARRIVED:
        case ORDER_STATE_PASSENGER_GETON:
        case ORDER_STATE_FINISHED:
        {
            // call driver
            NSString * sPhoneNum = mDetOrderInfo.driver_info.phone;
            // call this phone number
           // [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@", sPhoneNum]]];
            NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",sPhoneNum]];
            UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
            [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
            [self.view addSubview:phoneCallWebView];
            break;
        }
        case ORDER_STATE_PAYED:
        {
            // show evaluate dialog
            Cus_EvalDriverViewController *popup = [[Cus_EvalDriverViewController alloc] initWithNibName:@"Cus_EvalDriverViewController" bundle:nil];
            
            popup.delegate = self;
            [popup setParent:self];
            
            [self presentPopupViewController:popup animated:YES completion:^(void) {
                NSLog(@"popup view presented");
            }];
            
            break;
        }
        case ORDER_STATE_EVALUATED:
        {
            // show evalute result
            Cus_EvalDriverViewController *popup = [[Cus_EvalDriverViewController alloc] initWithNibName:@"Cus_EvalDriverViewController" bundle:nil];
            
            popup.delegate = self;
            [popup setParent:self];
            
            [self presentPopupViewController:popup animated:YES completion:^(void) {
                NSLog(@"popup view presented");
            }];
            
            break;
        }
        case ORDER_STATE_CLOSED:
        {
            break;
        }
            
        default:
            break;
    }
}

- (IBAction)onClickedBtnOperate2:(id)sender
{
    // change view by state
    switch (mDetOrderInfo.state) {
        case ORDER_STATE_DRIVER_ACCEPTED:
        case ORDER_STATE_PUBLISHED:
        case ORDER_STATE_GRABBED:
        {
            // show driver position
            Cus_OrderDrvPosViewController * vcAccept = (Cus_OrderDrvPosViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"order_driver_pos"];
            
            vcAccept.mOrderInfo = mOrderInfo;
            SHOW_VIEW(vcAccept);
            
            break;
        }
        case ORDER_STATE_STARTED:
        case ORDER_STATE_DRIVER_ARRIVED:
        case ORDER_STATE_PASSENGER_GETON:
        case ORDER_STATE_FINISHED:
        {
            // pay order
            
            break;
        }
        case ORDER_STATE_PAYED:
        case ORDER_STATE_EVALUATED:
        {
            // go to reserve
            
            break;
        }
        case ORDER_STATE_CLOSED:
        {
            break;
        }
            
        default:
            break;
    }
}


- (IBAction)onClickedStopOneDay:(id)sender
{
    [self callPauseOrder];
}

- (IBAction)onClickedCancelServe:(id)sender
{
    [self callCancelOrder];
}

- (IBAction)onClickedComplain:(id)sender
{
    // init with phone number
    NSString * sPhoneNum = COMPLAIN_TEL_NUM;
    // call this phone number
   // [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@", sPhoneNum]]];
    
    NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",sPhoneNum]];
    UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
    [self.view addSubview:phoneCallWebView];
}


#pragma mark - Evaluate Dialog Delegate
- (void) submitComment:(NSString *)message level:(int)level
{
    // call evaluate driver service
    [self callEvaluateDriver:message level:level];
}

////////////////////////////////////////////////////////////////////////////
#pragma mark - OrderSvcMgr Service Methods

- (void) callGetDetailOrder
{
	TEST_NETWORK_RETURN
	
    [SVProgressHUD show];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetDetailedCustomerOrderInfo:[Common getUserID] OrderId:self.mOrderInfo.uid OrderType:self.mOrderInfo.type DevToken:[Common getDeviceMacAddress]];
}

- (void) callCancelOrder
{
	TEST_NETWORK_RETURN
	
    [SVProgressHUD show];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] CancelOnOffOrder:[Common getUserID] OrderId:mOrderInfo.uid DevToken:[Common getDeviceMacAddress]];
}

- (void) callPauseOrder
{
	TEST_NETWORK_RETURN
	
    [SVProgressHUD show];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] PauseOnOffOrder:[Common getUserID] OrderId:mOrderInfo.uid DevToken:[Common getDeviceMacAddress]];
}

- (void) callEvaluateDriver : (NSString *)message level:(int)level
{
	TEST_NETWORK_RETURN
	
    [SVProgressHUD show];
    [[CommManager getCommMgr] orderExecuteSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderExecuteSvcMgr] EvaluateOnOffOrderDriver:[Common getUserID] DriverId:mOrderInfo.driver_id OrderId:mOrderInfo.uid Level:level Msg:message DevToken:[Common getDeviceMacAddress]];
}

#pragma OrderSvcMgr Delegate Methods
- (void) getDetailedCustomerOrderInfoResult:(NSString *)result OrderInfo:(STDetailedCusOrderInfo *)order_info
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        mDetOrderInfo = order_info;
        // update ui using detailed info
        [self updateUI];
        
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void) pauseOnOffOrderResult:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismissWithSuccess:MSG_SUCCESS afterDelay:DEF_DELAY];
        // back to parent
        [self performSelector:@selector(onClickedBack:) withObject:nil afterDelay:DEF_DELAY];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void) cancelOnOffOrderResult:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismissWithSuccess:MSG_SUCCESS afterDelay:DEF_DELAY];
        // back to parent
        [self performSelector:@selector(onClickedBack:) withObject:nil afterDelay:DEF_DELAY];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

@end
