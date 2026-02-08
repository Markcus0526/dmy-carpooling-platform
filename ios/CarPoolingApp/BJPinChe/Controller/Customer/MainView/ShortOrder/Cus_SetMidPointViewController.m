//
//  Cus_SetMidPointViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/30/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
// 中途点 设置页面

#import "Cus_SetMidPointViewController.h"
#import "iflyMSC/IFlySpeechConstant.h"
#import "iflyMSC/IFlySpeechUtility.h"
#import "iflyMSC/IFlyRecognizerView.h"

@interface Cus_SetMidPointViewController ()

@end

#define TAG_ADDRESS1                        1
#define TAG_ADDRESS2                        2
#define TAG_ADDRESS3                        3
#define TAG_ADDRESS4                        4

@implementation Cus_SetMidPointViewController

@synthesize mParentTag;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
- (NSMutableArray *)mitPointArry
{
  if(_mitPointArry ==nil)
  {
      _mitPointArry =[[NSMutableArray alloc]init];
  }
    return _mitPointArry;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
   // MA
        
        if(self.mitPointArry.count>=1)
        {
            mAddress1 =self.mitPointArry[0];
            [_lblAddress1 setText:mAddress1.name];
            if(self.mitPointArry.count >=2)
            {
                mAddress2 = self.mitPointArry[1];
               [_lblAddress2 setText:mAddress2.name];
                if(self.mitPointArry.count >=3)
                {
                    mAddress3 = self.mitPointArry[2];
                    [_lblAddress3 setText:mAddress3.name];
                    if(self.mitPointArry.count >=4)
                    {
                        mAddress4 = self.mitPointArry[3];
                         [_lblAddress4 setText:mAddress4.name];
                    }
                }
                
            }
            
       }
    
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


- (void) initControls
{
    //---- initialize voice recognizer
    iflyRecognizerView= [[IFlyRecognizerView alloc] initWithCenter:self.view.center];
    iflyRecognizerView.delegate = self;
    
    [iflyRecognizerView setParameter: @"iat" forKey:[IFlySpeechConstant IFLY_DOMAIN]];
    [iflyRecognizerView setParameter: @"asr.pcm" forKey:[IFlySpeechConstant ASR_AUDIO_PATH]];
    
    // | result_type   | 返回结果的数据格式，可设置为json，xml，plain，默认为json。
    [iflyRecognizerView setParameter:@"plain" forKey:[IFlySpeechConstant RESULT_TYPE]];
    
    //    [_iflyRecognizerView setParameter:@"asr_audio_path" value:nil];   当你再不需要保存音频时，请在必要的地方加上这行。
	

}

///////////////////////////////////////////////////////////////////////////
#pragma mark - Address selector delegation
- (void) onSelectedAddress:(STBaiduAddrInfo *)addrInfo parentTag:(NSInteger)parentTag
{
    switch (parentTag) {
        case TAG_ADDRESS1:
            mAddress1 = addrInfo;
            // show selected address
            [_lblAddress1 setText:addrInfo.name];
            break;
        case TAG_ADDRESS2:
            mAddress2 = addrInfo;
            // show selected address
            [_lblAddress2 setText:addrInfo.name];
            break;
        case TAG_ADDRESS3:
            mAddress3 = addrInfo;
            // show selected address
            [_lblAddress3 setText:addrInfo.name];
            break;
        case TAG_ADDRESS4:
            mAddress4 = addrInfo;
            // show selected address
            [_lblAddress4 setText:addrInfo.name];
            break;
        default:
            break;
    }
}


///////////////////////////////////////////////////////////////////////////
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
        //AddrSelectorViewController * controller = [self.storyboard instantiateViewControllerWithIdentifier:@"addrselector"];
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




///////////////////////////////////////////////////////////////////////////////
#pragma mark - UI User Event
/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

/**
 * Confirm button click event implementation
 */
- (IBAction)onClickedConfirm:(id)sender
{
    NSMutableArray * midPoints = [[NSMutableArray alloc] init];
    
    if (mAddress1 != nil) {
        [midPoints addObject:mAddress1];
    }
    if (mAddress2 != nil) {
        [midPoints addObject:mAddress2];
    }
    if (mAddress3 != nil) {
        [midPoints addObject:mAddress3];
    }
    if (mAddress4 != nil) {
        [midPoints addObject:mAddress4];
    }
    
    [self.delegate selectedMidPoints:midPoints parentTag:mParentTag];
    [self.navigationController popViewControllerAnimated:YES];
   // [self dismissViewControllerAnimated:YES completion:nil];
}


- (IBAction)onClickedAddr1:(id)sender
{
    // show address selector view controller
  //  AddrSelectorViewController * controller = [self.storyboard instantiateViewControllerWithIdentifier:@"addrselector"];
    AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
    
    controller.mParentTag = TAG_ADDRESS1;
    controller.delegate = self;
    
//    [self presentModalViewController:controller animated:YES];
    [self presentViewController:controller animated:YES completion:nil];
}

- (IBAction)onClickedAddr2:(id)sender
{

    AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
    
    controller.mParentTag = TAG_ADDRESS2;
    controller.delegate = self;
    
//    [self presentModalViewController:controller animated:YES];
    [self presentViewController:controller animated:YES completion:nil];
}

- (IBAction)onClickedAddr3:(id)sender
{
  
    AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
    
    controller.mParentTag = TAG_ADDRESS3;
    controller.delegate = self;
    
//    [self presentModalViewController:controller animated:YES];
    [self presentViewController:controller animated:YES completion:nil];
}

- (IBAction)onClickedAddr4:(id)sender
{
    
    AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
    
    controller.mParentTag = TAG_ADDRESS4;
    controller.delegate = self;
    
//    [self presentModalViewController:controller animated:YES];
    [self presentViewController:controller animated:YES completion:nil];
}

- (IBAction)onClickedAddr1Voice:(id)sender
{
    mSelectedVoiceTag = TAG_ADDRESS1;
    
    [iflyRecognizerView start];
    NSLog(@"start listenning...");
}

- (IBAction)onClickedAddr2Voice:(id)sender
{
    mSelectedVoiceTag = TAG_ADDRESS2;
    
    [iflyRecognizerView start];
    NSLog(@"start listenning...");
}

- (IBAction)onClickedAddr3Voice:(id)sender
{
    mSelectedVoiceTag = TAG_ADDRESS3;
    
    [iflyRecognizerView start];
    NSLog(@"start listenning...");
}

- (IBAction)onClickedAddr4Voice:(id)sender
{
    mSelectedVoiceTag = TAG_ADDRESS4;
    
    [iflyRecognizerView start];
    NSLog(@"start listenning...");
}


- (IBAction)onClickedRemoveAddr1:(id)sender
{
    mAddress1 = nil;
    [_lblAddress1 setText:@"中途点1详细地址"];
}

- (IBAction)onClickedRemoveAddr2:(id)sender
{
    mAddress2 = nil;
    [_lblAddress2 setText:@"中途点2详细地址"];
}

- (IBAction)onClickedRemoveAddr3:(id)sender
{
    mAddress3 = nil;
    [_lblAddress3 setText:@"中途点3详细地址"];
}

- (IBAction)onClickedRemoveAddr4:(id)sender
{
    mAddress4 = nil;
    [_lblAddress4 setText:@"中途点4详细地址"];
}


@end
