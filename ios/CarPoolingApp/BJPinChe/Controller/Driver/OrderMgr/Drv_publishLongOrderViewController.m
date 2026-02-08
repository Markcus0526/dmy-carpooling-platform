//
//  Drv_publishLongOrderViewController.m
//  BJPinChe
//
//  Created by CKK on 14-8-30.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//  车主 发布长途订单界面


#import "Drv_publishLongOrderViewController.h"
#import "AddrSelectorViewController.h"
#import "iflyMSC/IFlySpeechConstant.h"
#import "iflyMSC/IFlySpeechUtility.h"
#import "iflyMSC/IFlyRecognizerView.h"
#import "Drv_MainTabViewController.h"
#define TAG_START 1
#define TAG_END 2
#define ADDR_TAG_STSTART            1
#define ADDR_TAG_STEND              2



@interface Drv_publishLongOrderViewController ()<UIAlertViewDelegate>

@end

@implementation Drv_publishLongOrderViewController
{
    BOOL keyboardVisible;
    STBaiduAddrInfo *               mStartAddr;
    STBaiduAddrInfo *               mEndAddr;
    LongWayOrderSvcMgr * longWayOrderSvcMgr;
    IFlyRecognizerView *                iflyRecognizerView;
    NSInteger mSelectedVoiceTag;
    InfoFeeCalcMethodInfo * _infoFeeCalc;
}

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
	[self GetInfoFeeCalcMethod];
    
    UIScreen *screen =[UIScreen mainScreen];
    if (screen.bounds.size.height<500) {
        _scrollMain.contentSize = CGSizeMake(320, 600);
    }
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





- (void) initControls
{
    longWayOrderSvcMgr = [[CommManager getCommMgr] longWayOrderSvcMgr];
    longWayOrderSvcMgr.delegate = self;
    if(![[Common getCurrentCity ] isEqualToString:@""])
    {
      [self.startCity setText:[Common getCurrentCity]];
    }
    if (![[Common getCurrentAdress] isEqualToString:@""]) {
        [self.startCityDetail setText:[Common getCurrentAdress]];
    }
    self.startCity.delegate = self;
    self.startCityDetail.delegate = self;
    self.endCity.delegate = self;
    self.endCityDetail.delegate = self;
    self.priceTextField.delegate = self;
    self.siteNumberTextField.delegate = self;
    self.remarkTextView.delegate = self;
    
////    //给座位价格添加监听事件
//     [self.priceTextField addObserver:self forKeyPath:@"text" options:NSKeyValueObservingOptionNew|NSKeyValueObservingOptionOld context:nil];

	_priceTextField.text = @"100";

	UITapGestureRecognizer * tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapBackGround:)];

	[self.view addGestureRecognizer:tapRecognizer];


	//---- initialize voice recognizer
	iflyRecognizerView= [[IFlyRecognizerView alloc] initWithCenter:self.view.center];
	iflyRecognizerView.delegate = self;

	[iflyRecognizerView setParameter: @"iat" forKey:[IFlySpeechConstant IFLY_DOMAIN]];
	[iflyRecognizerView setParameter: @"asr.pcm" forKey:[IFlySpeechConstant ASR_AUDIO_PATH]];

	// | result_type   | 返回结果的数据格式，可设置为json，xml，plain，默认为json。
	[iflyRecognizerView setParameter:@"plain" forKey:[IFlySpeechConstant RESULT_TYPE]];

	//    [_iflyRecognizerView setParameter:@"asr_audio_path" value:nil];   当你再不需要保存音频时，请在必要的地方加上这行。

}

//当座位价格发生变化时，平台信息费发生变化
-(void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
    if (object == self.priceTextField) {
        [self refreshInfoFee];
		NSLog(@"KeyPath");
    }else{
        [super observeValueForKeyPath:keyPath ofObject:object change:change context:context];
    }
}



-(void)refreshInfoFee
{
    float price = _infoFeeCalc.calc_method == 1 ? _infoFeeCalc.value : [self.priceTextField.text floatValue] * _infoFeeCalc.value / 100.0f;
    
    MyLog(@"%f",price);
    [self.infoFee setTextColor:[UIColor orangeColor]];
    self.infoFee.text = [NSString stringWithFormat:@"平台信息费:%.2f点/座  保险费：%.2f点/座 ",price,_infoFeeCalc.insu_fee];
    self.infoFee.adjustsFontSizeToFitWidth = YES;
}




/**
 * Hide keyboard when tapped background
 */
- (void) handleTapBackGround:(id)sender
{
    [self.view endEditing:YES];
}


