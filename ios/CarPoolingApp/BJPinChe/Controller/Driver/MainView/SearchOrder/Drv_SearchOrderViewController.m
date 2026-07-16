//
//  Drv_SearchOrderViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/27/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//车主 查询市内订单

#import "Drv_SearchOrderViewController.h"
#import "Drv_SingleTimeOrderCell.h"
//#import "Drv_WorkTimeOrderCell.h"
#import "MJRefresh.h"
#import "SoundTools.h"
#import "Drv_SuccessOrderViewController.h"
#import "ResultDlgView.h"
#import "InsuranceModal.h"
#import "UIViewController+CWPopup.h"

@interface Drv_SearchOrderViewController ()<Drv_SingleDelegate,insuranceFeeDelegate>
@property(nonatomic,strong)NSTimer *mSearchTimer;
@end


#define TABCOUNT                            1
#define SINGLE_TIME_ORDER_CELLID            @"SingleTimeOrderCell"
#define WORK_TIME_ORDER_CELLID              @"WorkTimeOrderCell"
#define ORDERCELL_HEIGHT                    155

@implementation Drv_SearchOrderViewController

- (NSMutableArray *)mSTOrderArray
{
	if(_mSTOrderArray ==nil)
	{
		_mSTOrderArray =[[NSMutableArray alloc]init];
	}

	return _mSTOrderArray;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)loadView
{
    [super loadView];
    self.hidesBottomBarWhenPushed =YES;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self initControls];
    
    mWaitCount = 0;
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(applicationDidEnterBackground:) name:UIApplicationDidEnterBackgroundNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(applicationDidBecomeActive:) name:UIApplicationDidBecomeActiveNotification object:nil];
    UIScreen *screen = [UIScreen mainScreen];
    if (screen.bounds.size.height <500) {
        [_tblSingleTimeOrder setFrame:CGRectMake(_tblSingleTimeOrder.frame.origin.x, _tblSingleTimeOrder.frame.origin.y, _tblSingleTimeOrder.frame.size.width, _tblSingleTimeOrder.frame.size.height -88)];
    }
}

-(void)applicationDidEnterBackground:(NSNotificationCenter *)notication{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:@"NO" forKey:@"SingleOrder"];
    
    [defaults setObject:[self timerNow] forKey:@"timerNow"];
    
    [defaults synchronize];
    if (mWaitAgreeTimer !=nil) {
        [mWaitAgreeTimer invalidate];
        mWaitAgreeTimer = nil;
    }
}

-(void)applicationDidBecomeActive:(NSNotificationCenter *)notication{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
//    [defaults setObject:@"YES" forKey:@"SingleOrder"];
    if ([[defaults objectForKey:@"SingleOrder"] isEqualToString:@"NO"]) {
        NSString *str = [self timerNow];
        NSString *strOld = [defaults objectForKey:@"timerNow"];
        NSString *timerStr = [self shijian:str oldTimer:strOld];
        if (![timerStr isEqualToString:@"NO"]) {
            if (mWaitCount - [timerStr intValue] >0) {
                mWaitCount -=[timerStr intValue];
                [[[CommManager getCommMgr] orderSvcMgr] CheckOnceOrderAgree:[Common getUserID] OrderId:mOrderInfo.uid DevToken:[Common getDeviceMacAddress]];
                
                NSString *timeTag = [NSString stringWithFormat:@"%d",mWaitCount];
                //    [SVProgressHUD showWithStatus:timeTag];
                [SVProgressHUD showWithStatus:timeTag maskType:SVProgressHUDMaskTypeClear];
                if (mWaitAgreeTimer !=nil) {
                    [mWaitAgreeTimer invalidate];
                    mWaitAgreeTimer = nil;
                }

                mWaitAgreeTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(countDownFunc:) userInfo:nil repeats:YES];
            }
        }
    }
}

