//
//  Drv_VerifyCar3ViewController.m
//  BJPinChe
//
//  Created by Kimoc on 14-8-22.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "Drv_VerifyCarThrViewController.h"
#import "RDActionSheet.h"
#import "NonRotateImagePickerController.h"
#import "ImageHelper.h"

@interface Drv_VerifyCarThrViewController ()

@end

@implementation Drv_VerifyCarThrViewController

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


-(void) initControls
{
    mBmpFrontPhoto = [Common loadImageFromLocalFile:CAR_VERIFY_DRIVING_LICENSE_FORE_IMAGE];
    mBmpBackPhoto = [Common loadImageFromLocalFile:CAR_VERIFY_DRIVING_LICENSE_BACK_IMAGE];
    
    [_imgFront setImage:mBmpFrontPhoto];
    [_imgBack setImage:mBmpBackPhoto];
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
                NSLog(@"Pressed %i", buttonIndex);
                
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
//                    [self presentModalViewController:picker animated:YES];
                    [self presentViewController:picker animated:YES completion:nil];
                }
                break;
            }
            case RDActionSheetResultResultCancelled:
                NSLog(@"Sheet cancelled");
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
                NSLog(@"Pressed %i", buttonIndex);
                
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
//                    [self presentModalViewController:picker animated:YES];
                    [self presentViewController:picker animated:YES completion:nil];
                }
                break;
            }
            case RDActionSheetResultResultCancelled:
                NSLog(@"Sheet cancelled");
                break;
        }
    };
    [actionSheet showFrom:self.view];
    
}

- (IBAction)onClickedNext:(id)sender
{
    STCarVerifyingInfo *carVerifyingInfo = [Common getCarVerifyingInfo];
    carVerifyingInfo.userid = [Common getUserID];
    carVerifyingInfo.driving_license_fore = [Common base64forData:UIImageJPEGRepresentation(mBmpFrontPhoto, 0.5)];
    carVerifyingInfo.driving_license_back = [Common base64forData:UIImageJPEGRepresentation(mBmpBackPhoto, 0.5)];
    [Common setCarVerifyingInfo:carVerifyingInfo];
    
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    TEST_NETWORK_RETURN;
    
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] VerifyDriver];
}

- (void) verifyDriverResult:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];
        
       // MOVE_FROM_LEFT
        [self.navigationController popToRootViewControllerAnimated:YES];
        //[self.parentViewController.parentViewController.parentViewController dismissViewControllerAnimated:NO completion:nil];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
        [self.navigationController popToRootViewControllerAnimated:YES];
    }
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
            [Common saveImage:image toLocalFile:CAR_VERIFY_DRIVING_LICENSE_FORE_IMAGE];
        } else {
            mBmpBackPhoto = image;
            [_imgBack setImage:mBmpBackPhoto];
            [Common saveImage:image toLocalFile:CAR_VERIFY_DRIVING_LICENSE_BACK_IMAGE];
        }
    }
    else
	{
        NSString * result = STRING_DATAMANAGER_PHOTOSIZE;
        [self.view makeToast:result duration:DEF_DELAY position:@"center"];
	}
	
//	[picker dismissModalViewControllerAnimated:YES];
    [picker dismissViewControllerAnimated:YES completion:nil];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
//	[picker dismissModalViewControllerAnimated:YES];
    [picker dismissViewControllerAnimated:YES completion:nil];
}

- (BOOL)popoverControllerShouldDismissPopover:(UIPopoverController *)popoverController {
    return YES;
}

@end
