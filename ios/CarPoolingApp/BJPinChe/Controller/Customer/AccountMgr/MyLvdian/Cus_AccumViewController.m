//
//  AccumViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//  乘客 我的减排绿点界面

#import "Cus_AccumViewController.h"
#import "Cus_AccumHistoryTableViewCell.h"
#import "Cus_ChargeViewController.h"
#import "Cus_WithdrawViewController.h"

@interface Cus_AccumViewController ()

@end


#define ACCUMCELL_HEIGHT               45



@implementation Cus_AccumViewController

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
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self initControls];
    [self callGetTsLogsPage:mCurPageNum];
    self.RefreshOrNot =YES;
   
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    if(self.RefreshOrNot)
    {
        STAccumHisInfo *leatest = [mAccumHisArray firstObject];
        [self callGetLatestTsLogs:leatest.uid];
    }
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
    _mainTable = [[PullToRefreshTable alloc] initWithFrame:CGRectMake(0, 0, rt.size.width, rt.size.height)];
    _mainTable.delegate = self;
    _mainTable.dataSource = self;
    _mainTable.pulldelegate = self;
    
    [_vwTableFrame addSubview:_mainTable];
}

/**
 * update ui for UITableView ( get new data list )
 */
- (void) updateUI
{
    [_lblLeftAccum setText:[NSString stringWithFormat:@"%.02f", mLeftMoney]];
    
    [_mainTable stopLoading];
    [_mainTable reloadData];
}


#pragma mark - Pull refresh event
- (void) pullRefresh:(UIScrollView *)scrollView
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
#pragma mark - Scroll event of main table view

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView {
  //  MyLog(@"11");
    
    [_mainTable BeginDragging];
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
   // MyLog(@"11");
    
    [_mainTable DidScroll:scrollView];
}

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate {
   // MyLog(@"11");
    
    [_mainTable DidEndDragging:scrollView];
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
    Cus_AccumHistoryTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"accumcell"];
    if (cell == nil)
	{
		cell = (Cus_AccumHistoryTableViewCell *)[Cus_AccumHistoryTableViewCell cellFromNibNamed:@"Cus_AccumHistoryTableViewCell"];
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
#warning 少一个查询按钮的响应事件
/**
 *   充值
 Chongzhi menu button clicked event implementation
 */
- (IBAction)onClickedMenuCharge:(id)sender
{
//    UIViewController * presentViewCtrl = self.presentingViewController;
//    [self dismissViewControllerAnimated:NO completion:^
//     {
//        
//     }];
    /* storyboard  跳转
    Cus_ChargeViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"Charge"];
    [self presentViewController:ctrl animated:YES completion:nil];
     */
    Cus_ChargeViewController *chargeVC =[[Cus_ChargeViewController alloc]initWithNibName:@"ChargeView" bundle:nil];
    [self presentViewController:chargeVC animated:YES completion:nil];
}

/**
  提现
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
    /* storyboard 跳转
    UIViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"Withdraw"];
    [self presentViewController:ctrl animated:NO completion:nil];
     */
    Cus_WithdrawViewController *withDrawVC =[[Cus_WithdrawViewController alloc]initWithNibName:@"WithDrawView" bundle:nil];
    [self presentViewController:withDrawVC animated:YES completion:nil];
}
@end
