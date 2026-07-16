//
//  Drv_MyWTOrderDetViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/3/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_MyWTOrderDetViewController.h"

@interface Drv_MyWTOrderDetViewController ()

@end

@implementation Drv_MyWTOrderDetViewController

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
    [self setInfoForNormal];
    
	TEST_NETWORK_RETURN
	
    [SVProgressHUD show];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetDetailedDriverOrderInfo:[Common getUserID] OrderId:self.mOrderInfo.uid OrderType:self.mOrderInfo.type DevToken:[Common getDeviceMacAddress]];
}

- (void) setInfoForNormal
{
    [_vwStopServeInfo setHidden:YES];
}

- (void) setInfoForStopServeState
{
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
 * Refresh button clicke event
 */
- (IBAction)onClickedRefresh:(id)sender
{
    
}

/**
 * First operation button click event (first circle button)
 */
- (IBAction)onClickedOPButton1:(id)sender
{
    
}

/**
 * Second operation button click event (second circle button)
 */
- (IBAction)onClickedOPButton2:(id)sender
{
    
}

- (IBAction)onClickedStopServe:(id)sender
{
    
}

- (IBAction)onClickedComplain:(id)sender
{
    // init with phone number
    NSString * sPhoneNum = COMPLAIN_TEL_NUM;
    // call this phone number
  //  [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@", sPhoneNum]]];
    
    NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",sPhoneNum]];
    UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
    [self.view addSubview:phoneCallWebView];
}


#pragma OrderSvcMgr Delegate Methods
-(void) getDetailedDriverOrderInfoResult:(NSString *)result OrderInfo:(STDetailedDrvOrderInfo *)order_info
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        if([order_info.pass_list count] > 0)
        {
            STPassengerInfo *passenger = [order_info.pass_list objectAtIndex:0];
            [_imgUser setImageWithURL:[NSURL URLWithString:passenger.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
            if(passenger.sex == 0) {
                [_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
            }
            else {
                [_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
                _lblAge.textColor = MYCOLOR_GREEN;
            }
            [_lblAge setText:[NSString stringWithFormat:@"%d", passenger.age]];
            [_lblName setText:[NSString stringWithFormat:@"%@", passenger.name]];
        }
        
        [_lblStartPos setText:order_info.start_addr];
        [_lblEndPos setText:order_info.end_addr];
        [_lblPrice setText:[NSString stringWithFormat:@"%f", order_info.price]];
        [_lblSysInfoPrice setText:order_info.sysinfo_fee_desc];
        BOOL isFirst = YES;
        NSString *strMidPoints = @"";
        for(STMidPoint *mid_point in order_info.mid_points)
        {
            if(!isFirst)
            {
                strMidPoints = [NSString stringWithFormat:@", %@", mid_point.address];
            }
            else
            {
                strMidPoints = mid_point.address;
            }
        }
        [_lblMidPoint setText:strMidPoints];
        
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}


@end
