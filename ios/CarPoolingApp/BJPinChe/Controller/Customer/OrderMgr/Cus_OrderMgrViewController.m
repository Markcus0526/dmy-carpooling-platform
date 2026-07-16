//
//  NewsMgrViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/23/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//
/**
 *  乘客身份 我的订单 界面
 */
#import "Cus_OrderMgrViewController.h"
#import "MJRefresh.h"
#import "Cus_OnceOrderDetailVC.h"
#import "Cus_WTOrderDetWaitViewController.h"
#import "Cus_WTOrderDetAcceptViewController.h"
#import "Cus_WTOrderDetMainViewController.h"
#import "UIViewController+CWPopup.h"
#import "Cus_LongOrderDetViewController.h"
@interface Cus_OrderMgrViewController ()
@property(nonatomic,assign)BOOL HeaderRefresh;
@end


#define ORDER_CELLID                        @"CusOrderCell"
#define ORDERCELL_HEIGHT                    150

@implementation Cus_OrderMgrViewController

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
    
    
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self initControls];
}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(NSMutableArray *)mOrderArray
{
	if(_mOrderArray ==nil)
		_mOrderArray =[[NSMutableArray alloc]init];

	return _mOrderArray;
}



///////////////////////////////// Initialize ////////////////////////////////
#pragma mark - Initialize data info
- (void)initControls
{
	mCurLimitTime = @"";
	mCurOrderType = 1;
    
    [_radio2 setSelected:YES];
    [_radio3 setSelected:NO];
    [_radio4 setSelected:NO];
    [_radio5 setSelected:NO];

	lblNoOrder.hidden = YES;

	_tableView.separatorStyle = NO;

	// mOrderArray = [[NSMutableArray alloc] init];
	[self setupRefresh];

	// call service to get data list
	[self footerRereshing];
}



/**
 *  集成刷新控件
 */
- (void)setupRefresh
{
    // 1.下拉刷新(进入刷新状态就会调用self的headerRereshing)
    [_tableView addHeaderWithTarget:self action:@selector(headerRereshing)];
#warning 自动刷新(一进入程序就下拉刷新)
    //[_tableView headerBeginRefreshing];
    
    // 2.上拉加载更多(进入刷新状态就会调用self的footerRereshing)
    [_tableView addFooterWithTarget:self action:@selector(footerRereshing)];
    
    // 设置文字(也可以不设置,默认的文字在MJRefreshConst中修改)
    _tableView.headerPullToRefreshText = MSG_TBLHEADER_PULL;
    _tableView.headerReleaseToRefreshText = MSG_TBLHEADER_RELEASE;
    _tableView.headerRefreshingText = MSG_TBLHEADER_REFRESHING;
    
    _tableView.footerPullToRefreshText = MSG_TBLFOOTER_PULL;
    _tableView.footerReleaseToRefreshText = MSG_TBLFOOTER_RELEASE;
    _tableView.footerRefreshingText = MSG_TBLFOOTER_REFRESHING;
}


#pragma mark 下拉刷新
- (void)headerRereshing
{
	NSString *latestOrderNum = @"";
	int latestOrderType = 1;

	//数组非空
	if([self.mOrderArray count] > 0)
	{
		STOrderInfo *firstOrder = [self.mOrderArray objectAtIndex:0];
		latestOrderNum = firstOrder.order_num;
		MyLog(@"%@",latestOrderNum);
		latestOrderType = firstOrder.type;
	}

    self.HeaderRefresh =YES;

	// mCurLimitTime = @"";

//	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
//    [[[CommManager getCommMgr] orderSvcMgr] GetPagedPassengerOrders:[Common getUserID] OrderType:mCurOrderType LimitTime:@"" DevToken:[Common getDeviceMacAddress]];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetLatestPassengerOrders:[Common getUserID] OrderType:mCurOrderType OrderNum:latestOrderNum LimitedType:latestOrderType DevToken:[Common getDeviceMacAddress]];
    
}


#pragma mark 上拉加载更多
- (void)footerRereshing
{
    self.HeaderRefresh =NO;

	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetPagedPassengerOrders:[Common getUserID] OrderType:mCurOrderType LimitTime:mCurLimitTime DevToken:[Common getDeviceMacAddress]];
}


