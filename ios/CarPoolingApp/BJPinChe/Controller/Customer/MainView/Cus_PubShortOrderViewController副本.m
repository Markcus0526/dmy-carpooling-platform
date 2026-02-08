//
//  Cus_PubShortOrderViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Cus_PubShortOrderViewController.h"
#import "Cus_PubOrderWaitingViewController.h"
#import "iflyMSC/IFlySpeechConstant.h"
#import "iflyMSC/IFlySpeechUtility.h"
#import "iflyMSC/IFlyRecognizerView.h"
#import "Cus_LoginViewController.h"
#import "LoginVC.h"
@interface Cus_PubShortOrderViewController ()

@end


#define TABCOUNT                    2
#define ADDR_TAG_STSTART            1
#define ADDR_TAG_STEND              2
#define ADDR_TAG_WTSTART            3
#define ADDR_TAG_WTEND              4
#define MIDPOINT_TAG_ST             1
#define MIDPOINT_TAG_WT             2


@implementation Cus_PubShortOrderViewController

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
    self.navigationController.navigationBar.translucent =YES;
    //因为有scrollview
    self.automaticallyAdjustsScrollViewInsets =NO;
    [super loadView];
    
    
//      [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
   // self.hidesBottomBarWhenPushed =YES;
    self.navigationController.navigationBar.translucent =YES;
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
    mCurTab = 0;
    
    _txtSTNote.delegate = self;
    _txtSTValue.delegate = self;
    _txtWTNote.delegate = self;
    _txtWTValue.delegate = self;
    
    // initialize news view frame
    CGRect rcFrame = _vwSingleOrder.frame;
	rcFrame = CGRectMake(_vwSingleOrder.frame.size.width, rcFrame.origin.y, rcFrame.size.width, rcFrame.size.height);
	_vwWorkOrder.frame = rcFrame;
    
    // initialize main scroll content size
    [_scrollMain setContentSize:CGSizeMake(_vwSingleOrder.frame.size.width * 2, _vwSingleOrder.frame.size.height)];
    
    // make tap recognizer to hide keyboard
    UITapGestureRecognizer * tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapBackGround:)];
    
    [self.view addGestureRecognizer:tapRecognizer];
    
    mSTCarType = 1;
    mWTCarType = 1;
    
    
    mSTPrice = 100;
    [_txtSTValue setText:[NSString stringWithFormat:@"%.2f", mSTPrice]];
    mWTPrice = 100;
    [_txtWTValue setText:[NSString stringWithFormat:@"%.2f", mWTPrice]];
    
    //---- initialize voice recognizer
    iflyRecognizerView= [[IFlyRecognizerView alloc] initWithCenter:self.view.center];
    iflyRecognizerView.delegate = self;
    
    [iflyRecognizerView setParameter: @"iat" forKey:[IFlySpeechConstant IFLY_DOMAIN]];
    [iflyRecognizerView setParameter: @"asr.pcm" forKey:[IFlySpeechConstant ASR_AUDIO_PATH]];
    
    // | result_type   | 返回结果的数据格式，可设置为json，xml，plain，默认为json。
    [iflyRecognizerView setParameter:@"plain" forKey:[IFlySpeechConstant RESULT_TYPE]];
    
    //    [_iflyRecognizerView setParameter:@"asr_audio_path" value:nil];   当你再不需要保存音频时，请在必要的地方加上这行。
	
	// setting start time
	NSDate *date = [NSDate dateWithTimeIntervalSinceNow:60 * 15];
	[_datePicker setDate:date];
	NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
    NSString *destDateString = [dateFormatter stringFromDate:date];
    [_btnSTTime setTitle:destDateString forState:UIControlStateNormal];
    
    [dateFormatter setDateFormat:@"HH:mm"];
    destDateString = [dateFormatter stringFromDate:date];
    [_btnWTTime setTitle:destDateString forState:UIControlStateNormal];

}

