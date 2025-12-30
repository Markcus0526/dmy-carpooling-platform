//
//  Cus_STOrderChangePriceViewController.m
//  BJPinChe
//
//  Created by YunSI on 9/10/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//   乘客改价

#import "Cus_STOrderChangePriceViewController.h"
#import "Cus_PubOrderWaitingViewController.h"
#import "SystemPriceInfoResult.h"
@interface Cus_STOrderChangePriceViewController ()

@end

@implementation Cus_STOrderChangePriceViewController

@synthesize mOrderType;
@synthesize mPrice;
@synthesize mRepubTimes;
@synthesize mOrderPubData;

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
    
//    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
//    [btn setFrame:CGRectMake(0, 0, 44, 44)];
//    [btn setBackgroundColor:[UIColor clearColor]];
//    [btn addTarget:self action:@selector(fanhuiClick) forControlEvents:UIControlEventTouchUpInside];
//    UIBarButtonItem *item = [[UIBarButtonItem alloc]initWithCustomView:btn];
//    self.navigationItem.leftBarButtonItem = item;
	
	[self initControls];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void) initControls
{
    
      [self callgetSystemPrice];
	[_txtPrice setText:[NSString stringWithFormat:@"%.2f", self.mPrice]];
    [driverCountLabel setText:[NSString stringWithFormat:@"%d",_driverCount+20]];
  
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    CGRect rect = [[UIScreen mainScreen] bounds];
    CGSize size = rect.size;
    CGFloat width = size.width;
//    CGFloat height = size.height;
    myScrollView.scrollEnabled =YES;
    myScrollView.contentSize =CGSizeMake(width, 350+158);
    if (size.height <500)
    {
        myScrollView.contentSize =CGSizeMake(width, 600);
    }

}

- (IBAction)btnClick:(id)sender {
    [SVProgressHUD showWithStatus:@"不急，我们继续为您寻找，若有车主接单，会马上通知您" maskType:DEF_DELAY];
    [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(fanhui) userInfo:nil repeats:NO];
}

- (IBAction)onClickedBack:(id)sender
{
    [SVProgressHUD showWithStatus:@"不急，我们继续为您寻找，若有车主接单，会马上通知您" maskType:DEF_DELAY];
    [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(fanhui) userInfo:nil repeats:NO];
   // [self.navigationController popToRootViewControllerAnimated:YES];
}
-(void)fanhui
{
    [self dismissViewControllerAnimated:NO completion:^{
        
        [self.pubWaitingVC.navigationController popToRootViewControllerAnimated:YES];
        
    }];
}
- (IBAction)onClickedMinus:(id)sender
{
	self.mPrice -= 1;
	if(self.mPrice <= 0)
	{
		self.mPrice = 0;
	}
	[_txtPrice setText:[NSString stringWithFormat:@"%.2f", self.mPrice]];
}

- (IBAction)onClickedPlus:(id)sender
{
	self.mPrice += 1;
	if(self.mPrice > 10000)
	{
		self.mPrice = 10000;
	}
	[_txtPrice setText:[NSString stringWithFormat:@"%.2f", self.mPrice]];
}
/**
 *  APP_USER
    获取平台价格信息的网络请求
 */
- (void)callgetSystemPrice
{
	TEST_NETWORK_RETURN;
    [SVProgressHUD show];
    [[CommManager getCommMgr]orderSvcMgr].delegate =self;
//    [[[CommManager getCommMgr]orderSvcMgr ]getSystemPriceInfo:self.mOrderPubData];
    [[[CommManager getCommMgr]orderSvcMgr ]time_left:[Common getUserID] OrderID:_orderId DevToken:[Common getDeviceMacAddress]];
}
/**
 *  APP_USER
 *  代理方法接收结果  SystemPriceInfoResult  数组
 *  @param result  <#result description#>
 *  @param retData <#retData description#>
 */
