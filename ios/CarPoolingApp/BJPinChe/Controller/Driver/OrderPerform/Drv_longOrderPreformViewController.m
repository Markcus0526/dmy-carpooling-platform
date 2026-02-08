//
//  Drv_longOrderPreformViewController.m
//  BJPinChe
//
//  Created by CKK on 14-9-1.
//  Copyright (c) 2014年 KimOC. All rights reserved.
// 车主 执行长途订单界面

#import "Drv_longOrderPreformViewController.h"
#import "Drv_LongOrderPerformPassengerCell.h"
#import "UIViewController+CWPopup.h"

@interface Drv_longOrderPreformViewController ()

@end

#define PRO_ARRIVE                          0.3
#define PRO_START                           0.6
#define PRO_FINISH                          1.0
#define CELL_HEIGHT                         95

@implementation Drv_longOrderPreformViewController

@synthesize mOrderInfo;

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
    _tblPassenger.separatorStyle = NO;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


///////////////////////////////// Initialize ////////////////////////////////
#pragma mark - Initialize data info
- (void) initControls
{
    mFirstRun = YES;
    
    // disable operate button
    [_btnArrive setEnabled:NO];
    [_btnStart setEnabled:NO];
    [_btnFinish setEnabled:NO];
    
    // get order detail info
    [self callGetDetailedDriverOrderInfo];
    
    // make tap recognizer to hide keyboard && popup
    UITapGestureRecognizer * tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapBackGround:)];
    
    [self.view addGestureRecognizer:tapRecognizer];
}

- (void) handleTapBackGround:(id)sender
{
    [self.view endEditing:YES];
    
    // dismiss popup
    if (self.popupViewController != nil) {
        [self dismissPopupViewControllerAnimated:YES completion:^{
            NSLog(@"popup view dismissed");
        }];
    }
}


///////////////////////////////////////////////////////////////////////////////
/**
 * update ui using detailed order info
 * change controls' state
 */
- (void) updateUI
{
    //预定出发时间
    
    
    // reload passenger table view
    [_tblPassenger reloadData];
    
    // check order state
    switch (mDetailedOrderInfo.state) {
        case ORDER_STATE_DRIVER_ACCEPTED:
        case ORDER_STATE_GRABBED:
        {
            // call execute service
            [self setProgressbarWithNum:0];
            [self callExecuteLongOrder];
            break;
        }
        case ORDER_STATE_PUBLISHED:
        case ORDER_STATE_STARTED:
        {
            [self setProgressbarWithNum:0];
            // change operate button state
            [_btnArrive setEnabled:YES];
            [_btnStart setEnabled:NO];
            [_btnFinish setEnabled:NO];
            break;
        }
        case ORDER_STATE_DRIVER_ARRIVED:
        {
            [self setProgressbarWithNum:PRO_ARRIVE];
            // change operate button state
            if ([self CanDriverSetUp]) {
                [_btnArrive setEnabled:NO];
                [_btnStart setEnabled:YES];
            }else{
                [_btnArrive setEnabled:NO];
                [_btnStart setEnabled:YES];
            }
            [_btnFinish setEnabled:NO];
            
            break;
        }
        case ORDER_STATE_PASSENGER_GETON:
        {
            [self setProgressbarWithNum:PRO_START];
            // change operate button state
            [_btnArrive setEnabled:NO];
            [_btnStart setEnabled:NO];
            [_btnFinish setEnabled:YES];
            
            break;
        }
        case ORDER_STATE_FINISHED:
        case ORDER_STATE_PAYED:
        case ORDER_STATE_EVALUATED:
        case ORDER_STATE_CLOSED:
        {
            [self setProgressbarWithNum:PRO_FINISH];
            // change operate button state
            [_btnArrive setEnabled:NO];
            [_btnStart setEnabled:NO];
            [_btnFinish setEnabled:NO];
            
            break;
        }
            
        default:
            break;
    }
}
//判断车主是否可以发车
- (BOOL)CanDriverSetUp
{
    for (STPassengerInfo * passenger in mDetailedOrderInfo.pass_list) {
        if (passenger.state == 2) {
            return NO;
        }
    }
    return YES;
}


/**
 *  执行动画
 *
 *  @param percent <#percent description#>
 */
-(void)setProgressbarWithNum:(float)percent
{
    [UIView animateWithDuration:0.5 animations:^{
        self.progressBarBack.frame = CGRectMake(self.progressBarBack.frame.origin.x, self.progressBarBack.frame.origin.x, 10 + 260 * percent, self.progressBarBack.frame.size.height);
        self.progressBarButton.frame = CGRectMake( 260 * percent, self.progressBarButton.frame.origin.y, self.progressBarButton.frame.size.width, self.progressBarButton.frame.size.height);
    }];
    
}

/**
 * set passenger upload in tableview cell
 */
- (void) SetPassengerUpload : (long)passid password:(NSString *)password
{
    [self callSignLongOrderPassengerUpload:passid password:password];
}

/**
 * set passenger giveup in tableview cell
 */
- (void) SetPassengerGiveup : (long)passid
{
    [self callSignLongOrderPassengerGiveup:passid];
}

///////////////////////////////////////////////////////////////////////////
#pragma OrderSvcMgr Delegate Methods

/**
 * get detailed driver order info ( calling service )
 */
