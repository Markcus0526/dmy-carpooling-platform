//
//  ShareViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/25/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//  乘客分享界面

#import <UIKit/UIKit.h>

@class AppDelegate;

@interface Cus_ShareViewController : SuperViewController <AccountSvcDelegate>
{
    AppDelegate *_appDelegate;
    
    /********************** UI Controls **********************/
    IBOutlet UILabel *              _lblInviteCode;
    IBOutlet UIImageView *          _imgQRCode;
    
    IBOutlet UIWebView      * webView;
    /********************** Member Variable **********************/
    NSString *                      mContents1;
    NSString *                      mContents2;
    NSString *                      mContents3;
    
}

- (IBAction)onClickSina:(id)sender;
- (IBAction)onClickTengxun:(id)sender;
- (IBAction)onClickQQFriend:(id)sender;
- (IBAction)onClickQQSpace:(id)sender;
- (IBAction)onClickWeixin:(id)sender;
- (IBAction)onClickFriendSpace:(id)sender;
- (IBAction)onClickSms:(id)sender;
- (IBAction)onClickEmail:(id)sender;
- (IBAction)onclickYixin:(id)sender;

@end
