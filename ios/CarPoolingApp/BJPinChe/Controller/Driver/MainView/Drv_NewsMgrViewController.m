//
//  NewsMgrViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/23/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_NewsMgrViewController.h"
#import "CouponDetailViewController.h"
#import "UIViewController+CWPopup.h"
#import "MJRefresh.h"

@interface Drv_NewsMgrViewController ()

@end

#define TABCOUNT                    3
#define NEWSCELL_HEIGHT             86

#define TBL_POS0                    0
#define TBL_POS1                    1
#define TBL_POS2                    2

@implementation Drv_NewsMgrViewController

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
    
    [self callGetAnnouncePageList:mCurPageNum1];
    [self callGetOrderInfoPage:mCurPageNum2];
    [self callGetNotiInfoPage:mCurPageNum3];
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
    
    mNewsArray1 = [[NSMutableArray alloc] init];
    mNewsArray2 = [[NSMutableArray alloc] init];
    mNewsArray3 = [[NSMutableArray alloc] init];
    
    mCurPageNum1 = 0;
    mCurPageNum2 = 0;
    mCurPageNum3 = 0;
    
    // initialize news view frame
    CGRect rcFrame = _vwNewsView1.frame;
	rcFrame = CGRectMake(_vwNewsView1.frame.size.width, rcFrame.origin.y, rcFrame.size.width, rcFrame.size.height);
	_vwNewsView2.frame = rcFrame;
    
    rcFrame = _vwNewsView3.frame;
    rcFrame = CGRectMake(_vwNewsView1.frame.size.width * 2, rcFrame.origin.y, rcFrame.size.width, rcFrame.size.height);
	_vwNewsView3.frame = rcFrame;
    
    // initialize pull refresh table
    CGRect rt = _vwNewsFrame1.frame;
    _newsTable1 = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, rt.size.width, rt.size.height)];
    _newsTable1.delegate = self;
    _newsTable1.dataSource = self;
    
    [_vwNewsFrame1 addSubview:_newsTable1];
    
    rt = _vwNewsFrame2.frame;
    _newsTable2 = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, rt.size.width, rt.size.height)];
    _newsTable2.delegate = self;
    _newsTable2.dataSource = self;
    
    [_vwNewsFrame2 addSubview:_newsTable2];
    
    rt = _vwNewsFrame3.frame;
    _newsTable3 = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, rt.size.width, rt.size.height)];
    _newsTable3.delegate = self;
    _newsTable3.dataSource = self;
    
    [_vwNewsFrame3 addSubview:_newsTable3];
    
    // initialize main scroll content size
    [_MainScrollView setContentSize:CGSizeMake(_vwNewsView1.frame.size.width + _vwNewsView2.frame.size.width + _vwNewsView3.frame.size.width, _vwNewsFrame1.frame.size.height)];
    
    // make tap recognizer to hide keyboard && popup
    UITapGestureRecognizer * tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapBackGround:)];
    
    [self.view addGestureRecognizer:tapRecognizer];
    
    // setup pull to refresh table message
    [self setupRefresh];
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
    [_newsTable1 addHeaderWithTarget:self action:@selector(headerRereshing1)];
    
    // 2.上拉加载更多(进入刷新状态就会调用self的footerRereshing)
    [_newsTable1 addFooterWithTarget:self action:@selector(footerRereshing1)];
    
    // 设置文字(也可以不设置,默认的文字在MJRefreshConst中修改)
    _newsTable1.headerPullToRefreshText = MSG_TBLHEADER_PULL;
    _newsTable1.headerReleaseToRefreshText = MSG_TBLHEADER_RELEASE;
    _newsTable1.headerRefreshingText = MSG_TBLHEADER_REFRESHING;
    
    _newsTable1.footerPullToRefreshText = MSG_TBLFOOTER_PULL;
    _newsTable1.footerReleaseToRefreshText = MSG_TBLFOOTER_RELEASE;
    _newsTable1.footerRefreshingText = MSG_TBLFOOTER_REFRESHING;
    
    // 1.下拉刷新(进入刷新状态就会调用self的headerRereshing)
    [_newsTable2 addHeaderWithTarget:self action:@selector(headerRereshing2)];
    
    // 2.上拉加载更多(进入刷新状态就会调用self的footerRereshing)
    [_newsTable2 addFooterWithTarget:self action:@selector(footerRereshing2)];
    
    // 设置文字(也可以不设置,默认的文字在MJRefreshConst中修改)
    _newsTable2.headerPullToRefreshText = MSG_TBLHEADER_PULL;
    _newsTable2.headerReleaseToRefreshText = MSG_TBLHEADER_RELEASE;
    _newsTable2.headerRefreshingText = MSG_TBLHEADER_REFRESHING;
    
    _newsTable2.footerPullToRefreshText = MSG_TBLFOOTER_PULL;
    _newsTable2.footerReleaseToRefreshText = MSG_TBLFOOTER_RELEASE;
    _newsTable2.footerRefreshingText = MSG_TBLFOOTER_REFRESHING;
    
    // 1.下拉刷新(进入刷新状态就会调用self的headerRereshing)
    [_newsTable3 addHeaderWithTarget:self action:@selector(headerRereshing3)];
    
    // 2.上拉加载更多(进入刷新状态就会调用self的footerRereshing)
    [_newsTable3 addFooterWithTarget:self action:@selector(footerRereshing3)];
    
    // 设置文字(也可以不设置,默认的文字在MJRefreshConst中修改)
    _newsTable3.headerPullToRefreshText = MSG_TBLHEADER_PULL;
    _newsTable3.headerReleaseToRefreshText = MSG_TBLHEADER_RELEASE;
    _newsTable3.headerRefreshingText = MSG_TBLHEADER_REFRESHING;
    
    _newsTable3.footerPullToRefreshText = MSG_TBLFOOTER_PULL;
    _newsTable3.footerReleaseToRefreshText = MSG_TBLFOOTER_RELEASE;
    _newsTable3.footerRefreshingText = MSG_TBLFOOTER_REFRESHING;
}



