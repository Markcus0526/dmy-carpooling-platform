//
//  NewsMgrViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/23/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "NewsMgrVC.h"
#import "CouponDetailViewController.h"
#import "UIViewController+CWPopup.h"
#import "MJRefresh.h"

@interface NewsMgrVC ()

@end

#define TABCOUNT                    3
#define NEWSCELL_HEIGHT             86

#define TBL_POS0                    0
#define TBL_POS1                    1
#define TBL_POS2                    2

#define SCROLL_OFFSET               113

@implementation NewsMgrVC


@synthesize announcement_count;
@synthesize ordernotify_count;
@synthesize personnotify_count;


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

	// initialize main scroll content size
	//    [_MainScrollView setContentSize:CGSizeMake(_vwNewsView1.frame.size.width + _vwNewsView2.frame.size.width + _vwNewsView3.frame.size.width, _vwNewsView1.frame.size.height)];
	[_MainScrollView setContentSize:CGSizeMake(_vwNewsView1.frame.size.width + _vwNewsView2.frame.size.width + _vwNewsView3.frame.size.width, _vwNewsView1.frame.size.height - SCROLL_OFFSET)];

	// make tap recognizer to hide keyboard && popup
	UITapGestureRecognizer * tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapBackGround:)];

	[self.view addGestureRecognizer:tapRecognizer];

	// set pull to refresh table
	[self setupRefresh1];
	[self setupRefresh2];
	[self setupRefresh3];

	_lblAncCount.hidden = YES;
	_lblOrderNotify.hidden = YES;
	_lblPersonNotify.hidden = YES;

	_lblAncCount.layer.cornerRadius = 5;
	_lblOrderNotify.layer.cornerRadius = 5;
	_lblPersonNotify.layer.cornerRadius = 5;
	_lblAncCount.clipsToBounds = YES;
	_lblOrderNotify.clipsToBounds = YES;
	_lblPersonNotify.clipsToBounds = YES;


	[self refreshNewsCounts];
}



- (void)refreshNewsCounts
{
	if (announcement_count > 0)
	{
		[_lblAncCount setText:[NSString stringWithFormat:@"%d", announcement_count]];
		_lblAncCount.hidden = NO;
	}
	else
	{
		_lblAncCount.hidden = YES;
	}

	if (ordernotify_count > 0)
	{
		[_lblOrderNotify setText:[NSString stringWithFormat:@"%d", ordernotify_count]];
		_lblOrderNotify.hidden = NO;
	}
	else
	{
		_lblOrderNotify.hidden = YES;
	}

	if (personnotify_count > 0)
	{
		[_lblPersonNotify setText:[NSString stringWithFormat:@"%d", personnotify_count]];
		_lblPersonNotify.hidden = NO;
	}
	else
	{
		_lblPersonNotify.hidden = YES;
	}
}



/**
 *  集成刷新控件
 */
- (void)setupRefresh1
{
	// 1.下拉刷新(进入刷新状态就会调用self的headerRereshing)
	[_newsTable1 addHeaderWithTarget:self action:@selector(headerRefreshing1)];
	// 2.上拉加载更多(进入刷新状态就会调用self的footerRereshing)
	[_newsTable1 addFooterWithTarget:self action:@selector(footerRefreshing1)];
	
	// 设置文字(也可以不设置,默认的文字在MJRefreshConst中修改)
	_newsTable1.headerPullToRefreshText = MSG_TBLHEADER_PULL;
	_newsTable1.headerReleaseToRefreshText = MSG_TBLHEADER_RELEASE;
	_newsTable1.headerRefreshingText = MSG_TBLHEADER_REFRESHING;
	
	_newsTable1.footerPullToRefreshText = MSG_TBLFOOTER_PULL;
	_newsTable1.footerReleaseToRefreshText = MSG_TBLFOOTER_RELEASE;
	_newsTable1.footerRefreshingText = MSG_TBLFOOTER_REFRESHING;
}

- (void)setupRefresh2
{
	// 1.下拉刷新(进入刷新状态就会调用self的headerRereshing)
	[_newsTable2 addHeaderWithTarget:self action:@selector(headerRefreshing2)];
	// 2.上拉加载更多(进入刷新状态就会调用self的footerRereshing)
	[_newsTable2 addFooterWithTarget:self action:@selector(footerRefreshing2)];
	
	// 设置文字(也可以不设置,默认的文字在MJRefreshConst中修改)
	_newsTable2.headerPullToRefreshText = MSG_TBLHEADER_PULL;
	_newsTable2.headerReleaseToRefreshText = MSG_TBLHEADER_RELEASE;
	_newsTable2.headerRefreshingText = MSG_TBLHEADER_REFRESHING;
	
	_newsTable2.footerPullToRefreshText = MSG_TBLFOOTER_PULL;
	_newsTable2.footerReleaseToRefreshText = MSG_TBLFOOTER_RELEASE;
	_newsTable2.footerRefreshingText = MSG_TBLFOOTER_REFRESHING;
}