/**
 * Hide keyboard when tapped background
 */
- (void) handleTapBackGround:(id)sender
{
    [self.view endEditing:YES];
}

- (void) showWTSuccessView
{
    _wtAlertView.hidden = NO;
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

- (void) updateUI : (STUserInfo *)userInfo
{
    
}


///////////////////////////////////////////////////////////////////////////
#pragma mark - Address selector delegation
- (void) onSelectedAddress:(STBaiduAddrInfo *)addrInfo parentTag:(NSInteger)parentTag
{
    switch (parentTag) {
        case ADDR_TAG_STSTART:
            mSTStartAddr = addrInfo;
            // show selected address
            [_lblSTStartAddr setText:addrInfo.name];
            break;
        case ADDR_TAG_STEND:
            mSTEndAddr = addrInfo;
            // show selected address
            [_lblSTEndAddr setText:addrInfo.name];
            break;
        case ADDR_TAG_WTSTART:
            mWTStartAddr = addrInfo;
            // show selected address
            [_lblWTStartAddr setText:addrInfo.name];
            break;
        case ADDR_TAG_WTEND:
            mWTEndAddr = addrInfo;
            // show selected address
            [_lblWTEndAddr setText:addrInfo.name];
            break;
        default:
            break;
    }
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - Mid point selector delegation
- (void) selectedMidPoints:midPoints parentTag:(NSInteger)parentTag
{
    if (parentTag == MIDPOINT_TAG_ST) {
        mSTMidPoints = midPoints;
        // set mid point count
        [_lblSTMidPointCnt setText:[NSString stringWithFormat:@"%d", mSTMidPoints.count]];
    } else {
        mWTMidPoints = midPoints;
        // set mid point count
        [_lblWTMidPointCnt setText:[NSString stringWithFormat:@"%d", mWTMidPoints.count]];
    }
}


///////////////////////////////////////////////////////////////////////////
#pragma mark - IFlyRecognizerViewDelegate 飞讯代理方法
/** 识别结果回调方法
 @param resultArray 结果列表
 @param isLast YES 表示最后一个，NO表示后面还有结果
 */
- (void)onResult:(NSArray *)resultArray isLast:(BOOL)isLast
{
    NSMutableString *result = [[NSMutableString alloc] init];
    NSDictionary *dic = [resultArray objectAtIndex:0];
    for (NSString *key in dic) {
        [result appendFormat:@"%@",key];
    }
    
    MyLog(@"voice : %@", result);
    
    if ([result length] > 0) {
        // show address selector view controller & set voice text
       // AddrSelectorViewController * controller = [self.storyboard instantiateViewControllerWithIdentifier:@"addrselector"];
        AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
        
        controller.mParentTag = mSelectedVoiceTag;
        controller.defText = result;
        controller.delegate = self;
        
        [self presentModalViewController:controller animated:YES];
    }
}

/** 识别结束回调方法
 @param error 识别错误
 */
- (void)onError:(IFlySpeechError *)error
{
    MyLog(@"errorCode:%d",[error errorCode]);
}



///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

// main control event
/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    //BACK_VIEW;
    [self.navigationController popViewControllerAnimated:YES];
}

/**
 * Clicked first tab ( user info edit tab )
 */
- (IBAction)onClickedView1:(id)sender
{
    [_scrollMain scrollRectToVisible:CGRectMake(0, 0, _vwSingleOrder.frame.size.width, _vwSingleOrder.frame.size.height) animated:YES];
}

/**
 * Clicked second tab ( change password tab )
 */
- (IBAction)onClickedView2:(id)sender
{
    [_scrollMain scrollRectToVisible:CGRectMake(_vwSingleOrder.frame.size.width, 0, _vwSingleOrder.frame.size.width, _vwSingleOrder.frame.size.height) animated:YES];
}

- (IBAction)onClickedSTPublish:(id)sender
{
	// check login state
    if ([Common getUserInfo] == nil)
    {
        // go to login view controller
#warning 要改
//        Cus_LoginViewController *viewController = (Cus_LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
//        
//        [self presentViewController:viewController animated:YES completion:nil];
		LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
        [self presentViewController:loginVC animated:YES completion:nil];
		return;
    }
	
    NSString *strMidPoints = @"";
    for(STBaiduAddrInfo *midPoint in mSTMidPoints)
    {
        if(midPoint != nil)
        {
            if([strMidPoints length] <= 0)
            {
                strMidPoints = [NSString stringWithFormat:@"%f, %f, %@", midPoint.lat, midPoint.lng, midPoint.name];
            }
            else
            {
                strMidPoints = [NSString stringWithFormat:@"%@, %f, %f, %@", strMidPoints, midPoint.lat, midPoint.lng, midPoint.name];
            }
        }
    }
    
    mSTPrice = [_txtSTValue.text doubleValue];
    
    [SVProgressHUD show];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] publishOnceOrder:[Common getUserID] StartAddr:mSTStartAddr.name StartLat:mSTStartAddr.lat StartLng:mSTStartAddr.lng EndAddr:mSTEndAddr.name EndLat:mSTEndAddr.lat EndLng:mSTEndAddr.lng StartTime:[_btnSTTime titleLabel].text  MidPoints:strMidPoints Remark:_txtSTNote.text ReqStyle:mSTCarType Price:mSTPrice City:[Common getCurrentCity] WaitTime:0 DevToken:[Common getDeviceMacAddress]];
    //Cus_PubOrderWaitingViewController * viewCtrl = [self.storyboard instantiateViewControllerWithIdentifier:@"PubOrderWating"];
    //SHOW_VIEW(viewCtrl);
	
	mOrderPubData = [[STOnceOrderPubData alloc] init];
	mOrderPubData.start_addr = mSTStartAddr.name;
	mOrderPubData.start_lat = mSTStartAddr.lat;
	mOrderPubData.start_lng = mSTStartAddr.lng;
	mOrderPubData.end_addr = mSTEndAddr.name;
	mOrderPubData.end_lat = mSTEndAddr.lat;
	mOrderPubData.end_lng = mSTEndAddr.lng;
	mOrderPubData.start_time = [_btnSTTime titleLabel].text;
	mOrderPubData.mid_points = strMidPoints;
	mOrderPubData.remark = _txtSTNote.text;
	mOrderPubData.req_style = mSTCarType;
	mOrderPubData.price = mSTPrice;
	mOrderPubData.city = [Common getCurrentCity];
	mOrderPubData.wait_time = 0;
}


