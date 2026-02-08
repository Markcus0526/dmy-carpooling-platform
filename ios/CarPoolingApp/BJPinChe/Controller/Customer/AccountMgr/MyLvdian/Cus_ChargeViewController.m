//
//  ChargeViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//  充值界面   

#import "Cus_ChargeViewController.h"
#import "Cus_WithdrawViewController.h"
#import "Cus_AccumViewController.h"
#import "WapPayViewController.h"
#import <CommonCrypto/CommonDigest.h>


@interface Cus_ChargeViewController ()

@end

@implementation Cus_ChargeViewController

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
	[self callGetMoney];
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




/////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Initilaize

- (void) initControls
{
    UITapGestureRecognizer * tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapBackGround:)];
    
    [self.view addGestureRecognizer:tapRecognizer];
    _txtChargeVal.delegate = self;
}

/**
 * Hide keyboard when tapped background
 */
- (void) handleTapBackGround:(id)sender
{
    [self.view endEditing:YES];
}

- (void) updateUI
{
    [_lblLeftAccum setText:[NSString stringWithFormat:@"%.02f", mLeftMoney]];
}

//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/**
 * call get the current money
 */
- (void) callGetMoney
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetMoeny service routine.
    [[CommManager getCommMgr] tradeSvcMgr].delegate = self;
    [[[CommManager getCommMgr] tradeSvcMgr] GetMoney:[Common getUserID] devtoken:[Common getDeviceMacAddress]];
}

- (void)getMoneyResult:(NSString *)result money:(double)money
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        mLeftMoney = money;
        
        // refresh table
        [self updateUI];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}