-(NSString *)shijian:(NSString *)newTimer oldTimer:(NSString *)oldTimer
{
    NSArray *array=[oldTimer componentsSeparatedByString:@":"];
    NSArray *array1=[newTimer componentsSeparatedByString:@":"];
    int time1=[[array objectAtIndex:0] integerValue];
    int time2=[[array objectAtIndex:1] integerValue];
    int time3=[[array objectAtIndex:2] integerValue];

	int time4=[[array1 objectAtIndex:0] integerValue];
    int time5=[[array1 objectAtIndex:1] integerValue];
    int time6=[[array1 objectAtIndex:2] integerValue];
    
    int time;
    if ((time6 - time3) <0) {
        time = time6 - time3 +60;
        time5--;
    }else
        time = time6 -time3;
    if (time >=60) {
        time -=60;
        time5 ++;
    }
    if ((time5 - time2) <0) {
        time +=(time5 - time2 +60)*60;
        time4 --;
    }else
        time +=(time5 - time2)*60;

	if ((time4 - time1) != 0 && (time5 - time2 +60) >=60) {
        return @"NO";
    }

    NSString *str =[NSString stringWithFormat:@"%d",time];
    return str;
}

-(NSString *)timerNow
{
    NSDateFormatter *datePickerFormat = [[NSDateFormatter alloc] init];
    [datePickerFormat setDateFormat:@"HH:mm:ss"];
    NSString *str=[datePickerFormat stringFromDate:[[NSDate date] dateByAddingTimeInterval:(0 * 60)]];
    return str;
}

- (void)viewWillAppear:(BOOL)animated
{
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    
    self.mSearchTimer = [NSTimer scheduledTimerWithTimeInterval:20 target:self selector:@selector(headerRefreshingSTime) userInfo:nil repeats:YES];
    [[NSRunLoop currentRunLoop] addTimer:self.mSearchTimer forMode:NSDefaultRunLoopMode];
    [self.mSearchTimer fire];
    
    mSingleCurPageNo = 0;
    [self footerRefreshingSTime];
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
#pragma mark - Basic Function

/**
 * Initilaize all ui controls & member variable
 */
- (void) initControls
{
    mCurTab = 0;
 //   mSTOrderArray = [[NSMutableArray alloc] init];
  //  mWTOrderArray = [[NSMutableArray alloc] init];
    
    mSingleCurOrderType = 1;
    mSingleCurPageNo = 0;
    mSingleLastOrderID = 0;
//    mWorkCurStartAddr= @"";
//    mWorkCurEndAddr = @"";
//    mWorkCurPageNo = 0;
//    mWorklastOrderID = 0;
    
    // initialize news view frame
    CGRect rcFrame = _vwSingleTimeOrder.frame;
	rcFrame = CGRectMake(_vwSingleTimeOrder.frame.size.width, rcFrame.origin.y, rcFrame.size.width, rcFrame.size.height);
	_vwWorkTimeOrder.frame = rcFrame;
    
    // initialize main scroll content size
    [_scrollMain setContentSize:CGSizeMake(_vwSingleTimeOrder.frame.size.width * TABCOUNT, _vwSingleTimeOrder.frame.size.height)];
    
    [self setupRefresh];


	TEST_NETWORK_RETURN
	
	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
	
	[SVProgressHUD show];
	[[[CommManager getCommMgr] orderSvcMgr] GetPagedAcceptableOnceOrders:[Common getUserID] PageNo:mSingleCurPageNo OrderType:mSingleCurOrderType DevToken:[Common getDeviceMacAddress]];
//	[[[CommManager getCommMgr] orderSvcMgr] GetPagedAcceptableOnOffOrders:[Common getUserID] PageNo:mWorkCurPageNo StartAddr:mWorkCurStartAddr EndAddr:mWorkCurEndAddr DevToken:[Common getDeviceMacAddress]];  上下班删除
    
    [[[CommManager getCommMgr] orderSvcMgr] insertDriverAcceptableOrders:[Common getUserID] DevToken:[Common getDeviceMacAddress]];
  
}


-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [self.mSearchTimer invalidate];
}

/**
 *  集成刷新控件
 */