- (void)setupRefresh3
{
	// 1.下拉刷新(进入刷新状态就会调用self的headerRereshing)
	[_newsTable3 addHeaderWithTarget:self action:@selector(headerRefreshing3)];
	// 2.上拉加载更多(进入刷新状态就会调用self的footerRereshing)
	[_newsTable3 addFooterWithTarget:self action:@selector(footerRefreshing3)];
	
	// 设置文字(也可以不设置,默认的文字在MJRefreshConst中修改)
	_newsTable3.headerPullToRefreshText = MSG_TBLHEADER_PULL;
	_newsTable3.headerReleaseToRefreshText = MSG_TBLHEADER_RELEASE;
	_newsTable3.headerRefreshingText = MSG_TBLHEADER_REFRESHING;
	
	_newsTable3.footerPullToRefreshText = MSG_TBLFOOTER_PULL;
	_newsTable3.footerReleaseToRefreshText = MSG_TBLFOOTER_RELEASE;
	_newsTable3.footerRefreshingText = MSG_TBLFOOTER_REFRESHING;
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
			MyLog(@"popup view dismissed");
		}];
	}
}

/**
 * update ui for UITableView ( get new data list )
 */
- (void) updateUI : (int)tab
{
	if (tab == 0)
	{
		[_newsTable1 headerEndRefreshing];
		[_newsTable1 footerEndRefreshing];
		[_newsTable1 reloadData];
	}
	else if (tab == 1)
	{
		[_newsTable2 headerEndRefreshing];
		[_newsTable2 footerEndRefreshing];
		[_newsTable2 reloadData];
	}
	else if (tab == 2)
	{
		[_newsTable3 headerEndRefreshing];
		[_newsTable3 footerEndRefreshing];
		[_newsTable3 reloadData];
	}
}



#pragma mark - Announce datalist update Function

- (void) addOldNews1 : (NSMutableArray *)tempArray
{
	BOOL hasUpdate = NO;
	
	for (int i = 0; i < [tempArray count]; i++)
	{
		STNewsInfo * newInfo = [tempArray objectAtIndex:i];
		
		BOOL isExist = NO;
		for (int j = 0; j < [mNewsArray1 count]; j++)
		{
			STNewsInfo * oldInfo = [mNewsArray1 objectAtIndex:j];
			if (oldInfo.uid == newInfo.uid)
			{
				isExist = YES;
				break;
			}
		}
		
		if (isExist)    continue;
		
		[mNewsArray1 addObject:newInfo];
		hasUpdate = YES;
	}
	
	if (hasUpdate) {
		mCurPageNum1 = [mNewsArray1 count] / DEF_PAGECOUNT;
	}
	
	[self updateUI:TBL_POS0];
}

- (void) insertLatestNews1 : (NSMutableArray *)tempArray
{
	BOOL hasUpdate = NO;
	
	for (int i = 0; i < [tempArray count]; i++)
	{
		STNewsInfo * newInfo = [tempArray objectAtIndex:i];
		
		BOOL isExist = NO;
		for (int j = 0; j < [mNewsArray1 count]; j++)
		{
			STNewsInfo * oldInfo = [mNewsArray1 objectAtIndex:j];
			if (oldInfo.uid == newInfo.uid)
			{
				isExist = YES;
				break;
			}
		}
		
		if (isExist)    continue;
		
		[mNewsArray1 insertObject:newInfo atIndex:0];
		hasUpdate = YES;
	}
	
	if (hasUpdate) {
		mCurPageNum1 = [mNewsArray1 count] / DEF_PAGECOUNT;
	}
	
	[self updateUI:TBL_POS0];
}


#pragma mark - Order news datalist update Function

- (void) addOldNews2 : (NSMutableArray *)tempArray
{
	BOOL hasUpdate = NO;
	
	for (int i = 0; i < [tempArray count]; i++)
	{
		STNewsInfo * newInfo = [tempArray objectAtIndex:i];
		
		BOOL isExist = NO;
		for (int j = 0; j < [mNewsArray2 count]; j++)
		{
			STNewsInfo * oldInfo = [mNewsArray2 objectAtIndex:j];
			if (oldInfo.uid == newInfo.uid)
			{
				isExist = YES;
				break;
			}
		}
		
		if (isExist)    continue;
		
		[mNewsArray2 addObject:newInfo];
		hasUpdate = YES;
	}
	
	if (hasUpdate) {
		mCurPageNum2 = [mNewsArray2 count] / DEF_PAGECOUNT;
	}
	
	[self updateUI:TBL_POS1];
}

