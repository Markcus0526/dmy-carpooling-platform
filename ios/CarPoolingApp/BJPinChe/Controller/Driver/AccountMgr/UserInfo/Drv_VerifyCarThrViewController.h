//
//  Drv_VerifyCar3ViewController.h
//  BJPinChe
//
//  Created by Kimoc on 14-8-22.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Drv_VerifyCarThrViewController : SuperViewController <AccountSvcDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UIImageView *              _imgFront;
    IBOutlet UIImageView *              _imgBack;
    
    /********************** Member Variable ***********************/
    int                                 mCurSelPos;
    UIImage *                           mBmpFrontPhoto;
    UIImage *                           mBmpBackPhoto;    
}


- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedIDCardImg1:(id)sender;
- (IBAction)onClickedIDCardImg2:(id)sender;
- (IBAction)onClickedNext:(id)sender;

@end




