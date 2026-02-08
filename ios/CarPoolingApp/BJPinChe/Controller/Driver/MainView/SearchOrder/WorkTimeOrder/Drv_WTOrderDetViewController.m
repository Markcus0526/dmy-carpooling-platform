//
//  Drv_SearchOrderDetViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/28/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_WTOrderDetViewController.h"
#import "Drv_SuccessOrderViewController.h"
#import "Drv_MainTabViewController.h"

@interface Drv_WTOrderDetViewController ()

@end

@implementation Drv_WTOrderDetViewController

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
    // map position
    _mapView.delegate = self;
 
	TEST_NETWORK_RETURN

	[SVProgressHUD show];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] getAcceptableInCityOrderDetailInfo:[Common getUserID] OrderID:self.mOrderID OrderType:2 DevToken:[Common getDeviceMacAddress]];
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


/**
 * Publish button click event implementation
 */
- (IBAction)onClickedPublish:(id)sender
{
    NSString *strDays = @"";
    for(int i = 100; i < 106; i++)
    {
        UIButton *dayButton = (UIButton *)[self.view viewWithTag:i];
        if([dayButton isSelected])
        {
            if([strDays length] == 0)
            {
                strDays = [NSString stringWithFormat:@"%d", i - 100];
            }
            else
            {
                strDays = [NSString stringWithFormat:@"%@,%d", strDays, i - 100];
            }
        }
    }
    
	TEST_NETWORK_RETURN
    [SVProgressHUD show];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] acceptOnOffOrder:[Common getUserID] OrderID:self.mOrderID Days:strDays DevToken:[Common getDeviceMacAddress]];
}

/**
 * user driver select day event
 */
- (IBAction)onSelectDay:(id)sender
{
    // select selected button
    UIButton * curDay = (UIButton *)sender;
    
    // check select state
    if ([curDay isSelected]) {
        [curDay setSelected:NO];
    } else {
        [curDay setSelected:YES];
    }
}

// st alert view background
- (IBAction)onClickedAlertBG:(id)sender
{
    // show main tab view controller
    Drv_MainTabViewController *viewController = (Drv_MainTabViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];
    
    SHOW_VIEW(viewController);
}


///////////////////////////////////////////////////////////////////////////
#pragma mark - OrderSvcMgr Delegate Methods
- (void)getAcceptableInCityOrderDetailInfoResult:(NSString *)result OrderInfo:(STDetailedDrvInCityOrderInfo *)order_info
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        STPassengerInfo *passenger = order_info.pass_info;
        
        [_imgUser setImageWithURL:[NSURL URLWithString:passenger.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
        
        // set sex image & change age color according to sex
        if(passenger.sex == SEX_MALE) {
            [_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
            _lblAge.textColor = MYCOLOR_GREEN;
        }
        else {
            [_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
            _lblAge.textColor = [UIColor orangeColor];
        }
        
        [_lblAge setText:[NSString stringWithFormat:@"%d", passenger.age]];
        [_lblName setText:[NSString stringWithFormat:@"%@", passenger.name]];
        
        // check passenger verif state
        if (passenger.pverified == STATE_VERIFIED) {
            [_markVerif setEnabled:YES];
        } else {
            [_markVerif setEnabled:NO];
        }
        [_lblVerifDesc setText:passenger.pverified_desc];
        [_lblEvalPro setText:passenger.goodeval_rate_desc];
        [_lblCarpoolCnt setText:passenger.carpool_count_desc];
        
        [_lblStartPos setText:order_info.start_addr];
        [_lblEndPos setText:order_info.end_addr];
        [_lblPrice setText:order_info.price_desc];
        [_lblSysInfoPrice setText:order_info.sysinfo_fee_desc];
        
        NSString *strMidPoints = @"";
        for(STMidPoint *mid_point in order_info.mid_points)
        {
            if([strMidPoints length] > 0)
            {
                strMidPoints = [NSString stringWithFormat:@", %@", mid_point.address];
            }
            else
            {
                strMidPoints = mid_point.address;
            }
        }
        [_lblMidPoint setText:strMidPoints];
        
        
        // show selected days
        NSArray * selDays = [order_info.valid_days componentsSeparatedByString:@","];
        if ([selDays count] > 0) {
            for (int i = 0; i < selDays.count; i++) {
                int dayNum = [selDays[i] intValue];
                UIButton * button = (UIButton *)[self.view viewWithTag:(100 + dayNum)];
                [button setEnabled:YES];
            }
        }
        
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void)acceptOnOffOrderResult:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        // show alert view
        [_vwAlertView setHidden:NO];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

@end






