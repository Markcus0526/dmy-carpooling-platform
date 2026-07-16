//
//  VerifyUserViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/31/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "VerifyUserViewController.h"
#import "RDActionSheet.h"
#import "NonRotateImagePickerController.h"
#import "ImageHelper.h"

@interface VerifyUserViewController ()

@end

@implementation VerifyUserViewController

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
    mCurSelPos = 0;
    mBmpFrontPhoto = nil;
    mBmpBackPhoto = nil;
}



//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation


/**
 * call service to submit verify information
 * @param : mobile [in], current user's phone number
 */
- (void) callVerifyPerson:(NSString *)foreimg backimg:(NSString *)backimg
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] VerifyPersonInfo:[Common getUserID] idforeimg:foreimg idbackimg:backimg devtoken:[Common getDeviceMacAddress]];
}

- (void) verifyPersonInfoResult:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
        [SVProgressHUD dismissWithSuccess:MSG_SUCCESS afterDelay:DEF_DELAY];
		[self.navigationController popoverPresentationController];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}


///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

// main control event
/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    BACK_VIEW;
}

- (IBAction)onClickedIDCardImg1:(id)sender
{
    mCurSelPos = 0;
    
    RDActionSheet *actionSheet = [RDActionSheet alloc];
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]) {
        [actionSheet initWithCancelButtonTitle:@"取消"
                            primaryButtonTitle:nil
                            destroyButtonTitle:nil
                             otherButtonTitles:@"相册", @"拍照", nil];
    }
    else {
        [actionSheet initWithCancelButtonTitle:@"取消"
                            primaryButtonTitle:nil
                            destroyButtonTitle:nil
                             otherButtonTitles:@"相册",nil];
    }
    actionSheet.callbackBlock = ^(RDActionSheetResult result, NSInteger buttonIndex) {
        
        switch (result) {
            case RDActionSheetButtonResultSelected:
            {
                MyLog(@"Pressed %i", buttonIndex);
                
                UIImagePickerController *picker = [[NonRotateImagePickerController alloc] init];
                //                picker.delegate = [HuiYuanZhongXinViewController sharedInstance];
                picker.delegate = self;
                picker.allowsEditing = YES;
                
                if (buttonIndex == 0)
                    picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
                else
                    picker.sourceType = UIImagePickerControllerSourceTypeCamera;
                
                if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPad) {
                    //picker.modalInPopover = YES;
                    UIPopoverController *popover = [[UIPopoverController alloc] initWithContentViewController:picker];
                    //                    popover.delegate = [HuiYuanZhongXinViewController sharedInstance];
                    popover.delegate = self;
                    [popover presentPopoverFromRect:CGRectMake(90, 150, 270, 300)
                                             inView:self.view
                           permittedArrowDirections:UIPopoverArrowDirectionLeft
                                           animated:YES];
                    //[popover setPopoverContentSize:CGSizeMake(500, 500)];
                } else {
                    [self presentModalViewController:picker animated:YES];
                }
                break;
            }
            case RDActionSheetResultResultCancelled:
                MyLog(@"Sheet cancelled");
                break;
        }
    };
    [actionSheet showFrom:self.view];
}

- (IBAction)onClickedIDCardImg2:(id)sender
{
    mCurSelPos = 1;

    RDActionSheet *actionSheet = [RDActionSheet alloc];
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]) {
        [actionSheet initWithCancelButtonTitle:@"取消"
                            primaryButtonTitle:nil
                            destroyButtonTitle:nil
                             otherButtonTitles:@"相册", @"拍照", nil];
    }
    else {
        [actionSheet initWithCancelButtonTitle:@"取消"
                            primaryButtonTitle:nil
                            destroyButtonTitle:nil
                             otherButtonTitles:@"相册",nil];
    }
    actionSheet.callbackBlock = ^(RDActionSheetResult result, NSInteger buttonIndex) {
        
        switch (result) {
            case RDActionSheetButtonResultSelected:
            {
                MyLog(@"Pressed %i", buttonIndex);
                
                UIImagePickerController *picker = [[NonRotateImagePickerController alloc] init];
                //                picker.delegate = [HuiYuanZhongXinViewController sharedInstance];
                picker.delegate = self;
                picker.allowsEditing = YES;
                
                if (buttonIndex == 0)
                    picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
                else
                    picker.sourceType = UIImagePickerControllerSourceTypeCamera;
                
                if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPad) {
                    //picker.modalInPopover = YES;
                    UIPopoverController *popover = [[UIPopoverController alloc] initWithContentViewController:picker];
                    //                    popover.delegate = [HuiYuanZhongXinViewController sharedInstance];
                    popover.delegate = self;
                    [popover presentPopoverFromRect:CGRectMake(90, 150, 270, 300)
                                             inView:self.view
                           permittedArrowDirections:UIPopoverArrowDirectionLeft
                                           animated:YES];
                    //[popover setPopoverContentSize:CGSizeMake(500, 500)];
                } else {
                    [self presentModalViewController:picker animated:YES];
                }
                break;
            }
            case RDActionSheetResultResultCancelled:
                MyLog(@"Sheet cancelled");
                break;
        }
    };
    [actionSheet showFrom:self.view];
}

- (IBAction)onClickedConfirm:(id)sender
{
    if (mBmpFrontPhoto == nil || mBmpBackPhoto == nil) {
		SHOW_MSG(MSG_SELECT_IMAGE);
        return;
    }
    
    NSData *data = UIImageJPEGRepresentation(mBmpFrontPhoto, 0.5);
    NSString * base64FrontImage = [Common base64forData:data];
    data = UIImageJPEGRepresentation(mBmpBackPhoto, 0.5);
    NSString * base64BackImage = [Common base64forData:data];
    
    [self callVerifyPerson:base64FrontImage backimg:base64BackImage];
}


- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
	
    UIImage *srcImage = [info objectForKey:UIImagePickerControllerEditedImage];
	UIImage *orgImage = [info objectForKey:UIImagePickerControllerOriginalImage];
    if (srcImage == nil)
        srcImage = orgImage;
    
    //UIImage *image = [srcImage imageByScalingAndCroppingForSize:CGSizeMake(128, 128)];
    UIImage * image = srcImage;
    
    if (image != nil)
    {
        if (mCurSelPos == 0) {
            mBmpFrontPhoto = image;
            [_imgFront setImage:mBmpFrontPhoto];
        } else {
            mBmpBackPhoto = image;
            [_imgBack setImage:mBmpBackPhoto];
        }
    }
    else
	{
        NSString * result = STRING_DATAMANAGER_PHOTOSIZE;
		SHOW_MSG(result);
	}
	
	[picker dismissModalViewControllerAnimated:YES];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
	[picker dismissModalViewControllerAnimated:YES];
}

- (BOOL)popoverControllerShouldDismissPopover:(UIPopoverController *)popoverController {
    return YES;
}


@end
