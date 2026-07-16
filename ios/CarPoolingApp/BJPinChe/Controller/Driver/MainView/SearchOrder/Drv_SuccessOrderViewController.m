//
//  Drv_SuccessOrderViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/28/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_SuccessOrderViewController.h"
#import "Drv_MainTabViewController.h"
#import "Drv_PassengerInfoViewController.h"


@interface Drv_SuccessOrderViewController ()

@end



@implementation Drv_SuccessOrderViewController

@synthesize orderid;

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
	[SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];

	CommManager* commMgr = [CommManager getCommMgr];
	OrderSvcMgr* svcMgr = commMgr.orderSvcMgr;
	svcMgr.delegate = self;
	[svcMgr GetDetailedDriverOrderInfo:[Common getUserID] OrderId:orderid OrderType:1 DevToken:[Common getDeviceMacAddress]];
    
    UIScreen *screen = [UIScreen mainScreen];
    if (screen.bounds.size.height<500) {
        myScrollView.contentSize = CGSizeMake(320, 600);
    }
}



- (void)getDetailedDriverOrderInfoResult:(NSString *)result OrderInfo:(STDetailedDrvOrderInfo *)order_info {

	if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
		[SVProgressHUD dismiss];

		order_detail_info = order_info;

		STPassengerInfo* passinfo = nil;
		NSMutableArray* arrPass = order_info.pass_list;
		if (arrPass != nil && arrPass.count > 0) {
			passinfo = [arrPass objectAtIndex:0];
		}

		if (passinfo != nil) {
			[_imgUser setImageWithURL:[NSURL URLWithString:passinfo.image]];
			if (passinfo.sex == 0) {
				[_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
			} else {
				[_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
			}

			[_lblAge setText:[NSString stringWithFormat:@"%d", passinfo.age]];
			[_lblName setText:passinfo.name];
		}

		[_lblTime setText:order_detail_info.start_time];
		[_lblAddress setText:order_detail_info.start_addr];
	} else {
		[SVProgressHUD dismissWithError:result];
	}
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
#pragma mark - UI Button clicked event implementation

// main control event
/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    Drv_MainTabViewController *controller = (Drv_MainTabViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];
    
    // set target tab as Order
    controller.mCurTab = TAB_ORDER;
    
    SHOW_VIEW(controller);
}

- (IBAction)onClickedGoOrder:(id)sender
{
    Drv_MainTabViewController *controller = (Drv_MainTabViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];

	// set target tab as Order
    controller.mCurTab = TAB_ORDER;

	SHOW_VIEW(controller);
}



- (IBAction)onClickedPhoto:(id)sender {
	STPassengerInfo* passinfo = nil;
	NSMutableArray* arrPass = order_detail_info.pass_list;
	if (arrPass != nil && arrPass.count > 0) {
		passinfo = [arrPass objectAtIndex:0];
	}

	if (passinfo == nil)
		return;


	Drv_PassengerInfoViewController *viewController = (Drv_PassengerInfoViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"passenger_info"];

	viewController.mPassID = passinfo.uid;

	[self presentViewController:viewController animated:YES completion:nil];
}


@end