//语音按钮
- (IBAction)OnStartCityVoiceButtonClick:(id)sender {
    
    NSString * startCity = self.startCity.text;
    if (startCity.length == 0) {
        [self shakeAnimationForView:self.startCity.superview];
        return;
    }

    mSelectedVoiceTag = ADDR_TAG_STSTART;
    [iflyRecognizerView start];
    NSLog(@"start listenning...");
}
//语音按钮
- (IBAction)OnEndCityVoiceButtonClick:(id)sender {
    
    NSString * endCity = self.endCity.text;
    if (endCity.length == 0) {
        [self shakeAnimationForView:self.endCity.superview];
        return;
    }
    
    mSelectedVoiceTag = ADDR_TAG_STEND;
    [iflyRecognizerView start];
    NSLog(@"start listenning...");
}

- (IBAction)OnSelectStartDateClick:(id)sender {
    NSLog(@"select is onClick");
    [self.view endEditing:YES];
    [self.datePickerView setHidden:NO];

}
#pragma mark 发布按钮点击事件
- (IBAction)OnPublishClick:(id)sender {
    
    //获取各个参数
    NSString * startCity = self.startCity.text;
    if (startCity.length == 0) {
        [self shakeAnimationForView:self.startCity.superview];
        [SVProgressHUD showErrorWithStatus:@"起始城市不能为空" duration:DEF_DELAY];
        return;
    }

	NSString * startAddr = self.startCityDetail.text;
    if (startAddr.length == 0) {
        [self shakeAnimationForView:self.startCityDetail.superview];
         [SVProgressHUD showErrorWithStatus:@"起始地点不能为空" duration:DEF_DELAY];
        return;
    }

	NSString * endCity = self.endCity.text;
    if (endCity.length == 0) {
		[self shakeAnimationForView:self.endCity.superview];
		[SVProgressHUD showErrorWithStatus:@"目的城市不能为空" duration:DEF_DELAY];
		return;
	}

	NSString * endAddr = self.endCityDetail.text;
	if (endAddr.length == 0) {
		[self shakeAnimationForView:self.endCityDetail.superview];
		[SVProgressHUD showErrorWithStatus:@"目的地点不能为空" duration:DEF_DELAY];
		return;
	}

	NSString * startTime = [self.selectStartDateButton.titleLabel.text stringByAppendingString:@":00"];
	if ([self.selectStartDateButton.titleLabel.text isEqualToString:@"选择出发日期"]) {
        [self shakeAnimationForView:self.selectStartDateButton];
        [SVProgressHUD showErrorWithStatus:@"（必填项）未填写”" duration:DEF_DELAY];
        return;
    }
    
    NSDate* selDate = [_datePicker date];
    NSDate* curDate = [NSDate date];
    
    if ([curDate timeIntervalSince1970] > [selDate timeIntervalSince1970])
    {
        [SVProgressHUD showErrorWithStatus:@"出发时间已经过啦" duration:2];
        return;
    }

	// NSString * remark = self.remarkTextView.text;
	NSString * price = self.priceTextField.text;
	int seatsCount = [self.siteNumberTextField.text intValue];
	double startLat = mStartAddr.lat;
	double startLng = mStartAddr.lng;
	double endLat = mEndAddr.lat;
	double endLng = mEndAddr.lng;

	[SVProgressHUD show];

	//ramark  备注被删除  传@“”
	[longWayOrderSvcMgr PublishLongOrderWithUserID:[Common getUserID] AndStartCity:startCity AndStartAddr:startAddr AndEndCity:endCity AndEndAddr:endAddr AndStartlat:startLat AndStartLng:startLng AndEndLat:endLat AndEndLng:endLng AndStartTime:startTime AndRemark:@"" AndPrice:price AndSeatsCount:seatsCount AndDevtoken:[Common getDeviceMacAddress]];

}



//数据请求的回调函数
-(void)PublishLongOrder:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        NSLog(@"发布成功");
        [SVProgressHUD dismiss];
      UIAlertView *succeseAlert = [[UIAlertView alloc]initWithTitle:@"" message:@"订单已经发布成功，您可以从我的订单中查看该订单详情" delegate:self cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [succeseAlert show];
       // [SVProgressHUD dismissWithSuccess:@"发布成功" afterDelay:DEF_DELAY];
       // BACK_VIEW;
     }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }

}
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    Drv_MainTabViewController *controller = (Drv_MainTabViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];
    controller.mCurTab = TAB_ORDER;
    
    SHOW_VIEW(controller);

}



