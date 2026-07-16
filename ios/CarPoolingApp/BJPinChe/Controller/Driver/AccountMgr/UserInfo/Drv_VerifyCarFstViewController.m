//
//  Drv_VerifyCar1ViewController.m
//  BJPinChe
//
//  Created by Kimoc on 14-8-22.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "Drv_VerifyCarFstViewController.h"
#import "RDActionSheet.h"
#import "NonRotateImagePickerController.h"
#import "ImageHelper.h"
#import "Drv_VerifyCarSecViewController.h"

@interface Drv_VerifyCarFstViewController ()

@end

@implementation Drv_VerifyCarFstViewController

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
    mBmpFrontPhoto = [Common loadImageFromLocalFile:CAR_VERIFY_DRIVER_LICENSE_FORE_IMAGE];
    mBmpBackPhoto = [Common loadImageFromLocalFile:CAR_VERIFY_DRIVER_LICENSE_BACK_IMAGE];
    
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
//                    [self presentModalViewController:picker animated:YES];
                    [self presentViewController:picker animated:YES completion:nil];
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
//                    [self presentModalViewController:picker animated:YES];
                    [self presentViewController:picker animated:YES completion:nil];
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

- (IBAction)onClickedNext:(id)sender
{
	if (mBmpFrontPhoto == nil) {
		[SVProgressHUD showErrorWithStatus:@"请上传驾照正面图" duration:2];
		return;
	}

	if (mBmpBackPhoto == nil) {
		[SVProgressHUD showErrorWithStatus:@"请上传驾照反面图" duration:2];
		return;
	}

	STCarVerifyingInfo *carVerifyingInfo = [[STCarVerifyingInfo alloc] init];
	NSData *frontImageData = UIImageJPEGRepresentation(mBmpFrontPhoto, 0.5);
	NSString *strFrontImage = [Common base64forData:frontImageData];
	NSData *backImageData = UIImageJPEGRepresentation(mBmpBackPhoto, 0.5);
	NSString *strBackImage = [Common base64forData:backImageData];

	carVerifyingInfo.driver_license_fore = strFrontImage;
	carVerifyingInfo.driver_license_back = strBackImage;
	[Common setCarVerifyingInfo:carVerifyingInfo];

	Drv_VerifyCarSecViewController *controller = (Drv_VerifyCarSecViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"driver_verify_2"];

	[self.navigationController pushViewController:controller animated:YES];
	// SHOW_VIEW(controller);
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
            [Common saveImage:image toLocalFile:CAR_VERIFY_DRIVER_LICENSE_FORE_IMAGE];
        } else {
            mBmpBackPhoto = image;
            [_imgBack setImage:mBmpBackPhoto];
            [Common saveImage:image toLocalFile:CAR_VERIFY_DRIVER_LICENSE_BACK_IMAGE];
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