- (void) insertLatestNews2 : (NSMutableArray *)tempArray
{
	BOOL hasUpdate = NO;
	
	for (int i = 0; i < [tempArray count]; i++)
	{
		STNewsInfo * newInfo = [tempArray objectAtIndex:i];
		
		BOOL isExist = NO;
		for (int j = 0; j < [mNewsArray1 count]; j++)
		{
			STNewsInfo * oldInfo = [mNewsArray1 objectAtIndex:j];
			if (oldInfo.uid == newInfo.uid)
			{
				isExist = YES;
				break;
			}
		}
		
		if (isExist)    continue;
		
		[mNewsArray1 insertObject:newInfo atIndex:0];
		hasUpdate = YES;
	}
	
	if (hasUpdate) {
		mCurPageNum1 = [mNewsArray1 count] / DEF_PAGECOUNT;
	}
	
	[self updateUI:TBL_POS0];
}



#pragma mark - Notice datalist update Function

- (void) addOldNews3 : (NSMutableArray *)tempArray
{
	BOOL hasUpdate = NO;
	
	for (int i = 0; i < [tempArray count]; i++)
	{
		STNewsInfo * newInfo = [tempArray objectAtIndex:i];
		
		BOOL isExist = NO;
		for (int j = 0; j < [mNewsArray3 count]; j++)
		{
			STNewsInfo * oldInfo = [mNewsArray3 objectAtIndex:j];
			if (oldInfo.uid == newInfo.uid)
			{
				isExist = YES;
				break;
			}
		}
		
		if (isExist)    continue;
		
		[mNewsArray3 addObject:newInfo];
		hasUpdate = YES;
	}
	
	if (hasUpdate) {
		mCurPageNum3 = [mNewsArray3 count] / DEF_PAGECOUNT;
	}
	
	[self updateUI:TBL_POS2];
}



- (void) insertLatestNews3 : (NSMutableArray *)tempArray
{
	BOOL hasUpdate = NO;

	for (int i = 0; i < [tempArray count]; i++)
	{
		STNewsInfo * newInfo = [tempArray objectAtIndex:i];

		BOOL isExist = NO;
		for (int j = 0; j < [mNewsArray3 count]; j++)
		{
			STNewsInfo * oldInfo = [mNewsArray3 objectAtIndex:j];
			if (oldInfo.uid == newInfo.uid)
			{
				isExist = YES;
				break;
			}
		}

		if (isExist)    continue;

		[mNewsArray3 insertObject:newInfo atIndex:0];
		hasUpdate = YES;
	}

	if (hasUpdate) {
		mCurPageNum3 = [mNewsArray3 count] / DEF_PAGECOUNT;
	}

	[self updateUI:TBL_POS2];
}



////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - UI Main Tab click event

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
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:2];
	}
	[self updateUI:TBL_POS0];
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
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:2];
	}
    [self updateUI:TBL_POS0];
	
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
		
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:2];
	}
	[self updateUI:TBL_POS1];
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
		
		
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:2];
	}
    // refresh table
    [self updateUI:TBL_POS1];
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
		
		
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:2];
	}
    // refresh table
    [self updateUI:TBL_POS2];
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
		
		
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:2];
	}
    // refresh table
    [self updateUI:TBL_POS2];
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
			MyLog(@"popup view presented");
		}];
		
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
	
}