- (IBAction)OnSiteNumberminusClick:(id)sender {
    
    if (![self.siteNumberTextField.text isEqualToString:@""]) {
        if ([self.siteNumberTextField.text intValue] <= 1) {
            [SVProgressHUD show];
            [SVProgressHUD dismissWithError:LONG_ORDER_SEAT_NUMBER_LOW_ERROR afterDelay:DEF_DELAY];
            self.siteNumberTextField.text = @"1";
        }else{
            self.siteNumberTextField.text = [NSString stringWithFormat:@"%d",[self.siteNumberTextField.text intValue] - 1];
        }
    }else{
        self.siteNumberTextField.text = @"1";
    }
    

    
}
- (IBAction)OnPriceMinusClick:(id)sender {
    if (![self.priceTextField.text isEqualToString:@""] ) {
        if([self.priceTextField.text floatValue] <= 1){
            [SVProgressHUD show];
            [SVProgressHUD dismissWithError:LONG_ORDER_SEAT_PRICE_LOW_ERROR afterDelay:DEF_DELAY];
            self.priceTextField.text = @"1";
        }else{
            self.priceTextField.text = [NSString stringWithFormat:@"%d",[self.priceTextField.text intValue] - 1];
        }
    }else{
        self.priceTextField.text = @"0";
    }
    

}

- (IBAction)OnSiteNumberPlusClick:(id)sender {
    
    if (![self.siteNumberTextField.text isEqualToString:@""]) {
        if ([self.siteNumberTextField.text intValue] >= 30) {
            [SVProgressHUD show];
            [SVProgressHUD dismissWithError:LONG_ORDER_SEAT_NUMBER_HIGH_ERROR afterDelay:DEF_DELAY];
            self.siteNumberTextField.text = @"30";
        }else{
            self.siteNumberTextField.text = [NSString stringWithFormat:@"%d",[self.siteNumberTextField.text intValue] + 1];
        }
    }else{
        self.siteNumberTextField.text = @"0";
    }
    
    
}

- (IBAction)OnPricePlusClick:(id)sender {
    
    if (![self.priceTextField.text isEqualToString:@""]) {
        if([self.priceTextField.text floatValue] >= 9999){
            [SVProgressHUD show];
            [SVProgressHUD dismissWithError:LONG_ORDER_SEAT_PRICE_HIGH_ERROR afterDelay:DEF_DELAY];
            _priceTextField.text = @"9999";
        }else{
            int num = [self.priceTextField.text intValue]+5;
            if (num >9999) {
                _priceTextField.text = @"9999";
            }else
            {
                self.priceTextField.text = [NSString stringWithFormat:@"%d",[self.priceTextField.text intValue] + 5];
            }
        }
    }else{
        self.priceTextField.text = @"1";
    }
    
}


- (IBAction)OnDatePickerCancelClick:(id)sender {
    
    [self.datePickerView setHidden:YES];

    
}

- (IBAction)OnDatePickerSureClick:(id)sender {
    
    NSDate *selected = [_datePicker date];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
    NSString *destDateString = [dateFormatter stringFromDate:selected];
    
    NSLog(@"出发日期为：%@",destDateString);
    
    [self.datePickerView setHidden:YES];
    
    [self.selectStartDateButton setTitle:destDateString forState:UIControlStateNormal];
}
- (IBAction)OnBackView:(id)sender {
    [self.priceTextField removeObserver:self forKeyPath:@"text"];
    BACK_VIEW;
}

///////////////////////////////////////////////////////////////////
#pragma mark - Scroll When Keyboard Focus     详细地址选择跳转
- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    if (textField.tag == 21) {
        [self.view endEditing:YES];
        
        //判断城市是否为空
        if ((self.startCity.text.length == 0)|[self.startCity.text isEqualToString:@"起始城市"]) {
            [self shakeAnimationForView:self.startCity.superview];
            return;
        }
        
        // show address selector view controller
		AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
        controller.mParentTag =TAG_START;
        controller.delegate = self;
        controller.city = self.startCity.text;
        controller.defText = self.startCityDetail.text;

        [self presentViewController:controller animated:YES completion:nil];
    } else if(textField.tag == 22){
        [self.view endEditing:YES];

		//判断城市是否为空
        if ((self.endCity.text.length == 0)|[self.endCity.text isEqualToString:@"目的城市"]) {
            [self shakeAnimationForView:self.endCity.superview];
            return;
        }

		// show address selector view controller
		AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
		controller.mParentTag =TAG_END;
		controller.delegate = self;
		controller.city = self.endCity.text;

		[self presentViewController:controller animated:YES completion:nil];
	} else {
	}

}



