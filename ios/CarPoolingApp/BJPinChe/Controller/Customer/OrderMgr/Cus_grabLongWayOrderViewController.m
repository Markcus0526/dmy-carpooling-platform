//
//  Cus_grabLongWayOrderViewController.m
//  BJPinChe
//
//  Created by CKK on 14-8-25.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//
/**
 *  乘客身份 长途抢单VC
 *
 *  @return
 */
#import "Cus_grabLongWayOrderViewController.h"
#import "LongWayOrderSvcMgr.h"
#import "GrabLongWayOrderTableViewCell.h"
#import "Cus_LongOrderSureViewController.h"
#import "MJRefresh.h"
#import "UIViewController+CWPopup.h"
#import "Cus_longOrderSuccessViewController.h"
#import "Cus_NotEnoughViewController.h"
#import "Cus_ChargeViewController.h"
#import "ResultDlgView.h"

@interface Cus_grabLongWayOrderViewController ()

@end

@implementation Cus_grabLongWayOrderViewController
{
    NSMutableArray * _dataArray;//数据源
    LongWayOrderSvcMgr * longWayOrderSvcMgr;
    int _currentPageNO;
    UILabel * _selectNumberLabel;
    long _currentOrderId;
    NSString * _password;
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
    self.view.backgroundColor = [UIColor whiteColor];
    
    if ([Common getCurrentCity] !=nil) {
        _startCityTextField.text = [Common getCurrentCity];
    }
    
    //创建数据源
    [self createDataSource];
    //安装刷新
    [self setupRefresh];
    
    self.picker.delegate = self;
    self.picker.dataSource = self;
    
    [self footerRefreshing];
    [self.view endEditing:YES];

	[self headerRefreshing];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
    
}


#pragma mark - 数据源
- (void)createDataSource
{
	TEST_NETWORK_RETURN;

	_dataArray = [[NSMutableArray alloc]init];
    _pickerArray = [[NSMutableArray alloc]init];
    longWayOrderSvcMgr = [[CommManager getCommMgr] longWayOrderSvcMgr];
    longWayOrderSvcMgr.delegate = self;
}



#pragma mark - 响应事件

//

- (IBAction)OnDatePickerCancelClick:(id)sender
{
    [self.datePickerView setHidden:YES];
}
- (IBAction)OnDatePickerSureClick:(id)sender
{
    _selectNumberLabel.text = [NSString stringWithFormat:@"%d 座",[self.picker selectedRowInComponent:0] + 1];
    [self.datePickerView setHidden:YES];
}

//点击下拉框
-(void)OnSelectNumberClickWithNumber:(int)seats WithLabel:(UILabel *)selectNumLabel
{
    NSMutableArray * tmpPickerArray = [[NSMutableArray alloc]init];
    for (int i = 1; i <= seats; i ++) {
        [tmpPickerArray addObject:[NSString stringWithFormat:@"%d 座",i]];
    }
    [_pickerArray setArray:tmpPickerArray];
    _selectNumberLabel = selectNumLabel;
    
    [self.picker reloadAllComponents];
    
    [self.datePickerView setHidden:NO];
}



#pragma mark 点击搜索
- (IBAction)onSearchButtonClick:(id)sender {
    _currentPageNO = 0;
    [self footerRefreshing];
    [self.view endEditing:YES];
}
- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
  [self.view endEditing:YES];
}
//返回按钮

- (IBAction)onBackClick:(id)sender {
    //BACK_VIEW; 现在的按钮不起作用，实际起作用的是backbarItem
    
}

#pragma mark 点击“抢”按钮
- (void)onAcceptClickWithWithLongOrderInfo:(STAcceptableLongOrderInfo *) longOrderInfo AndSeatsCount:(int)count
{
	_currentOrderId = (long)longOrderInfo.uid;
    //发送抢订单信息至服务器
    [longWayOrderSvcMgr AcceptLongOrderWithUserID:[Common getUserID] AndOrderId:longOrderInfo.uid AndSeatsCount:count];
}