- (void)setupRefresh
{
    // 1.下拉刷新(进入刷新状态就会调用self的headerRereshing)
    [_tblSingleTimeOrder addHeaderWithTarget:self action:@selector(headerRefreshingSTime)];
#warning 自动刷新(一进入程序就下拉刷新)
    //[_tableView headerBeginRefreshing];
    
    // 2.上拉加载更多(进入刷新状态就会调用self的footerRereshing)
    [_tblSingleTimeOrder addFooterWithTarget:self action:@selector(footerRefreshingSTime)];
    
    // 设置文字(也可以不设置,默认的文字在MJRefreshConst中修改)
    _tblSingleTimeOrder.headerPullToRefreshText = MSG_TBLHEADER_PULL;
    _tblSingleTimeOrder.headerReleaseToRefreshText = MSG_TBLHEADER_RELEASE;
    _tblSingleTimeOrder.headerRefreshingText = MSG_TBLHEADER_REFRESHING;
    
    _tblSingleTimeOrder.footerPullToRefreshText = MSG_TBLFOOTER_PULL;
    _tblSingleTimeOrder.footerReleaseToRefreshText = MSG_TBLFOOTER_RELEASE;
    _tblSingleTimeOrder.footerRefreshingText = MSG_TBLFOOTER_REFRESHING;
    
    // 1.下拉刷新(进入刷新状态就会调用self的headerRereshing)
    [_tblWorkTimeOrder addHeaderWithTarget:self action:@selector(headerRefreshingWTime)];
#warning 自动刷新(一进入程序就下拉刷新)
    //[_tableView headerBeginRefreshing];
    
    // 2.上拉加载更多(进入刷新状态就会调用self的footerRereshing)
    [_tblWorkTimeOrder addFooterWithTarget:self action:@selector(footerRefreshingWTime)];
    
    // 设置文字(也可以不设置,默认的文字在MJRefreshConst中修改)
    _tblWorkTimeOrder.headerPullToRefreshText = MSG_TBLHEADER_PULL;
    _tblWorkTimeOrder.headerReleaseToRefreshText = MSG_TBLHEADER_RELEASE;
    _tblWorkTimeOrder.headerRefreshingText = MSG_TBLHEADER_REFRESHING;
    
    _tblWorkTimeOrder.footerPullToRefreshText = MSG_TBLFOOTER_PULL;
    _tblWorkTimeOrder.footerReleaseToRefreshText = MSG_TBLFOOTER_RELEASE;
    _tblWorkTimeOrder.footerRefreshingText = MSG_TBLFOOTER_REFRESHING;
}


#pragma mark Single time order table refreshing

- (void)headerRefreshingSTime
{
   // mSingleLastOrderID = 0;

	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetLatestAcceptableOnceOrders:[Common getUserID] LimitId:mSingleLastOrderID OrderType:mSingleCurOrderType DevToken:[Common getDeviceMacAddress]];
}

- (void)footerRefreshingSTime
{
	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetPagedAcceptableOnceOrders:[Common getUserID] PageNo:mSingleCurPageNo OrderType:mSingleCurOrderType DevToken:[Common getDeviceMacAddress]];
}

#pragma mark Work time order table refreshing

- (void)headerRefreshingWTime
{
	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
	[[[CommManager getCommMgr] orderSvcMgr] GetLatestAcceptableOnOffOrders:[Common getUserID] LimitId:mWorklastOrderID StartAddr:mWorkCurStartAddr EndAddr:mWorkCurEndAddr DevToken:[Common getDeviceMacAddress]];
}

- (void)footerRefreshingWTime
{
	[[CommManager getCommMgr] orderSvcMgr].delegate = self;
	[[[CommManager getCommMgr] orderSvcMgr] GetPagedAcceptableOnOffOrders:[Common getUserID] PageNo:mWorkCurPageNo StartAddr:mWorkCurStartAddr EndAddr:mWorkCurEndAddr DevToken:[Common getDeviceMacAddress]];
}