-(BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string{
	if (textField == _priceTextField)
	{
		if ([string isEqualToString:@""])
		{
			[self refreshInfoFee];
            return YES;
		}

		if (textField.text.length >= 4)
		{
			[SVProgressHUD showErrorWithStatus:@"最大金额9999" duration:DEF_DELAY];
			return NO;
		}

		if ([string isEqualToString:@"0"] && textField.text.length == 0)
		{
			[SVProgressHUD showErrorWithStatus:@"开始不能输入0" duration:DEF_DELAY];
			return NO;
		}

		NSCharacterSet *invalidCharSet = [[NSCharacterSet characterSetWithCharactersInString:@"1234567890"] invertedSet];
		NSString *filtered = [[string componentsSeparatedByCharactersInSet:invalidCharSet] componentsJoinedByString:@""];

		if ([string isEqualToString:filtered]) {
			[self refreshInfoFee];
			return YES;
		} else {
			[SVProgressHUD showErrorWithStatus:@"只能输入数字" duration:DEF_DELAY];
			return NO;
		}
	}

	return YES;

}



- (void)textFieldDidEndEditing:(UITextField *)textField
{
	// 绿点数和剩余座位数
	if (textField == self.priceTextField || textField == self.siteNumberTextField) {
		if (textField == self.siteNumberTextField && ([textField.text intValue] > 30 || [textField.text intValue] < 1)) {
			[SVProgressHUD show];

			if ([textField.text intValue] >30) {
				[SVProgressHUD dismissWithError:LONG_ORDER_SEAT_NUMBER_HIGH_ERROR afterDelay:DEF_DELAY];
				textField.text = @"30";
			}

			if ([textField.text intValue] < 1) {
				[SVProgressHUD dismissWithError:LONG_ORDER_SEAT_NUMBER_LOW_ERROR afterDelay:DEF_DELAY];
				textField.text = @"1";
			}

			[self shakeAnimationForView:textField.superview];
		}

		if (textField == self.priceTextField && ([textField.text floatValue] >= 10000|| [textField.text floatValue] < 1)) {
			[SVProgressHUD show];
			if([textField.text floatValue] >= 10000){
				[SVProgressHUD dismissWithError:LONG_ORDER_SEAT_PRICE_HIGH_ERROR afterDelay:DEF_DELAY];
				textField.text = @"9999.99";
			}

			if ([textField.text floatValue] < 1) {
				[SVProgressHUD dismissWithError:LONG_ORDER_SEAT_PRICE_LOW_ERROR afterDelay:DEF_DELAY];
				textField.text = @"1";
			}

			[self shakeAnimationForView:textField.superview];
		}

		//修改平台信息费点数
		if (textField == self.priceTextField) {
			[self refreshInfoFee];
		}
	}

	//开始城市和结束城市 长度小于10
	if (textField == self.startCity || textField == self.endCity) {
		if (textField.text.length > 10) {
			textField.text = [textField.text substringToIndex:9];
			[self shakeAnimationForView:textField.superview];
		}
	}

}


#pragma mark - Address selector delegation
- (void) onSelectedAddress:(STBaiduAddrInfo *)addrInfo parentTag:(NSInteger)parentTag
{
    if (parentTag == TAG_START)
    {
        mStartAddr = addrInfo;
        // show selected address
        [self.startCityDetail setText:mStartAddr.name];
        
    }
    else if (parentTag == TAG_END)
    {
        mEndAddr = addrInfo;
        // show selected address
        [self.endCityDetail setText:mEndAddr.name];
    }
}


- (void)textViewDidEndEditing:(UITextView *)textView
{
    //备注文字最多输入100汉字，超过自动删除超过的
    if (textView == self.remarkTextView && textView.text.length >= 200) {
        [self shakeAnimationForView:self.remarkTextView];
        textView.text = [textView.text substringToIndex:199];
        [SVProgressHUD show];
        [SVProgressHUD dismissWithError:LONG_ORDER_REAMARK_LENGTH_ERROR afterDelay:DEF_DELAY];
    }
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];

    //给座位价格添加监听事件
    [self.priceTextField addObserver:self forKeyPath:@"text" options:NSKeyValueObservingOptionNew|NSKeyValueObservingOptionOld context:nil];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [self.priceTextField removeObserver:self forKeyPath:@"text"];
	[super viewWillDisappear:animated];
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

#pragma mark - IFlyRecognizerViewDelegate
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
    
    NSLog(@"voice : %@", result);
    
    if ([result length] > 0) {
        // show address selector view controller & set voice text
      //  AddrSelectorViewController * controller = [self.storyboard instantiateViewControllerWithIdentifier:@"addrselector"];
        
        AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
        
        controller.mParentTag = mSelectedVoiceTag;
        controller.defText = result;
        controller.delegate = self;
        
//        [self presentModalViewController:controller animated:YES];
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

#pragma mark - 获取信息费计算方式

-(void)GetInfoFeeCalcMethod
{
    [longWayOrderSvcMgr GetInfoFeeCalcMethodWithUserID:[Common getUserID] AndCity:[Common getCurrentCity]];
}

-(void)GetInfoFeeCalcMethod:(NSString *)result dataList:(NSMutableArray *)dataList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        _infoFeeCalc = dataList[0];
        [self refreshInfoFee];
    }else{
        [SVProgressHUD show];
        [SVProgressHUD dismissWithError:LONG_ORDER_INFO_FEE_ERROR afterDelay:DEF_DELAY];
//        [self GetInfoFeeCalcMethod];
    }
}




@end