- (void) callGetDetailedDriverOrderInfo
{
	TEST_NETWORK_RETURN
	
    [SVProgressHUD show];
    
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetDetailedDriverOrderInfo:[Common getUserID] OrderId:self.mOrderInfo.uid OrderType:self.mOrderInfo.type DevToken:[Common getDeviceMacAddress]];
}

- (void) getDetailedDriverOrderInfoResult:(NSString *)result OrderInfo:(STDetailedDrvOrderInfo *)order_info
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        mDetailedOrderInfo = order_info;
        
        // update ui using detail information
        [self updateUI];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}

/**
 * call execute long order service
 */
- (void) callExecuteLongOrder
{
	TEST_NETWORK_RETURN
	
    [SVProgressHUD show];
    
    [[CommManager getCommMgr] longWayOrderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] longWayOrderSvcMgr] ExecuteLongOrder:[Common getUserID] orderid:self.mOrderInfo.uid devToken:[Common getDeviceMacAddress]];
}

- (void) executeLongOrderResult : (NSString*)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        [self setProgressbarWithNum:0];
        // change operate button state
        [_btnArrive setEnabled:YES];
        [_btnStart setEnabled:NO];
        [_btnFinish setEnabled:NO];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

/**
 * call sign long order driver arrival service
 */
- (void) callSignLongOrderDriverArrival : (long)driverid
{
	TEST_NETWORK_RETURN
	
    [SVProgressHUD show];
    
    [[CommManager getCommMgr] longWayOrderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] longWayOrderSvcMgr] SignLongOrderDriverArrival:[Common getUserID] orderid:self.mOrderInfo.uid devToken:[Common getDeviceMacAddress]];
}

- (void) signLongOrderDriverArrivalResult : (NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        [self setProgressbarWithNum:PRO_ARRIVE];
        // get order detail info
        [self callGetDetailedDriverOrderInfo];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

/**
 * call sign long order passenger upload service
 */
- (void) callSignLongOrderPassengerUpload : (long)passid password:(NSString *)password
{
	TEST_NETWORK_RETURN
	
    [SVProgressHUD show];
    
    [[CommManager getCommMgr] longWayOrderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] longWayOrderSvcMgr] SignLongOrderPassengerUpload:[Common getUserID] orderid:self.mOrderInfo.uid passid:passid password:password devToken:[Common getDeviceMacAddress]];
}

- (void) signLongOrderPassengerUpload : (NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];

		// get order detail info
        [self callGetDetailedDriverOrderInfo];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}


/**
 * call sign long order passenger give up service
 */
- (void) callSignLongOrderPassengerGiveup : (long)passid
{
	TEST_NETWORK_RETURN

	[SVProgressHUD show];

	[[CommManager getCommMgr] longWayOrderSvcMgr].delegate = self;
	[[[CommManager getCommMgr] longWayOrderSvcMgr] SignLongOrderPassengerGiveup:[Common getUserID] orderid:self.mOrderInfo.uid passid:passid devToken:[Common getDeviceMacAddress]];
}


- (void) signLongOrderPassengerGiveup : (NSString *)result
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		[SVProgressHUD dismiss];
		if (self.popupViewController != nil) {
			[self dismissPopupViewControllerAnimated:YES completion:^{
				NSLog(@"popup view dismissed");
				// get order detail info
				[self callGetDetailedDriverOrderInfo];
			}];
		}
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
}


/**
 * call start long order driving service
 */
- (void) callStartLongOrderDriving
{
	TEST_NETWORK_RETURN
	
    [SVProgressHUD show];
    
    [[CommManager getCommMgr] longWayOrderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] longWayOrderSvcMgr] StartLongOrderDriving:[Common getUserID] orderid:self.mOrderInfo.uid devToken:[Common getDeviceMacAddress]];
}

- (void) startLongOrderDriving : (NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        // get order detail info
//        [self setProgressbarWithNum:PRO_START];
        [self callGetDetailedDriverOrderInfo];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}


/**
 * call end long order service
 */
- (void) callEndLongOrder
{
	TEST_NETWORK_RETURN
	
    [SVProgressHUD show];
    
    [[CommManager getCommMgr] longWayOrderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] longWayOrderSvcMgr] EndLongOrder:[Common getUserID] orderid:mOrderInfo.uid  devToken:[Common getDeviceMacAddress]];
}

- (void) endLongOrder : (NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        [self callGetDetailedDriverOrderInfo];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - TableView Delegate

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (mDetailedOrderInfo == nil || mDetailedOrderInfo.pass_list == nil) {
        return 0;
    }
    
    return mDetailedOrderInfo.pass_list.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	return CELL_HEIGHT;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *cellIdentifier = @"longorder_perform_cell";
    
    STPassengerInfo * dataInfo = [mDetailedOrderInfo.pass_list objectAtIndex:indexPath.row];
    
    Drv_LongOrderPerformPassengerCell * cell = (Drv_LongOrderPerformPassengerCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    [cell initData:dataInfo parent:self];
    cell.stateDriver = mDetailedOrderInfo.state;
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
	return cell;
}




///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

- (IBAction)onClickedBack:(id)sender
{
    BACK_VIEW;
}

- (IBAction)onClickedCallCustomer:(id)sender
{
    BACK_VIEW;
}

- (IBAction)onClickedSetCustomer:(id)sender
{
    [self callSignLongOrderDriverArrival:[Common getUserID]];
}
- (IBAction)onClickedChkCustomer:(id)sender
{
    [self callStartLongOrderDriving];
}
- (IBAction)onClickedFinish:(id)sender
{
    [self callEndLongOrder];
    
}
@end
