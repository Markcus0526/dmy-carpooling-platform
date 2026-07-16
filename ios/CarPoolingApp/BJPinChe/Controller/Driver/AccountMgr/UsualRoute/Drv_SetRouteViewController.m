//
//  Drv_SetRouteViewController.m
//  BJPinChe
//
//  Created by Kimoc on 14-8-20.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "Drv_SetRouteViewController.h"

@interface Drv_SetRouteViewController ()

@end

#define TAG_START                       1
#define TAG_END                         2


@implementation Drv_SetRouteViewController

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
    mCurDayType = TYPE_NORMAL;
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



//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/**
 * call add new usual route service
 */
- (void) callAddRoute
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    NSString *destDateString = [dateFormatter stringFromDate:mStartTime];
    
    // Call the GetUsualRoutes service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
	[[[CommManager getCommMgr] accountSvcMgr] AddRoute:[Common getUserID] type:ROUTETYPE_SHORT daytype:mCurDayType startcity:@"" endcity:@"" startaddr:mStartAddr.name endaddr:mEndAddr.name startlat:mStartAddr.lat startlng:mStartAddr.lng endlat:mEndAddr.lat endlng:mEndAddr.lng city:[Common getCurrentCity] start_time:destDateString devtoken:[Common getDeviceMacAddress]];
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



///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    BACK_VIEW;
}

- (IBAction)onClickedNext:(id)sender
{
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
    
    [self callAddRoute];
}

/**
 * Back to parent view controller
 */
- (IBAction)onClickedCancel:(id)sender
{
    BACK_VIEW;
}

/**
 * work time type option 1 (working day)'
 */
- (IBAction)onClickedType1:(id)sender
{
    [_radioWorkType1 setSelected:YES];
    [_radioWorkType2 setSelected:NO];
    [_radioWorkType3 setSelected:NO];
    
    mCurDayType = TYPE_NORMAL;
}

/**
 * work time type option 2 (weekend)'
 */
- (IBAction)onClickedType2:(id)sender
{
    [_radioWorkType1 setSelected:NO];
    [_radioWorkType2 setSelected:YES];
    [_radioWorkType3 setSelected:NO];
    
    mCurDayType = TYPE_WEEKEND;
}

/**
 * work time type option 3 (everyday)'
 */
- (IBAction)onClickedType3:(id)sender
{
    [_radioWorkType1 setSelected:NO];
    [_radioWorkType2 setSelected:NO];
    [_radioWorkType3 setSelected:YES];
    
    mCurDayType = TYPE_EVERYDAY;
}

/**
 * click start pos text area
 */
- (IBAction)onClickedSP:(id)sender
{
    // show address selector view controller
   // AddrSelectorViewController * controller = [self.storyboard instantiateViewControllerWithIdentifier:@"addrselector"];
    
    AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
    
    
    controller.mParentTag = TAG_START;
    controller.delegate = self;
    
//    [self presentModalViewController:controller animated:YES];
    [self presentViewController:controller animated:YES completion:nil];
}

/**
 * click start pos set voice button
 */
- (IBAction)onClickedSPVoice:(id)sender
{
    
}

/**
 * click end pos text area
 */
- (IBAction)onClickedEP:(id)sender
{
    // show address selector view controller
    //AddrSelectorViewController * controller = [self.storyboard instantiateViewControllerWithIdentifier:@"addrselector"];
    
    AddrSelectorViewController *  controller =[[AddrSelectorViewController alloc]initWithNibName:@"AddressSelectorView" bundle:nil];
    
    controller.mParentTag = TAG_END;
    controller.delegate = self;
    
//    [self presentModalViewController:controller animated:YES];
    [self presentViewController:controller animated:YES completion:nil];
}

/**
 * click end pos voice button
 */
- (IBAction)onClickedEPVoice:(id)sender
{
    
}

/**
 * click event of start time button
 */
- (IBAction)onClickedTime:(id)sender
{
//    if (mStartTime != nil) {
//        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
//        [dateFormatter setDateFormat:@"HH-mm"];
//        NSDate * date = [dateFormatter dateFromString:mStartTime];
//        [_timePicker setDate:date];
//    }
    [_pickerView setHidden:NO];
}

- (IBAction)onClickedPickerOK:(id)sender
{
    NSDate *selected = [_timePicker date];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@" HH - mm "];
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


@end
