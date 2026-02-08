//
//  Drv_VerifyCar2ViewController.m
//  BJPinChe
//
//  Created by Kimoc on 14-8-22.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "Drv_VerifyCarSecViewController.h"
#import "RDActionSheet.h"
#import "NonRotateImagePickerController.h"
#import "ImageHelper.h"
#import "Drv_VerifyCarThrViewController.h"

@interface Drv_VerifyCarSecViewController ()

@end

@implementation Drv_VerifyCarSecViewController

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
    mBmpCarPhoto = [Common loadImageFromLocalFile:CAR_VERIFY_CAR_IMAGE];
    [_imgCar setImage:mBmpCarPhoto];
    
    mBrandArray = [[NSMutableArray alloc] init];
    mTypeArray = [[NSMutableArray alloc] init];
    mColorArray = [[NSMutableArray alloc] init];
	TEST_NETWORK_RETURN

    [SVProgressHUD show];
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] GetBrandsAndColors];
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

- (IBAction)onClickedNext:(id)sender
{
    STCarVerifyingInfo *carVerifyingInfo = [Common getCarVerifyingInfo];
    carVerifyingInfo.brand = mSelectedBrand;
    carVerifyingInfo.type = mSelectedType;
    carVerifyingInfo.color = mSelectedColor;
    carVerifyingInfo.carimg = [Common base64forData:UIImageJPEGRepresentation(mBmpCarPhoto, 0.5)];
    [Common setCarVerifyingInfo:carVerifyingInfo];
    
    Drv_VerifyCarThrViewController *controller = (Drv_VerifyCarThrViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"driver_verify_3"];
    [self.navigationController pushViewController:controller animated:YES];
    
   // SHOW_VIEW(controller);
}

- (IBAction)onClickedCarImg:(id)sender
{
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

- (IBAction)onClickedBrand:(id)sender
{
    [_vwBrandSel setHidden:NO];
}

- (IBAction)onClickedType:(id)sender
{
    [_vwTypeSel setHidden:NO];
}

- (IBAction)onClickedColor:(id)sender
{
    [_vwColorSel setHidden:NO];
}

- (IBAction)onCancelBrandClicked:(id)sender
{
    [_vwBrandSel setHidden:YES];
}

- (IBAction)onDoneBrandClicked:(id)sender
{
    if(![mSelectedBrand isEqualToString:_lblCarBrand.text])
    {
        if([mTypeArray count] > 0) {
            mSelectedType = [mTypeArray objectAtIndex:0];
            [_lblCarType setText:mSelectedType];
        }
    }
    
    [_lblCarBrand setText:mSelectedBrand];
    
    [_vwBrandSel setHidden:YES];
}

- (IBAction)onCancelTypeClicked:(id)sender
{
    [_vwTypeSel setHidden:YES];
}

- (IBAction)onDoneTypeClicked:(id)sender
{
    [_lblCarType setText:mSelectedType];
    [_vwTypeSel setHidden:YES];
}

- (IBAction)onCancelColorClicked:(id)sender
{
    [_vwColorSel setHidden:YES];
}

- (IBAction)onDoneColorClicked:(id)sender
{
    [_lblCarColor setText:mSelectedColor];
    [_vwColorSel setHidden:YES];
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
        mBmpCarPhoto = image;
        [_imgCar setImage:mBmpCarPhoto];
        [Common saveImage:image toLocalFile:CAR_VERIFY_CAR_IMAGE];
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


#pragma mark UIPickerView related stuff

//Method to define how many columns/dials to show
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}
#pragma  mark 数据源
// Method to define the numberOfRows in a component using the array.
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent :(NSInteger)component
{
    if(pickerView == _pickerBrand)
    {
        return [mBrandArray count];
    }
    else if(pickerView == _pickerType)
    {
        return [mTypeArray count];
    }
    else if(pickerView == _pickerColor)
    {
        return [mColorArray count];
    }
    return 0;
}
- (CGFloat)pickerView:(UIPickerView *)pickerView rowHeightForComponent:(NSInteger)component
{
    return 50;
}

// Method to show the title of row for a component.

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    if(pickerView == _pickerBrand)
    {
        if([mBrandArray count] <= 0)
            return @"";
        
        return [mBrandArray objectAtIndex:row];
    }
    else if(pickerView == _pickerType)
    {
        if([mTypeArray count] <= 0)
            return @"";
        
        return [mTypeArray objectAtIndex:row];
    }
    else if(pickerView == _pickerColor)
    {
        if([mColorArray count] <= 0)
            return @"";
        
        return [mColorArray objectAtIndex:row];
    }
    
    return @"";
}

#pragma mark pickerView  选择方法
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    if(pickerView == _pickerBrand)
    {
        if([mBrandArray count] > 0) {
            mSelectedBrand = [mBrandArray objectAtIndex:row];
            mTypeArray = [mAllTypeArray objectAtIndex:row];
            [_pickerType reloadAllComponents];
        }
    }
    else if(pickerView == _pickerType)
    {
        if([mTypeArray count] > 0)
            mSelectedType = [mTypeArray objectAtIndex:row];
    }
    else if(pickerView == _pickerColor)
    {
        if([mColorArray count] > 0)
            mSelectedColor = [mColorArray objectAtIndex:row];
    }
}

#pragma AccountSvcMgr Delegate Methods
- (void)getBrandsAndColorsResult:(NSString *)result brandList:(NSMutableArray *)brandList colorList:(NSMutableArray *)colorList
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        mBrandArray = [[NSMutableArray alloc] init];
        mTypeArray = [[NSMutableArray alloc] init];
        mColorArray = [[NSMutableArray alloc] init];
        mAllTypeArray = [[NSMutableArray alloc] init];
        
        for(STCarBrandInfo *brand in brandList)
        {
            [mBrandArray addObject:brand.name];
            NSMutableArray *typeArray = [[NSMutableArray alloc] init];
            for(STCarTypeInfo *type in brand.types)
            {
                [typeArray addObject:type.name];
            }
            [mAllTypeArray addObject:typeArray];
        }
        
        if([mAllTypeArray count] > 0)
            mTypeArray = [mAllTypeArray objectAtIndex:0];
        
        for(STCarColorInfo *color in colorList)
        {
            [mColorArray addObject:color.name];
        }
        
        [_pickerBrand reloadAllComponents];
        [_pickerType reloadAllComponents];
        [_pickerColor reloadAllComponents];
        
        if([mBrandArray count] > 0)
            [_lblCarBrand setText:[mBrandArray objectAtIndex:0]];
        mSelectedBrand =[mBrandArray objectAtIndex:0];
        if([mTypeArray count] > 0)
            [_lblCarType setText:[mTypeArray objectAtIndex:0]];
        mSelectedType =[mTypeArray objectAtIndex:0];
        if([mColorArray count] > 0)
            [_lblCarColor setText:[mColorArray objectAtIndex:0]];
        mSelectedColor =[mColorArray objectAtIndex:0];
        [SVProgressHUD dismiss];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

@end