- (IBAction)onClickedWTPublish:(id)sender
{
	// check login state
    if ([Common getUserInfo] == nil)
    {
        // go to login view controller

        LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
        [self presentViewController:loginVC animated:YES completion:nil];
		
		return;
    }
	
	
    NSString *strMidPoints = @"";
    for(STBaiduAddrInfo *midPoint in mWTMidPoints)
    {
        if([strMidPoints length] <= 0)
        {
            strMidPoints = [NSString stringWithFormat:@"%f,%f,%@", midPoint.lat, midPoint.lng, midPoint.name];
        }
        else
        {
            strMidPoints = [NSString stringWithFormat:@"%@,%f,%f,%@", strMidPoints, midPoint.lat, midPoint.lng, midPoint.name];
        }
    }
    
    NSString *strDays = @"";
    for(int i = 100; i <= 106; i++)
    {
        UIButton * btnDay = (UIButton *)[self.view viewWithTag:i];
        if([btnDay isSelected])
        {
            if([strDays length] <= 0)
            {
                strDays = [NSString stringWithFormat:@"%d", i - 100];
            }
            else
            {
                strDays = [NSString stringWithFormat:@"%@,%d", strDays, i - 100];
            }
        }
    }
    
    mWTPrice = [_txtWTValue.text doubleValue];
    
    // make temp date using time
    NSString * tempDate = [NSString stringWithFormat:@"2000-01-01 %@", _btnWTTime.titleLabel.text];
    
    [SVProgressHUD show];
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] publishOnOffOrder:[Common getUserID] StartAddr:mWTStartAddr.name StartLat:mWTStartAddr.lat StartLng:mWTStartAddr.lng EndAddr:mWTEndAddr.name EndLat:mWTEndAddr.lat EndLng:mWTEndAddr.lng StartTime:tempDate MidPoints:strMidPoints Remark:_txtWTNote.text ReqStyle:mWTCarType Price:mWTPrice Days:strDays City:[Common getCurrentCity] DevToken:[Common getDeviceMacAddress]];
    //Cus_PubOrderWaitingViewController * viewCtrl = [self.storyboard instantiateViewControllerWithIdentifier:@"PubOrderWating"];
    //SHOW_VIEW(viewCtrl);
}


