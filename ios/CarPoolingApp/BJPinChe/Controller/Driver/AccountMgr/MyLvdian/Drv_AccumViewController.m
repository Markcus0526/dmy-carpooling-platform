//
//  AccumViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_AccumViewController.h"
#import "Drv_AccumHistoryTableViewCell.h"
#import "Drv_ChargeViewController.h"
#import "Drv_WithdrawViewController.h"
#import "MJRefresh.h"

@interface Drv_AccumViewController ()

@end


#define ACCUMCELL_HEIGHT               45



@implementation Drv_AccumViewController

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
    [self callGetTsLogsPage:mCurPageNum];
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
    mAccumHisArray = [[NSMutableArray alloc] init];
    
    mCurPageNum = 0;
    
    // initialize pull refresh table
    CGRect rt = _vwTableFrame.frame;
    _mainTable = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, rt.size.width, rt.size.height)];
    _mainTable.delegate = self;
    _mainTable.dataSource = self;
    
    [_vwTableFrame addSubview:_mainTable];
    
    [self setupRefresh];
}

- (void)setupRefresh
{
    // 1.下拉刷新(进入刷新状态就会调用self的headerRereshing)
    [_mainTable addHeaderWithTarget:self action:@selector(headerRereshing)];
    
    // 2.上拉加载更多(进入刷新状态就会调用self的footerRereshing)
    [_mainTable addFooterWithTarget:self action:@selector(footerRereshing)];
    
    // 设置文字(也可以不设置,默认的文字在MJRefreshConst中修改)
    _mainTable.headerPullToRefreshText = MSG_TBLHEADER_PULL;
    _mainTable.headerReleaseToRefreshText = MSG_TBLHEADER_RELEASE;
    _mainTable.headerRefreshingText = MSG_TBLHEADER_REFRESHING;
    
    _mainTable.footerPullToRefreshText = MSG_TBLFOOTER_PULL;
    _mainTable.footerReleaseToRefreshText = MSG_TBLFOOTER_RELEASE;
    _mainTable.footerRefreshingText = MSG_TBLFOOTER_REFRESHING;
}



/**
 * update ui for UITableView ( get new data list )
 */
- (void) updateUI
{
    [_lblLeftAccum setText:[NSString stringWithFormat:@"%.02f", mLeftMoney]];
    
    [_mainTable reloadData];
    
    // stop refreshing
    [_mainTable headerEndRefreshing];
    [_mainTable footerEndRefreshing];
}


#pragma mark - Pull refresh event

- (void)headerRereshing
{
    if (mAccumHisArray.count == 0) {
        [self callGetTsLogsPage:mCurPageNum];
    }
    
    STAccumHisInfo * item = (STAccumHisInfo *)[mAccumHisArray objectAtIndex:0];
    [self callGetLatestTsLogs:[item uid]];
}

- (void)footerRereshing
{
    mCurPageNum++;
    [self callGetTsLogsPage:mCurPageNum];
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
        
        // append new data at first place
        [dataList addObjectsFromArray:mAccumHisArray];
        mAccumHisArray = dataList;
        mLeftMoney = money;
        
        // refresh table
        [self updateUI];
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
        // append data list
        [mAccumHisArray addObjectsFromArray:dataList];
        
        // refresh table
        [self updateUI];
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

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
	//return height1;
	return ACCUMCELL_HEIGHT;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (mAccumHisArray == nil)
        return nil;
    
	STAccumHisInfo *data = (STAccumHisInfo *)[mAccumHisArray objectAtIndex:indexPath.row];
    Drv_AccumHistoryTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"accumcell"];
    if (cell == nil)
	{
		cell = (Drv_AccumHistoryTableViewCell *)[Drv_AccumHistoryTableViewCell cellFromNibNamed:@"Drv_AccumHistoryTableViewCell"];
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
    UIViewController * presentViewCtrl = self.presentingViewController;
    [self dismissViewControllerAnimated:NO completion:^
     {
         UIViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"Charge"];
         [presentViewCtrl presentViewController:ctrl animated:NO completion:nil];
     }];
}

/**
 * Tixian menu button clicked event implementation
 */
- (IBAction)onClickedMenuWithdraw:(id)sender
{
    UIViewController * presentViewCtrl = self.presentingViewController;
    [self dismissViewControllerAnimated:NO completion:^
     {
         UIViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"Withdraw"];
         [presentViewCtrl presentViewController:ctrl animated:NO completion:nil];
     }];
}
@end