/**
 * update ui for UITableView ( get new data list )
 */
- (void) updateUI : (int)tab
{
    if (tab == 0)
    {
        [_newsTable1 reloadData];
    }
    else if (tab == 1)
    {
        [_newsTable2 reloadData];
    }
    else if (tab == 2)
    {
        [_newsTable3 reloadData];
    }
}

/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)onClickedTab1:(id)sender
{
    [_MainScrollView scrollRectToVisible:CGRectMake(0, 0, _vwNewsView1.frame.size.width, _vwNewsView1.frame.size.height) animated:YES];
}

- (IBAction)onClickedTab2:(id)sender
{
    [_MainScrollView scrollRectToVisible:CGRectMake(_vwNewsView1.frame.size.width, 0, _vwNewsView1.frame.size.width, _vwNewsView1.frame.size.height) animated:YES];
}

- (IBAction)onClickedTab3:(id)sender
{
    [_MainScrollView scrollRectToVisible:CGRectMake(_vwNewsView1.frame.size.width * 2, 0, _vwNewsView1.frame.size.width, _vwNewsView1.frame.size.height) animated:YES];
}


//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/**
 * call get the latest announce list ( by limit id)
 * @param : limitid [in], the last first id ( used for refreshing )
 */
- (void) callGetLatestAnnounce : (long)limitid
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] mainSvcMgr].delegate = self;
    [[[CommManager getCommMgr] mainSvcMgr] GetLatestAnnounce:[Common getCurrentCity] driververif:[Common getVerifyState] limitid:[NSString stringWithFormat:@"%ld", limitid] userid:[Common getUserID] devtoken:[Common getDeviceMacAddress]];
}

- (void) getLatestAnnounceResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        // append new data at first place
        [dataList addObjectsFromArray:mNewsArray1];
        mNewsArray1 = dataList;
        
        // refresh table
        [self updateUI:TBL_POS0];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
    
    // end refreshing
    [_newsTable1 headerEndRefreshing];
    [_newsTable1 footerEndRefreshing];
}


/**
 * call get the latest announce list ( by page no )
 * @param : pangeno [in], current page number ( used to get next data list  )
 */
- (void) callGetAnnouncePageList : (int)pageno
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] mainSvcMgr].delegate = self;
    [[[CommManager getCommMgr] mainSvcMgr] GetAnnouncePage:[Common getCurrentCity] driververif:[Common getVerifyState] pageno:pageno userid:[Common getUserID] devtoken:[Common getDeviceMacAddress]];
}

- (void) getAnnouncePageResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        // append data list
        [mNewsArray1 addObjectsFromArray:dataList];
        
        // refresh table
        [self updateUI:TBL_POS0];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
    
    // end refreshing
    [_newsTable1 headerEndRefreshing];
    [_newsTable1 footerEndRefreshing];
}

/**
 * call get the latest order info list ( by limit id)
 * @param : limitid [in], the last first id ( used for refreshing )
 */
