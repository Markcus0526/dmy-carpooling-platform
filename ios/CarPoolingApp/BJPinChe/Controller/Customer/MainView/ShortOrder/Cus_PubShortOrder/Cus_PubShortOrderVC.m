//
//  Cus_PubShortOrderVC.m
//  BJPinChe
//
//  Created by APP_USER on 14-10-23.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "Cus_PubShortOrderVC.h"

#import "Cus_PubOrderWaitingViewController.h"
#import "iflyMSC/IFlySpeechConstant.h"
#import "iflyMSC/IFlySpeechUtility.h"
#import "iflyMSC/IFlyRecognizerView.h"
#import "LoginVC.h"
#import "SystemPriceInfoResult.h"
#import "Cus_PubShortNotEnoughVC.h"
#import "UIViewController+CWPopup.h"
#import "Cus_ChargeViewController.h"
#import "Config.h"

@interface Cus_PubShortOrderVC ()<Cus_PubShortNotEnoughDelegate>
@property(nonatomic,strong)STOnceOrderPubData *tempOnceOrder;
@property(nonatomic,strong)SystemPriceInfoResult *tempPriceResult;
@property(nonatomic,strong)SystemPriceInfoResult *taxiPriceResult;
@property(nonatomic,strong)NSMutableArray *priceResultArryM;
@end

#define TABCOUNT                    2
#define ADDR_TAG_STSTART            1
#define ADDR_TAG_STEND              2
#define ADDR_TAG_WTSTART            3
#define ADDR_TAG_WTEND              4
#define MIDPOINT_TAG_ST             1
#define MIDPOINT_TAG_WT             2

@implementation Cus_PubShortOrderVC