- (void) updateUI : (int)index
{
    if (index == 0) {
        [_tblSingleTimeOrder reloadData];
        
        [_tblSingleTimeOrder headerEndRefreshing];
        [_tblSingleTimeOrder footerEndRefreshing];

    }
//    else {
//        [_tblWorkTimeOrder reloadData];
//        
//        [_tblWorkTimeOrder headerEndRefreshing];
//        [_tblWorkTimeOrder footerEndRefreshing];
//    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////
- (STSingleTimeOrderInfo *) createRandomValue
{
    STSingleTimeOrderInfo * oneItem = [[STSingleTimeOrderInfo alloc] init];
    
    oneItem.uid = 100;
    oneItem.startPos = @"三元桥";
    oneItem.endPos = @"西直门";
    oneItem.name = @"张先生";
    oneItem.sex = 1;
    oneItem.age = 35;
    oneItem.price = 100;
    oneItem.start_time = @"6月20日 18:00";
    oneItem.sysinfo_fee_desc = @"平台信息费X点";
    
    return oneItem;
}

- (STWorkTimeOrderInfo *) createRandomValue1
{
    STWorkTimeOrderInfo * oneItem = [[STWorkTimeOrderInfo alloc] init];
    
    oneItem.uid = 100;
    oneItem.startPos = @"三元桥";
    oneItem.endPos = @"西直门";
    oneItem.name = @"张先生";
    oneItem.sex = 1;
    oneItem.age = 35;
    oneItem.price = 100;
    oneItem.start_time = @"6月20日 18:00";
    oneItem.sysinfo_fee_desc = @"平台信息费X点";
    
    return oneItem;
}

-(void) reloadOrdersFromService
{
	TEST_NETWORK_RETURN
	
    if(mCurTab == 0)
    {
        
        [SVProgressHUD show];
        [[CommManager getCommMgr] orderSvcMgr].delegate = self;
        [[[CommManager getCommMgr] orderSvcMgr] GetPagedAcceptableOnceOrders:[Common getUserID] PageNo:mSingleCurPageNo OrderType:mSingleCurOrderType DevToken:[Common getDeviceMacAddress]];
    }
    else
    {
      
        [SVProgressHUD show];
        [[CommManager getCommMgr] orderSvcMgr].delegate = self;
        [[[CommManager getCommMgr] orderSvcMgr] GetPagedAcceptableOnOffOrders:[Common getUserID] PageNo:mWorkCurPageNo StartAddr:mWorkCurStartAddr EndAddr:mWorkCurEndAddr DevToken:[Common getDeviceMacAddress]];
    }
    
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - Scroll Delegate

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if (scrollView == _scrollMain)
	{
		CGPoint ptOffset = scrollView.contentOffset;
		int nIndicatorWidth = _ctrlIndicator.frame.size.width;
		int nLeftMargin = (self.view.frame.size.width - nIndicatorWidth * TABCOUNT) / (TABCOUNT - 1);
		int nPageWidth = _scrollMain.frame.size.width;
        
		CGRect rcIndFrame = _ctrlIndicator.frame;
		
		if (ptOffset.x * nIndicatorWidth / nPageWidth < nIndicatorWidth / 2)
		{
            // change indicator position
            rcIndFrame = CGRectMake(ptOffset.x * nIndicatorWidth / nPageWidth, rcIndFrame.origin.y, rcIndFrame.size.width, rcIndFrame.size.height);
            
			// first tab selected
            mCurTab = 0;
			_lblTitle1.textColor = MYCOLOR_GREEN;
			_lblTitle2.textColor = [UIColor blackColor];
            
        }
		else
		{
            // change indicator position
            rcIndFrame = CGRectMake(nLeftMargin + ptOffset.x * nIndicatorWidth / nPageWidth, rcIndFrame.origin.y, rcIndFrame.size.width, rcIndFrame.size.height);
            
			// second tab selected
            mCurTab = 1;
			_lblTitle1.textColor = [UIColor blackColor];
            _lblTitle2.textColor = MYCOLOR_GREEN;
			
		}
        
        _ctrlIndicator.frame = rcIndFrame;
	}
}


////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Standard TableView delegates

////////////////////////////////////////////////////////////////////////////////////////////////////
- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (tableView == _tblSingleTimeOrder) {
        
        if (self.mSTOrderArray == nil) {
            return 0;
        }
        
        return self.mSTOrderArray.count;
        
    } else if (tableView == _tblWorkTimeOrder) {
        
//        if (mWTOrderArray == nil) {
//            return 0;
       // }
        
       // return mWTOrderArray.count;
    }
    
    return 0;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	//return height1;
	return ORDERCELL_HEIGHT;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *cellIdentifier = @"";
    UITableViewCell *cell = nil;
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    if (tableView == _tblSingleTimeOrder) {
		// single time order cell
		STSingleTimeOrderInfo * dataInfo = [self.mSTOrderArray objectAtIndex:indexPath.row];
		cellIdentifier = SINGLE_TIME_ORDER_CELLID;
		Drv_SingleTimeOrderCell * orderCell = (Drv_SingleTimeOrderCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
		[orderCell initData:dataInfo parent:self];
		orderCell.delegate = self;
		orderCell.indexPathCell = indexPath.row;
		cell = orderCell;
    } else if (tableView == _tblWorkTimeOrder) {
//		work time order cell
//		STWorkTimeOrderInfo * dataInfo = [mWTOrderArray objectAtIndex:indexPath.row];
//		cellIdentifier = WORK_TIME_ORDER_CELLID;
//		Drv_WorkTimeOrderCell * orderCell = (Drv_WorkTimeOrderCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
//		[orderCell initData:dataInfo parent:self];
//		cell = orderCell;
	}

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

/**
 * Clicked first tab ( user info edit tab )
 */
- (IBAction)onClickedView1:(id)sender
{
    [_scrollMain scrollRectToVisible:CGRectMake(0, 0, _vwSingleTimeOrder.frame.size.width, _vwSingleTimeOrder.frame.size.height) animated:YES];
}

/**
 * Clicked second tab ( change password tab )
 */
- (IBAction)onClickedView2:(id)sender
{
    [_scrollMain scrollRectToVisible:CGRectMake(_vwSingleTimeOrder.frame.size.width, 0, _vwSingleTimeOrder.frame.size.width, _vwSingleTimeOrder.frame.size.height) animated:YES];
}

///////////////////////////////////////////////////////////////////////////
// single order control event
- (IBAction)onSetArrange1:(id)sender
{
    [_radioST1 setSelected:YES];
    [_radioST2 setSelected:NO];
    [_radioST3 setSelected:NO];
    
    if(mSingleCurOrderType != 1)
    {
        mSingleCurOrderType = 1;
        mSingleCurPageNo = 0;
        [self.mSTOrderArray removeAllObjects];
        [self reloadOrdersFromService];
    }
}

- (IBAction)onSetArrange2:(id)sender
{
    [_radioST1 setSelected:NO];
    [_radioST2 setSelected:YES];
    [_radioST3 setSelected:NO];
    
    if(mSingleCurOrderType != 2)
    {
        mSingleCurOrderType = 2;
        mSingleCurPageNo = 0;
        [self.mSTOrderArray removeAllObjects];

        [self reloadOrdersFromService];
    }
}

- (IBAction)onSetArrange3:(id)sender
{
    [_radioST1 setSelected:NO];
    [_radioST2 setSelected:NO];
    [_radioST3 setSelected:YES];
    
    if(mSingleCurOrderType != 3)
    {
        mSingleCurOrderType = 3;
        mSingleCurPageNo = 0;
        [self.mSTOrderArray removeAllObjects];

        [self reloadOrdersFromService];
    }
}


#pragma OrderSvcMgr Delegate Methods
-(void)getLatestAcceptableOnceOrdersResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
       // [self.mSTOrderArray removeAllObjects];
        for (STSingleTimeOrderInfo *orderInfo in dataList)
        {
			BOOL isExist = NO;
			for (STSingleTimeOrderInfo* old_order in self.mSTOrderArray)
			{
				if (old_order.uid == orderInfo.uid)
				{
					isExist = YES;
					break;
				}
			}

			if (!isExist)
	            [self.mSTOrderArray insertObject:orderInfo atIndex:0];
        }
		
		if([dataList count] > 0)
		{
			STSingleTimeOrderInfo *lastOrder = [dataList objectAtIndex:0];
            mSingleLastOrderID = lastOrder.uid;
            [[SoundTools sharedSoundTools]playSoundWithFileName:@"5103.wav"];
		}
        
        [_tblSingleTimeOrder reloadData];
//        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
    [_tblSingleTimeOrder headerEndRefreshing];
    [_tblSingleTimeOrder footerEndRefreshing];
    
}

-(void)getPagedAcceptableOnceOrdersResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        if(mSingleCurPageNo == 0)
        {
            [self.mSTOrderArray removeAllObjects];
            
            NSMutableArray *resultData = [[NSMutableArray alloc]init];
            NSArray *paths=NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
            NSString *path=[paths objectAtIndex:0];
            NSString *filename=[path stringByAppendingPathComponent:@"orderdes.plist"];
            
            //读文件
            NSDictionary* dic2 = [NSDictionary dictionaryWithContentsOfFile:filename];
            resultData = [dic2 objectForKey:@"order"];
            
            for (NSDictionary *orderInfo in resultData)
            {
                STSingleTimeOrderInfo *stOrder = [[STSingleTimeOrderInfo alloc] init];
                stOrder.uid = [[orderInfo objectForKey:@"uid"] longValue];
                stOrder.order_num = [orderInfo objectForKey:@"order_num"];
                stOrder.pass_id = [[orderInfo objectForKey:@"pass_id"] intValue];
                stOrder.image = [orderInfo objectForKey:@"pass_img"];
                stOrder.name = [orderInfo objectForKey:@"pass_name"];
                stOrder.sex = [[orderInfo objectForKey:@"pass_gender"] intValue];
                stOrder.age = [[orderInfo objectForKey:@"pass_age"] intValue];
                stOrder.price = [[orderInfo objectForKey:@"price"] doubleValue];
                stOrder.sysinfo_fee = [[orderInfo objectForKey:@"sysinfo_fee"] doubleValue];
                stOrder.sysinfo_fee_desc = [orderInfo objectForKey:@"sysinfo_fee_desc"];
                stOrder.insun_fee =[[orderInfo objectForKey:@"insun_fee"]doubleValue];
                stOrder.startPos = [orderInfo objectForKey:@"start_addr"];
                stOrder.endPos = [orderInfo objectForKey:@"end_addr"];
                stOrder.distance = [[orderInfo objectForKey:@"distance"] doubleValue];
                stOrder.distance_desc = [orderInfo objectForKey:@"distance_desc"];
                stOrder.midpoints = [[orderInfo objectForKey:@"midpoints"] intValue];
                stOrder.midpoints_desc = [orderInfo objectForKey:@"midpoints_desc"];
                stOrder.start_time = [orderInfo objectForKey:@"start_time"];
                stOrder.create_time = [orderInfo objectForKey:@"create_time"];
                stOrder.status = [orderInfo objectForKey:@"status"];
                stOrder.mileage = [[orderInfo objectForKey:@"mileage"] doubleValue];
//                [dataList addObject:stOrder];
                [self.mSTOrderArray addObject:stOrder];
            }
        }
        
		for (STSingleTimeOrderInfo *orderInfo in dataList)
		{
			BOOL isExist = NO;
			for (STSingleTimeOrderInfo* old_order in self.mSTOrderArray)
			{
				if (old_order.uid == orderInfo.uid)
				{
					isExist = YES;
					break;
				}
			}
			
			if (!isExist)
				[self.mSTOrderArray addObject:orderInfo];
		}
        NSArray *sortDescriptors = [NSArray arrayWithObject:[NSSortDescriptor sortDescriptorWithKey:@"status" ascending:YES]];
        [self.mSTOrderArray sortUsingDescriptors:sortDescriptors];
        

		if ([dataList count] > 0)
		{
			mSingleCurPageNo ++;
            [[SoundTools sharedSoundTools]playSoundWithFileName:@"5103.wav"];
        }
		
        [_tblSingleTimeOrder reloadData];
        
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
    [_tblSingleTimeOrder headerEndRefreshing];
    [_tblSingleTimeOrder footerEndRefreshing];
}