///////////////////////////// single time order control event //////////////////////////
#pragma mark 单次拼车 起始详细地点
- (IBAction)onClickedSTStartPos:(id)sender
{
    // show address selector view controller
 //   AddrSelectorViewController * controller = [self.storyboard instantiateViewControllerWithIdentifier:@"addrselector"];
#warning 测试修改
    
    AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
    
    controller.mParentTag = ADDR_TAG_STSTART;
    controller.delegate = self;
    
    [self presentModalViewController:controller animated:YES];
}
#pragma mark 单次拼车 目的地详细地点
- (IBAction)onClickedSTEndPos:(id)sender
{
    // show address selector view controller
   // AddrSelectorViewController * controller = [self.storyboard instantiateViewControllerWithIdentifier:@"addrselector"];
    
    AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
    
    controller.mParentTag = ADDR_TAG_STEND;
    controller.delegate = self;
    
    [self presentModalViewController:controller animated:YES];
}
#pragma mark 单次拼车 起始 语音
- (IBAction)onClickedSTStartPosVoice:(id)sender
{
    mSelectedVoiceTag = ADDR_TAG_STSTART;
    
    [iflyRecognizerView start];
    MyLog(@"start listenning...");
}
#pragma mark 单次拼车 目的地 语音
- (IBAction)onClickedSTEndPosVoice:(id)sender
{
    mSelectedVoiceTag = ADDR_TAG_STEND;
    
    [iflyRecognizerView start];
    MyLog(@"start listenning...");
}
#pragma mark 单次拼车  时间选择控件
- (IBAction)onClickedSTTime:(id)sender
{
    _datePicker.datePickerMode = UIDatePickerModeDateAndTime;
    [_pickerView setHidden:NO];
}

#pragma mark 单次拼车 车型选择
- (IBAction)onClickedSTCarType1:(id)sender
{
    [_st_carType1 setSelected:YES];
    [_st_carType2 setSelected:NO];
    [_st_carType3 setSelected:NO];
    [_st_carType4 setSelected:NO];
    // change text color
    [_st_carType1name setTextColor:MYCOLOR_GREEN];
    [_st_carType2name setTextColor:[UIColor blackColor]];
    [_st_carType3name setTextColor:[UIColor blackColor]];
    [_st_carType4name setTextColor:[UIColor blackColor]];
    
    mSTCarType = 1;
}

- (IBAction)onClickedSTCarType2:(id)sender
{
    [_st_carType1 setSelected:NO];
    [_st_carType2 setSelected:YES];
    [_st_carType3 setSelected:NO];
    [_st_carType4 setSelected:NO];
    // change text color
    [_st_carType1name setTextColor:[UIColor blackColor]];
    [_st_carType2name setTextColor:MYCOLOR_GREEN];
    [_st_carType3name setTextColor:[UIColor blackColor]];
    [_st_carType4name setTextColor:[UIColor blackColor]];
    
    mSTCarType = 2;
}

