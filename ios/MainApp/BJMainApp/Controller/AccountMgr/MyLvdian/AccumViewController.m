//
//  AccumViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "AccumViewController.h"
#import "AccumHistoryTableViewCell.h"
#import "ChargeViewController.h"
#import "WithdrawViewController.h"
#import "MJRefresh.h"

@interface AccumViewController ()

@end


#define ACCUMCELL_HEIGHT               45



@implementation AccumViewController

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
    [self callGetTsLogsPage:mCurPageNum];
}
/**
 *  页面出现 刷新数据  获取最新的数据
 *
 *  @param animated <#animated description#>
 */
- (void)viewWillAppear:(BOOL)animated
{
   
     STAccumHisInfo *leatest = [mAccumHisArray firstObject];
    [self callGetLatestTsLogs:leatest.uid];

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
    mCurPageNum = 0;
    mAccumHisArray = [[NSMutableArray alloc] init];

    // set pull to refresh table
    [self setupRefresh];
}


/**
 *  集成刷新控件
 */
- (void)setupRefresh
{
    // 1.下拉刷新(进入刷新状态就会调用self的headerRereshing)
    [_mainTable addHeaderWithTarget:self action:@selector(headerRefreshing)];
#warning 自动刷新(一进入程序就下拉刷新)
    //[_tableView headerBeginRefreshing];
    
    // 2.上拉加载更多(进入刷新状态就会调用self的footerRereshing)
    [_mainTable addFooterWithTarget:self action:@selector(footerRefreshing)];
    
    // 设置文字(也可以不设置,默认的文字在MJRefreshConst中修改)
    _mainTable.headerPullToRefreshText = MSG_TBLHEADER_PULL;
    _mainTable.headerReleaseToRefreshText = MSG_TBLHEADER_RELEASE;
    _mainTable.headerRefreshingText = MSG_TBLHEADER_REFRESHING;
    
    _mainTable.footerPullToRefreshText = MSG_TBLFOOTER_PULL;
    _mainTable.footerReleaseToRefreshText = MSG_TBLFOOTER_RELEASE;
    _mainTable.footerRefreshingText = MSG_TBLFOOTER_REFRESHING;
}


#pragma mark header刷新方法 车主获取最新订单
- (void)headerRefreshing
{
    if ((mAccumHisArray == nil) || ([mAccumHisArray count] == 0))
    {
        mCurPageNum = 0;
        [self callGetTsLogsPage:mCurPageNum];
    }
    else
    {
        [self callGetLatestTsLogs:[[mAccumHisArray objectAtIndex:0] uid]];
    }
}

- (void)footerRefreshing
{
    mCurPageNum++;
    [self callGetTsLogsPage:mCurPageNum];
}


/**
 * update ui for UITableView ( get new data list )
 */
- (void) updateUI
{
    [_lblLeftAccum setText:[NSString stringWithFormat:@"%.02f", mLeftMoney]];
    
    [_mainTable headerEndRefreshing];
    [_mainTable footerEndRefreshing];
    [_mainTable reloadData];
}

- (void) addOldLogs : (NSMutableArray *)tempArray
{
    BOOL hasUpdate = NO;
    
    for (int i = 0; i < [tempArray count]; i++)
    {
        STAccumHisInfo * newInfo = [tempArray objectAtIndex:i];
        
        BOOL isExist = NO;
        for (int j = 0; j < [mAccumHisArray count]; j++)
        {
            STAccumHisInfo * oldInfo = [mAccumHisArray objectAtIndex:j];
            if (oldInfo.uid == newInfo.uid)
            {
                isExist = YES;
                break;
            }
        }
        
        if (isExist)    continue;
        
        [mAccumHisArray addObject:newInfo];
        hasUpdate = YES;
    }
    
    if (hasUpdate) {
        mCurPageNum = [mAccumHisArray count] / DEF_PAGECOUNT;
    }
    
    [self updateUI];
}