//点击“抢”按钮回调函数
- (void)AcceptLongOrder:(int)retcode retmsg:(NSString *)result dataList:(NSMutableArray *)dataList
{
	//抢座成功
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		PasswordCheckModal *popup = [[PasswordCheckModal alloc] initWithNibName:@"PasswordCheckModal" bundle:nil];
		popup.delegate = self;

		//电子票界面弹出
		[self presentPopupViewController:popup animated:YES completion:^(void) {
			MyLog(@"popup view presented");
		}];
	}
	else if (retcode == NotEnoughLvDian)			// 抢座失败
	{
		Cus_NotEnoughViewController * notEnoughView = [[Cus_NotEnoughViewController alloc]initWithNibName:@"Cus_NotEnoughViewController" bundle:nil];

		STGrabLongOrderFailInfo * failInfo = dataList[0];
		notEnoughView.leftPrice = [NSString stringWithFormat:@"%@",failInfo.rembal];
		notEnoughView.needPrice = [NSString stringWithFormat:@"%@",failInfo.total_fee];
		notEnoughView.delegate = self;

		[self presentPopupViewController:notEnoughView animated:YES completion:^(void) {
			MyLog(@"Cus_NotEnoughViewController's view presented");
		}];
	}
	else if (retcode == SVCERR_ALREADY_ACCEPTED)
	{
		[ResultDlgView showInController:self success:false message:@"手太慢, 座位已经不足了"];
	}
	else
	{
		[self.view makeToast:result duration:2 position:@"center"];
	}
}
//点击绿点不足页面的按钮
-(void)Cus_notEnoughViewChargeOrNot:(BOOL)chargeOrNot
{
    if (chargeOrNot) {
        
        if (self.popupViewController != nil) {
            [self dismissPopupViewControllerAnimated:YES completion:^{
                MyLog(@"popup view dismissed");
                //跳转到充值页面
#warning 添加
                Cus_ChargeViewController *charge =[[Cus_ChargeViewController alloc]initWithNibName:@"ChargeView" bundle:nil];
                  //   UIViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"Charge"];
                     [self presentViewController:charge animated:NO completion:nil];

            }];
        }
        
    }else{
        //点击取消
        // dismiss popup
        if (self.popupViewController != nil) {
            [self dismissPopupViewControllerAnimated:YES completion:^{
                MyLog(@"popup view dismissed");
            }];
        }

    }
}


//输入密码点击确定时的操作
- (void)tappedDone:(NSString *)result
{
    _password = result;
    [longWayOrderSvcMgr SetLongOrderPasswordWithUserID:[Common getUserID] AndOrderId:_currentOrderId AndPassword:result];
}


// 设定长途订单密码回调函数
-(void)SetLongOrderPassword:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        // dismiss popup
        if (self.popupViewController != nil) {
            [self dismissPopupViewControllerAnimated:YES completion:^{
                MyLog(@"popup view dismissed");
                Cus_longOrderSuccessViewController * successViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"LongOrderSuccess"];
                successViewController.successInfo = dataList[0];
                successViewController.successInfo.uid = [NSString stringWithFormat:@"%ld",_currentOrderId];
                
                SHOW_VIEW(successViewController);
                
            }];
        }
        
    }
    else
    {
    }

}


#pragma mark - 协议实现部分
///////////////////////////////////////////tableView/////////////////////////////////////////////////
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString * cellName = @"grabLongWayOrderCell";
    GrabLongWayOrderTableViewCell * LongWayCell = [_tableView dequeueReusableCellWithIdentifier:cellName];
    if (!LongWayCell) {
        LongWayCell = [[[NSBundle mainBundle]loadNibNamed:@"GrabLongWayOrderTableViewCell" owner:self options:nil]lastObject];
    }

	LongWayCell.model = _dataArray[indexPath.row];
    LongWayCell.parentView = self;
    [LongWayCell reloadData];

	return LongWayCell;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _dataArray.count;
}

//当cell被点击  进入长途拼车确认订单
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    MyLog(@"%d is onClick",indexPath.row);
    Cus_LongOrderSureViewController * longOrderSureViewController = (Cus_LongOrderSureViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"LongOrderSure"];
    longOrderSureViewController.orderId = ((STAcceptableLongOrderInfo *)_dataArray[indexPath.row]).uid;
    [self.navigationController pushViewController:longOrderSureViewController animated:YES];
    
    //SHOW_VIEW(longOrderSureViewController);
}

