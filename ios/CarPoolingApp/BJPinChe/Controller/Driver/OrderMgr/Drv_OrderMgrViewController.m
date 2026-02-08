//
//  AppMgrViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//车主身份 我的订单界面

#import "Drv_OrderMgrViewController.h"
#import "Config.h"
#import "MJRefresh.h"
#import "Drv_OrderPerformViewController.h"
#import "Drv_LongOrderDetailViewController.h"
#import "Drv_MySTOrderDetViewController.h"
#import "Drv_MyWTOrderDetViewController.h"
#import "UIViewController+CWPopup.h"
#import "Drv_longOrderPreformViewController.h"
#import "InsuranceModal.h"
#import "Cus_EvalDriverLatestViewController.h"

@interface Drv_OrderMgrViewController ()<insuranceFeeDelegate>
@property(nonatomic,assign)BOOL HeaderRefrsh;
@end


#define SINGLE_ORDER_CELLID					@"SingleOrderCell"
#define MULTI_ORDER_CELLID					@"MultiOrderCell"
#define ORDERCELL_HEIGHT					150
#define EXECUTE_ALERT_TAG					1


@implementation Drv_OrderMgrViewController

- (NSMutableArray *)mOrderArray
{
	if (_mOrderArray ==nil)
		_mOrderArray =[[NSMutableArray alloc]init];

	return _mOrderArray;
}



- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];

	if (self) {
        // Drvtom initialization
    }

    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initControls];
    CGRect rect = [[UIScreen mainScreen] bounds];
    if (rect.size.height>500) {
        [_tableView setFrame:CGRectMake(_tableView.frame.origin.x, _tableView.frame.origin.y, _tableView.frame.size.width, _tableView.frame.size.height+88)];
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


///////////////////////////////// Initialize ////////////////////////////////
#pragma mark - Initialize data info
- (void) initControls
{
	mCurLimitTime = @"";
	mCurOrderType = 1;

	_lblNoOrder.hidden = YES;

	[_radio2 setSelected:YES];
	[_radio1 setSelected:NO];
	[_radio3 setSelected:NO];
	[_radio4 setSelected:NO];
	[_radio5 setSelected:NO];

	[self setupRefresh];
	[self reloadOrdersFromService];
}




/**
 *  集成刷新控件
 */
- (void)setupRefresh
{
    // 1.下拉刷新(进入刷新状态就会调用self的headerRereshing)
    [_tableView addHeaderWithTarget:self action:@selector(headerRefreshing)];
#warning 自动刷新(一进入程序就下拉刷新)
    //[_tableView headerBeginRefreshing];
    
    // 2.上拉加载更多(进入刷新状态就会调用self的footerRereshing)
    [_tableView addFooterWithTarget:self action:@selector(footerRefreshing)];
    
    // 设置文字(也可以不设置,默认的文字在MJRefreshConst中修改)
    _tableView.headerPullToRefreshText = MSG_TBLHEADER_PULL;
    _tableView.headerReleaseToRefreshText = MSG_TBLHEADER_RELEASE;
    _tableView.headerRefreshingText = MSG_TBLHEADER_REFRESHING;
    
    _tableView.footerPullToRefreshText = MSG_TBLFOOTER_PULL;
    _tableView.footerReleaseToRefreshText = MSG_TBLFOOTER_RELEASE;
    _tableView.footerRefreshingText = MSG_TBLFOOTER_REFRESHING;
}


#pragma mark header刷新方法 车主获取最新订单
- (void)headerRefreshing
{
    self.HeaderRefrsh =YES;
   
    NSString *latestOrderNum = @"0";
    int latestOrderType = 0;
    if([self.mOrderArray count] > 0)
    {
        STOrderInfo *firstOrder = [self.mOrderArray objectAtIndex:0];
        latestOrderNum = firstOrder.order_num;
        latestOrderType = firstOrder.type;
    }
//    //车主获取最新订单
//    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
//    [[[CommManager getCommMgr] orderSvcMgr] GetLatestDriverOrders:[Common getUserID] OrderType:mCurOrderType OrderNum:latestOrderNum LimitedType:latestOrderType DevToken:[Common getDeviceMacAddress]];
    
   // mCurLimitTime = @"";
    //mCurOrderType = 1;

    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetPagedDriverOrders:[Common getUserID] OrderType:mCurOrderType LimitTime:@"" DevToken:[Common getDeviceMacAddress]];
    
}