- (NSString*)createOrderInfoWithWithGoodsName:(NSString*)name price:(double)price num:(int)num
{
	NSString* partner_id = @"9000100005";
	NSString* md5_private = @"pSAw3bzfMKYAXML53dgQ3R4LsKp758Ss";


	NSNumber* bigPrice = [NSNumber numberWithDouble:price * 100]; // 创建BigDecimal对象   1yuan = 100
	NSNumber* bigNum = [NSNumber numberWithInt:num];

	NSNumber* bigInterest = [NSNumber numberWithDouble:price * 100 * num];

	NSMutableString* orderUrlInfo = [NSMutableString stringWithFormat:@"currency=1&extra=%ld", [Common getUserID]];
	NSMutableString* szOrderInfo = [NSMutableString stringWithFormat:@"currency=1&extra=%ld", [Common getUserID]];

	CommManager* commMgr = [CommManager getCommMgr];

	int year = 0;
	int month = 0;
	int day = 0;
	int hour = 0;
	int minute = 0;
	int second = 0;

	NSDate* curDate = [NSDate date];
	NSCalendar* cal = [NSCalendar currentCalendar];

//	[cal getEra:&era year:&year month:&month day:&day fromDate:curDate];
//	[cal getHour:&hour minute:&minute second:&second nanosecond:&nanosecond fromDate:curDate];

	NSDateComponents* yearComponent = [cal components:NSCalendarUnitYear fromDate:curDate];
	NSDateComponents* monthComponent = [cal components:NSCalendarUnitMonth fromDate:curDate];
	NSDateComponents* dayComponent = [cal components:NSCalendarUnitDay fromDate:curDate];
	NSDateComponents* hourComponent = [cal components:NSCalendarUnitHour fromDate:curDate];
	NSDateComponents* minuteComponent = [cal components:NSCalendarUnitMinute fromDate:curDate];
	NSDateComponents* secondComponent = [cal components:NSCalendarUnitSecond fromDate:curDate];

	year = [yearComponent year];
	month = [monthComponent month];
	day = [dayComponent day];
	hour = [hourComponent hour];
	minute = [minuteComponent minute];
	second = [secondComponent second];

	int nRand = arc4random_uniform(1000);

	NSString* szDate = [NSString stringWithFormat:@"%04d%02d%02d%02d%02d%02d", year, month, day, hour, minute, second];
	NSString* szOrderNo = [NSString stringWithFormat:@"%04d%02d%02d%02d%02d%02d%03d", year, month, day, hour, minute, second, nRand];

	NSString* goods_name = @"OO快拼测试";
	orderUrlInfo = [[orderUrlInfo stringByAppendingFormat:@"&goods_name=%@", [goods_name stringByAddingPercentEscapesUsingEncoding:GBK_ENCODING]] mutableCopy];
	szOrderInfo = [[szOrderInfo stringByAppendingFormat:@"&goods_name=%@", goods_name] mutableCopy];

	orderUrlInfo = [[orderUrlInfo stringByAppendingFormat:@"&goods_url=%@", [self urlencode:@"http://item.jd.com/736610.html"]] mutableCopy];
	szOrderInfo = [[szOrderInfo stringByAppendingFormat:@"&goods_url=%@", @"http://item.jd.com/736610.html"] mutableCopy];

	orderUrlInfo = [[orderUrlInfo stringByAppendingFormat:@"&input_charset=1&order_create_time=%@&order_no=%@", szDate, szOrderNo] mutableCopy];
	szOrderInfo = [[szOrderInfo stringByAppendingFormat:@"&input_charset=1&order_create_time=%@&order_no=%@", szDate, szOrderNo] mutableCopy];

	orderUrlInfo = [[orderUrlInfo stringByAppendingFormat:@"&pay_type=2&return_url=%@chargeWithBaidu", [self urlencode:commMgr.bathServiceUrl]] mutableCopy];
	szOrderInfo = [[szOrderInfo stringByAppendingFormat:@"&pay_type=2&return_url=%@chargeWithBaidu", commMgr.bathServiceUrl] mutableCopy];

	orderUrlInfo = [[orderUrlInfo stringByAppendingFormat:@"&service_code=1&sign_method=1&sp_no=%@", partner_id] mutableCopy];
	szOrderInfo = [[szOrderInfo stringByAppendingFormat:@"&service_code=1&sign_method=1&sp_no=%@", partner_id] mutableCopy];

	orderUrlInfo = [[orderUrlInfo stringByAppendingFormat:@"&total_amount=%@", bigInterest] mutableCopy];
	szOrderInfo = [[szOrderInfo stringByAppendingFormat:@"&total_amount=%@", bigInterest] mutableCopy];

	orderUrlInfo = [[orderUrlInfo stringByAppendingFormat:@"&transport_amount=0&unit_amount=%@", bigPrice] mutableCopy];
	szOrderInfo = [[szOrderInfo stringByAppendingFormat:@"&transport_amount=0&unit_amount=%@", bigPrice] mutableCopy];

	orderUrlInfo = [[orderUrlInfo stringByAppendingFormat:@"&unit_count=%@&version=2", bigNum] mutableCopy];
	szOrderInfo = [[szOrderInfo stringByAppendingFormat:@"&unit_count=%@&version=2", bigNum] mutableCopy];

	NSString* signedString = [self md5:[NSString stringWithFormat:@"%@&key=%@", szOrderInfo, md5_private]];

	return [NSString stringWithFormat:@"%@&sign=%@", orderUrlInfo, signedString];
}



- (NSString *)urlencode:(NSString*)org_str {
	NSMutableString *output = [NSMutableString string];
	const unsigned char *source = (const unsigned char *)[org_str UTF8String];
	int sourceLen = strlen((const char *)source);
	for (int i = 0; i < sourceLen; ++i) {
		const unsigned char thisChar = source[i];
		if (thisChar == ' '){
			[output appendString:@"+"];
		} else if (thisChar == '.' || thisChar == '-' || thisChar == '_' || thisChar == '~' ||
				   (thisChar >= 'a' && thisChar <= 'z') ||
				   (thisChar >= 'A' && thisChar <= 'Z') ||
				   (thisChar >= '0' && thisChar <= '9')) {
			[output appendFormat:@"%c", thisChar];
		} else {
			[output appendFormat:@"%%%02X", thisChar];
		}
	}
	return output;
}




- (NSString *)md5:(NSString*)srcStr
{
#if true
	int nLen = srcStr.length * 4;

	char *cStr = malloc(nLen);
	[srcStr getCString:cStr maxLength:nLen encoding:GBK_ENCODING];
#else
	const char *cStr = [srcStr UTF8String];
#endif

	unsigned char result[CC_MD5_DIGEST_LENGTH];
	CC_MD5(cStr, strlen(cStr), result); // This is the md5 call
	return [NSString stringWithFormat:
			@"%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x",
			result[0], result[1], result[2], result[3],
			result[4], result[5], result[6], result[7],
			result[8], result[9], result[10], result[11],
			result[12], result[13], result[14], result[15]
			];
}