////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
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
	static NSString *cellIdentifier = ORDER_CELLID;
	Cus_MyOrderCell * orderCell = (Cus_MyOrderCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];

	if (orderCell ==nil)
	{
		orderCell =[[Cus_MyOrderCell alloc]init];
	}

	//UITableViewCell *cell = nil;
	STOrderInfo * dataInfo = [self.mOrderArray objectAtIndex:indexPath.row];

	orderCell.selectionStyle = UITableViewCellSelectionStyleNone;

	[orderCell initData:dataInfo parent:self];
	// cell = orderCell;

	return orderCell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if([self.mOrderArray count] <= indexPath.row)
        return;
    
    STOrderInfo *orderInfo = [self.mOrderArray objectAtIndex:indexPath.row];
    
    switch (orderInfo.type) {
        case TYPE_SINGLE_ORDER:
        {
            Cus_OnceOrderDetailVC *onceOrder =[[Cus_OnceOrderDetailVC alloc]initWithNibName:@"Cus_OnceOrderDetailView" bundle:nil];
            
           // Cus_OrderDetViewController * viewController = (Cus_OrderDetViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"my_st_order_detail"];
            onceOrder.hidesBottomBarWhenPushed =YES;
            onceOrder.mOrderInfo = orderInfo;
            [self.navigationController pushViewController:onceOrder animated:YES];
            
            break;
        }
        case TYPE_WORK_ORDER:
        {
            switch (orderInfo.state) {
                case ORDER_STATE_DRIVER_ACCEPTED:
                {
                    Cus_WTOrderDetAcceptViewController * vcAccept = (Cus_WTOrderDetAcceptViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"my_wt_order_accept"];
                    vcAccept.hidesBottomBarWhenPushed =YES;
                    vcAccept.mOrderInfo = orderInfo;
                   // SHOW_VIEW(vcAccept);
                    [self.navigationController pushViewController:vcAccept animated:YES];
                    break;
                }
                case ORDER_STATE_PUBLISHED:
                {
                    
                    Cus_WTOrderDetWaitViewController * vcWait = (Cus_WTOrderDetWaitViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"my_wt_order_wating"];
                    vcWait.hidesBottomBarWhenPushed =YES;
                    vcWait.mOrderInfo = orderInfo;
                    //SHOW_VIEW(vcWait);
                    [self.navigationController pushViewController:vcWait animated:YES];
                    break;
                }
                    
                case ORDER_STATE_GRABBED:
                case ORDER_STATE_STARTED:
                case ORDER_STATE_DRIVER_ARRIVED:
                case ORDER_STATE_PASSENGER_GETON:
                case ORDER_STATE_FINISHED:
                case ORDER_STATE_PAYED:
                case ORDER_STATE_EVALUATED:
                case ORDER_STATE_CLOSED:
                {
                    Cus_WTOrderDetMainViewController * vcMain = (Cus_WTOrderDetMainViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"my_wt_order_main"];
                    vcMain.hidesBottomBarWhenPushed =YES;
                    vcMain.mOrderInfo = orderInfo;
                   // SHOW_VIEW(vcMain);
                    [self.navigationController pushViewController:vcMain animated:YES];
                    break;
                }
                    
                default:
                    break;
            }
            // show work time order by order state ( three case )
            /*
            Cus_WTOrderDetWaitViewController * viewController = (Cus_WTOrderDetWaitViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"my_wt_order_wating"];
            
            SHOW_VIEW(viewController);
            */
            
            /*
             Cus_WTOrderDetAcceptViewController * viewController = (Cus_WTOrderDetAcceptViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"my_wt_order_accept"];
             
             SHOW_VIEW(viewController);
             */
            
            /*
             Cus_WTOrderDetMainViewController * viewController = (Cus_WTOrderDetMainViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"my_wt_order_main"];
             
             SHOW_VIEW(viewController);
             */
            
            break;
        }
        case TYPE_LONG_ORDER:
        {
            Cus_LongOrderDetViewController * longOrderDetViewController = (Cus_LongOrderDetViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"LongOrderDetail"];
            longOrderDetViewController.hidesBottomBarWhenPushed =YES;
            longOrderDetViewController.mOrderInfo = orderInfo;
           // SHOW_VIEW(longOrderDetViewController);
            [self.navigationController pushViewController:longOrderDetViewController animated:YES];
            break;
        }
        default:
            break;
    }
    
}

///////////////////////////////////////////////////////////////////////////////
#pragma mark - UI User Event