-(void)getLatestAcceptableOnOffOrdersResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        for (STWorkTimeOrderInfo *orderInfo in dataList)
        {
           // [self.mWTOrderArray insertObject:orderInfo atIndex:0];
        }
		
		if([dataList count] > 0)
		{
			STWorkTimeOrderInfo *lastOrder = [dataList objectAtIndex:0];
            mWorklastOrderID = lastOrder.uid;
		}
        
        [_tblWorkTimeOrder reloadData];
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
    [_tblWorkTimeOrder headerEndRefreshing];
    [_tblWorkTimeOrder footerEndRefreshing];
}

-(void)getPagedAcceptableOnOffOrdersResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        if([dataList count] > 0) {
       //     [mWTOrderArray addObjectsFromArray:dataList];
			mWorkCurPageNo ++;
        }
        
        [_tblWorkTimeOrder reloadData];
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
    [_tblWorkTimeOrder headerEndRefreshing];
    [_tblWorkTimeOrder footerEndRefreshing];
}
#pragma mark 抢单回调
-(void)drv_singleCell:(int)indexPath
{
    NSLog(@"--%d",indexPath);
    mOrderInfo = [self.mSTOrderArray objectAtIndex:indexPath];
    
	TEST_NETWORK_RETURN

	[SVProgressHUD showWithStatus:@"乘客正在确认，请稍候" maskType:SVProgressHUDMaskTypeClear];
    
    if ([mOrderInfo.status isEqualToString:@"2"]) {
        NSMutableArray *resultData = [[NSMutableArray alloc]init];
        NSArray *paths=NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
        NSString *path=[paths objectAtIndex:0];
        NSString *filename=[path stringByAppendingPathComponent:@"orderdes.plist"];
        
        //读文件
        NSDictionary* dic2 = [NSDictionary dictionaryWithContentsOfFile:filename];
        resultData = [dic2 objectForKey:@"order"];
        for (int i= 0; i<resultData.count; i++) {
            NSDictionary *dic =[resultData objectAtIndex:i];
            if ([mOrderInfo.start_time isEqualToString:[dic objectForKey:@"start_time"]]) {
                [dic setValue:@"3" forKey:@"status"];
            }
        }
        NSDictionary *dic =[NSDictionary dictionaryWithObject:resultData forKey:@"order"];
        [dic writeToFile:filename atomically:YES];
        
        [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(cancelOrder) userInfo:nil repeats:NO];
        
        return;
    }
    
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] acceptOnceOrder:[Common getUserID] OrderID:mOrderInfo.uid Latitude:[Common getCurrentLatitude] Longitude:[Common getCurrentLongitude] DevToken:[Common getDeviceMacAddress]];
}
-(void)cancelOrder
{
    mSingleCurPageNo = 0;
    [self footerRefreshingSTime];
    [SVProgressHUD dismiss];
    [ResultDlgView showInController:self.navigationController success:NO message:@"手太慢了！"];
}