- (void) callGetLatestOrderList : (long)limitid
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] mainSvcMgr].delegate = self;
    [[[CommManager getCommMgr] mainSvcMgr] GetLatestOrderInfos:[Common getUserID] limitid:limitid devtoken:[Common getDeviceMacAddress]];
}

- (void) getLatestOrderinfosResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        // append new data at first place
        [dataList addObjectsFromArray:mNewsArray2];
        mNewsArray2 = dataList;
        
        // refresh table
        [self updateUI:TBL_POS1];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
    
    // end refreshing
    [_newsTable2 headerEndRefreshing];
    [_newsTable2 footerEndRefreshing];
}


/**
 * call get the order info list ( by page no )
 * @param : pangeno [in], current page number ( used to get next data list  )
 */
- (void) callGetOrderInfoPage : (int)pageno
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] mainSvcMgr].delegate = self;
    [[[CommManager getCommMgr] mainSvcMgr] GetOrderInfoPage:[Common getUserID] pageno:pageno devtoken:[Common getDeviceMacAddress]];
}

- (void) getOrderInfoPageResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        // append data list
        [mNewsArray2 addObjectsFromArray:dataList];
        
        // refresh table
        [self updateUI:TBL_POS1];
        
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
    
    // end refreshing
    [_newsTable2 headerEndRefreshing];
    [_newsTable2 footerEndRefreshing];

}


/**
 * call get the latest notification list ( by limit id)
 * @param : limitid [in], the last first id ( used for refreshing )
 */
- (void) callGetLatestNoti : (long)limitid
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] mainSvcMgr].delegate = self;
    [[[CommManager getCommMgr] mainSvcMgr] GetLatestNoti:[Common getUserID] limitid:limitid devtoken:[Common getDeviceMacAddress]];
}

- (void) getLatestNotiResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        // append new data at first place
        [dataList addObjectsFromArray:mNewsArray3];
        mNewsArray3 = dataList;
        
        // refresh table
        [self updateUI:TBL_POS2];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
    
    // end refreshing
    [_newsTable3 headerEndRefreshing];
    [_newsTable3 footerEndRefreshing];

}


/**
 * call get the notification list ( by page no )
 * @param : pangeno [in], current page number ( used to get next data list  )
 */
- (void) callGetNotiInfoPage : (int)pageno
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] mainSvcMgr].delegate = self;
    [[[CommManager getCommMgr] mainSvcMgr] GetNotiPage:[Common getUserID] pageno:pageno devtoken:[Common getDeviceMacAddress]];
}

- (void) getNotiPageResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        // append data list
        [mNewsArray3 addObjectsFromArray:dataList];
        
        // refresh table
        [self updateUI:TBL_POS2];
        
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
    
    // end refreshing
    [_newsTable3 headerEndRefreshing];
    [_newsTable3 footerEndRefreshing];
}


/**
 * call get coupon detail info
 * @param : couponid [in], coupon id
 */
- (void) callGetCouponDetail : (long)couponid
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] mainSvcMgr].delegate = self;
    [[[CommManager getCommMgr] mainSvcMgr] GetCouponDetail:[Common getUserID] couponid:couponid devtoken:[Common getDeviceMacAddress]];
}

- (void)getCouponDetailResult:(NSString *)result dataInfo:(STCouponInfo *)dataInfo
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        
        CouponDetailViewController *popup = [[CouponDetailViewController alloc] initWithNibName:@"CouponDetailViewController" bundle:nil];
        
        popup.mContents = dataInfo.contents;
        popup.mCode = dataInfo.couponcode;
        popup.mUnitname = dataInfo.unitname;
        
        [self presentPopupViewController:popup animated:YES completion:^(void) {
            NSLog(@"popup view presented");
        }];
        
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}


