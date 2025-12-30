//
//  MyCouponViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_MyCouponViewController.h"
#import "CouponDetailViewController.h"
#import "UIViewController+CWPopup.h"
#import "MJRefresh.h"

@interface Drv_MyCouponViewController ()

@end


#define COUPONCELL_HEIGHT               45


@implementation Drv_MyCouponViewController

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
    [self callGetCouponPage:mCurPageNum];
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
    mCouponArray = [[NSMutableArray alloc] init];
    
    mCurPageNum = 0;
    
    // initialize pull refresh table
    CGRect rt = _vwTableFrame.frame;
    _mainTable = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, rt.size.width, rt.size.height)];
    _mainTable.delegate = self;
    _mainTable.dataSource = self;
    
    [_vwTableFrame addSubview:_mainTable];
    
    // setup pull to refresh table message
    [self setupRefresh];
    
    // make tap recognizer to hide keyboard
    UITapGestureRecognizer * tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapBackGround:)];
    
    [self.view addGestureRecognizer:tapRecognizer];
}


/**
 * Hide keyboard when tapped background
 */
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

/**
 *  集成刷新控件
 */
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
    [_mainTable reloadData];
    
}



#pragma mark - Pull refresh event
- (void)headerRereshing
{
    if (mCouponArray.count == 0) {
        [self callGetCouponPage:mCurPageNum];
    }
    
    STCouponInfo * item = (STCouponInfo *)[mCouponArray objectAtIndex:0];
    [self callGetLatestCoupon:[item uid]];
}

- (void)footerRereshing
{
    mCurPageNum++;
    [self callGetCouponPage:mCurPageNum];
}



//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/**
 * call get the latest coupon list ( by limit id)
 * @param : limitid [in], the last first id ( used for refreshing )
 */
- (void) callGetLatestCoupon : (long)limitid
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] tradeSvcMgr].delegate = self;
    [[[CommManager getCommMgr] tradeSvcMgr] GetLatestCoupon:[Common getUserID] limitid:limitid devtoken:[Common getDeviceMacAddress]];
}

- (void) getLatestCouponResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        // append new data at first place
        [dataList addObjectsFromArray:mCouponArray];
        mCouponArray = dataList;
        
        // refresh table
        [self updateUI];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
    [_mainTable headerEndRefreshing];
    [_mainTable footerEndRefreshing];
}


/**
 * call get coupon list ( by page no )
 * @param : pangeno [in], current page number ( used to get next data list  )
 */
- (void) callGetCouponPage : (int)pageno
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetPagedCoupon service routine.
    [[CommManager getCommMgr] tradeSvcMgr].delegate = self;
    [[[CommManager getCommMgr] tradeSvcMgr] GetPagedCoupon:[Common getUserID] pageno:pageno devtoken:[Common getDeviceMacAddress]];
}

- (void) getPagedCouponResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        // append data list
        [mCouponArray addObjectsFromArray:dataList];
        
        // refresh table
        [self updateUI];
        
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
    [_mainTable headerEndRefreshing];
    [_mainTable footerEndRefreshing];
}


/**
 * call add one coupon
 */
- (void) callAddCoupon
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the AddCoupon service routine.
    [[CommManager getCommMgr] tradeSvcMgr].delegate = self;
    [[[CommManager getCommMgr] tradeSvcMgr] AddCoupon:[Common getUserID] active_code:[_txtSearch text] devtoken:[Common getDeviceMacAddress]];
}

- (void) addCouponResult:(NSString *)result coupon:(STCouponInfo *)coupon
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        [mCouponArray insertObject:coupon atIndex:0];
        
        [self updateUI];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - Scroll event of table cell ( Drvtomed )
- (void)cellClickedDelegate:(long)uid
{
    STCouponInfo * dataInfo = nil;
    // get coupon data by uid
    for (int i = 0; i < mCouponArray.count; i++) {
        STCouponInfo * oneInfo = (STCouponInfo *)[mCouponArray objectAtIndex:i];
        if (oneInfo.uid == uid) {
            dataInfo = oneInfo;
        }
    }
    
    // check coupon data info
    if (dataInfo == nil) {
        return;
    }
    
    CouponDetailViewController *popup = [[CouponDetailViewController alloc] initWithNibName:@"CouponDetailViewController" bundle:nil];
    
    popup.mContents = dataInfo.contents;
    popup.mCode = dataInfo.couponcode;
    popup.mUnitname = dataInfo.unitname;
    
    [self presentPopupViewController:popup animated:YES completion:^(void) {
        NSLog(@"popup view presented");
    }];
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - delegate event of main table view
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (mCouponArray == nil)
        return 0;
    
    return [mCouponArray count];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
	//return height1;
	return COUPONCELL_HEIGHT;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (mCouponArray == nil)
        return nil;
    
	STCouponInfo *data = (STCouponInfo *)[mCouponArray objectAtIndex:indexPath.row];
    Drv_MyCouponTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"couponcell"];
    if (cell == nil)
	{
		cell = (Drv_MyCouponTableViewCell *)[Drv_MyCouponTableViewCell cellFromNibNamed:@"Drv_MyCouponTableViewCell"];
	}
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.delegate = self;
    
	[cell initWithData:(id)data reuseIdentifier:@"couponcell" parent:self];
	
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

- (IBAction)onClickedAdd:(id)sender
{
    if ([[_txtSearch text] isEqualToString:@""]) {
        [_txtSearch becomeFirstResponder];
        return;
    }
    
    [self callAddCoupon];
}

@end
