//
//  Drv_PassengerInfoViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/27/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_PassengerInfoViewController.h"
#import "MJRefresh.h"


@implementation Drv_PassEvalCell

@end


@interface Drv_PassengerInfoViewController ()

@end


#define CELL_HEIGHT                             60
#define EVAL_CELLID                             @"passenger_eval_cell"

#define EVAL_HIGH_COLOR     [UIColor colorWithRed:238.0/255.0 green:42.0/255.0 blue:71/255.0 alpha:1.0]
#define EVAL_MEDIUM_COLOR   [UIColor colorWithRed:52.0/255.0 green:135.0/255.0 blue:91.0/255.0 alpha:1.0]
#define EVAL_LOW_COLOR      [UIColor colorWithRed:170.0/255.0 green:170.0/255.0 blue:170.0/255.0 alpha:1.0]


@implementation Drv_PassengerInfoViewController

@synthesize mPassID;

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
    if (_startTimer.length !=0) {
        falsePassDic = [[NSDictionary alloc]init];
        
        NSMutableArray *resultData = [[NSMutableArray alloc]init];
        NSArray *paths=NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
        NSString *path=[paths objectAtIndex:0];
        NSString *filename=[path stringByAppendingPathComponent:@"orderdes.plist"];
        
        //读文件
        NSDictionary* dic2 = [NSDictionary dictionaryWithContentsOfFile:filename];
        resultData = [dic2 objectForKey:@"order"];
        for (int i= 0; i<resultData.count; i++) {
            NSDictionary *dic =[resultData objectAtIndex:i];
            if ([_startTimer isEqualToString:[dic objectForKey:@"start_time"]]) {
                falsePassDic = dic;
            }
        }
        
        [_markVerif setEnabled:YES];
        
        [_lblAge setText:[NSString stringWithFormat:@"%@", [falsePassDic objectForKey:@"pass_age"]]];
        [_lblName setText:[NSString stringWithFormat:@"%@", [falsePassDic objectForKey:@"pass_name"]]];
        [_lblEvalRate setText:[falsePassDic objectForKey:@"EvalPro"]];
        [_lblCount setText:[falsePassDic objectForKey:@"ServeCnt"]];
        
        return;
    }
    
    mEvalArray = [[NSMutableArray alloc] init];

	[self setUserInfo];
    [self setupRefresh];

	mCurEvalPageNo = 0;
    mLastEvalId = 0;

	TEST_NETWORK_RETURN

	[SVProgressHUD show];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] GetPassengerInfo:[Common getUserID] PassId:self.mPassID DevToken:[Common getDeviceMacAddress]];
   
    [self headerRefreshing];
    
   // [[[CommManager getCommMgr] orderSvcMgr] GetPassengerPagedEvalInfo:[Common getUserID] PassId:self.mPassID PageNo:mCurEvalPageNo DevToken:[Common getDeviceMacAddress]];
}

- (void) setUserInfo
{
    
    // set user image
    //_imgUser
    
    // set sex image & change age color according to sex
    //_imgSex
    
    if (mPassengerInfo.sex == SEX_MALE) {
        _lblAge.textColor = MYCOLOR_GREEN;
    }
    [_lblAge setText:[NSString stringWithFormat:@"%d", mPassengerInfo.age]];
    
    [_lblName setText:mPassengerInfo.name];
    
    if (mPassengerInfo.pverified == 0) {
        [_markVerif setEnabled:NO];
    } else {
        [_markVerif setEnabled:YES];
    }
    
    [_lblEvalRate setText:mPassengerInfo.goodeval_rate_desc];
    [_lblCount setText:mPassengerInfo.carpool_count_desc];
    
    if(mPassengerInfo.sex == 0) {
        [_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
    }
    else {
        [_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
        _lblAge.textColor = MYCOLOR_GREEN;
    }

}

/**
 *  集成刷新控件
 */
- (void)setupRefresh
{
    // 1.下拉刷新(进入刷新状态就会调用self的headerRereshing)
    [_tblEval addHeaderWithTarget:self action:@selector(headerRefreshing)];
#warning 自动刷新(一进入程序就下拉刷新)
    //[_tableView headerBeginRefreshing];
    
    // 2.上拉加载更多(进入刷新状态就会调用self的footerRereshing)
    [_tblEval addFooterWithTarget:self action:@selector(footerRefreshing)];
    
    // 设置文字(也可以不设置,默认的文字在MJRefreshConst中修改)
    _tblEval.headerPullToRefreshText = MSG_TBLHEADER_PULL;
    _tblEval.headerReleaseToRefreshText = MSG_TBLHEADER_RELEASE;
    _tblEval.headerRefreshingText = MSG_TBLHEADER_REFRESHING;
    
    _tblEval.footerPullToRefreshText = MSG_TBLFOOTER_PULL;
    _tblEval.footerReleaseToRefreshText = MSG_TBLFOOTER_RELEASE;
    _tblEval.footerRefreshingText = MSG_TBLFOOTER_REFRESHING;
}


#pragma mark Single time order table refreshing

- (void)headerRefreshing
{
   // [mEvalArray addObject:[self createRandomValue]];
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2.0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self updateUI];
    });
    
	TEST_NETWORK_RETURN
	
    [[[CommManager getCommMgr] orderSvcMgr] GetPassengerLatestEvalInfo:[Common getUserID] PassId:self.mPassID LimitId:mLastEvalId DevToken:[Common getDeviceMacAddress]];
}