- (IBAction)onClickedSTCarType3:(id)sender
{
    [_st_carType1 setSelected:NO];
    [_st_carType2 setSelected:NO];
    [_st_carType3 setSelected:YES];
    [_st_carType4 setSelected:NO];
    // change text color
    [_st_carType1name setTextColor:[UIColor blackColor]];
    [_st_carType2name setTextColor:[UIColor blackColor]];
    [_st_carType3name setTextColor:MYCOLOR_GREEN];
    [_st_carType4name setTextColor:[UIColor blackColor]];
    
    mSTCarType = 3;
}

- (IBAction)onClickedSTCarType4:(id)sender
{
    [_st_carType1 setSelected:NO];
    [_st_carType2 setSelected:NO];
    [_st_carType3 setSelected:NO];
    [_st_carType4 setSelected:YES];
    // change text color
    [_st_carType1name setTextColor:[UIColor blackColor]];
    [_st_carType2name setTextColor:[UIColor blackColor]];
    [_st_carType3name setTextColor:[UIColor blackColor]];
    [_st_carType4name setTextColor:MYCOLOR_GREEN];
    
    mSTCarType = 4;
}
#pragma mark 单次拼车 报价 减号按钮
- (IBAction)onClickedSTMinus:(id)sender
{
    mSTPrice -= 1;
    if(mSTPrice <= 0)
        mSTPrice = 0;
    [_txtSTValue setText:[NSString stringWithFormat:@"%.2f", mSTPrice]];
}
#pragma mark 单次拼车 报价 加好按钮
- (IBAction)onClickedSTPlus:(id)sender
{
    mSTPrice += 1;
    if(mSTPrice >= 1000)
        mSTPrice = 1000;
    [_txtSTValue setText:[NSString stringWithFormat:@"%.2f", mSTPrice]];
}

////////////////////////////// work time order control event  上下班拼车 定制页面////////////////////////////
#pragma mark 上下班拼车 起始详细地点输入
- (IBAction)onClickedWTStartPos:(id)sender
{
    // show address selector view controller
  //  AddrSelectorViewController * controller = [self.storyboard instantiateViewControllerWithIdentifier:@"addrselector"];
    
    AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
    
    controller.mParentTag = ADDR_TAG_WTSTART;
    controller.delegate = self;
    
    [self presentModalViewController:controller animated:YES];
}
#pragma mark 上下班拼车 目的地详细地点输入
- (IBAction)onClickedWTEndPos:(id)sender
{
    // show address selector view controller
   // AddrSelectorViewController * controller = [self.storyboard instantiateViewControllerWithIdentifier:@"addrselector"];
    
    AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
    
    controller.mParentTag = ADDR_TAG_WTEND;
    controller.delegate = self;
    
    [self presentModalViewController:controller animated:YES];
}

/**
 * voice recognize address
 */
#pragma mark 上下班拼车 语音
- (IBAction)onClickedWTStartPosVoice:(id)sender
{
    mSelectedVoiceTag = ADDR_TAG_WTSTART;
    
    [iflyRecognizerView start];
    MyLog(@"start listenning...");
}

- (IBAction)onClickedWTEndPosVoice:(id)sender
{
    mSelectedVoiceTag = ADDR_TAG_WTEND;
    
    [iflyRecognizerView start];
    MyLog(@"start listenning...");
}

- (IBAction)onClickedWTTime:(id)sender
{
    _datePicker.datePickerMode = UIDatePickerModeCountDownTimer;
    [_pickerView setHidden:NO];
}


- (IBAction)onClickedWTCarType1:(id)sender
{
    [_wt_carType1 setSelected:YES];
    [_wt_carType2 setSelected:NO];
    [_wt_carType3 setSelected:NO];
    [_wt_carType4 setSelected:NO];
    // change text color
    [_wt_carType1name setTextColor:MYCOLOR_GREEN];
    [_wt_carType2name setTextColor:[UIColor blackColor]];
    [_wt_carType3name setTextColor:[UIColor blackColor]];
    [_wt_carType4name setTextColor:[UIColor blackColor]];
    
    mWTCarType = 1;
}

