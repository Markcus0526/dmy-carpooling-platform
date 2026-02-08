//
//  VerifyUserViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/31/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//


//////////////////////////////
//此界面不用了
/////////////////////////////

#import <UIKit/UIKit.h>

@interface Drv_VerifyUserViewController : SuperViewController <AccountSvcDelegate>
{
    __weak IBOutlet UIImageView *frontImage;
    /********************** UI Controls **********************/
    IBOutlet UIImageView *              _imgFront;
    IBOutlet UIImageView *              _imgBack;
    
    __weak IBOutlet UIImageView *backImage;
    /********************** Member Variable ***********************/
    int                                 mCurSelPos;
    UIImage *                           mBmpFrontPhoto;
    UIImage *                           mBmpBackPhoto;
}
//////////////////////////////
//此界面不用了
/////////////////////////////
- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedIDCardImg1:(id)sender;
- (IBAction)onClickedIDCardImg2:(id)sender;
- (IBAction)onClickedConfirm:(id)sender;


@end