- (void)onPaySuccess:(long)orderid
{
	for (int i = 0; i < self.mOrderArray.count; i++)
	{
		STOrderInfo* orderinfo = [self.mOrderArray objectAtIndex:i];
		if (orderinfo.uid == orderid)
		{
			orderinfo.state = ORDER_STATE_PAYED;
			orderinfo.state_desc = @"已收绿点/待评价";

			Cus_MyOrderCell* cell = (Cus_MyOrderCell*)[_tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:i inSection:0]];
			[cell initData:orderinfo parent:self];

			break;
		}
	}
}


- (void)onClickedPerform:(STOrderInfo *)orderInfo
{
	if(orderInfo.state < ORDER_STATE_PAYED)
	{
		Cus_PayOrderViewController * viewController = (Cus_PayOrderViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"pay_order"];
		viewController.mOrderInfo = orderInfo;
		viewController.payDelegate = self;

		SHOW_VIEW(viewController);
	}
    else if (orderInfo.state == ORDER_STATE_PAYED) {
        // show evaluation dialog
        Cus_EvalDriverViewController *popup = [[Cus_EvalDriverViewController alloc] initWithNibName:@"Cus_EvalDriverViewController" bundle:nil];

		popup.delegate = self;
        [popup setParent:self];
        
        [self presentPopupViewController:popup animated:YES completion:^(void) {
            MyLog(@"popup view presented");
        }];
		
		mTargetOrderInfo = orderInfo;
    }
    if (orderInfo.state == ORDER_STATE_EVALUATED) {
        TEST_NETWORK_RETURN;
        [SVProgressHUD show];
        [[CommManager getCommMgr] orderSvcMgr].delegate = self;
        [[[CommManager getCommMgr] orderSvcMgr] GetDriverLatestEvalInfo:[Common getUserID] DriverId:orderInfo.driver_id LimitId:orderInfo.uid DevToken:[Common getDeviceMacAddress]];

    }
}
/**
 *  从网络获取订单方法
 */
-(void) reloadOrdersFromService
{
	TEST_NETWORK_RETURN;
   // self.mOrderArray = [[NSMutableArray alloc] init];
    [SVProgressHUD show];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetPagedPassengerOrders:[Common getUserID] OrderType:mCurOrderType LimitTime:mCurLimitTime DevToken:[Common getDeviceMacAddress]];
}
/**
 *  顶部菜单唯一选择
 *
 *  @param sender <#sender description#>
 */