/////////////////////////////////////textfield///////////////////////////////////////////////////////
//当textField检测到return被点击，接收数据
-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    
    switch (textField.tag) {
        case 1:
        {
            UITextField * textField2 = (id)[self.view viewWithTag:2];
            [textField2 becomeFirstResponder];
            break;
        }
        case 2:
        {
            [textField resignFirstResponder];
            break;
        }
        default:
            break;
    }
    
    return YES;
}
/////////////////////////////////////数据请求议//////////////////////////////////////////////////
- (void) GetPagedAcceptableLongOrders : (NSString *)result dataList:(NSMutableArray *)dataList
{
	[_tableView headerEndRefreshing];
	[_tableView footerEndRefreshing];

	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        int page =_currentPageNO;
        if([dataList count] >= 10)
            _currentPageNO++;
        if (page != _currentPageNO) {
            [_dataArray addObjectsFromArray:dataList];
        }else
        {
            _currentPageNO = 0;
            _dataArray = dataList;
        }

		if (_dataArray.count == 0) {
			[self.view makeToast:@"暂时没有您要求的长途订单，个人中心可设定长途订单路线需求，有符合条件的订单时会马上通知您哦"];
		}

		// 刷新表格
        [_tableView reloadData];
	}
    else
    {
    }

}
- (void) GetLatestAcceptableLongOrders : (NSString *)result dataList:(NSMutableArray *)dataList
{
	[_tableView headerEndRefreshing];
	[_tableView footerEndRefreshing];

	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		for (STOrderInfo *orderInfo in dataList)
		{
			[_dataArray insertObject:orderInfo atIndex:0];
		}

		if (dataList.count == 0) {
			[self.view makeToast:@"没有更多订单啦" duration:2 position:@"center"];
		}

		if (_dataArray.count == 0) {
			[self.view makeToast:@"暂时没有您要求的长途订单，个人中心可设定长途订单路线需求，有符合条件的订单时会马上通知您哦"];
		}

		// 刷新表格
		[_tableView reloadData];
	}
    else
	{
		[self.view makeToast:result duration:2 position:@"center"];
	}
}

////////////////////////UIPickerView////////////////////////////////////////
// returns the number of 'columns' to display.
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return [_pickerArray count];
}

-(NSString*) pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return [_pickerArray objectAtIndex:row];
}

#pragma mark - 刷新相关


/**
 *  集成刷新控件
 */
- (void)setupRefresh
{
    // 1.下拉刷新(进入刷新状态就会调用self的headerRereshing)
    [_tableView addHeaderWithTarget:self action:@selector(headerRefreshing)];
//    #warning 自动刷新(一进入程序就下拉刷新)
//    [_tableView headerBeginRefreshing];
    
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



- (void)headerRefreshing
{
    NSString *latestOrderNum = @"";

    if([_dataArray count] > 0)
    {
        STAcceptableLongOrderInfo *firstInfo = [_dataArray objectAtIndex:0];
        latestOrderNum = [NSString stringWithFormat:@"%ld",firstInfo.uid];
    }else{
        [_tableView headerEndRefreshing];
        return;
    }
    
    NSString * startAddrString = ((UITextField *)[self.view viewWithTag:11]).text;
    NSString * endAddrString = ((UITextField *)[self.view viewWithTag:12]).text;
    
    [longWayOrderSvcMgr GetLatestAcceptableLongOrdersWithUserid:[Common getUserID] Limitid:latestOrderNum StartAddr:startAddrString EndAddr:endAddrString DevToken:[Common getDeviceMacAddress]];
}

- (void)footerRefreshing
{
    NSString * startAddrString = ((UITextField *)[self.view viewWithTag:11]).text;
    NSString * endAddrString = ((UITextField *)[self.view viewWithTag:12]).text;

    [longWayOrderSvcMgr GetPagedAcceptableLongOrdersWithUserid:[Common getUserID] PageNo:_currentPageNO StartAddr:startAddrString EndAddr:endAddrString DevToken:[Common getDeviceMacAddress]];
}



-(BOOL)textField:(UITextField*)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
	NSUInteger newLength = textField.text.length + string.length - range.length;
	int nMaxLength = 0;
	if (textField == _startCityTextField)
	{
		nMaxLength = 20;
	}
	else if (textField == _endCityTextField)
	{
		nMaxLength = 20;
	}
	
	return newLength <= nMaxLength;
}



@end