#pragma OrderSvcMgr Delegate Methods
#pragma mark  ===== 点击抢单回调 ====
- (void) acceptOnceOrderResult:(int)result retmsg:(NSString *)retmsg wait_time:(int)wait_time
{
	TEST_NETWORK_RETURN

	if (result == SVCERR_SUCCESS)
	{
		[[[CommManager getCommMgr] orderSvcMgr] CheckOnceOrderAgree:[Common getUserID] OrderId:mOrderInfo.uid DevToken:[Common getDeviceMacAddress]];
		mWaitCount = wait_time;

		NSString *timeTag = [NSString stringWithFormat:@"%d",mWaitCount];

        tishiString = @"拼友确认中，请耐心等待";
        [SVProgressHUD showWithStatusGra:timeTag maskType:SVProgressHUDMaskTypeClear tishi:tishiString];

		mWaitAgreeTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(countDownFunc:) userInfo:nil repeats:YES];

	}
    else if (result == SVCERR_ALREADY_CANCELLED)
    {
        if (mWaitAgreeTimer !=nil) {
            [mWaitAgreeTimer invalidate];
            mWaitAgreeTimer = nil;
        }
        [SVProgressHUD dismiss];
        [ResultDlgView showInController:self.navigationController success:NO message:@"拼友未确认，本订单取消"];
        
    }
	else
	{
		[SVProgressHUD dismiss];
		[ResultDlgView showInController:self.navigationController success:NO message:retmsg];
	}
}
-(void)duplicateUser:(NSString *)result
{
    if (mWaitAgreeTimer !=nil) {
        [mWaitAgreeTimer invalidate];
        mWaitAgreeTimer = nil;
    }
    [SVProgressHUD dismiss];
    [ResultDlgView showInController:self.navigationController success:NO message:@"拼友未确认，本订单取消"];
}
- (void) checkOnceOrderAgreeResult:(NSString *)result PassImg:(NSString *)pass_img PassName:(NSString *)pass_name PassGender:(int)pass_gender PassAge:(int)pass_age StartTime:(NSString *)start_time StartAddr:(NSString *)start_addr EndAddr:(NSString *)end_addr
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		if(mWaitAgreeTimer != nil)
		{
			[mWaitAgreeTimer invalidate];
			mWaitAgreeTimer = nil;
			mWaitCount = 0;
		}

		[SVProgressHUD dismiss];
