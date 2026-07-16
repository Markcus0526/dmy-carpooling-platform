//
//  Drv_VerifyCar2ViewController.h
//  BJPinChe
//
//  Created by Kimoc on 14-8-22.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Drv_VerifyCarSecViewController : SuperViewController <AccountSvcDelegate, UIPickerViewDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UIImageView *              _imgCar;
    IBOutlet UILabel *                  _lblCarBrand;
    IBOutlet UILabel *                  _lblCarType;
    IBOutlet UILabel *                  _lblCarColor;
    
    IBOutlet UIView *                   _vwBrandSel;
    IBOutlet UIPickerView  *            _pickerBrand;
    IBOutlet UIView *                   _vwTypeSel;
    IBOutlet UIPickerView  *            _pickerType;
    IBOutlet UIView *                   _vwColorSel;
    IBOutlet UIPickerView  *            _pickerColor;
    
    /********************** Member Variable ***********************/
    UIImage *                           mBmpCarPhoto;
    
    NSMutableArray *                    mBrandArray;
    NSMutableArray *                    mTypeArray;
    NSMutableArray *                    mColorArray;
    NSMutableArray *                    mAllTypeArray;
    
    NSString *                          mSelectedBrand;
    NSString *                          mSelectedType;
    NSString *                          mSelectedColor;
}


- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedNext:(id)sender;

- (IBAction)onClickedCarImg:(id)sender;
- (IBAction)onClickedBrand:(id)sender;
- (IBAction)onClickedType:(id)sender;
- (IBAction)onClickedColor:(id)sender;
- (IBAction)onCancelBrandClicked:(id)sender;
- (IBAction)onDoneBrandClicked:(id)sender;
- (IBAction)onCancelTypeClicked:(id)sender;
- (IBAction)onDoneTypeClicked:(id)sender;
- (IBAction)onCancelColorClicked:(id)sender;
- (IBAction)onDoneColorClicked:(id)sender;

@end