///////////////////////////////////////////////////////////////////////////
#pragma mark - News Cell Delegate
- (void)cellClickedDelegate:(long)uid withType:(int)news_type
{
	NSArray* curArray = nil;

	if (news_type == NEWSTYPE_ANNOUNCEMENT)
		curArray = mNewsArray1;
	else if (news_type == NEWSTYPE_ORDERNOTIFY)
		curArray = mNewsArray2;
	else
		curArray = mNewsArray3;

	if (curArray == nil)
		return;


	STNewsInfo* newsItem = nil;

	for (int i = 0; i < curArray.count; i++)
	{
		STNewsInfo* arrayItem = [curArray objectAtIndex:i];

		if (arrayItem.uid == uid)
		{
			newsItem = arrayItem;
			break;
		}
	}


	if (newsItem == nil)
		return;


	// Mark the item as read state and show state
	newsItem.hasread = 1;

	UIAlertView* alertView = [[UIAlertView alloc] initWithTitle:newsItem.title message:newsItem.contents delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
	[alertView show];

	TEST_NETWORK_RETURN

	CommManager* commMgr = [CommManager getCommMgr];
	MainSvcMgr* mainSvcMgr = [commMgr mainSvcMgr];

	mainSvcMgr.delegate = self;

	if (news_type == NEWSTYPE_ANNOUNCEMENT)
	{
//		if (announcement_count > 0)
//			announcement_count--;
	}
	else if (news_type == NEWSTYPE_ORDERNOTIFY)
	{
		if (ordernotify_count > 0)
			ordernotify_count--;
		[mainSvcMgr readOrderNotifsWithUserID:[Common getUserID] orderNotifyID:newsItem.uid devtoken:[Common getDeviceMacAddress]];
	}
	else if (news_type == NEWSTYPE_PERSONNOTIFY)
	{
		if (personnotify_count > 0)
			personnotify_count--;
		[mainSvcMgr readPersonNotifsWithUserID:[Common getUserID] personNotifyID:newsItem.uid devtoken:[Common getDeviceMacAddress]];
	}

	[self refreshNewsCounts];
}



#pragma mark Mark for read delegates
- (void)readOrderNotifyResultCode:(int)retcode retmsg:(NSString *)retmsg uid:(long)ordernotifid
{
	STNewsInfo* newsItem = nil;
	int nIndex = -1;

	for (int i = 0; i < mNewsArray2.count; i++)
	{
		STNewsInfo* arrayItem = [mNewsArray2 objectAtIndex:i];

		if (arrayItem.uid == ordernotifid)
		{
			newsItem = arrayItem;
			nIndex = i;

			break;
		}
	}

	if (newsItem == nil)
		return;

	newsItem.hasread = 1;

	NewsTableViewCell* cell = (NewsTableViewCell*)[_newsTable2 cellForRowAtIndexPath:[NSIndexPath indexPathForRow:nIndex inSection:0]];
	[cell hasRead];
}


- (void)readPersonNotifyResultCode:(int)retcode retmsg:(NSString *)retmsg uid:(long)personnotifyid
{
	STNewsInfo* newsItem = nil;
	int nIndex = -1;

	for (int i = 0; i < mNewsArray2.count; i++)
	{
		STNewsInfo* arrayItem = [mNewsArray2 objectAtIndex:i];

		if (arrayItem.uid == personnotifyid)
		{
			newsItem = arrayItem;
			nIndex = i;

			break;
		}
	}

	if (newsItem == nil)
		return;

	newsItem.hasread = 1;

	NewsTableViewCell* cell = (NewsTableViewCell*)[_newsTable2 cellForRowAtIndexPath:[NSIndexPath indexPathForRow:nIndex inSection:0]];
	[cell hasRead];
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

- (void)headerRefreshing1
{
	if ((mNewsArray1 == nil) || ([mNewsArray1 count] == 0))
	{
		mCurPageNum1 = 0;
		[self callGetAnnouncePageList:mCurPageNum1];
	}
	else
	{
		[self callGetLatestAnnounce:[(STNewsInfo*)[mNewsArray1 objectAtIndex:0] uid]];
	}
}

- (void)footerRefreshing1
{
	mCurPageNum1++;
	[self callGetAnnouncePageList:mCurPageNum1];
}


- (void)headerRefreshing2
{
	if ((mNewsArray2 == nil) || ([mNewsArray2 count] == 0))
	{
		mCurPageNum2 = 0;
		[self callGetOrderInfoPage:mCurPageNum2];
	}
	else
	{
		[self callGetLatestOrderList:[(STNewsInfo*)[mNewsArray2 objectAtIndex:0] uid]];
	}
}

- (void)footerRefreshing2
{
	mCurPageNum3++;
	[self callGetOrderInfoPage:mCurPageNum3];
}


- (void)headerRefreshing3
{
	if ((mNewsArray3 == nil) || ([mNewsArray3 count] == 0))
	{
		mCurPageNum3 = 0;
		[self callGetNotiInfoPage:mCurPageNum3];
	}
	else
	{
		[self callGetLatestNoti:[(STNewsInfo*)[mNewsArray3 objectAtIndex:0] uid]];
	}
}

- (void)footerRefreshing3
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

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
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
	NewsTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"newscell"];
	if (cell == nil)
	{
		cell = (NewsTableViewCell *)[NewsTableViewCell cellFromNibNamed:@"NewsTableViewCell"];
	}

	cell.selectionStyle = UITableViewCellSelectionStyleNone;
	cell.delegate = self;

	[cell initWithData:(id)data reuseIdentifier:@"newscell" parent:self];

	return cell;
}



- (IBAction)onClickedBack:(id)sender
{
	[self dismissViewControllerAnimated:YES completion:nil];
}


@end
