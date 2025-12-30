//
//  Cus_LongOrderCancelViewController.m
//  BJPinChe
//
//  Created by CKK on 14-9-2.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "Cus_LongOrderCancelViewController.h"
#import "UIViewController+CWPopup.h"
@interface Cus_LongOrderCancelViewController ()

@end

@implementation Cus_LongOrderCancelViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void)loadView
{
    [super loadView];
    self.hidesBottomBarWhenPushed =YES;
	
	
}
- (void)setOrderDetailedInfo:(STDetailedCusOrderInfo *)orderDetailedInfo
{
    _orderDetailedInfo =orderDetailedInfo;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
	
	TEST_NETWORK_RETURN;
	[SVProgressHUD show];
	
    if (_orderID !=0) {
        [[CommManager getCommMgr] orderSvcMgr].delegate = self;
        [[[CommManager getCommMgr] orderSvcMgr] GetLongOrderCancelInfo:[Common getUserID] OrderID:_orderID DevToken:[Common getDeviceMacAddress]];
    }else
    {
        [[CommManager getCommMgr] orderSvcMgr].delegate = self;
        [[[CommManager getCommMgr] orderSvcMgr] GetLongOrderCancelInfo:[Common getUserID] OrderID:self.orderDetailedInfo.uid DevToken:[Common getDeviceMacAddress]];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated
{
	[self loadView];
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

- (IBAction)OnBackVeiw:(id)sender {
    if (_orderID !=0)
    {
        BACK_VIEW;
    }
}


- (IBAction)OnCancelButtonClick:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}


- (IBAction)OnSureButtonClick:(id)sender
{
	TEST_NETWORK_RETURN;

	[SVProgressHUD show];

	if (_orderID != 0) {
		[[CommManager getCommMgr] orderSvcMgr].delegate = self;
		[[[CommManager getCommMgr] orderSvcMgr] GetLongOrderCancelInfo:[Common getUserID] OrderID:_orderID DevToken:[Common getDeviceMacAddress]];
	} else {
		[[CommManager getCommMgr] longWayOrderSvcMgr].delegate = self;
		[[[CommManager getCommMgr] longWayOrderSvcMgr] SignLongOrderPassengerGiveup:0 orderid:self.orderDetailedInfo.uid passid:[Common getUserID] devToken:[Common getDeviceMacAddress]];
	}
}


- (void)getLongOrderCancelInfo:(NSString *)result RetData:(NSDictionary *)RetData
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		[SVProgressHUD dismiss];

		_lblStartAddr.text = [RetData objectForKey:@"start_addr"];
		_lblEndAddr.text = [RetData objectForKey:@"end_addr"];
		_lblOrderNum.text = [RetData objectForKey:@"order_num"];
		_lblPrice.text = [NSString stringWithFormat:@"%.1f点", [[RetData objectForKey:@"price"] doubleValue]];
		_lblPubTime.text = [RetData objectForKey:@"create_time"];
		_lblReserveTime.text = [RetData objectForKey:@"start_time"];
		_lblLeftDays.text = [RetData objectForKey:@"time_interval_desc"];
		_lblReduce.text = [RetData objectForKey:@"balance_desc"];

		NSArray * arrRules = [RetData objectForKey:@"balance_rule"];
		int i = 0;
		for (NSDictionary * ruleItem in arrRules) {
			switch (i) {
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
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
}


- (void) signLongOrderPassengerGiveup : (NSString *)result
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		[SVProgressHUD showSuccessWithStatus:@"退票成功" duration:2];
		[NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(fanhui) userInfo:nil repeats:NO];
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
}


-(void)fanhui{
    if (_orderID !=0)
    {
        controller = (Drv_MainTabViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];
        
        // set target tab as Order
        controller.mCurTab = TAB_ORDER;
        
        SHOW_VIEW(controller);
        return;
    }
    [self.navigationController popViewControllerAnimated:YES];
}

@end