-(void)getsystemPriceInfoResult:(NSString *)result RetData:(NSMutableArray *)retData
{
    SystemPriceInfoResult *myresult;
    
    for(SystemPriceInfoResult *item in retData)
    {
       if(item.car_style ==  self.mOrderPubData.req_style )
       {
           myresult =item;
           break;
       }
    }
    
    if(self.mPrice < myresult.aver_price)
    {
     [_lblSystemPrice setText:[NSString stringWithFormat:@"%.1f",myresult.aver_price]];
    }else
    {
     [_lblSystemPrice setText:[NSString stringWithFormat:@"%.1f",(self.mPrice +myresult.price_interval)]];
    }
    
    MyLog(@"%@",myresult.description);

}
-(void)getTime_left:(NSString *)result RetData:(NSDictionary *)retData
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        
        [SVProgressHUD dismiss];
        time_leftLabel.text =[NSString stringWithFormat:@"%@",[retData objectForKey:@"time_left"]];
        average_timeLabel.text =[NSString stringWithFormat:@"%@",[retData objectForKey:@"average_time"]];
    }else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}
- (IBAction)onClickedRepub:(id)sender
{
	TEST_NETWORK_RETURN;
	[SVProgressHUD show];
	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
	[[[CommManager getCommMgr] orderSvcMgr] publishOnceOrder:[Common getUserID] StartAddr:self.mOrderPubData.start_addr StartLat:self.mOrderPubData.start_lat StartLng:self.mOrderPubData.start_lng EndAddr:self.mOrderPubData.end_addr EndLat:self.mOrderPubData.end_lat EndLng:self.mOrderPubData.end_lng StartTime:self.mOrderPubData.start_time MidPoints:self.mOrderPubData.mid_points Remark:self.mOrderPubData.remark ReqStyle:self.mOrderPubData.req_style Price:self.mOrderPubData.price City:self.mOrderPubData.city WaitTime:self.mOrderPubData.wait_time DevToken:[Common getDeviceMacAddress]];
}


#pragma OrderSvcDelegate Methods
- (void) publishOnceOrderResult:(NSString *)result RetData:(STPubOnceOrderRet *)retData
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
		
		
		[self dismissViewControllerAnimated:YES completion:^{
			
        
        Cus_PubOrderWaitingViewController *viewCtrl =self.pubWaitingVC;

			viewCtrl.mOrderId = retData.order_id;
			viewCtrl.mPrice = [_txtPrice.text doubleValue];
			if([_txtPrice.text doubleValue] > self.mPrice)
			{
				switch (self.mRepubTimes) {
					case 1:
						viewCtrl.mWaitTime = retData.add_price_time1;
						break;
					case 2:
						viewCtrl.mWaitTime = retData.add_price_time2;
						break;
					case 3:
						viewCtrl.mWaitTime = retData.add_price_time3;
						break;
					case 4:
						viewCtrl.mWaitTime = retData.add_price_time4;
						break;
					case 5:
						viewCtrl.mWaitTime = retData.add_price_time5;
						break;
						
					default:
						break;
				}
			}
			else
			{
				switch (self.mRepubTimes) {
					case 1:
						viewCtrl.mWaitTime = retData.same_price_time1;
						break;
					case 2:
						viewCtrl.mWaitTime = retData.same_price_time2;
						break;
					case 3:
						viewCtrl.mWaitTime = retData.same_price_time3;
						break;
					case 4:
						viewCtrl.mWaitTime = retData.same_price_time4;
						break;
					case 5:
						viewCtrl.mWaitTime = retData.same_price_time5;
						break;
						
					default:
						break;
				}
			}
			viewCtrl.mRepubTimes = self.mRepubTimes;
			viewCtrl.mOrderPubData.price = [_txtPrice.text doubleValue];
			viewCtrl.mOrderPubData = mOrderPubData;
			
//
//			
//			CATransition *animation = [CATransition animation]; \
//			[animation setDuration:0.3]; \
//			[animation setType:kCATransitionPush]; \
//			[animation setSubtype:kCATransitionFromRight]; \
//			[animation setTimingFunction:[CAMediaTimingFunction functionWithName: kCAMediaTimingFunctionEaseInEaseOut]]; \
//			[[curCtrl.view.superview layer] addAnimation:animation forKey:@"SwitchToView"];
			
            //[curCtrl presentViewController:viewCtrl animated:NO completion:nil];
		}];
		
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}


- (IBAction)callClick:(id)sender {
    NSString * sPhoneNum = COMPLAIN_TEL_NUM;
    // call this phone number
   // [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@", sPhoneNum]]];
    
    NSURL *phoneURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",sPhoneNum]];
    UIWebView *  phoneCallWebView = [[UIWebView alloc] initWithFrame:CGRectZero];
    [phoneCallWebView loadRequest:[NSURLRequest requestWithURL:phoneURL]];
    [self.view addSubview:phoneCallWebView];
    
    
}
@end