- (void)footerRefreshing
{
    self.HeaderRefrsh =NO;
   
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetPagedDriverOrders:[Common getUserID] OrderType:mCurOrderType LimitTime:mCurLimitTime DevToken:[Common getDeviceMacAddress]];
}



#pragma mark - Standard TableView delegates


////////////////////////////////////////////////////////////////////////////////////////////////////
- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.mOrderArray == nil) {
        return 0;
    }
    
    return self.mOrderArray.count;
}



- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	//return height1;
	return ORDERCELL_HEIGHT;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier = @"";
	UITableViewCell *cell = nil;
	STOrderInfo * dataInfo = [self.mOrderArray objectAtIndex:indexPath.row];

	cell.selectionStyle = UITableViewCellSelectionStyleNone;

	if (dataInfo.type != TYPE_LONG_ORDER) {
		cellIdentifier = SINGLE_ORDER_CELLID;
		Drv_OrderSingleCell * orderCell = (Drv_OrderSingleCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
		[orderCell initData:dataInfo parent:self];
		cell = orderCell;
	} else {
		cellIdentifier = MULTI_ORDER_CELLID;
		Drv_OrderMultiCell * orderCell = (Drv_OrderMultiCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
		[orderCell initData:dataInfo parent:self];
		cell = orderCell;
	}

	return cell;
}


-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	STOrderInfo *orderInfo = [self.mOrderArray objectAtIndex:indexPath.row];

	switch (orderInfo.type) {
	case TYPE_SINGLE_ORDER:
        {
            Drv_MySTOrderDetViewController * viewController = (Drv_MySTOrderDetViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"my_st_order_detail"];

			viewController.mOrderInfo = orderInfo;
            viewController.hidesBottomBarWhenPushed =YES;
            [self.navigationController pushViewController:viewController animated:YES];
           // SHOW_VIEW(viewController);
            
            break;
        }
//        case TYPE_WORK_ORDER:
//        {
//            Drv_MyWTOrderDetViewController * viewController = (Drv_MyWTOrderDetViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"my_wt_order_detail"];
//            
//            viewController.mOrderInfo = orderInfo;
//            [self.navigationController pushViewController:viewController animated:YES];
//            //SHOW_VIEW(viewController);
//            
//            break;
//        }
        case TYPE_LONG_ORDER:
        {
            Drv_LongOrderDetailViewController * viewController = [self.storyboard instantiateViewControllerWithIdentifier:@"LongOrderDetail"];
            viewController.mOrderInfo = orderInfo;
            viewController.hidesBottomBarWhenPushed =YES;
             [self.navigationController pushViewController:viewController animated:YES];
          //  SHOW_VIEW(viewController);
            
            break;
        }
        default:
            break;
    }

    
}



///////////////////////////////////////////////////////////////////////////////
#pragma mark - UI User Event