//		[self.view makeToast:@"抢单成功" duration:2 position:@"center"];

        UIStoryboard *story = [UIStoryboard storyboardWithName:@"DriverStoryboard" bundle:nil];
        Drv_SuccessOrderViewController *viewController = [story instantiateViewControllerWithIdentifier:@"success_order"];
        viewController.orderid = mOrderInfo.uid;
        [self presentViewController:viewController animated:YES completion:nil];
        
//		UIViewController * curCtrl = (UIViewController *)self.presentingViewController;
//
//		[self dismissViewControllerAnimated:NO completion:^{
//			Drv_SuccessOrderViewController *viewController = (Drv_SuccessOrderViewController *)[curCtrl.storyboard instantiateViewControllerWithIdentifier:@"success_order"];
//			viewController.orderid = mOrderInfo.uid;
//
//			CATransition *animation = [CATransition animation]; \
//			[animation setDuration:0.3]; \
//			[animation setType:kCATransitionPush]; \
//			[animation setSubtype:kCATransitionFromRight]; \
//			[animation setTimingFunction:[CAMediaTimingFunction functionWithName: kCAMediaTimingFunctionEaseInEaseOut]]; \
//			[[curCtrl.view.superview layer] addAnimation:animation forKey:@"SwitchToView"];
//
//			[curCtrl presentViewController:viewController animated:NO completion:nil];
//		}];
    }
    else
	{
       
	}
}

