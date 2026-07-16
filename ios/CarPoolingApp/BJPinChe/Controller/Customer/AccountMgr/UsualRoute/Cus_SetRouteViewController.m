//
//  Cus_SetRouteViewController.m
//  BJPinChe
//
//  Created by Kimoc on 14-8-20.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//
/**
 *  设置日常路线界面
 *
 *  @param  <# description#>
 *
 *  @return <#return value description#>
 */
#import "Cus_SetRouteViewController.h"
#import "Common.h"
#define TAG_START                       1
#define TAG_END                         2

@interface Cus_SetRouteViewController () {
	IFlyRecognizerView*			iflyRecognizerView;
	int							mSearchTag;
}

@end

@implementation Cus_SetRouteViewController

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

- (void)initControls
{
	_txtStartCity.delegate = self;
	_txtEndCity.delegate = self;

	UITapGestureRecognizer * tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapBackGround:)];
	[self.view addGestureRecognizer:tapRecognizer];

	if(self.mSaveType == 1)		// change route
	{
		_lblTitle.text = @"修改路线";

		if(self.mDataInfo != nil)
		{
			_txtStartCity.text = self.mDataInfo.startcity;
			_txtEndCity.text = self.mDataInfo.endcity;
			mStartAddr = [[STBaiduAddrInfo alloc] init];
			mStartAddr.name = self.mDataInfo.startaddr;
			mStartAddr.lat = self.mDataInfo.startlat;
			mStartAddr.lng = self.mDataInfo.startlng;

			mEndAddr = [[STBaiduAddrInfo alloc] init];
			mEndAddr.name = self.mDataInfo.endaddr;
			mEndAddr.lat = self.mDataInfo.endlat;
			mEndAddr.lng = self.mDataInfo.endlng;

			NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
			[dateFormatter setDateFormat:@"yyyy-MM-dd"];
			mStartTime = [dateFormatter dateFromString:self.mDataInfo.time];

			_lblStartPos.text = self.mDataInfo.startaddr;
			_lblEndPos.text = self.mDataInfo.endaddr;
			_lblTime.text = self.mDataInfo.time;
		}
	}
	else						// new route
	{
		if ([Common getCurrentCity].length !=0) {
			_txtStartCity.text = [Common getCurrentCity];
		}

		_lblTitle.text = @"添加新路线";
	}


	iflyRecognizerView = [[IFlyRecognizerView alloc] initWithCenter:self.view.center];
	iflyRecognizerView.delegate = self;

	[iflyRecognizerView setParameter: @"iat" forKey:[IFlySpeechConstant IFLY_DOMAIN]];
	[iflyRecognizerView setParameter: @"asr.pcm" forKey:[IFlySpeechConstant ASR_AUDIO_PATH]];

	// | result_type   | 返回结果的数据格式，可设置为json，xml，plain，默认为json。
	[iflyRecognizerView setParameter:@"plain" forKey:[IFlySpeechConstant RESULT_TYPE]];

}

- (void)shakeAnimationForView:(UIView *) view
{
	// 获取到当前的View
	
	CALayer *viewLayer = view.layer;
	
	// 获取当前View的位置
	
	CGPoint position = viewLayer.position;
	
	// 移动的两个终点位置
	
	CGPoint x = CGPointMake(position.x + 10, position.y);
	
	CGPoint y = CGPointMake(position.x - 10, position.y);
	
	// 设置动画
	
	CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"position"];
	
	// 设置运动形式
	
	[animation setTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionDefault]];
	
	// 设置开始位置
	
	[animation setFromValue:[NSValue valueWithCGPoint:x]];
	
	// 设置结束位置
	
	[animation setToValue:[NSValue valueWithCGPoint:y]];
	
	// 设置自动反转
	
	[animation setAutoreverses:YES];
	
	// 设置时间
	
	[animation setDuration:.06];
	
	// 设置次数
	
	[animation setRepeatCount:3];
	
	// 添加上动画
	
	[viewLayer addAnimation:animation forKey:nil];
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - Address selector delegation
- (void) onSelectedAddress:(STBaiduAddrInfo *)addrInfo parentTag:(NSInteger)parentTag
{
	if (parentTag == TAG_START)
	{
		mStartAddr = addrInfo;
		// show selected address
		[_lblStartPos setText:mStartAddr.name];
	}
	else if (parentTag == TAG_END)
	{
		mEndAddr = addrInfo;
		// show selected address
		[_lblEndPos setText:mEndAddr.name];
	}
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

/**
 * click start pos text area
 */
- (IBAction)onClickedSP:(id)sender
{
	NSString * startCity = _txtStartCity.text;
	if (startCity.length == 0) {
		[self shakeAnimationForView:_txtStartCity];
		return;
	}

	AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];

	controller.city = _txtStartCity.text;
	controller.mParentTag = TAG_START;
	controller.delegate = self;

	[self presentViewController:controller animated:YES completion:nil];
}

/**
 * click start pos set voice button
 */
- (IBAction)onClickedSPVoice:(id)sender
{
	NSString * startCity = _txtStartCity.text;
	if (startCity.length == 0) {
		[self shakeAnimationForView:_txtStartCity];
		return;
	}

	mSearchTag = TAG_START;
	[iflyRecognizerView start];
	NSLog(@"start listenning...");
}



/**
 * click end pos text area
 */
- (IBAction)onClickedEP:(id)sender
{
	NSString * endCity = _txtEndCity.text;
	if (endCity.length == 0) {
		[self shakeAnimationForView:_txtEndCity];
		return;
	}

	AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];

	controller.city = _txtEndCity.text;
	controller.mParentTag = TAG_END;
	controller.delegate = self;

	[self presentViewController:controller animated:YES completion:nil];
}



/**
 * click end pos voice button
 */