- (IBAction)onClickedRadio1:(id)sender
{
    [_radio1 setSelected:YES];
    [_radio2 setSelected:NO];
    [_radio3 setSelected:NO];
    [_radio4 setSelected:NO];
    [_radio5 setSelected:NO];
    
    if(mCurOrderType != 1)
    {
        mCurOrderType = 1;
        mCurLimitTime = @"";
        self.HeaderRefresh =YES;
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
    
    if(mCurOrderType != 1)
    {
        mCurOrderType = 1;
        mCurLimitTime = @"";
        self.HeaderRefresh =YES;
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
    
    if(mCurOrderType != 3)
    {
        mCurOrderType = 3;
        mCurLimitTime = @"";
        self.HeaderRefresh =YES;
        [self reloadOrdersFromService];
    }
}
/*
- (IBAction)onClickedRadio4:(id)sender
{
    [_radio1 setSelected:NO];
    [_radio2 setSelected:NO];
    [_radio3 setSelected:NO];
    [_radio4 setSelected:YES];
    [_radio5 setSelected:NO];
    
    if(mCurOrderType != 4)
    {
        mCurOrderType = 4;
        mCurLimitTime = @"";
        self.HeaderRefresh =YES;
        [self reloadOrdersFromService];
    }
}
*/
- (IBAction)onClickedRadio5:(id)sender
{
	[_radio1 setSelected:NO];
	[_radio2 setSelected:NO];
	[_radio3 setSelected:NO];
	[_radio4 setSelected:NO];
	[_radio5 setSelected:YES];

	if(mCurOrderType != 5)
    {
        mCurOrderType = 5;
        mCurLimitTime = @"";
        self.HeaderRefresh =YES;
        [self reloadOrdersFromService];
    }
}



#pragma OrderSvcMgr Delegate Methods  代理方法获得最新的订单数据
- (void) getLatestPassengerOrdersResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		//新数据插入到最前端 0
		BOOL isExist = NO;
		int i = 0;
		for (STOrderInfo *orderInfo in dataList)
		{
			isExist = NO;
			for (i = 0; i < self.mOrderArray.count; i++)
			{
				STOrderInfo* oldItem = [self.mOrderArray objectAtIndex:i];
				if (oldItem.uid == orderInfo.uid)
				{
					isExist = YES;
					break;
				}
			}

			if (isExist)
				[self.mOrderArray setObject:orderInfo atIndexedSubscript:i];
			else
				[self.mOrderArray insertObject:orderInfo atIndex:0];
		}

		if (self.mOrderArray != nil && self.mOrderArray.count != 0)
			lblNoOrder.hidden = YES;
		else
			lblNoOrder.hidden = NO;

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



#pragma mark 代理方法获得之前未显示的旧数据
- (void) getPagedPassengerOrdersResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        if(self.HeaderRefresh==YES)
        {
            [self.mOrderArray removeAllObjects];
        }

		if ([dataList count] > 0)
		{
			BOOL isExist = NO;
			int i = 0;

			for (STOrderInfo* orderInfo in dataList)
			{
				isExist = NO;
				for (i = 0; i < self.mOrderArray.count; i++)
				{
					STOrderInfo* oldItem = [self.mOrderArray objectAtIndex:i];
					if (oldItem.uid == orderInfo.uid)
					{
						isExist = YES;
						break;
					}
				}

				if (isExist)
					[self.mOrderArray setObject:orderInfo atIndexedSubscript:i];
				else
					[self.mOrderArray addObject:orderInfo];
			}

			STOrderInfo *lastOrder = [self.mOrderArray objectAtIndex:[self.mOrderArray count] - 1];
			mCurLimitTime = lastOrder.create_time;
		}

		self.HeaderRefresh =NO;

		if (self.mOrderArray != nil &&
			self.mOrderArray.count != 0)
		{
			lblNoOrder.hidden = YES;
		}
		else
		{
			lblNoOrder.hidden = NO;
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



#pragma EvalDrvDelegate Methods 点击评价回调
- (void) submitComment : (NSString *)message level:(int)level
{
	TEST_NETWORK_RETURN;
	[SVProgressHUD show];
	if(mTargetOrderInfo.type == TYPE_SINGLE_ORDER)//获取评价内容
	{
        [[CommManager getCommMgr] orderExecuteSvcMgr].delegate =self;
		[[[CommManager getCommMgr] orderExecuteSvcMgr] EvaluateOnceOrderDriver:[Common getUserID] DriverId:mTargetOrderInfo.driver_id OrderId:mTargetOrderInfo.uid Level:level Msg:message DevToken:[Common getDeviceMacAddress]];
	}
	else//评价车主
	{
        [[CommManager getCommMgr] longWayOrderSvcMgr].delegate = self;
        [[[CommManager getCommMgr] longWayOrderSvcMgr] EvaluateLongOrderDriver:[Common getUserID] orderid:mTargetOrderInfo.uid level:level msg:message devToken:[Common getDeviceMacAddress] driverId:mTargetOrderInfo.driver_id];
	}
}

- (void)evaluateOnceOrderDriverResult:(NSString *)result Level:(int)level
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        self.HeaderRefresh =YES;
        
        [[CommManager getCommMgr] orderSvcMgr].delegate = self;
        [[[CommManager getCommMgr] orderSvcMgr] GetPagedPassengerOrders:[Common getUserID] OrderType:mCurOrderType LimitTime:@"" DevToken:[Common getDeviceMacAddress]];
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

//评价回返
-(void)evaluateLongOrderDriver:(NSString *)result
{
    [SVProgressHUD showSuccessWithStatus:result];
    [self footerRereshing];
}
-(void)getDriverLatestEvalInfoResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        if (dataList.count !=0) {
            Cus_EvalDriverLatestViewController *popup = [[Cus_EvalDriverLatestViewController alloc] initWithNibName:@"Cus_EvalDriverLatestViewController" bundle:nil];
            
            [self presentPopupViewController:popup animated:YES completion:^(void) {
                MyLog(@"popup view presented");
            }];
            STDriverEvalInfo *dtivet = [dataList objectAtIndex:0];
            [popup setParent:self message:dtivet.contents level:dtivet.eval];
        }
        
    }else
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    
    
}

@end