- (IBAction)onClickedWTCarType2:(id)sender
{
    [_wt_carType1 setSelected:NO];
    [_wt_carType2 setSelected:YES];
    [_wt_carType3 setSelected:NO];
    [_wt_carType4 setSelected:NO];
    // change text color
    [_wt_carType1name setTextColor:[UIColor blackColor]];
    [_wt_carType2name setTextColor:MYCOLOR_GREEN];
    [_wt_carType3name setTextColor:[UIColor blackColor]];
    [_wt_carType4name setTextColor:[UIColor blackColor]];
    
    mWTCarType = 2;
}

- (IBAction)onClickedWTCarType3:(id)sender
{
    [_wt_carType1 setSelected:NO];
    [_wt_carType2 setSelected:NO];
    [_wt_carType3 setSelected:YES];
    [_wt_carType4 setSelected:NO];
    // change text color
    [_wt_carType1name setTextColor:[UIColor blackColor]];
    [_wt_carType2name setTextColor:[UIColor blackColor]];
    [_wt_carType3name setTextColor:MYCOLOR_GREEN];
    [_wt_carType4name setTextColor:[UIColor blackColor]];
    
    mWTCarType = 3;
}

- (IBAction)onClickedWTCarType4:(id)sender
{
    [_wt_carType1 setSelected:NO];
    [_wt_carType2 setSelected:NO];
    [_wt_carType3 setSelected:NO];
    [_wt_carType4 setSelected:YES];
    // change text color
    [_wt_carType1name setTextColor:[UIColor blackColor]];
    [_wt_carType2name setTextColor:[UIColor blackColor]];
    [_wt_carType3name setTextColor:[UIColor blackColor]];
    [_wt_carType4name setTextColor:MYCOLOR_GREEN];
    
    mWTCarType = 4;
}

- (IBAction)onClickedWTMinus:(id)sender
{
    mWTPrice -= 1;
    if(mWTPrice <= 0)
        mWTPrice = 0;
    [_txtWTValue setText:[NSString stringWithFormat:@"%.2f", mWTPrice]];
}

- (IBAction)onClickedWTPlus:(id)sender
{
    mWTPrice += 1;
    if(mWTPrice >= 1000)
        mWTPrice = 1000;
    [_txtWTValue setText:[NSString stringWithFormat:@"%.2f", mWTPrice]];
}

- (IBAction)onSelectDay:(id)sender
{
    UIButton * btnDay = (UIButton *)sender;
    
    if ([btnDay isSelected]) {
        [btnDay setSelected:NO];
    } else {
        [btnDay setSelected:YES];
    }
}



- (IBAction)onClickedPickerOK:(id)sender
{
    NSDate *selected = [_datePicker date];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    
    if (mCurTab == 0) {
        [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
        NSString *destDateString = [dateFormatter stringFromDate:selected];
        [_btnSTTime setTitle:destDateString forState:UIControlStateNormal];
    } else if(mCurTab == 1){
        [dateFormatter setDateFormat:@"HH:mm"];
        NSString *destDateString = [dateFormatter stringFromDate:selected];
        [_btnWTTime setTitle:destDateString forState:UIControlStateNormal];
    }
    
    
    [_pickerView setHidden:YES];
}

- (IBAction)onClickedPickerCancel:(id)sender
{
    [_pickerView setHidden:YES];
}

// st alert view background
- (IBAction)onClickedAlertBG:(id)sender
{
    UIViewController *viewController = (UIViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];
    
    SHOW_VIEW(viewController);
}

///////////////////////////////////////////////////////////////////
#pragma mark - Prepare for Segue
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    NSString * ST_SEGUE_ID = @"segue_st_midpoint";
    NSString * WT_SEGUE_ID = @"segue_wt_midpoint";
    
    if ([segue.identifier isEqualToString:ST_SEGUE_ID])
    {
        ((Cus_SetMidPointViewController *)segue.destinationViewController).mParentTag = MIDPOINT_TAG_ST;
        ((Cus_SetMidPointViewController *)segue.destinationViewController).delegate = self;
    } else if ([segue.identifier isEqualToString:WT_SEGUE_ID])
    {
        ((Cus_SetMidPointViewController *)segue.destinationViewController).mParentTag = MIDPOINT_TAG_WT;
        ((Cus_SetMidPointViewController *)segue.destinationViewController).delegate = self;
    }
    
}