-(void)getHas_clickedchargingbtn:(NSString *)result RetData:(NSDictionary *)RetData
{
    if(mWaitAgreeTimer != nil)
    {
        [mWaitAgreeTimer invalidate];
        mWaitAgreeTimer = nil;
        mWaitCount = 0;
    }
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        int timer = [[RetData objectForKey:@"waittime_when_charging"] integerValue];
        if (timer == 0) {
            [SVProgressHUD dismissWithError:@"拼友未确认，本订单取消" afterDelay:DEF_DELAY];
        }else
        {
            mWaitCount = timer;
            NSString *timeTag = [NSString stringWithFormat:@"%d",mWaitCount];
            //    [SVProgressHUD showWithStatus:timeTag];
//            [SVProgressHUD showWithStatus:timeTag maskType:SVProgressHUDMaskTypeClear];
            tishiString = @"拼友正在充值，请耐心等待";
            [SVProgressHUD showWithStatusGra:timeTag maskType:SVProgressHUDMaskTypeClear tishi:tishiString];
            
            mWaitAgreeTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(countDownFunc:) userInfo:nil repeats:YES];
        }
        
    }else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}
- (void) countDownFunc:(NSTimer *)timer
{
    mWaitCount -= 1;
    
	TEST_NETWORK_RETURN

	NSString *timeTag = [NSString stringWithFormat:@"%d", mWaitCount];
    [SVProgressHUD showWithStatusGra:timeTag maskType:SVProgressHUDMaskTypeClear tishi:tishiString];
//    [SVProgressHUD showWithStatus:timeTag maskType:SVProgressHUDMaskTypeClear];
    // stop timer
    if (mWaitCount <= 1) {
        //时间到了判断是否充值
        [[CommManager getCommMgr] orderSvcMgr].delegate = self;
		[[[CommManager getCommMgr] orderSvcMgr] has_clickedchargingbtn:[Common getUserID] OrderID:mOrderInfo.uid DevToken:[Common getDeviceMacAddress]];
        [mWaitAgreeTimer invalidate];
        mWaitAgreeTimer = nil;
    }
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] CheckOnceOrderAgree:[Common getUserID] OrderId:mOrderInfo.uid DevToken:[Common getDeviceMacAddress]];
}


-(void)onClickedPerform:(STOrderInfo *)sender
{
    InsuranceModal *feeView =[[InsuranceModal alloc]initWithNibName:@"InsuranceModal" bundle:nil];
    feeView.morderInfo =sender;
    feeView.delegate =self;
    [self presentPopupViewController:feeView animated:YES completion:nil];
}
- (void)tappedDone
{
    [self dismissPopupViewControllerAnimated:YES completion:nil];
}
@end