- (void)onClickedPerform:(STOrderInfo *)orderInfo
{
	mSelectedOrderInfo = orderInfo;
	
	if(orderInfo.state < ORDER_STATE_STARTED)
	{
		TEST_NETWORK_RETURN

		UIAlertView* alertView = [[UIAlertView alloc] initWithTitle:@"" message:@"是否确认执行订单?" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
		alertView.tag = EXECUTE_ALERT_TAG;
		[alertView show];
	}
    else if (orderInfo.state == ORDER_STATE_PAYED) {
        // show evaluation dialog
        Drv_EvalCustomerViewController *popup = [[Drv_EvalCustomerViewController alloc] initWithNibName:@"Drv_EvalCustomerViewController" bundle:nil];
        popup.delegate = self;
		STPassengerInfo *passInfo = [[STPassengerInfo alloc] init];
		passInfo.uid = orderInfo.pass_id;
		[popup initData:self dataInfo:passInfo];
        
        [self presentPopupViewController:popup animated:YES completion:^(void) {
			NSLog(@"popup view presented");
		}];
    }
    else if (orderInfo.state ==ORDER_STATE_EVALUATED)
    {
        Cus_EvalDriverLatestViewController* controller = [[Cus_EvalDriverLatestViewController alloc] initWithNibName:@"Cus_EvalDriverLatestViewController" bundle:nil];
        
        [self presentPopupViewController:controller animated:YES completion:^(void) {
            MyLog(@"popup view presented");
        }];
        [controller setParent:self message:orderInfo.eval_content level:orderInfo.evaluated];
    }
		
}
- (void)onClickedPerformInsuranceModal:(STOrderInfo *)orderInfo
{
 InsuranceModal *feeView =[[InsuranceModal alloc]initWithNibName:@"InsuranceModal" bundle:nil];
    feeView.morderInfo =orderInfo;
    feeView.delegate =self;
    [self presentPopupViewController:feeView animated:YES completion:nil];

}
- (void)tappedDone
{
    [self dismissPopupViewControllerAnimated:YES completion:nil];
}
- (void)onClickedPerformLongOrder:(STOrderInfo *)orderInfo
{
    if (orderInfo.customerNum == 0) {
        [SVProgressHUD showSuccessWithStatus:@"还没有乘客和您拼车呢" duration:2];
        return;
    }
    Drv_longOrderPreformViewController * controller = (Drv_longOrderPreformViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"LongOrderPreform"];
    controller.mOrderInfo = orderInfo;
    SHOW_VIEW(controller);
}


- (IBAction)onClickedRadio1:(id)sender
{
    [_radio1 setSelected:YES];
    [_radio2 setSelected:NO];
    [_radio3 setSelected:NO];
    [_radio4 setSelected:NO];
    [_radio5 setSelected:NO];
    [self.mOrderArray removeAllObjects];
    if(mCurOrderType != 1)
    {
        mCurOrderType = 1;
        mCurLimitTime = @"";
        [self reloadOrdersFromService];
    }
}

- (IBAction)onClickedRadio2:(id)sender
{
    [_radio1 setSelected:NO];
    [_radio2 setSelected:YES];
    [_radio3 setSelected:NO];
    [_radio4 setSelected:NO];
    [_radio5 setSelected:NO];
     [self.mOrderArray removeAllObjects];
    if(mCurOrderType != 2)
    {
        mCurOrderType = 2;
        mCurLimitTime = @"";
        [self reloadOrdersFromService];
    }
}

- (IBAction)onClickedRadio3:(id)sender
{
    [_radio1 setSelected:NO];
    [_radio2 setSelected:NO];
    [_radio3 setSelected:YES];
    [_radio4 setSelected:NO];
    [_radio5 setSelected:NO];
     [self.mOrderArray removeAllObjects];
    if(mCurOrderType != 3)
    {
        mCurOrderType = 3;
        mCurLimitTime = @"";
        [self reloadOrdersFromService];
    }
}

- (IBAction)onClickedRadio4:(id)sender
{
    [_radio1 setSelected:NO];
    [_radio2 setSelected:NO];
    [_radio3 setSelected:NO];
    [_radio4 setSelected:YES];
    [_radio5 setSelected:NO];
     [self.mOrderArray removeAllObjects];
    if(mCurOrderType != 4)
    {
        mCurOrderType = 4;
        mCurLimitTime = @"";
        [self reloadOrdersFromService];
    }
}

- (IBAction)onClickedRadio5:(id)sender
{
    [_radio1 setSelected:NO];
    [_radio2 setSelected:NO];
    [_radio3 setSelected:NO];
    [_radio4 setSelected:NO];
    [_radio5 setSelected:YES];
     [self.mOrderArray removeAllObjects];
    if(mCurOrderType != 5)
    {
        mCurOrderType = 5;
        mCurLimitTime = @"";
        [self reloadOrdersFromService];
    }
}



- (void)reloadOrdersFromService
{
	TEST_NETWORK_RETURN

	[SVProgressHUD show];
	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
	[[[CommManager getCommMgr] orderSvcMgr] GetPagedDriverOrders:[Common getUserID] OrderType:mCurOrderType LimitTime:mCurLimitTime DevToken:[Common getDeviceMacAddress]];
}


// ******************* OrderSvcDelegate callback ****************************************
- (void)getPagedDriverOrdersResult : (NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        if(self.HeaderRefrsh ==YES)
        {
            //删除所有数据
            [self.mOrderArray removeAllObjects];
        }

		if ([dataList count] > 0) {
            [self.mOrderArray addObjectsFromArray:dataList];
            STOrderInfo *lastOrder = [self.mOrderArray objectAtIndex:[self.mOrderArray count] - 1];
            mCurLimitTime = lastOrder.create_time;
        }

		if (_mOrderArray.count == 0) {
			_lblNoOrder.hidden = NO;
		} else {
			_lblNoOrder.hidden = YES;
		}

		[SVProgressHUD dismiss];
        self.HeaderRefrsh=NO;
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    [_tableView reloadData];
    [_tableView headerEndRefreshing];
    [_tableView footerEndRefreshing];
}

- (void) getLatestDriverOrdersResult : (NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        for (STOrderInfo *orderInfo in dataList)
        {
            [self.mOrderArray insertObject:orderInfo atIndex:0];
        }


		if (dataList.count == 0) {
			_lblNoOrder.hidden = NO;
		} else {
			_lblNoOrder.hidden = YES;
		}

        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    [_tableView reloadData];
    
    [_tableView headerEndRefreshing];
    [_tableView footerEndRefreshing];
}

- (void) executeOnceOrderResult:(NSString *)result
{
	Drv_OrderPerformViewController *controller = (Drv_OrderPerformViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"orderperform"];
	controller.mOrderId = mSelectedOrderInfo.uid;
	controller.mOrderType = mSelectedOrderInfo.type;
	
    SHOW_VIEW(controller);
}

- (void) executeOnOffOrderResult:(NSString *)result
{
	Drv_OrderPerformViewController *controller = (Drv_OrderPerformViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"orderperform"];
	controller.mOrderId = mSelectedOrderInfo.uid;
	controller.mOrderType = mSelectedOrderInfo.type;
	
    SHOW_VIEW(controller);
}



#pragma EvalPassengerDelegate Methods
- (void) submitComment : (NSString *)message level:(int)level passInfo:(STPassengerInfo *)passInfo
{
	[SVProgressHUD show];
    [[CommManager getCommMgr] orderExecuteSvcMgr].delegate =self;
	[[[CommManager getCommMgr] orderExecuteSvcMgr] EvaluateOnceOrderPass:[Common getUserID] PassId:passInfo.uid OrderId:mSelectedOrderInfo.uid Level:level Msg:message DevToken:[Common getDeviceMacAddress]];
}

- (void)evaluateOnceOrderPassResult:(NSString *)result Level:(int)level
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [self.mOrderArray removeAllObjects];
        mCurOrderType = 2;
        mCurLimitTime = @"";
        [self reloadOrdersFromService];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
	if (alertView.tag == EXECUTE_ALERT_TAG)
	{
		if (buttonIndex == 1)
		{
			[SVProgressHUD show];

			[[CommManager getCommMgr] orderExecuteSvcMgr].delegate = self;
			if(mSelectedOrderInfo.type == TYPE_SINGLE_ORDER) {
				[[[CommManager getCommMgr] orderExecuteSvcMgr] ExecuteOnceOrder:[Common getUserID] OrderId:mSelectedOrderInfo.uid DevToken:[Common getDeviceMacAddress]];
			} else if(mSelectedOrderInfo.type == TYPE_WORK_ORDER) {
				[[[CommManager getCommMgr] orderExecuteSvcMgr] ExecuteOnOffOrder:[Common getUserID] OrderId:mSelectedOrderInfo.uid DevToken:[Common getDeviceMacAddress]];
			}
		}
	}
}


@end