- (void)footerRefreshing
{
   // [mEvalArray addObject:[self createRandomValue]];
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2.0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self updateUI];
    });
    
    [[[CommManager getCommMgr] orderSvcMgr] GetPassengerPagedEvalInfo:[Common getUserID] PassId:self.mPassID PageNo:mCurEvalPageNo DevToken:[Common getDeviceMacAddress]];
}

- (void) updateUI
{
    [_tblEval reloadData];
        
    [_tblEval headerEndRefreshing];
    [_tblEval footerEndRefreshing];
}


////////////////////////////////////////////////////////////////////////////////////////////////////
- (STPassengerEvalInfo *) createRandomValue
{
    STPassengerEvalInfo * oneItem = [[STPassengerEvalInfo alloc] init];
    
    oneItem.uid = 100;
    oneItem.driver_name = @"张先生";
    oneItem.eval = 1;
    oneItem.eval_desc = @"好评!";
    oneItem.contents = @"默认好评";
    oneItem.time = @"6月20日 18:00";
    
    return oneItem;
}


////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Standard TableView delegates

////////////////////////////////////////////////////////////////////////////////////////////////////
- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (tableView == _tblEval) {
        
        if (mEvalArray == nil) {
            return 0;
        }
        
        return mEvalArray.count;
    }
    
    return 0;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	//return height1;
	return CELL_HEIGHT;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *cellIdentifier = EVAL_CELLID;
    
    Drv_PassEvalCell * cell = (Drv_PassEvalCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    STPassengerEvalInfo * dataInfo = [mEvalArray objectAtIndex:indexPath.row];
    [cell._lblName setText:dataInfo.driver_name];
    [cell._lblEvalState setText:dataInfo.eval_desc];
    [cell._lblContents setText:dataInfo.contents];
    [cell._lblTime setText:dataInfo.time];
    
    if (dataInfo.eval == EVAL_HIGH) {
        [cell._lblEvalState setTextColor:EVAL_HIGH_COLOR];
    } else if (dataInfo.eval == EVAL_MEDIUM) {
        [cell._lblEvalState setTextColor:EVAL_MEDIUM_COLOR];
    } else {
        [cell._lblEvalState setTextColor:EVAL_LOW_COLOR];
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
    [self dismissViewControllerAnimated:YES completion:nil];
}


#pragma mark - OrderSvcMgr Delegate Methods
- (void) getPassengerInfoResult:(NSString *)result PassengerInfo:(STPassengerInfo *)passenger_info
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        mPassengerInfo = passenger_info;
        
        [_imgUser setImageWithURL:[NSURL URLWithString:passenger_info.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
        if(passenger_info.sex == 0) {
            [_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
        }
        else {
            [_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
            _lblAge.textColor = MYCOLOR_GREEN;
        }
        [_lblAge setText:[NSString stringWithFormat:@"%d", passenger_info.age]];
        [_lblName setText:[NSString stringWithFormat:@"%@", passenger_info.name]];
        [_lblVerifDesc setText:passenger_info.pverified_desc];
        [_lblEvalRate setText:passenger_info.goodeval_rate_desc];
        [_lblCount setText:passenger_info.carpool_count_desc];
        if (passenger_info.pverified == 0) {
            [_markVerif setEnabled:NO];
        } else {
            [_markVerif setEnabled:YES];
        }
        
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void) getPassengerLatestEvalInfoResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [mEvalArray addObjectsFromArray:dataList];
        
        if([dataList count] > 0) {
            STPassengerEvalInfo *evalInfo = [dataList objectAtIndex:0];
            mLastEvalId = evalInfo.uid;
        }
        
        [_tblEval reloadData];
    }
    else
    {
    }
}

- (void) getPassengerPagedEvalInfoResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        if (mCurPageNO !=mCurEvalPageNo) {
            [mEvalArray addObjectsFromArray:dataList];
        }
        if (mCurEvalPageNo ==0) {
            [mEvalArray removeAllObjects];
            [mEvalArray addObjectsFromArray:dataList];
        }
        mCurPageNO = mCurEvalPageNo;
        if([dataList count] >= 10)
            mCurEvalPageNo++;
    }
    else
    {
    }
    [_tblEval reloadData];
}

@end