- (void) insertLatestLogs : (NSMutableArray *)tempArray
{
    BOOL hasUpdate = NO;
    
    for (int i = 0; i < [tempArray count]; i++)
    {
        STAccumHisInfo * newInfo = [tempArray objectAtIndex:i];
        
        BOOL isExist = NO;
        for (int j = 0; j < [mAccumHisArray count]; j++)
        {
            STAccumHisInfo * oldInfo = [mAccumHisArray objectAtIndex:j];
            if (oldInfo.uid == newInfo.uid)
            {
                isExist = YES;
                break;
            }
        }
        
        if (isExist)    continue;
        
        [mAccumHisArray insertObject:newInfo atIndex:0];
        hasUpdate = YES;
    }
    
    if (hasUpdate) {
        mCurPageNum = [mAccumHisArray count] / DEF_PAGECOUNT;
    }
    
    [self updateUI];
}


//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/**
 * call get the latest trade history list ( by limit id)
 * @param : limitid [in], the last first id ( used for refreshing )
 */
- (void) callGetLatestTsLogs : (long)limitid
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] tradeSvcMgr].delegate = self;
    [[[CommManager getCommMgr] tradeSvcMgr] GetLatestTsLogs:[Common getUserID] limitid:limitid devtoken:[Common getDeviceMacAddress]];
}

- (void) getLatestTsLogsResult:(NSString *)result money:(double)money dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        mLeftMoney = money;
        
        // refresh table
        [self insertLatestLogs:dataList];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}


/**
 * call get the trade history list ( by page no )
 * @param : pangeno [in], current page number ( used to get next data list  )
 */
- (void) callGetTsLogsPage : (int)pageno
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] tradeSvcMgr].delegate = self;
    [[[CommManager getCommMgr] tradeSvcMgr] GetTsLogsPage:[Common getUserID] pageno:pageno devtoken:[Common getDeviceMacAddress]];
}

- (void) getTsLogsPageResult:(NSString *)result money:(double)money dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        mLeftMoney = money;
        
        // refresh table
        [self addOldLogs:dataList];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}



///////////////////////////////////////////////////////////////////////////
#pragma mark - delegate event of main table view
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (mAccumHisArray == nil)
        return 0;
    
    return [mAccumHisArray count];
}

- (float)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
	//return height1;
	return ACCUMCELL_HEIGHT;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (mAccumHisArray == nil)
        return nil;
    
	STAccumHisInfo *data = (STAccumHisInfo *)[mAccumHisArray objectAtIndex:indexPath.row];
    AccumHistoryTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"accumcell"];
    if (cell == nil)
	{
		cell = (AccumHistoryTableViewCell *)[AccumHistoryTableViewCell cellFromNibNamed:@"AccumHistoryTableViewCell"];
	}
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
	[cell initWithData:(id)data reuseIdentifier:@"accumcell" parent:self];
	
	return cell;
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
 * Chongzhi menu button clicked event implementation
 */
- (IBAction)onClickedMenuCharge:(id)sender
{
//    UIViewController * presentViewCtrl = self.presentingViewController;
//    [self dismissViewControllerAnimated:NO completion:^
//     {
//         UIViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"Charge"];
//         [presentViewCtrl presentViewController:ctrl animated:NO completion:nil];
//     }];
    UIViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"Charge"];
    [self presentViewController:ctrl animated:NO completion:nil];
}

/**
 * Tixian menu button clicked event implementation
 */
- (IBAction)onClickedMenuWithdraw:(id)sender
{
//    UIViewController * presentViewCtrl = self.presentingViewController;
//    [self dismissViewControllerAnimated:NO completion:^
//     {
//         UIViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"Withdraw"];
//         [presentViewCtrl presentViewController:ctrl animated:NO completion:nil];
//     }];
    
    UIViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"Withdraw"];
    [self presentViewController:ctrl animated:NO completion:nil];
}
@end
