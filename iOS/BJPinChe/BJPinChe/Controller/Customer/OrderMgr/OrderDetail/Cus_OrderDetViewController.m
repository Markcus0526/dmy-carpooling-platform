//
//  Cus_OrderDetViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/1/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//  单次拼车  乘客 详情界面

// 未完成

#import "Cus_OrderDetViewController.h"
#import "InsuranceDetailModal.h"
#import "UIViewController+CWPopup.h"
@interface Cus_OrderDetViewController ()<insuranceDetailDelegate>
@property(nonatomic,strong)STDetailedCusOrderInfo *mDetailOrderInfo;

@end

@implementation Cus_OrderDetViewController

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
    /******************** set main order info ******************/
    // set user image
    //_imgUser
    
    // set sex image & change age color according to sex
    //_imgSex
    //if ([mOrderInfo.sex isEqualToString:SEX_MALE]) {
    if (mOrderInfo.sex == SEX_MALE) {
        _lblAge.textColor = MYCOLOR_GREEN;
    }
    [_lblAge setText:[NSString stringWithFormat:@"%d", mOrderInfo.age]];
    
    [_lblName setText:mOrderInfo.name];
    
    
    /************** show order detail view by order state ***************/
    if(self.mOrderInfo.state <= 2)
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
#pragma mark - Setting Control by order state

- (void) showWaitOrderDetail
{
    // show & hide detain view
    [_vwOrderInfo setHidden:NO];
    [_vwOrderInfo1 setHidden:YES];
    
    // set control value
    [_lblStartPos setText:mOrderInfo.startPos];
    [_lblEndPos setText:mOrderInfo.endPos];
    
    
}

- (void) showAfterOPOrderDetail
{
    // show & hide detain view
    [_vwOrderInfo setHidden:YES];
    [_vwOrderInfo1 setHidden:NO];
    
    // set control value
    [_lblStartPos1 setText:mOrderInfo.startPos];
    [_lblEndPos1 setText:mOrderInfo.endPos];
    
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
    BACK_VIEW;
}

/**
 * refresh button click event implementation
 */
- (IBAction)onClickedRefresh:(id)sender
{
    [SVProgressHUD show];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetDetailedCustomerOrderInfo:[Common getUserID] OrderId:self.mOrderInfo.uid OrderType:self.mOrderInfo.type DevToken:[Common getDeviceMacAddress]];
}

//////////////////////////////// wait operate view
/**
 * call driver button clicked event
 */
- (IBAction)onClickedCallDriver:(id)sender
{
    
}

/**
 * search driver position button clicked event
 */
- (IBAction)onClickedDriverPos:(id)sender
{
    
}

/**
 * cancel order button clicked event
 */
- (IBAction)onClickedCancelOrder:(id)sender
{
	TEST_NETWORK_RETURN;

	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] CancelOnceOrder:[Common getUserID]  OrderId:self.mOrderInfo.order_num DevToken:[Common getDeviceMacAddress]];
}

/**
 * complain button clicked event阿哥萨博不是吧 
 */
- (IBAction)onClickedComplain:(id)sender
{
    
}

///////////////////////////////// after operate view
/**
 * complain button clicked event (started operate state)
 */
- (IBAction)onClickedComplainAfter:(id)sender
{
    
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

- (IBAction)onClickedInsuranceDetail:(id)sender {
    
    
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
    
}

/**
 * Second button clicked event (started operate state)
 * pay or evalution button
 */
- (IBAction)onClickedPayOrEval:(id)sender
{
    
}


#pragma OrderSvcMgr Delegate Methods
- (void) getDetailedCustomerOrderInfoResult:(NSString *)result OrderInfo:(STDetailedCusOrderInfo *)order_info
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {

        [_imgUser setImageWithURL:[NSURL URLWithString:order_info.driver_info.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
        if(mOrderInfo.sex == 0) {
            [_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
        }
        else {
            [_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
            _lblAge.textColor = MYCOLOR_GREEN;
        }
        [_imgCar setImageWithURL:[NSURL URLWithString:order_info.driver_info.carimg] placeholderImage:[UIImage imageNamed:@"demo_carimg.png"]];
        
        [_lblAge setText:[NSString stringWithFormat:@"%d", order_info.driver_info.age]];
        [_lblName setText:order_info.driver_info.name];
        [_lblEvalPro setText:order_info.driver_info.goodeval_rate_desc];
        [_lblServeCnt setText:order_info.driver_info.carpool_count_desc];
        
        if(order_info.state <= 2)
        {
            [_lblOrderNum setText:order_info.order_num];
            [_lblOrderState setText:order_info.state_desc];
            [_lblStartPos setText:order_info.start_addr];
            [_lblEndPos setText:order_info.end_addr];
            [_lblOrderPubTime setText:order_info.create_time];
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
            [_lblMidPoints setText:strMidPoints];
            [_lblCarNum setText:order_info.driver_info.carno];
        }
        else
        {
            [_lblOrderNum1 setText:order_info.order_num];
            [_lblOrderState1 setText:order_info.state_desc];
            [_lblStartPos1 setText:order_info.start_addr];
            [_lblEndPos1 setText:order_info.end_addr];
            [_lblOrderPubTime1 setText:order_info.create_time];
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
            [_lblMidPoints1 setText:strMidPoints];
        }
     
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

@end