///////////////////////////////////////////////////////////////////////////
#pragma mark - News Cell Delegate
- (void)cellClickedDelegate:(long)uid
{
    [self callGetCouponDetail:uid];
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - Scroll Delegate

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if (scrollView == _MainScrollView)
	{
		CGPoint ptOffset = scrollView.contentOffset;
		int nIndicatorWidth = _ctrlIndicator.frame.size.width;
		int nLeftMargin = (self.view.frame.size.width - nIndicatorWidth * TABCOUNT) / (TABCOUNT - 1);
		int nPageWidth = _MainScrollView.frame.size.width;
        
		CGRect rcIndFrame = _ctrlIndicator.frame;
		
		if (ptOffset.x * nIndicatorWidth / nPageWidth < nIndicatorWidth / 2)
		{
            // change indicator position
            rcIndFrame = CGRectMake(ptOffset.x * nIndicatorWidth / nPageWidth, rcIndFrame.origin.y, rcIndFrame.size.width, rcIndFrame.size.height);
            
			// first tab selected
            mCurTab = 0;
			_lblTitle1.textColor = MYCOLOR_GREEN;
			_lblTitle2.textColor = [UIColor blackColor];
            _lblTitle3.textColor = [UIColor blackColor];
            
		}
		else if(ptOffset.x * nIndicatorWidth / nPageWidth < (nIndicatorWidth / 2 + nIndicatorWidth))
		{
            // change indicator position
            rcIndFrame = CGRectMake(nLeftMargin + ptOffset.x * nIndicatorWidth / nPageWidth, rcIndFrame.origin.y, rcIndFrame.size.width, rcIndFrame.size.height);
            
			// second tab selected
            mCurTab = 1;
			_lblTitle1.textColor = [UIColor blackColor];
            _lblTitle2.textColor = MYCOLOR_GREEN;
			_lblTitle3.textColor = [UIColor blackColor];
            
		}
        else
        {
            // change indicator position
            rcIndFrame = CGRectMake(nLeftMargin * 2 + ptOffset.x * nIndicatorWidth / nPageWidth, rcIndFrame.origin.y, rcIndFrame.size.width, rcIndFrame.size.height);
            
            // third tab selected
            mCurTab = 2;
			_lblTitle1.textColor = [UIColor blackColor];
            _lblTitle2.textColor = [UIColor blackColor];
			_lblTitle3.textColor = MYCOLOR_GREEN;
        }
        
        _ctrlIndicator.frame = rcIndFrame;
	}
}


#pragma mark - Pull refresh event

- (void)headerRereshing1
{
    if (mNewsArray1.count == 0) {
        [self callGetAnnouncePageList:mCurPageNum1];
        return;
    }
    
    STNewsInfo * item = (STNewsInfo *)[mNewsArray1 objectAtIndex:0];
    [self callGetLatestAnnounce:[item uid]];
}

- (void)footerRereshing1
{
    mCurPageNum1++;
    [self callGetAnnouncePageList:mCurPageNum1];
}

- (void)headerRereshing2
{
    if (mNewsArray2.count == 0) {
        [self callGetOrderInfoPage:mCurPageNum2];
        return;
    }
    
    STNewsInfo * item = (STNewsInfo *)[mNewsArray2 objectAtIndex:0];
    [self callGetLatestOrderList:[item uid]];
}

- (void)footerRereshing2
{
    mCurPageNum2++;
    [self callGetOrderInfoPage:mCurPageNum2];
}

- (void)headerRereshing3
{
    if (mNewsArray3.count == 0) {
        [self callGetNotiInfoPage:mCurPageNum3];
        return;
    }
    
    STNewsInfo * item = (STNewsInfo *)[mNewsArray3 objectAtIndex:0];
    [self callGetLatestNoti:[item uid]];
}

- (void)footerRereshing3
{
    mCurPageNum3++;
    [self callGetNotiInfoPage:mCurPageNum3];
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - delegate event of main table view
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    NSInteger nCount = 0;
    
    if (tableView == _newsTable1)
    {
        if (mNewsArray1 != nil) nCount = [mNewsArray1 count];
    }
    else if (tableView == _newsTable2)
    {
        if (mNewsArray2 != nil) nCount = [mNewsArray2 count];
    }
    else if (tableView == _newsTable3)
    {
        if (mNewsArray3 != nil) nCount = [mNewsArray3 count];
    }
    
    return nCount;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	//return height1;
	return NEWSCELL_HEIGHT;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSMutableArray * curArray = [[NSMutableArray alloc] init];
    if (tableView == _newsTable1)
    {
        curArray = mNewsArray1;
    }
    else if (tableView == _newsTable2)
    {
        curArray = mNewsArray2;
    }
    else if (tableView == _newsTable3)
    {
        curArray = mNewsArray3;
    }
    
    if (curArray == nil)
        return nil;
    
    
	STNewsInfo *data = (STNewsInfo *)[curArray objectAtIndex:indexPath.row];
    Drv_NewsTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"newscell"];
    if (cell == nil)
	{
		cell = (Drv_NewsTableViewCell *)[Drv_NewsTableViewCell cellFromNibNamed:@"Drv_NewsTableViewCell"];
	}
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.delegate = self;
    
	[cell initWithData:(id)data reuseIdentifier:@"newscell" parent:self];
	
	return cell;
}




@end