- (STOnceOrderPubData *)tempOnceOrder
{
  if(_tempOnceOrder ==nil)
  {
      _tempOnceOrder =[[STOnceOrderPubData alloc]init];
  }
    return _tempOnceOrder;
}
- (SystemPriceInfoResult *)tempPriceResult
{
  if(_tempPriceResult ==nil)
  {
      _tempPriceResult =[[SystemPriceInfoResult alloc]init];
  
  }
    return _tempPriceResult;

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
    self.navigationController.navigationBar.translucent =YES;
    //因为有scrollview
    self.automaticallyAdjustsScrollViewInsets =NO;
    
    [_st_carType1 setTitleColor:MyColor(134, 206, 97) forState:UIControlStateNormal];
    [_st_carType1 setTitleColor:[UIColor whiteColor] forState:UIControlStateHighlighted];
     [_st_carType1 setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    [_st_carType2 setTitleColor:MyColor(252, 114, 115) forState:UIControlStateNormal];
    [_st_carType2 setTitleColor:[UIColor whiteColor] forState:UIControlStateHighlighted];
     [_st_carType2 setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    [_st_carType3 setTitleColor:MyColor(215, 115, 118) forState:UIControlStateNormal];
    [_st_carType3 setTitleColor:[UIColor whiteColor] forState:UIControlStateHighlighted];
     [_st_carType3 setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    [_st_carType4 setTitleColor:MyColor(48, 195, 225) forState:UIControlStateNormal];
    [_st_carType4 setTitleColor:[UIColor whiteColor] forState:UIControlStateHighlighted];
    [_st_carType4 setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    
    
    
    //      [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    // self.hidesBottomBarWhenPushed =YES;
//    self.navigationController.navigationBar.translucent =YES;
    mSTMidPoints =[[NSMutableArray alloc]init];
    
    _datePicker.datePickerMode = UIDatePickerModeDateAndTime;

	dispatch_queue_t queue =  dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
         //2.添加任务到队列中，就可以执行任务
         //异步函数：具备开启新线程的能力
         dispatch_async(queue, ^{
             
             });
    [self initControls];
}

-(void)myThreadMainMethod:(NSThread *)sender
{
    [self initControls];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    CGRect rect = [[UIScreen mainScreen] bounds];
    CGSize size = rect.size;
    CGFloat width = size.width;
    CGFloat height = size.height;
    _scrollMain.scrollEnabled =YES;
    _scrollMain.contentSize =CGSizeMake(width, 668);
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
    
    distance_Label.hidden =YES;
    price_Label.hidden =YES;
    taxiLabel.hidden = YES;
    
//    _txtWTNote.delegate = self;
//    _txtWTValue.delegate = self;
    
    // initialize news view frame
    CGRect rcFrame = _vwSingleOrder.frame;
	rcFrame = CGRectMake(_vwSingleOrder.frame.size.width, rcFrame.origin.y, rcFrame.size.width, rcFrame.size.height);
	//_vwWorkOrder.frame = rcFrame;
    
    // initialize main scroll content size
    [_scrollMain setContentSize:CGSizeMake(_vwSingleOrder.frame.size.width * 2, _vwSingleOrder.frame.size.height)];
    
    // make tap recognizer to hide keyboard
    UITapGestureRecognizer * tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapBackGround:)];
    
    [self.view addGestureRecognizer:tapRecognizer];
    //出发地址 默认设置当前地址
    [_lblSTStartAddr setText:[Common getCurrentAdress]];
    [self callSearchAddr:[Common getCurrentAdress] region:[Common getCurrentCity]];
    
    mSTCarType = 1;
    mWTCarType = 1;

	mSTPrice = 30;
    [_txtSTValue setText:[NSString stringWithFormat:@"%.0f", mSTPrice]];
//    mWTPrice = 100;
//    [_txtWTValue setText:[NSString stringWithFormat:@"%.2f", mWTPrice]];

	//---- initialize voice recognizer
	iflyRecognizerView= [[IFlyRecognizerView alloc] initWithCenter:self.view.center];
	iflyRecognizerView.delegate = self;

	[iflyRecognizerView setParameter: @"iat" forKey:[IFlySpeechConstant IFLY_DOMAIN]];
	[iflyRecognizerView setParameter: @"asr.pcm" forKey:[IFlySpeechConstant ASR_AUDIO_PATH]];

	// | result_type   | 返回结果的数据格式，可设置为json，xml，plain，默认为json。
	[iflyRecognizerView setParameter:@"plain" forKey:[IFlySpeechConstant RESULT_TYPE]];

	//    [_iflyRecognizerView setParameter:@"asr_audio_path" value:nil];   当你再不需要保存音频时，请在必要的地方加上这行。

	// setting start time
	NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
	[dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
	NSString *destDateString = [dateFormatter stringFromDate:[[NSDate date] dateByAddingTimeInterval:(25 * 60)]];
	[_btnSTTime setTitle:destDateString forState:UIControlStateNormal];

	NSDate *date = [dateFormatter dateFromString:destDateString];
	[_datePicker setDate:date];

	[dateFormatter setDateFormat:@"HH:mm"];
	destDateString = [dateFormatter stringFromDate:[[NSDate date] dateByAddingTimeInterval:(25*60)]];
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

/**
 *  APP_USER
 获取平台价格信息的网络请求
 */
- (void)callgetSystemPrice:(STOnceOrderPubData *)onceOrder
{
    [jisuanView setHidden:NO];
	TEST_NETWORK_RETURN;
    [[CommManager getCommMgr]orderSvcMgr].delegate =self;
    [[[CommManager getCommMgr]orderSvcMgr ]getSystemPriceInfo:onceOrder];
}
/**
 *  APP_USER
 *  代理方法接收结果  SystemPriceInfoResult  数组
 *  @param result  <#result description#>
 *  @param retData <#retData description#>
 */
-(void)getsystemPriceInfoResult:(NSString *)result RetData:(NSMutableArray *)retData
{
 
    self.priceResultArryM =retData;
    for(SystemPriceInfoResult *item in retData)
    {
        if(item.car_style ==  mSTCarType )
        {
            self.tempPriceResult =item;
            
        }else if(item.car_style ==0)
        {
            self.taxiPriceResult =item;
          break;
        }
    }
    [jisuanView setHidden:YES];
    iconImagView.hidden =YES;
    calculatingImageView.hidden =YES;
    price_Label.hidden=NO;
    distance_Label.hidden =NO;
    taxiLabel.hidden = NO;
    [distance_Label setText:[NSString stringWithFormat:@"本次拼车里程数为%.1f公里  本时段平台均价：",self.tempPriceResult.distance]];
    [price_Label setText:[NSString stringWithFormat:@"%.1f点/次",self.tempPriceResult.aver_price]];
    [taxi_priceLabel setText:[NSString stringWithFormat:@"%.1f点/次",self.taxiPriceResult.aver_price]];
    [price_Label setTextColor:[UIColor orangeColor]];
    [taxi_priceLabel setTextColor:[UIColor orangeColor]];
    [self textFieldPrice:price_Label.text];
    
    MyLog(@"%@",self.tempPriceResult.system_description);
    
}
-(void)textFieldPrice:(NSString *)str
{
    int price = [str intValue];
    double doublePrice = price + 0.5;
    if ([str doubleValue]>doublePrice) {
        price +=1;
        _txtSTValue.text = [NSString stringWithFormat:@"%d",price];
    }else
        _txtSTValue.text = [NSString stringWithFormat:@"%d",price];
    mSTPrice = price;
}
///////////////////////////////////////////////////////////////////////////
#pragma mark - Scroll Delegate
/*
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
}*/

- (void) updateUI : (STUserInfo *)userInfo
{
    
}


/**
 *  地址选择器代理返回方法
 *
 *  @param parentTag:NSInteger <#parentTag:NSInteger description#>
 *
 *  @return <#return value description#>
 */
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
        default:
            break;
    }
    if((mSTStartAddr !=nil) && (mSTEndAddr !=nil))
    {
        //中途点处理
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
        

        //临时OnceOrder 准备获得距离 平台推荐价格信息
        self.tempOnceOrder.start_addr = mSTStartAddr.name;
        self.tempOnceOrder.start_lat = mSTStartAddr.lat;
        self.tempOnceOrder.start_lng = mSTStartAddr.lng;
        self.tempOnceOrder.end_addr = mSTEndAddr.name;
        self.tempOnceOrder.end_lat = mSTEndAddr.lat;
        self.tempOnceOrder.end_lng = mSTEndAddr.lng;
        
        self.tempOnceOrder.mid_points = strMidPoints;
        self.tempOnceOrder.start_time = [_btnSTTime titleLabel].text;
       // mOrderPubData.req_style = mSTCarType;
        
        self.tempOnceOrder.city = [Common getCurrentCity];
        
        
        [self callgetSystemPrice:self.tempOnceOrder];
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
//        mWTMidPoints = midPoints;
//        // set mid point count
//        [_lblWTMidPointCnt setText:[NSString stringWithFormat:@"%d", mWTMidPoints.count]];
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
        if (mSelectedVoiceTag == ADDR_TAG_STEND) {
            controller.saveUser = @"1";
        }
        
//        [self presentModalViewController:controller animated:YES];
        [self presentViewController:controller animated:YES completion:nil];
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
#pragma mark 发布订单
- (IBAction)onClickedSTPublish:(id)sender
{
	// check login state
    if ([Common getUserInfo] == nil)
    {
        // go to login view controller
#warning 要改
      
		LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
        [self presentViewController:loginVC animated:YES completion:nil];
		return;
    }
    if ([_lblSTStartAddr.text isEqualToString:@"起始详细地点"] || [_lblSTStartAddr.text isEqualToString:@""]) {
        [SVProgressHUD showErrorWithStatus:@"请输出发地点" duration:2];
        return;
    }
    if ([_lblSTEndAddr.text isEqualToString:@"目的地详细地点"] || [_lblSTStartAddr.text isEqualToString:@""]) {
        [SVProgressHUD showErrorWithStatus:@"请输目的地点" duration:2];
        return;
    }

	// Time consideration
	NSDate* selDate = [_datePicker date];
	NSDate* curDate = [NSDate date];

	if ([curDate timeIntervalSince1970] > [selDate timeIntervalSince1970])
	{
		[SVProgressHUD showErrorWithStatus:@"出发时间已经过啦" duration:2];
		return;
	}

	//先判断旅绿点数
	TEST_NETWORK_RETURN;
    [SVProgressHUD show];
    [[CommManager getCommMgr] tradeSvcMgr].delegate = self;
    [[[CommManager getCommMgr] tradeSvcMgr] GetMoney:[Common getUserID] devtoken:[Common getDeviceMacAddress]];
    
    mSTPrice = [_txtSTValue.text doubleValue];

}

/*
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
    
//    mWTPrice = [_txtWTValue.text doubleValue];
//    
//    // make temp date using time
//    NSString * tempDate = [NSString stringWithFormat:@"2000-01-01 %@", _btnWTTime.titleLabel.text];
//    
//    [SVProgressHUD show];
//    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
//    [[[CommManager getCommMgr] orderSvcMgr] publishOnOffOrder:[Common getUserID] StartAddr:mWTStartAddr.name StartLat:mWTStartAddr.lat StartLng:mWTStartAddr.lng EndAddr:mWTEndAddr.name EndLat:mWTEndAddr.lat EndLng:mWTEndAddr.lng StartTime:tempDate MidPoints:strMidPoints Remark:_txtWTNote.text ReqStyle:mWTCarType Price:mWTPrice Days:strDays City:[Common getCurrentCity] DevToken:[Common getDeviceMacAddress]];
    //Cus_PubOrderWaitingViewController * viewCtrl = [self.storyboard instantiateViewControllerWithIdentifier:@"PubOrderWating"];
    //SHOW_VIEW(viewCtrl);
}
*/

///////////////////////////// single time order control event //////////////////////////
#pragma mark 单次拼车 起始详细地点
- (IBAction)onClickedSTStartPos:(id)sender
{
   
#warning 测试修改
    
    AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
    
    controller.mParentTag = ADDR_TAG_STSTART;
    controller.delegate = self;
    
//    [self presentModalViewController:controller animated:YES];
    [self presentViewController:controller animated:YES completion:nil];
}
#pragma mark 单次拼车 目的地详细地点
- (IBAction)onClickedSTEndPos:(id)sender
{
    // show address selector view controller
    AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
    
    controller.mParentTag = ADDR_TAG_STEND;
    controller.delegate = self;
    controller.saveUser =@"1";
    
//    [self presentModalViewController:controller animated:YES];
    [self presentViewController:controller animated:YES completion:nil];
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
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
    NSString *destDateString = [dateFormatter stringFromDate:[[NSDate date] dateByAddingTimeInterval:(25*60)]];
    NSDate *date = [dateFormatter dateFromString:destDateString];
    _datePicker.date =date;
    [_pickerView setHidden:NO];
    
    if((mSTStartAddr !=nil) && (mSTEndAddr !=nil))
    {
        self.tempOnceOrder.start_time = [_btnSTTime titleLabel].text;
        [self callgetSystemPrice:self.tempOnceOrder];
    }
}




/**
 * call get all usual route  百度地址网络请求方法
 */
#pragma mark - 百度地址search  place  获取起始地点
- (void) callSearchAddr : (NSString *)keyword region:(NSString *)region
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    //TEST_NETWORK_RETURN;
    
    //[self.selectedAdr removeAllObjects];
    // Call the GetUsualRoutes service routine.
    [[CommManager getCommMgr] globalSvcMgr].delegate = self;
    [[[CommManager getCommMgr] globalSvcMgr] BaiduSearchAddr:keyword region:region apikey:nil];
}

/**
 *  接收查询结果，关闭HUD 关闭当前页面
 *
 *  @param result   <#result description#>
 *  @param dataList <#dataList description#>
 */
#pragma 代理方法接收地址模型
- (void) baiduSearchAddrResult:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
       // self.selectedAdr = dataList;
        STBaiduAddrInfo *addrInfo =dataList[0];
        // refresh table
        //[self updateUI];
        mSTStartAddr =addrInfo;
        [_lblSTStartAddr setText:addrInfo.name];
        
        
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

#pragma mark 获取当前用户绿点数 --------------
- (void)getMoneyResult:(NSString *)result money:(double)money
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        [Config setLvdianAccount:[NSString stringWithFormat:@"%1.f",money]];
        if (mSTPrice > money) //绿点不足
        {
            Cus_PubShortNotEnoughVC *notenough =[[Cus_PubShortNotEnoughVC alloc]initWithNibName:@"Cus_PubShortNotEnoughView" bundle:nil];
            
            notenough.leftPrice = [NSString stringWithFormat:@"%1.f",money];
            notenough.needPrice = [NSString stringWithFormat:@"%1.f",mSTPrice];
            notenough.delegate = self;
            
            [self presentPopupViewController:notenough animated:YES completion:^(void) {
                MyLog(@"Cus_NotEnoughViewController's view presented") ;
            }];
        }else
        {
           [self fabuOrder];
        }
        
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}
#pragma mark  绿点不足弹出界面结果返回处理
- (void)Cus_OnceOrdernotEnoughViewChargeOrNot:(BOOL)chargeOrNot
{
  if(chargeOrNot)  //充值
  {
      if (self.popupViewController != nil)
      {
          [self dismissPopupViewControllerAnimated:YES completion:^{
              MyLog(@"popup view dismissed");
              //跳转到充值页面
#warning 添加
              Cus_ChargeViewController *charge =[[Cus_ChargeViewController alloc]initWithNibName:@"ChargeView" bundle:nil];
              //   UIViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"Charge"];
              [self.navigationController pushViewController:charge animated:YES];
//              [self presentViewController:charge animated:NO completion:nil];
              
          }];
      }
      
  }else{    //仍然发布
      // dismiss popup
      if (self.popupViewController != nil)
      {
          [self dismissPopupViewControllerAnimated:YES completion:^{
              [self fabuOrder];
              MyLog(@"popup view dismissed");
          }];
      }

  }
}
-(void)fabuOrder
{
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


	TEST_NETWORK_RETURN;

	[SVProgressHUD show];
   //Remark 是原设计中得备注 后被删除  因此传@""
    [[CommManager getCommMgr] orderSvcMgr].delegate = self;
    [[[CommManager getCommMgr] orderSvcMgr] publishOnceOrder:[Common getUserID] StartAddr:mSTStartAddr.name StartLat:mSTStartAddr.lat StartLng:mSTStartAddr.lng EndAddr:mSTEndAddr.name EndLat:mSTEndAddr.lat EndLng:mSTEndAddr.lng StartTime:[_btnSTTime titleLabel].text  MidPoints:strMidPoints Remark:@"" ReqStyle:mSTCarType Price:mSTPrice City:[Common getCurrentCity] WaitTime:0 DevToken:[Common getDeviceMacAddress]];

	mOrderPubData = [[STOnceOrderPubData alloc] init];
    mOrderPubData.start_addr = mSTStartAddr.name;
    mOrderPubData.start_lat = mSTStartAddr.lat;
    mOrderPubData.start_lng = mSTStartAddr.lng;
    mOrderPubData.end_addr = mSTEndAddr.name;
    mOrderPubData.end_lat = mSTEndAddr.lat;
    mOrderPubData.end_lng = mSTEndAddr.lng;
    mOrderPubData.start_time = [_btnSTTime titleLabel].text;
    mOrderPubData.mid_points = strMidPoints;
    mOrderPubData.remark = @"";
    mOrderPubData.req_style = mSTCarType;
    mOrderPubData.price = mSTPrice;
    mOrderPubData.city = [Common getCurrentCity];
    mOrderPubData.wait_time = 0;
}
/**
 *  中途点设定
 *
 *  @param sender <#sender description#>
 */
#pragma mark 跳转中途点设定界面
- (IBAction)onClickedMit:(id)sender {
  //  self.storyboard
    // getting the customer storyboard
    UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];

    Cus_SetMidPointViewController *controller = [customerStroyboard instantiateViewControllerWithIdentifier:@"SetMidPoint"];
    controller.mParentTag =MIDPOINT_TAG_ST;
    controller.delegate =self;
    if(mSTMidPoints.count != 0)
    {
      controller.mitPointArry = mSTMidPoints;
    }
    
    [self.navigationController pushViewController:controller animated:YES];
    
    
}

#pragma mark 单次拼车 车型选择
- (IBAction)onClickedSTCarType1:(id)sender
{
    [self.view endEditing:YES];
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
    
    if(self.priceResultArryM != nil)
    {
        for(SystemPriceInfoResult *item in self.priceResultArryM)
        {
            if(item.car_style ==  mSTCarType )
            {
                self.tempPriceResult =item;
                break;
            }
        }
       
        [price_Label setText:[NSString stringWithFormat:@"%.1f点/次",self.tempPriceResult.aver_price]];
        [taxi_priceLabel setText:[NSString stringWithFormat:@"%.1f点/次",self.taxiPriceResult.aver_price]];
        [self textFieldPrice:price_Label.text];
    }
   
}

- (IBAction)onClickedSTCarType2:(id)sender
{
    [self.view endEditing:YES];
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
    if(self.priceResultArryM !=nil)
    {
        for(SystemPriceInfoResult *item in self.priceResultArryM)
        {
            if(item.car_style ==  mSTCarType )
            {
                self.tempPriceResult =item;
                break;
            }
        }
       
        [price_Label setText:[NSString stringWithFormat:@"%.1f点/次",self.tempPriceResult.aver_price]];
        [taxi_priceLabel setText:[NSString stringWithFormat:@"%.1f点/次",self.taxiPriceResult.aver_price]];
        [self textFieldPrice:price_Label.text];
    }
   
}

- (IBAction)onClickedSTCarType3:(id)sender
{
    [self.view endEditing:YES];
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
    
    if(self.priceResultArryM !=nil)
    {
        for(SystemPriceInfoResult *item in self.priceResultArryM)
        {
            if(item.car_style ==  mSTCarType )
            {
                self.tempPriceResult =item;
                break;
            }
        }
    
        [price_Label setText:[NSString stringWithFormat:@"%.1f点/次",self.tempPriceResult.aver_price]];
        [taxi_priceLabel setText:[NSString stringWithFormat:@"%.1f点/次",self.taxiPriceResult.aver_price]];
        [self textFieldPrice:price_Label.text];
    }
}

- (IBAction)onClickedSTCarType4:(id)sender
{
    [self.view endEditing:YES];
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
    
    if(self.priceResultArryM !=nil)
    {
        for(SystemPriceInfoResult *item in self.priceResultArryM)
        {
            if(item.car_style ==  mSTCarType )
            {
                self.tempPriceResult =item;
                break;
            }
        }
    
        [price_Label setText:[NSString stringWithFormat:@"%.1f点/次",self.tempPriceResult.aver_price]];
        [taxi_priceLabel setText:[NSString stringWithFormat:@"%.1f点/次",self.taxiPriceResult.aver_price]];
        [self textFieldPrice:price_Label.text];
    }
}
#pragma mark 单次拼车 报价 减号按钮
- (IBAction)onClickedSTMinus:(id)sender
{
    mSTPrice -= 5;
    if(mSTPrice <= 0)
        mSTPrice = 1;
    [_txtSTValue setText:[NSString stringWithFormat:@"%.0f", mSTPrice]];
}
#pragma mark 单次拼车 报价 加好按钮
- (IBAction)onClickedSTPlus:(id)sender
{
    mSTPrice += 5;
    if(mSTPrice >= 10000)
        mSTPrice = 9999;
    [_txtSTValue setText:[NSString stringWithFormat:@"%.0f", mSTPrice]];
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
        NSLog(@"-----%@",destDateString);
//        [_btnSTTime setTitle:destDateString forState:UIControlStateNormal];
        _btnSTTime.titleLabel.text =destDateString;
    } else if(mCurTab == 1){
        [dateFormatter setDateFormat:@"HH:mm"];
        NSString *destDateString = [dateFormatter stringFromDate:selected];
      //  [_btnWTTime setTitle:destDateString forState:UIControlStateNormal];
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
-(BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string{
    if (textField == _txtSTValue) {
        if ([string isEqualToString:@""]) {
            return YES;
        }
        if (textField.text.length >=4) {
            [SVProgressHUD showErrorWithStatus:@"最大金额9999" duration:DEF_DELAY];
            return NO;
        }
        if ([string isEqualToString:@"0"] && textField.text.length == 0) {
            [SVProgressHUD showErrorWithStatus:@"开始不能输入0" duration:DEF_DELAY];
            return NO;
        }
        NSCharacterSet *invalidCharSet = [[NSCharacterSet characterSetWithCharactersInString:@"1234567890"] invertedSet];
        NSString *filtered = [[string componentsSeparatedByCharactersInSet:invalidCharSet] componentsJoinedByString:@""];
        if ([string isEqualToString:filtered]) {
            return YES;
        }else
        {
            [SVProgressHUD showErrorWithStatus:@"只能输入数字" duration:DEF_DELAY];
            return NO;
        }
        
    }
    return YES;
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
		viewCtrl.mDriverLockTime = retData.driver_lock_time;
		viewCtrl.mRepubTimes = 0;
		viewCtrl.mOrderPubData = mOrderPubData;
		viewCtrl.userLocation =self.userLocation;
        viewCtrl.drvCount =retData.drvcount;
        viewCtrl.price_add = retData.add_price_default;
        viewCtrl.price_addrange = retData.add_price_range;
        viewCtrl.tishi1 = retData.prompt_1st;
        viewCtrl.tishi2 = retData.prompt;
        viewCtrl.add_price_min = retData.add_price_min;
        viewCtrl.cut_price_range = retData.cut_price_range;
      //  SHOW_VIEW(viewCtrl);
        [self.navigationController pushViewController:viewCtrl animated:YES];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}
/*  上下班屏蔽
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
*/



- (IBAction)onSelectSysPrice:(id)sender {
	if (self.tempPriceResult == nil) {
		return;
	}

	[_txtSTValue setText:[NSString stringWithFormat:@"%.1f", self.tempPriceResult.aver_price]];
}


- (void)checkOnceOrderAcceptanceResult:(NSString *)result DriverInfo:(STDriverInfo *)driverInfo OrderInfo:(STSingleTimeOrderInfo *)orderInfo
{
#warning 这里要做什么
}

@end