- (IBAction)onClickedEPVoice:(id)sender
{
	NSString* endCity = _txtEndCity.text;
	if (endCity.length == 0) {
		[self shakeAnimationForView:_txtEndCity];
		return;
	}

	mSearchTag = TAG_END;
	[iflyRecognizerView start];
	NSLog(@"start listenning...");
}

/**
 * click event of start time button
 */
- (IBAction)onClickedTime:(id)sender
{
    [_pickerView setHidden:NO];
}

- (IBAction)onClickedPickerOK:(id)sender
{
	NSDate *selected = [_timePicker date];
	NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
	[dateFormatter setDateFormat:@"yyyy-MM-dd"];
	NSString *destDateString = [dateFormatter stringFromDate:selected];
	
	[_lblTime setText:destDateString];
	
	[_pickerView setHidden:YES];
	
	// save start time
	mStartTime = selected;
}

- (IBAction)onClickedPickerCancel:(id)sender
{
	[_pickerView setHidden:YES];
}

- (IBAction)onClickedSave:(id)sender
{
	if ([_txtStartCity.text isEqualToString:@""]) {
		[self.view makeToast:MSG_NO_STARTCITY duration:DEF_DELAY position:@"center"];
		return;
	}
	if ([_txtEndCity.text isEqualToString:@""]) {
		[self.view makeToast:MSG_NO_ENDCITY duration:DEF_DELAY position:@"center"];
		return;
	}
	
	if (mStartAddr == nil) {
		[self.view makeToast:MSG_NO_STARTADDR duration:DEF_DELAY position:@"center"];
		return;
	}
	if (mEndAddr == nil) {
		[self.view makeToast:MSG_NO_ENDADDR duration:DEF_DELAY position:@"center"];
		return;
	}
	if (mStartTime == nil) {
		[self.view makeToast:MSG_NO_STARTTIME duration:DEF_DELAY position:@"center"];
		return;
	}

	if ([mStartTime timeIntervalSince1970] <= [[NSDate date] timeIntervalSince1970]) {
		[self.view makeToast:@"出发时间已经过期" duration:DEF_DELAY position:@"center"];
		return;
	}

	if(self.mSaveType == 1)
	{
		[self callChangeLongRoute];
	}
	else
	{
		[self callAddLongRoute];
	}
	
}

- (IBAction)onClickedCancel:(id)sender
{
	[self.navigationController popViewControllerAnimated:YES];
}

- (void) callAddLongRoute
{
	[SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
	
	TEST_NETWORK_RETURN;
	
	NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
	[dateFormatter setDateFormat:@"yyyy-MM-dd"];
	NSString *destDateString = [dateFormatter stringFromDate:mStartTime];
	
	// Call the GetUsualRoutes service routine.
	[[CommManager getCommMgr] accountSvcMgr].delegate = self;
	[[[CommManager getCommMgr] accountSvcMgr] AddRoute:[Common getUserID] type:ROUTETYPE_LONG daytype:0 startcity:_txtStartCity.text endcity:_txtEndCity.text startaddr:mStartAddr.name endaddr:mEndAddr.name startlat:mStartAddr.lat startlng:mStartAddr.lng endlat:mEndAddr.lat endlng:mEndAddr.lng city:[Common getCurrentCity] start_time:destDateString devtoken:[Common getDeviceMacAddress]];
}

- (void) addRouteResult:(NSString *)result
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		[SVProgressHUD dismissWithSuccess:MSG_SUCCESS afterDelay:DEF_DELAY];
		[self performSelector:@selector(onClickedBack:) withObject:nil afterDelay:DEF_DELAY];
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
}

- (void)callChangeLongRoute
{
	[SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
	
	TEST_NETWORK_RETURN;
	
	NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
	[dateFormatter setDateFormat:@"yyyy-MM-dd"];
	NSString *destDateString = [dateFormatter stringFromDate:mStartTime];
	
	// Call the GetUsualRoutes service routine.
	[[CommManager getCommMgr] accountSvcMgr].delegate = self;
	[[[CommManager getCommMgr] accountSvcMgr] ChangeUsualRoute:[Common getUserID] route_id:self.mDataInfo.uid type:ROUTETYPE_LONG daytype:0  startcity:_txtStartCity.text endcity:_txtEndCity.text startaddr:mStartAddr.name endaddr:mEndAddr.name startlat:mStartAddr.lat startlng:mStartAddr.lng endlat:mEndAddr.lat endlng:mEndAddr.lng city:[Common getCurrentCity] start_time:destDateString devtoken:[Common getDeviceMacAddress]];
}

- (void)changeUsualRouteResult:(NSString *)result
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		[SVProgressHUD dismissWithSuccess:MSG_SUCCESS afterDelay:DEF_DELAY];
		[self performSelector:@selector(onClickedBack:) withObject:nil afterDelay:DEF_DELAY];
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
}

- (void) handleTapBackGround:(id)sender
{
	[self.view endEditing:YES];
}



- (void)onResult:(NSArray *)resultArray isLast:(BOOL)isLast
{
	NSMutableString *result = [[NSMutableString alloc] init];
	NSDictionary *dic = [resultArray objectAtIndex:0];
	for (NSString *key in dic) {
		[result appendFormat:@"%@",key];
	}
	
	NSLog(@"voice : %@", result);
	
	if ([result length] > 0) {
		AddrSelectorViewController* controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];

		controller.mParentTag = mSearchTag;
		controller.defText = result;
		controller.delegate = self;

		[self presentViewController:controller animated:YES completion:nil];
	}
}

/** 识别结束回调方法
 @param error 识别错误
 */
- (void)onError:(IFlySpeechError *)error
{
	NSLog(@"errorCode:%d",[error errorCode]);
}

@end