///////////////////////////////////////////////////////////////////
#pragma mark - Scroll When Keyboard Focus

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    curTextField = textField;
    if (keyboardVisible)
        [KeyboardHelper moveScrollView:curTextField scrollView:(UIScrollView*)self.view];
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    curTextField = nil;
    [textField resignFirstResponder];
}

- (void)textViewDidBeginEditing:(UITextView *)textView
{
    curTextField = textView;
    if (keyboardVisible)
        [KeyboardHelper moveScrollView:curTextField scrollView:(UIScrollView*)self.view];
}

- (void)textViewDidEndEditing:(UITextView *)textView
{
    curTextField = nil;
    [textView resignFirstResponder];
}

- (void)keyboardWillShow:(NSNotification *)notification
{
    //---gets the size of the keyboard---
    NSDictionary *userInfo = [notification userInfo];
    NSValue *keyboardValue = [userInfo objectForKey:UIKeyboardBoundsUserInfoKey];
    [keyboardValue getValue:&keyboardBounds];
    
	[KeyboardHelper moveScrollView:curTextField scrollView:(UIScrollView*)self.view];
    
    keyboardVisible = true;
}

- (void)keyboardWillHide:(NSNotification *)notification
{
    //---gets the size of the keyboard---
    NSDictionary *userInfo = [notification userInfo];
    NSValue *keyboardValue = [userInfo objectForKey:UIKeyboardBoundsUserInfoKey];
    [keyboardValue getValue:&keyboardBounds];
    
    [KeyboardHelper moveScrollView:nil scrollView:(UIScrollView*)self.view];
    
    keyboardVisible = false;
    
    curTextField = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) name:UIKeyboardWillShowNotification object:self.view.window];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillHide:) name:UIKeyboardWillHideNotification object:self.view.window];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification object:nil];
}

#pragma OrderSvcDelegate Methods
- (void) publishOnceOrderResult:(NSString *)result RetData:(STPubOnceOrderRet *)retData
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
       // Cus_PubOrderWaitingViewController * viewCtrl = [self.storyboard instantiateViewControllerWithIdentifier:@"PubOrderWating"];
        //修改至xib
        Cus_PubOrderWaitingViewController *viewCtrl =[[Cus_PubOrderWaitingViewController alloc]initWithNibName:@"Cus_PubOrderWaiting" bundle:nil];
        
		viewCtrl.mOrderId = retData.order_id;
		viewCtrl.mPrice = [_txtSTValue.text doubleValue];
		viewCtrl.mWaitTime = retData.wait_time;
		viewCtrl.mRepubTimes = 0;
		viewCtrl.mOrderPubData = mOrderPubData;
		
        SHOW_VIEW(viewCtrl);
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void) publishOnOffOrderResult:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
		[SVProgressHUD dismissWithSuccess:@"上下班订单发布成功" afterDelay:DEF_DELAY];
		BACK_VIEW;
        // show success
        //[self showWTSuccessView];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void)checkOnceOrderAcceptanceResult:(NSString *)result DriverInfo:(STDriverInfo *)driverInfo OrderInfo:(STSingleTimeOrderInfo *)orderInfo
{
#warning 这里要做什么
}

@end
