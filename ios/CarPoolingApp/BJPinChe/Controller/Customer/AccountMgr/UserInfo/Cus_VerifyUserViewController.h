//
//  VerifyUserViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/31/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Cus_VerifyUserViewController : SuperViewController <AccountSvcDelegate>
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
- (IBAction)onClickedConfirm:(id)sender;

@end