/**
 * call charge money service
 * @param : money [in], charge value
 */
- (void) callCharge : (double)money
{
//    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
//	
//    TEST_NETWORK_RETURN;
//	
//    // Call the GetCurOrderList service routine.
//    [[CommManager getCommMgr] tradeSvcMgr].delegate = self;
//    [[[CommManager getCommMgr] tradeSvcMgr] Charge:[Common getUserID] balance:money devtoken:[Common getDeviceMacAddress]];
	WapPayViewController* wapPayViewCtrl = [[WapPayViewController alloc] initWithNibName:@"WapPayViewController" bundle:nil];
	wapPayViewCtrl.orderInfo = [self createOrderInfoWithWithGoodsName:@"OO快拼测试" price:money num:1];
    if (self.navigationController ==nil) {
        [self presentViewController:wapPayViewCtrl animated:YES completion:nil];
    }
	[self.navigationController pushViewController:wapPayViewCtrl animated:YES];
}

- (void) chargeResult:(NSString *)result money:(double)money
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismissWithSuccess:MSG_SUCCESS afterDelay:DEF_DELAY];
        
        mLeftMoney = money;
        
        // refresh table
        [self updateUI];
        self.tabBarController.selectedIndex =0;
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    BACK_VIEW;
}

/**
 * Submit charge button clicked event implemenation
 */
- (IBAction)onClickedNext:(id)sender
{
    double money = 0;
    
    if ([[_txtChargeVal text] isEqualToString:@""] == YES) {
        [_txtChargeVal becomeFirstResponder];
        [SVProgressHUD showErrorWithStatus:@"请输入金额" duration:1.0];
        
        return;
    }
    
    money = [[_txtChargeVal text] doubleValue];
    [self callCharge:money];
}

/**
 * Chaxun menu button clicked event implementation
 */
- (IBAction)onClickedMenuAccum:(id)sender
{
//    UIViewController * presentViewCtrl = self.presentingViewController;
//    [self dismissViewControllerAnimated:NO completion:^
//     {
//         UIViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"AccumHistory"];
//         [presentViewCtrl presentViewController:ctrl animated:NO completion:nil];
//     }];
    [self dismissViewControllerAnimated:YES completion:nil];
    
}

/**
 * Tixian menu button clicked event implementation
 */
- (IBAction)onClickedMenuWithdraw:(id)sender
{
    Cus_AccumViewController* presentViewCtrl = (Cus_AccumViewController*)self.presentingViewController;
    [self dismissViewControllerAnimated:NO completion:^
     {
         Cus_WithdrawViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"Withdraw"];
         presentViewCtrl.RefreshOrNot =NO;
         [presentViewCtrl presentViewController:ctrl animated:YES completion:nil];
     }];
}


#pragma mark - UITextField - delegate
-(BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string{
	if (textField == _txtChargeVal) {
		NSUInteger newLength = textField.text.length + string.length - range.length;
		if (newLength > 6)
		{
			return NO;
		}

		if ([string isEqualToString:@""]) {
			return YES;
		}

		if (textField.text.length >=4) {
			[SVProgressHUD showErrorWithStatus:@"最大金额9999" duration:DEF_DELAY];
			return NO;
		}

		if ([string isEqualToString:@"0"] && textField.text.length == 0) {
			[SVProgressHUD showErrorWithStatus:@"开始不能输入0" duration:DEF_DELAY];
			return NO;
		}

		NSCharacterSet *invalidCharSet = [[NSCharacterSet characterSetWithCharactersInString:@"1234567890"] invertedSet];
		NSString *filtered = [[string componentsSeparatedByCharactersInSet:invalidCharSet] componentsJoinedByString:@""];

		if ([string isEqualToString:filtered]) {
			return YES;
		} else {
			[SVProgressHUD showErrorWithStatus:@"只能输入数字" duration:DEF_DELAY];
			return NO;
		}
	}

	return YES;
}
@end
