//
//  Cus_longOrderSuccessViewController.m
//  BJPinChe
//
//  Created by CKK on 14-8-28.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//  长途拼车抢座成功界面

#import "Cus_longOrderSuccessViewController.h"
#import "Drv_MainTabViewController.h"
#import "Cus_LongOrderCancelViewController.h"
#import "UIViewController+CWPopup.h"

@interface Cus_longOrderSuccessViewController ()

@end



@implementation Cus_longOrderSuccessViewController


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

	_mainScroll.frame = CGRectMake(0, _mainScroll.frame.origin.y, self.view.bounds.size.width, self.view.bounds.size.height - _mainScroll.frame.origin.y);
	_mainScroll.contentSize = CGSizeMake(320, 564);

	[self refreshView];

}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(void)refreshView
{
    STGrabLongOrderSuccessInfo * info = self.successInfo;
    self.password.text = info.password;
    [self.driverImg setImageWithURL:[NSURL URLWithString:info.drv_img] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
    
     self.age.text = [NSString stringWithFormat:@"%d",info.drv_age];
    if (info.drv_gender == 0) {
        [self.gender setImage:[UIImage imageNamed:@"icon_female.png"]];
        self.age.textColor = [UIColor colorWithRed:0.30f green:0.71f blue:0.49f alpha:1.00f];
    }else{
        [self.gender setImage:[UIImage imageNamed:@"icon_male.png"]];
        self.age.textColor = [UIColor colorWithRed:0.98f green:0.44f blue:0.45f alpha:1.00f];;
    }

    self.driverName.text = info.drv_name;
    self.brand.text = info.car_brand;
    self.carType.text = info.car_type;
    self.carColor.text = info.car_color;
    [self.carImg setImageWithURL:[NSURL URLWithString:info.car_img] placeholderImage:[UIImage imageNamed:@"demo_carimg.png"]];

    self.startAddr.text = info.start_addr;
    self.startTime.text = [self dateFormatter:info.start_time];
    self.carno.text = [NSString stringWithFormat:@"车牌号：%@",info.carno];
}

-(NSString *)dateFormatter:(NSString *)dateString
{
    NSString * year = [dateString componentsSeparatedByString:@"-"][0];
    NSString * month = [dateString componentsSeparatedByString:@"-"][1];
    NSString * day = [([dateString componentsSeparatedByString:@"-"][2]) componentsSeparatedByString:@" "][0];
    NSString * hour = [([dateString componentsSeparatedByString:@" "][1]) componentsSeparatedByString:@":"][0];
    NSString * minute = [([dateString componentsSeparatedByString:@" "][1]) componentsSeparatedByString:@":"][1];
    
    return [NSString stringWithFormat:@"%@年%@月%@日 %@:%@出发",year,month,day,hour,minute];
}




- (IBAction)OnBackViewClick:(id)sender {
    Drv_MainTabViewController *controller = (Drv_MainTabViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];
    
    // set target tab as Order
    controller.mCurTab = TAB_ORDER;
    
    SHOW_VIEW(controller);
}

- (IBAction)OnGotoMyOrderButtonClick:(id)sender {
    Drv_MainTabViewController *controller = (Drv_MainTabViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];

	// set target tab as Order
    controller.mCurTab = TAB_ORDER;

	SHOW_VIEW(controller);
}


- (IBAction)cancellOrder:(id)sender {
    Cus_LongOrderCancelViewController * cancel = [self.storyboard instantiateViewControllerWithIdentifier:@"LongOrderCancel"];
    cancel.orderID = self.successInfo.uid;
    SHOW_VIEW(cancel);
}




- (IBAction)onClickCancel:(id)sender
{
	TEST_NETWORK_RETURN

	[SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];

	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
	[[[CommManager getCommMgr] orderSvcMgr] GetLongOrderCancelInfo:[Common getUserID] OrderID:self.successInfo.uid DevToken:[Common getDeviceMacAddress]];
}


- (void)getLongOrderCancelInfo:(NSString *)result RetData:(NSDictionary *)RetData
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		[SVProgressHUD dismiss];

		NSArray * arrRules = [RetData objectForKey:@"balance_rule"];

		int i = 0;
		for (NSDictionary * ruleItem in arrRules)
		{
			switch (i)
			{
				case 0:
					_lblRule01.text = [ruleItem objectForKey:@"rule"];
					break;
				case 1:
					_lblRule02.text = [ruleItem objectForKey:@"rule"];
					break;
				case 2:
					_lblRule03.text = [ruleItem objectForKey:@"rule"];
					break;
				case 3:
					_lblRule04.text = [ruleItem objectForKey:@"rule"];
					break;
				default:
					break;
			}
			i++;
		}

		_cancelRuleView.hidden = NO;
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
}



- (IBAction)onClickConfirmCancel:(id)sender {
	[SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];

	[[CommManager getCommMgr] longWayOrderSvcMgr].delegate = self;
	[[[CommManager getCommMgr] longWayOrderSvcMgr] SignLongOrderPassengerGiveup:0 orderid:self.successInfo.uid passid:[Common getUserID] devToken:[Common getDeviceMacAddress]];
}


- (void) signLongOrderPassengerGiveup : (NSString *)result
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		[SVProgressHUD showSuccessWithStatus:@"退票成功" duration:2];
		[NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(returnToMainController) userInfo:nil repeats:NO];
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
}


- (void)returnToMainController
{
	Drv_MainTabViewController* controller = (Drv_MainTabViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];

	// set target tab as Order
	controller.mCurTab = TAB_ORDER;

	SHOW_VIEW(controller);
}



- (IBAction)onClickBackgound:(id)sender {
	_cancelRuleView.hidden = YES;
}



@end
