//
//  Cus_PayOrderViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/15/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//   假数据

#import "Cus_PayOrderViewController.h"

@implementation PayCouponCell

- (IBAction)onSelectCheckBox:(id)sender
{
    if ([self._chkMark isSelected] == YES) {
        [self._chkMark setSelected:NO];
    } else {
        [self._chkMark setSelected:YES];
    }
}
@end

@interface Cus_PayOrderViewController ()

@end

@implementation Cus_PayOrderViewController

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
    
    [_lblTotalPrice setText:[NSString stringWithFormat:@"%.1f",self.mOrderInfo.price]];
    [_lblRealPrice setText:[NSString stringWithFormat:@"%.1f",self.mOrderInfo.price]];
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

////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Standard TableView delegates

////////////////////////////////////////////////////////////////////////////////////////////////////
- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (mCouponArray == nil) {
        return 0;
    }
    
    return mCouponArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	//return height1;
	return 30;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *cellIdentifier = @"pay_coupon_cell";
    
    PayCouponCell * cell = (PayCouponCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
	return cell;
    
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

- (IBAction)onClickedPay:(id)sender
{
	TEST_NETWORK_RETURN;
    [SVProgressHUD show];
	[[CommManager getCommMgr] orderExecuteSvcMgr].delegate = self;
	[[[CommManager getCommMgr] orderExecuteSvcMgr] PayNormalOrder:[Common getUserID] OrderId:self.mOrderInfo.uid OrderType:self.mOrderInfo.type Price:self.mOrderInfo.price Coupons:@"" DevToken:[Common getDeviceMacAddress]];
}


#pragma OrderExecuteSvcMgr Delegate Methods
- (void) payNormalOrderResult:(NSString *)result
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
       // [SVProgressHUD dismissWithSuccess:@"支付成功！" afterDelay:];
        [SVProgressHUD dismissWithSuccess:@"支付成功！"  afterDelay:DEF_DELAY];
		[_payDelegate onPaySuccess:self.mOrderInfo.uid];
		BACK_VIEW;
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}


@end
